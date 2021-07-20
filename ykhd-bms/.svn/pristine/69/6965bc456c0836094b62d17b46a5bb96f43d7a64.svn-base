package com.ykhd.office.component.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ykhd.office.component.RedisService;
import com.ykhd.office.domain.bean.LoginCache;
import com.ykhd.office.domain.bean.common.ResponseMsg;
import com.ykhd.office.util.JacksonHelper;
import com.ykhd.office.util.dictionary.Consts;
import com.ykhd.office.util.dictionary.SystemEnums.ResponseCode;

/**
 * 登陆拦截器
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        String token = null;
        String uri = request.getRequestURI();
        if (uri.contains("/msg_sent")) { //参数形式为?key=value
            String[] tokenParameter = request.getParameterMap().get("token");
            if (tokenParameter != null && tokenParameter.length > 0)
                token = tokenParameter[0];
        } else
            token = request.getHeader(Consts.AUTH_TOKEN);
        if (token != null) {
            String jsonStr = redisService.getValue(Consts.TOKEN_PREFIX + token);
            if (jsonStr != null) {
                LoginCache loginCache = JacksonHelper.toParseObj(jsonStr, LoginCache.class);
                request.setAttribute(Consts.AUTH_TOKEN, loginCache);
                redisService.setExpire(Consts.TOKEN_PREFIX + token, Consts.LOGIN_EXPIRE);
                redisService.setExpire(Consts.LOGIN_TOKEN_PREFIX + loginCache.getId(), Consts.LOGIN_EXPIRE);
                redisService.setExpire(Consts.LOGIN_AUTH_PREFIX + loginCache.getId(), Consts.LOGIN_EXPIRE);
                return true;
            }
        }
        response.setContentType(Consts.APPLICATION_JSON_UTF8);
        response.getWriter().println(JacksonHelper.toJsonStr(new ResponseMsg(ResponseCode.UNLOGIN.getCode(), ResponseCode.UNLOGIN.name(), null)));
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
