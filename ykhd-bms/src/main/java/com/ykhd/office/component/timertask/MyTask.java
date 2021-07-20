package com.ykhd.office.component.timertask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ykhd.office.component.RedisService;
import com.ykhd.office.domain.entity.Customer;
import com.ykhd.office.domain.entity.OASchedule;
import com.ykhd.office.domain.entity.OfficeAccount;
import com.ykhd.office.domain.entity.WorkTask;
import com.ykhd.office.service.ICustomerService;
import com.ykhd.office.service.IOAScheduleService;
import com.ykhd.office.service.IOfficeAccountService;
import com.ykhd.office.service.IWorkTaskService;
import com.ykhd.office.util.DateUtil;
import com.ykhd.office.util.dictionary.Consts;
import com.ykhd.office.util.dictionary.StateEnums.State4OASchedule;
import com.ykhd.office.util.dictionary.StateEnums.State4WorkTask;

/**
 * @author zhoufan
 * @Date 2020/9/12
 */
@Component
@EnableScheduling
public class MyTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyTask.class);

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IOfficeAccountService officeAccountService;
    @Autowired
    private IOAScheduleService oaScheduleService;
    @Autowired
    private IWorkTaskService workTaskService;
    @Autowired
    private RedisService redisService;

//    /**
//     * 创建TaskScheduler Bean
//     */
//    @Bean
//    public TaskScheduler scheduledExecutorService() {
//        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
//        scheduler.setPoolSize(8);
//        scheduler.setThreadNamePrefix("scheduled-thread-");
//        return scheduler;
//    }

    /**
     * 两个月不存在排期的客户信息自动掉入公海
     */
//    @Scheduled(cron = "0 5 0 * * ?")
//    public void backHighSeas() {
//    	LOGGER.info("定时任务一：自动检测客户是否需要掉入公海");
//        Date now = new Date();
//        List<Customer> list = customerService.list(new QueryWrapper<Customer>().select("id", "draw_time").eq("is_lock", "0"));
//        if (!list.isEmpty()) {
//        	List<Integer> customerId = list.stream().filter(v -> v.getDrawTime() != null && v.getDrawTime().before(DateUtil.monthsAgo(now, 2)))
//        			.map(Customer::getId).collect(Collectors.toList());
//        	if (!customerId.isEmpty()) {
//        		LOGGER.info("掉入公海的客户数量：" + customerId.size());
//                if (customerService.update(new UpdateWrapper<Customer>().in("id", customerId).set("is_lock", "1").set("draw_time", null).set("belong_user", null)))
//                	LOGGER.info("掉入公海完成");
//        	}
//        }
//    }

    /**
     * 公众号掉入公海（移除对接媒介）
     * 每天凌晨两点执行
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void OfficeAccountbackHighSeas() {
    	LOGGER.info("定时任务二：自动检测公众号是否需要掉入公海");
    	Date date = new Date();
    	// 查询所有已有对接媒介的公众号ID
    	List<Integer> oaId_list = officeAccountService.list(new QueryWrapper<OfficeAccount>().select("id").isNotNull("medium_id"))
    			.stream().map(OfficeAccount::getId).collect(Collectors.toList());
    	// 查询存在排期及对应的最大投放时间
    	List<Integer> states = Arrays.asList(State4OASchedule.已完成.code(), State4OASchedule.待结算.code(), State4OASchedule.待确认.code());
    	List<OASchedule> schedule_list = oaScheduleService.list(new QueryWrapper<OASchedule>().select("office_account", "max(put_date) as putDate")
    			.in("office_account", oaId_list).in("state", states).groupBy("office_account"));
    	// 找出超过投放日期30天的排期对应的公众号ID
    	List<Integer> delete_oaId_list = schedule_list.stream().filter(v -> DateUtil.intervalDays(v.getPutDate(), date) > 30).map(OASchedule::getOfficeAccount).collect(Collectors.toList());
    	// 剩下未创建过排期的公众号ID
    	Set<Integer> set = schedule_list.stream().map(OASchedule::getOfficeAccount).collect(Collectors.toSet());
    	oaId_list.removeAll(set);
    	// 处理缓存中未创建过排期的公众号ID的出现次数
    	if (oaId_list.isEmpty())
    		redisService.delKV(Consts.UNUSED_OA);
    	else {
    		List<String> idStr_list = oaId_list.stream().map(String::valueOf).collect(Collectors.toList()); // String extends Object
    		Map<Object, Object> now_map = idStr_list.stream().collect(Collectors.toMap(v -> v, v -> "1")); // <"id", "1">
    		Map<Object, Object> history_map = redisService.getMap(Consts.UNUSED_OA);
    		if (history_map != null) {
    			idStr_list.forEach(v -> {
    				if (history_map.containsKey(v)) {
    					int count = Integer.valueOf(history_map.get(v).toString()) + 1;
    					if (count > 30) {
    						delete_oaId_list.add(Integer.valueOf(v));
    						now_map.remove(v);
    					} else
    						now_map.put(v, String.valueOf(count));
    				}
    			});
    		}
    		redisService.setMap(Consts.UNUSED_OA, now_map);
    	}
    	// 移除对接媒介
    	if (!delete_oaId_list.isEmpty()) {
    		LOGGER.info("掉入公海的公众号数量：" + delete_oaId_list.size());
    		if (officeAccountService.update(new UpdateWrapper<OfficeAccount>().in("id", delete_oaId_list).set("medium_id", null)))
    			LOGGER.info("掉入公海完成");
    	}
    }
    
    /**
     * 检测进行中的工作任务是否延期
     */
    @Scheduled(cron = "0 1 0 * * ?")
    public void checkWorkTask() {
        LOGGER.info("定时任务三：自动检测进行中的工作任务是否延期");
        Date short_date = DateUtil.yyyyMMdd2date(DateUtil.date2yyyyMMdd(new Date()));
        List<WorkTask> all = workTaskService.list(new QueryWrapper<WorkTask>().select("id", "plan_finish_date").eq("state", State4WorkTask.进行中.code()));
        List<Integer> ids = all.stream().filter(v -> DateUtil.yyyyMMdd2date(v.getPlanFinishDate()).before(short_date)).map(WorkTask::getId).collect(Collectors.toList());
        if (!ids.isEmpty()) {
        	LOGGER.info("延期任务数：" + ids.size());
            if (workTaskService.update(new UpdateWrapper<WorkTask>().in("id", ids).set("state", State4WorkTask.延期.code())))
                LOGGER.info("延期更改完成");
        }
    }

    /**
     * 数据备份
     * 每天中午十二点执行
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void saveBackup() {
        File file = new File("C:\\backup_file\\ykhd");
//        if (file.exists()) {
//            deleteFile(file);
//        }
        Runtime rt = Runtime.getRuntime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        // 要用来做导入用的sql目标文件： 
        String filename = "C:\\backup_file\\ykhd" + "\\" + "ykhd" + sdf.format(date) + ".sql";
        //调用外部执行exe文件的javaAPI
        try {
            //使用命令--ignore-table备份数据库时忽略yw_bill_voucher表  yw_medium_snipaste  yw_payment_voucher
            rt.exec("cmd /c mysqldump -hlocalhost -uroot -pfxd891219 -x yw_ykhd " +
                    "--ignore-table=yw_ykhd.yw_payment_voucher --ignore-table=yw_ykhd.yw_medium_snipaste --ignore-table=yw_ykhd.yw_bill_voucher >" + filename);
            LOGGER.info("数据库备份成功.......................................");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(File file) {
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f : files) {
            //打印文件名
            String name = file.getName();
            System.out.println(name);
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()) {
                deleteFile(f);
            } else {
                f.delete();
            }
        }
    }

}
