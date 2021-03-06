package com.ykhd.office.util.dictionary;

/**
 * 各种类别汇总
 */
public final class TypeEnums {

	/**修改申请记录主体*/
	public enum Type4Edit {
		排期(1);
		
		private int code;
		private Type4Edit(int code) {
			this.code = code;
		}
		public int code() {
			return code;
		}
	}
	
	/**内容变更记录主体*/
	public enum Type4Change {
		公众号(1), 员工职位(2), 员工薪水(3), 工作任务(4);
		
		private int code;
		private Type4Change(int code) {
			this.code = code;
		}
		public int code() {
			return code;
		}
	}
	
	/**支付申请类别*/
	public enum Type4PaymentApp {
		成本金额(0), 销售费用(1);
		
		private int code;
		private Type4PaymentApp(int code) {
			this.code = code;
		}
		public int code() {
			return code;
		}
	}
	
	/**上传事件类别*/
	public enum Type4Upload {
		公众号后台数据截图(1);
		
		private int code;
		private Type4Upload(int code) {
			this.code = code;
		}
		public int code() {
			return code;
		}
	}
	
	/**系统日志类别*/
	public enum Type4SystemLog {
		用户登陆(1);
		
		private int code;
		private Type4SystemLog(int code) {
			this.code = code;
		}
		public int code() {
			return code;
		}
	}
	
	/**审批消息推送类型*/
	public enum Type4ApprovalMsg {
		// 公众号
		公众号作废申请(1), 协议号作废申请(16),
		// 公众号排期
		公众号排期待审核(2), 公众号排期待确认(3), 公众号排期作废申请(4), 公众号排期作废待确认(5), 公众号排期修改申请(6),
		// 公众号排期支付申请
		公众号排期支付申请(7), 支付待复核(8),
		// 发票
		创建销项发票(9), 报送销项发票(10), 销项发票出纳待审核(11), 进项发票出纳待审核(12),
		// 公众号排期回款
		排期回款待确认(13), 排期回款待复核(15),
		// 工作任务
		任务延期(14), 询号待答复(23),
		// 绩效考核
		待确认绩效(24),
		// 集采排期
		公众号排期待集采确认(17), 集采公众号排期支付申请(18), 集采支付待复核(19), 集采排期回款待确认(20), 集采排期回款待复核(21),集采进项发票出纳待审核(22),
		
		// 登陆
		别处登陆被迫下线(100),
		;
		
		private int code;
		private Type4ApprovalMsg(int code) {
			this.code = code;
		}
		public int code() {
			return code;
		}
	}

	/**银行账户主体类别*/
	public enum Type4BankAccount {
		公众号(1), 客户(2);
		
		private int code;
		private Type4BankAccount(int code) {
			this.code = code;
		}
		public int code() {
			return code;
		}
	}

}
