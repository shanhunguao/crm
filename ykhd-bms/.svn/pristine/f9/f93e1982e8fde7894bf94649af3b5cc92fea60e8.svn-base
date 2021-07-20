package com.ykhd.office.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ykhd.office.component.RedisService;
import com.ykhd.office.domain.entity.Holiday;
import com.ykhd.office.domain.req.SystemLogCondition;
import com.ykhd.office.domain.resp.SystemInfo;
import com.ykhd.office.service.IHolidayService;
import com.ykhd.office.service.ISystemLogService;
import com.ykhd.office.service.impl.ApprovalMsgService;
import com.ykhd.office.util.MsgSentHelper;
import com.ykhd.office.util.dictionary.Consts;

/**
 * 系统相关
 */
@RestController
@RequestMapping("/system")
public class SystemController extends BaseController {

	public static Logger log = LoggerFactory.getLogger(SystemController.class);
	
	@Autowired
	private ISystemLogService systemLogService;
	@Autowired
	private ApprovalMsgService approvalMsgService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private IHolidayService holidayService;
	
	/**
	 * 查询基本设置
	 */
	@GetMapping("/base")
	public Object base() {
		String name = redisService.getValue("system_name");
		String motto = redisService.getValue("system_motto");
		Map<String, Object> map = new HashMap<>();
		map.put("name", name == null ? "" : name);
		map.put("motto", motto == null ? "" : motto);
		return map;
	}
	
	/**
	 * 保存基本设置
	 */
	@PutMapping("/base")
	public Object setBase(String name, String motto) {
		redisService.setKV("system_name", name, null);
		redisService.setKV("system_motto", motto, null);
		return success(null);
	}
	
	/**
	 * 查询系统信息
	 */
	@GetMapping("/info")
	public Object info() {
		return new SystemInfo(redisService.getValue("system_name"), redisService.getValue("system_motto"), holidayService.getworkingDays());
	}
	
	/**
	 * 查看系统日志
	 */
	@GetMapping("/log")
	public Object log(SystemLogCondition condition) {
		return systemLogService.getListByPage(condition);
	}

	/**
	 * 获取当月节假日列表
	 *
	 * @param month:yyyyMM格式
	 * @return
	 */
	@GetMapping("getHolidayByMonth")
	public List<Integer> getHolidayByMonth(String month) {
		List<Integer> list = holidayService.getHolidayByMonth(month + "01", month + "31");
		if (list == null)
			list = new ArrayList<>();
		return list;
	}

	/**
	 * 保存节假日设定
	 *
	 * @param holiday：yyyy-MM-dd 格式
	 * @return
	 */
	@PostMapping("setHoliday")
	public boolean setHoliday(String holiday) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		QueryWrapper<Holiday> wrapper = new QueryWrapper<>();
		wrapper.eq("holiday", holiday);
		Holiday temp = holidayService.getOne(wrapper);
		if (temp == null) {
			temp = new Holiday();
			try {
				temp.setHoliday(format.parse(holiday));
				holidayService.save(temp);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			holidayService.remove(wrapper);
		}
		return true;
	}
	
	/**
	 * 消息推送
	 */
	@GetMapping("/msg_sent")
	public void sent(HttpServletResponse response) {
		response.setContentType(Consts.TEXT_EVENT_STREAM_UTF8);
		Integer mid = BaseController.getManagerInfo().getId();
		MsgSentHelper.destroy(mid);
		PrintWriter pw = null;
		try {
			TimeUnit.SECONDS.sleep(1);
			pw = response.getWriter();
			MsgSentHelper.add(mid, pw, Thread.currentThread());
			pw.write("retry:10000\r\n");
			pw.flush();
			approvalMsgService.sendAll();
			TimeUnit.HOURS.sleep(1);
		} catch (IOException | InterruptedException e) {
			//log.error("##[msg/sent:error] --> {}", e.getMessage());
		} finally {
			if (pw != null)
				pw.close();
			MsgSentHelper.cleanPW(mid);
		}
	}

}
