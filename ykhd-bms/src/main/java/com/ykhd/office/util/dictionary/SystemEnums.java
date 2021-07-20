package com.ykhd.office.util.dictionary;

/**
 * system enums
 */
public final class SystemEnums {

	/**响应消息状态码*/
	public enum ResponseCode {
		UNLOGIN(0), SUCCESS(200), FAILURE(400);
		
		private int code;
		private ResponseCode(int code) {
			this.code = code;
		}
		public int getCode() {
			return code;
		}
	}
	
	/**
	 * OSS文件存储文件夹
	 */
	public enum OSSFolder {
		BMS, SF
	}
	
	/**角色查询标记*/
	public enum RoleSign {
		/**总经理*/
		general_mgr,
		/**广告部业务总监*/
		director,
		/**广告部主管*/
		dept,
		/**广告部组长*/
		group,
		/**广告部AE*/
		ae,
		/**广告部媒介*/
		medium,
		/**人事*/
		personnel,
		/**集采*/
		collector,
		/*媒介主管*/
		medium_director,
		/**其他*/
		other;
	}
	
}
