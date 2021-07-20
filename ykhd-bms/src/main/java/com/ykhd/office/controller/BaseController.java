package com.ykhd.office.controller;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.HtmlUtils;

import com.ykhd.office.domain.bean.LoginCache;
import com.ykhd.office.domain.bean.common.ResponseMsg;
import com.ykhd.office.util.dictionary.Consts;
import com.ykhd.office.util.dictionary.SystemEnums.ResponseCode;

public class BaseController {

	/**
	 * 操作成功
	 * @param data
	 * @return
	 */
	protected ResponseMsg success(Object data) {
		return new ResponseMsg(ResponseCode.SUCCESS.getCode(), null, data);
	}
	
	/**
	 * 操作失败
	 * @param msg
	 * @return
	 */
	protected ResponseMsg failure(String msg) {
		return new ResponseMsg(ResponseCode.FAILURE.getCode(), msg, null);
	}
	
	/**
	 * 获取登陆者信息
	 */
	public static LoginCache getManagerInfo() {
		return (LoginCache) ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getAttribute(Consts.AUTH_TOKEN);
	}
	
	public final static String nullValue = "参数空值";
	public final static String illegal = "参数非法";
	public final static String save_failure = "保存失败";
	public final static String update_failure = "修改失败";
	public final static String delete_failure = "删除失败";
	
	/**
	 * 验证是否输入特殊字符
	 */
	protected boolean htmlEscape(String param) {
		if (param.equals(HtmlUtils.htmlEscape(param)))
			return true;
		return false;
	}
	
}
