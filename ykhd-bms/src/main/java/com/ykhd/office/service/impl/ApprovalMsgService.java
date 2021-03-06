package com.ykhd.office.service.impl;

import java.util.List;

import com.ykhd.office.domain.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ykhd.office.controller.BaseController;
import com.ykhd.office.domain.bean.LoginCache;
import com.ykhd.office.domain.bean.MsgSentBean;
import com.ykhd.office.service.IAssessService;
import com.ykhd.office.service.IDepartmentService;
import com.ykhd.office.service.IEditRecordService;
import com.ykhd.office.service.IFapiaoService;
import com.ykhd.office.service.IJcFapiaoService;
import com.ykhd.office.service.IJcOASReturnService;
import com.ykhd.office.service.IJcPaymentAppService;
import com.ykhd.office.service.IManagerService;
import com.ykhd.office.service.IOASReturnService;
import com.ykhd.office.service.IOAScheduleService;
import com.ykhd.office.service.IOfficeAccountService;
import com.ykhd.office.service.IPaymentAppService;
import com.ykhd.office.service.IRoleService;
import com.ykhd.office.service.IWorkTaskService;
import com.ykhd.office.service.IXunhaoService;
import com.ykhd.office.util.MsgSentHelper;
import com.ykhd.office.util.dictionary.StateEnums;
import com.ykhd.office.util.dictionary.StateEnums.State4AppCancel;
import com.ykhd.office.util.dictionary.StateEnums.State4EditRecord;
import com.ykhd.office.util.dictionary.StateEnums.State4OASReturn;
import com.ykhd.office.util.dictionary.StateEnums.State4OASchedule;
import com.ykhd.office.util.dictionary.StateEnums.State4OfficeAccount;
import com.ykhd.office.util.dictionary.StateEnums.State4PaymentApp;
import com.ykhd.office.util.dictionary.StateEnums.State4WorkTask;
import com.ykhd.office.util.dictionary.StateEnums.State4Xunhao;
import com.ykhd.office.util.dictionary.TypeEnums.Type4ApprovalMsg;
import com.ykhd.office.util.dictionary.TypeEnums.Type4Edit;

/**
 * 审批流程消息
 */
@Service
public class ApprovalMsgService {
    @Autowired
    private IManagerService managerService;
    @Autowired
    private IOfficeAccountService officeAccountService;
    @Autowired
    private IOAScheduleService oAScheduleService;
    @Autowired
    private IPaymentAppService paymentAppService;
    @Autowired
    private IEditRecordService editRecordService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IFapiaoService fapiaoService;
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private IOASReturnService oASReturnService;
    @Autowired
    private IWorkTaskService workTaskService;
    @Autowired
    private IJcPaymentAppService jcPaymentAppService;
    @Autowired
    private IJcOASReturnService jcOASReturnService;
    @Autowired
    private IJcFapiaoService  jcFapiaoService;
    @Autowired
    private IXunhaoService xunhaoService;
    @Autowired
    private IAssessService assessService;


    /**
     * 登陆后尝试推送消息
     */
    public void sendAll() {
        LoginCache managerInfo = BaseController.getManagerInfo();
        Integer id = managerInfo.getId();
        Role role = roleService.getById(managerInfo.getRole());
        if (role != null) {
            if (role.getName().contains("媒介")) {
                sendApprovalMsg(Type4ApprovalMsg.公众号排期待确认, id);
                sendApprovalMsg(Type4ApprovalMsg.公众号排期作废待确认, id);
                sendApprovalMsg(Type4ApprovalMsg.询号待答复, id);
            } else if (role.getName().contains("广告部主管") || role.getName().contains("业务总监") || role.getName().contains("广告部组长")) {
            	sendApprovalMsg(Type4ApprovalMsg.公众号作废申请, null);
                sendApprovalMsg(Type4ApprovalMsg.公众号排期待审核, id);
                sendApprovalMsg(Type4ApprovalMsg.公众号排期作废申请, id);
                sendApprovalMsg(Type4ApprovalMsg.公众号排期修改申请, id);
                sendApprovalMsg(Type4ApprovalMsg.报送销项发票, id);
            } else if (role.getName().contains("出纳")) {
                sendApprovalMsg(Type4ApprovalMsg.公众号排期支付申请, id);
                sendApprovalMsg(Type4ApprovalMsg.集采公众号排期支付申请, id);
                sendApprovalMsg(Type4ApprovalMsg.销项发票出纳待审核, id);
                sendApprovalMsg(Type4ApprovalMsg.进项发票出纳待审核, id);
                sendApprovalMsg(Type4ApprovalMsg.排期回款待确认, id);
                sendApprovalMsg(Type4ApprovalMsg.集采排期回款待确认, id);
                sendApprovalMsg(Type4ApprovalMsg.集采进项发票出纳待审核, id);
            } else if (role.getName().contains("财务主管")) {
                sendApprovalMsg(Type4ApprovalMsg.支付待复核, id);
                sendApprovalMsg(Type4ApprovalMsg.排期回款待复核, id);
                sendApprovalMsg(Type4ApprovalMsg.集采支付待复核, id);
                sendApprovalMsg(Type4ApprovalMsg.集采排期回款待复核, id);
            } else if (role.getName().contains("集采主管")) {
            	sendApprovalMsg(Type4ApprovalMsg.协议号作废申请, id);
            	sendApprovalMsg(Type4ApprovalMsg.公众号排期待集采确认, id);
            } else if (role.getName().contains("集采专员")) {
            	
            }
        }
        sendApprovalMsg(Type4ApprovalMsg.任务延期, id);
        sendApprovalMsg(Type4ApprovalMsg.待确认绩效, id);
    }

    /**
     * 推送数量
     */
    public void sendApprovalMsg(Type4ApprovalMsg type, Integer reviewer) {
        switch (type) {
        case 公众号作废申请:
        	//由于任何媒介都可以对任何公众号申请作废，所以推给所有广告部主管。
        	int cancel_count = officeAccountService.count(new QueryWrapper<OfficeAccount>().eq("state", State4OfficeAccount.申请作废中.code()).isNull("agreement"));
        	Integer deptRole = roleService.deptRole();
        	if (deptRole != null) {
        		managerService.list(new QueryWrapper<Manager>().select("id").eq("role", deptRole)).stream().filter(v -> v != null).forEach(v -> {
        			if (MsgSentHelper.online(v.getId())) {
                        MsgSentHelper.sendMsg(new MsgSentBean(v.getId(), type.code(), cancel_count));
                    }
        		});
        	}
            break;
        case 协议号作废申请:
        	if (reviewer == null)
            	reviewer = departmentService.queryCollectionMrg();
        	if (reviewer != null) {
                if (MsgSentHelper.online(reviewer)) {
                	int count = officeAccountService.count(new QueryWrapper<OfficeAccount>().eq("state", State4OfficeAccount.申请作废中.code()).isNotNull("agreement"));
                    MsgSentHelper.sendMsg(new MsgSentBean(reviewer, type.code(), count));
                }
            }
        	break;
        case 公众号排期待审核:
            if (reviewer != null) {
                if (MsgSentHelper.online(reviewer)) {
                    int count = oAScheduleService.count(new QueryWrapper<OASchedule>().eq("state", State4OASchedule.提交待审核.code()).eq("reviewer", reviewer));
                    MsgSentHelper.sendMsg(new MsgSentBean(reviewer, type.code(), count));
                }
            }
            break;
        case 公众号排期待确认:
            if (reviewer != null) {
                if (MsgSentHelper.online(reviewer)) {
                    int count = oAScheduleService.count(new QueryWrapper<OASchedule>().eq("state", State4OASchedule.待确认.code()).eq("medium", reviewer));
                    MsgSentHelper.sendMsg(new MsgSentBean(reviewer, type.code(), count));
                }
            }
            break;
        case 公众号排期待集采确认:
        	if (reviewer == null) {
        		// 暂时推送给所有的集采人员
        		List<Integer> collectorRole = roleService.collectorRole();
        		if (!collectorRole.isEmpty()) {
        			int count = oAScheduleService.count(new QueryWrapper<OASchedule>().eq("state", State4OASchedule.待集采确认.code()));
            		managerService.list(new QueryWrapper<Manager>().select("id").in("role", collectorRole)).stream().filter(v -> v != null).forEach(v -> {
            			if (MsgSentHelper.online(v.getId())) {
                            MsgSentHelper.sendMsg(new MsgSentBean(v.getId(), type.code(), count));
                        }
            		});
            	}
        	} else {
                if (MsgSentHelper.online(reviewer)) {
                    int count = oAScheduleService.count(new QueryWrapper<OASchedule>().eq("state", State4OASchedule.待集采确认.code()));
                    MsgSentHelper.sendMsg(new MsgSentBean(reviewer, type.code(), count));
                }
            }
        	break;
        case 公众号排期作废申请:
            if (reviewer == null)
            	reviewer = departmentService.getSuperiorLeader(BaseController.getManagerInfo().getDepartment());
            if (reviewer != null) {
                if (MsgSentHelper.online(reviewer)) {
                    int count = oAScheduleService.count(new QueryWrapper<OASchedule>().eq("app_cancel", State4AppCancel.待主管审核.code()).eq("reviewer", reviewer));
                    MsgSentHelper.sendMsg(new MsgSentBean(reviewer, type.code(), count));
                }
            }
            break;
        case 公众号排期作废待确认:
            if (reviewer != null) {
                if (MsgSentHelper.online(reviewer)) {
                    int count = oAScheduleService.count(new QueryWrapper<OASchedule>().eq("app_cancel", State4AppCancel.待媒介审核.code()).eq("medium", reviewer));
                    MsgSentHelper.sendMsg(new MsgSentBean(reviewer, type.code(), count));
                }
            }
            break;
        case 公众号排期修改申请:
            if (reviewer != null) {
                if (MsgSentHelper.online(reviewer)) {
                    int count = editRecordService.count(new QueryWrapper<EditRecord>().eq("type", Type4Edit.排期.code()).eq("state", State4EditRecord.待审核.code()).eq("reviewer", reviewer));
                    MsgSentHelper.sendMsg(new MsgSentBean(reviewer, type.code(), count));
                }
            }
            break;
        case 公众号排期支付申请:
        	managerService.cashierList().forEach(v -> {
        		if (MsgSentHelper.online(v.getId())) {
                    int count = paymentAppService.count(new QueryWrapper<PaymentApp>().eq("state", State4PaymentApp.待支付.code()));
                    MsgSentHelper.sendMsg(new MsgSentBean(v.getId(), type.code(), count));
                }
        	});
            break;
        case 集采公众号排期支付申请:
        	managerService.cashierList().forEach(v -> {
        		if (MsgSentHelper.online(v.getId())) {
                    int count = jcPaymentAppService.count(new QueryWrapper<JcPaymentApp>().eq("state", State4PaymentApp.待支付.code()));
                    MsgSentHelper.sendMsg(new MsgSentBean(v.getId(), type.code(), count));
                }
        	});
            break;
        case 支付待复核:
            int count = paymentAppService.count(new QueryWrapper<PaymentApp>().eq("state", State4PaymentApp.待复核.code()));
            if (reviewer != null)
                MsgSentHelper.sendMsg(new MsgSentBean(reviewer, type.code(), count));
            else {
            	reviewer = departmentService.getLeader(BaseController.getManagerInfo().getDepartment());
            	MsgSentHelper.sendMsg(new MsgSentBean(reviewer, type.code(), count));
            }
            break;
        case 集采支付待复核:
            int count1 = jcPaymentAppService.count(new QueryWrapper<JcPaymentApp>().eq("state", State4PaymentApp.待复核.code()));
            if (reviewer != null)
                MsgSentHelper.sendMsg(new MsgSentBean(reviewer, type.code(), count1));
            else {
            	reviewer = departmentService.getLeader(BaseController.getManagerInfo().getDepartment());
            	MsgSentHelper.sendMsg(new MsgSentBean(reviewer, type.code(), count1));
            }
            break;
        case 创建销项发票:
            if (reviewer != null) {
                if (MsgSentHelper.online(reviewer)) {
                    count = fapiaoService.count(new QueryWrapper<Fapiao>().eq("state", StateEnums.State4Fapiao.创建.code()).eq("create_userid", reviewer));
                    MsgSentHelper.sendMsg(new MsgSentBean(reviewer, type.code(), count));
                }
            }
            break;
        case 报送销项发票:
            if (reviewer == null)
            	reviewer = departmentService.getSuperiorLeader(BaseController.getManagerInfo().getDepartment());
            if (reviewer != null) {
                if (MsgSentHelper.online(reviewer)) {
                    count = fapiaoService.count(new QueryWrapper<Fapiao>().eq("state", StateEnums.State4Fapiao.销项票待主管审核.code()).eq("audit_userid", reviewer));
                    MsgSentHelper.sendMsg(new MsgSentBean(reviewer, type.code(), count));
                }
            }
            break;
        case 销项发票出纳待审核:
            if (reviewer != null) {
                if (MsgSentHelper.online(reviewer)) {
                    count = fapiaoService.count(new QueryWrapper<Fapiao>().eq("state", StateEnums.State4Fapiao.审核通过_待财务审核.code()).eq("audit_userid", reviewer).eq("is_add",0));
                    MsgSentHelper.sendMsg(new MsgSentBean(reviewer, type.code(), count));
                }
            }
            break;
        case 进项发票出纳待审核:
            if (reviewer != null) {
                if (MsgSentHelper.online(reviewer)) {
                    count = fapiaoService.count(new QueryWrapper<Fapiao>().eq("state", StateEnums.State4Fapiao.审核通过_待财务审核.code()).eq("audit_userid", reviewer).eq("is_add",1));
                    MsgSentHelper.sendMsg(new MsgSentBean(reviewer, type.code(), count));
                }
            }
            break;
        case 排期回款待确认:
        	managerService.cashierList().forEach(v -> {
        		if (MsgSentHelper.online(v.getId())) {
        			int return_count = oASReturnService.count(new QueryWrapper<OASReturn>().eq("state", State4OASReturn.待确认.code()));
                    MsgSentHelper.sendMsg(new MsgSentBean(v.getId(), type.code(), return_count));
                }
        	});
            break;
        case 集采排期回款待确认:
        	managerService.cashierList().forEach(v -> {
        		if (MsgSentHelper.online(v.getId())) {
        			int return_count = jcOASReturnService.count(new QueryWrapper<JcOASReturn>().eq("state", State4OASReturn.待确认.code()));
                    MsgSentHelper.sendMsg(new MsgSentBean(v.getId(), type.code(), return_count));
                }
        	});
            break;
        case 排期回款待复核:
        	if (reviewer == null)
        		reviewer = departmentService.getLeader(BaseController.getManagerInfo().getDepartment());
        	if (MsgSentHelper.online(reviewer)) {
        		int return_count = oASReturnService.count(new QueryWrapper<OASReturn>().eq("state", State4OASReturn.待复核.code()));
        		MsgSentHelper.sendMsg(new MsgSentBean(reviewer, type.code(), return_count));
        	}
            break;
        case 集采排期回款待复核:
        	if (reviewer == null)
        		reviewer = departmentService.getLeader(BaseController.getManagerInfo().getDepartment());
        	if (MsgSentHelper.online(reviewer)) {
        		int return_count = jcOASReturnService.count(new QueryWrapper<JcOASReturn>().eq("state", State4OASReturn.待复核.code()));
        		MsgSentHelper.sendMsg(new MsgSentBean(reviewer, type.code(), return_count));
        	}
            break;
        case 任务延期:
            if (reviewer != null) {
            	if (MsgSentHelper.online(reviewer)) {
            		int task_count = workTaskService.count(new QueryWrapper<WorkTask>().eq("executor", reviewer).eq("state", State4WorkTask.延期.code()));
            		MsgSentHelper.sendMsg(new MsgSentBean(reviewer, type.code(), task_count));
            	}
            }
            break;
        case 集采进项发票出纳待审核:
            if (reviewer != null) {
                if (MsgSentHelper.online(reviewer)) {
                    count = jcFapiaoService.count(new QueryWrapper<JcFapiao>().eq("state", StateEnums.State4Fapiao.审核通过_待财务审核.code()).eq("audit_userid", reviewer).eq("is_add",1));
                    MsgSentHelper.sendMsg(new MsgSentBean(reviewer, type.code(), count));
                }
            }
            break;
        case 询号待答复:
            if (reviewer != null) {
                if (MsgSentHelper.online(reviewer)) {
                    count = xunhaoService.count(new QueryWrapper<Xunhao>().eq("medium", reviewer).eq("state", State4Xunhao.发起.code()));
                    MsgSentHelper.sendMsg(new MsgSentBean(reviewer, type.code(), count));
                }
            }
            break;
            case 待确认绩效:
                if (reviewer != null) {
                    if (MsgSentHelper.online(reviewer)) {
                        count = assessService.count(new QueryWrapper<Assess>().eq("user_id", reviewer).eq("state", StateEnums.State4Assess.待确认.code()));
                        MsgSentHelper.sendMsg(new MsgSentBean(reviewer, type.code(), count));
                    }
                }
                break;
		default:
			;
        }
    }

}
