package com.ykhd.office.component.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ykhd.office.component.RedisService;
import com.ykhd.office.domain.bean.LoginCache;
import com.ykhd.office.domain.bean.common.ResponseMsg;
import com.ykhd.office.util.JacksonHelper;
import com.ykhd.office.util.dictionary.Consts;
import com.ykhd.office.util.dictionary.SystemEnums.ResponseCode;
import com.ykhd.office.util.menu.MenuAuthorityUtil;

/**
 * 权限拦截器
 */
public class AuthorityInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private RedisService redisService;
	@Value("${server.servlet.context-path}")
	private String contextPath;
	
	/**
	 * 去除项目名及参数id
	 */
	private String uriHandler(String uri) {
		uri = uri.replace(contextPath, "");
		int position = uri.lastIndexOf("/") + 1;
		String last = uri.substring(position);
		if (StringUtils.isNumeric(last))
			uri = uri.substring(0, position);
		return uri;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		LoginCache loginCache = (LoginCache) request.getAttribute(Consts.AUTH_TOKEN);
		String uri = uriHandler(request.getRequestURI());
		String auth = MenuAuthorityUtil.getNameByURI(uri, request.getMethod());
		if (auth == null)
			return true;
		if (redisService.inSet(Consts.LOGIN_AUTH_PREFIX + loginCache.getId(), auth))
			return true;
		response.setContentType(Consts.APPLICATION_JSON_UTF8);
		response.getWriter().println(JacksonHelper.toJsonStr(new ResponseMsg(ResponseCode.FAILURE.getCode(), "无操作权限", null)));
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}
}
