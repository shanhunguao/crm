package com.ykhd.office.util.dictionary;

/**
 * 各种状态汇总
 */
public final class StateEnums {

	/**管理员*/
	public enum State4Manager {
		启用(0), 停用(1);
		
		private int code;
		private State4Manager(int code) {
			this.code = code;
		}
		public int code() {
			return code;
		}
	}
	
	/**客户*/
	public enum State4Customer {
		创建(1), 待审核(2), 审核不通过(3), 审核通过(4);
		
		private int code;
		private State4Customer(int code) {
			this.code = code;
		}
		public int code() {
			return code;
		}
	}
	
	/**公众号*/
	public enum State4OfficeAccount {
		创建(0), 提交启用(1), 待开发(3), 申请作废中(4), 已作废(5);
		
		private int code;
		private State4OfficeAccount(int code) {
			this.code = code;
		}
		public int code() {
			return code;
		}
	}
	
	/**公众号排期*/
	public enum State4OASchedule {
		创建(0), 提交待审核(1), 待确认(2), 审核不通过(3), 待结算(4), 已完成(5), 待集采确认(6), 作废(9);
		
		private int code;
		private State4OASchedule(int code) {
			this.code = code;
		}
		public int code() {
			return code;
		}
	}
	
	/**支付申请*/
	public enum State4PaymentApp {
		待支付(0), 驳回(1), 待复核(2), 已付款(4); //复核不通过(3)
		
		private int code;
		private State4PaymentApp(int code) {
			this.code = code;
		}
		public int code() {
			return code;
		}
	}
	
	/**发票*/
	public enum State4Fapiao {
		创建(0), 销项票待主管审核(1), // （0，1） 销项发票使用
		审核通过_待财务审核(2), 审核不通过(3), 审核通过(5), 已退回(7); // （其他） 销项、进项发票通用
		
		private int code;
		private State4Fapiao(int code) {
			this.code = code;
		}
		public int code() {
			return code;
		}
	}
	
	/**编辑记录*/
	public enum State4EditRecord {
		待审核(0), 审核通过(1), 审核未通过(2);
		
		private int code;
		private State4EditRecord(int code) {
			this.code = code;
		}
		public int code() {
			return code;
		}
	}
	
	/**排期申请作废*/
	public enum State4AppCancel {
		待主管审核(1), 待媒介审核(2), 主管审核未通过(3), 媒介审核未通过(4), 同意作废(5);
		
		private int code;
		private State4AppCancel(int code) {
			this.code = code;
		}
		public int code() {
			return code;
		}
	}
	
	/**排期回款申请*/
	public enum State4OASReturn {
		待确认(1), 驳回(2), 待复核(3), 已回款(4);
		
		private int code;
		private State4OASReturn(int code) {
			this.code = code;
		}
		public int code() {
			return code;
		}
	}

	/**绩效*/
	public enum State4Assess {
		未考核(0), 考核中(1),待确认(2),已考核(3);

		private int code;
		private State4Assess(int code) {
			this.code = code;
		}
		public int code() {
			return code;
		}
	}
	
	/**工作任务*/
	public enum State4WorkTask {
		延期(1), 进行中(2), 延期完成(3), 完成(4), 作废(5);
		
		private int code;
		private State4WorkTask(int code) {
			this.code = code;
		}
		public int code() {
			return code;
		}
	}

	/**
	 * 商品状态
	 */
	public enum commodityState {
		未审核(1), 待买手审核(2), 待主管审核(3), 待财务审核(4),已审过(5),未审过(6);

		private int code;

		private commodityState(int code) {
			this.code = code;
		}

		public int code() {
			return code;
		}
	}

	/**
	 * 赛峰档期状态
	 */
	public enum sfOrderState {
		待开团(1), 未开团(2), 已开团(3);

		private int code;

		private sfOrderState(int code) {
			this.code = code;
		}

		public int code() {
			return code;
		}
	}

	/**询号*/
	public enum State4Xunhao {
		发起(0), 已答复(1); // 已确认(2)
		
		private int code;
		private State4Xunhao(int code) {
			this.code = code;
		}
		public int code() {
			return code;
		}
	}
}
