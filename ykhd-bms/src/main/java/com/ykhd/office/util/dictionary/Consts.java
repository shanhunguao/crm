package com.ykhd.office.util.dictionary;

/**
 * system constants
 */
public final class Consts {

	/**登陆凭证header中的参数名*/
	public final static String AUTH_TOKEN = "auth-token";
	/**用户登陆有效时长（秒 ）*/
	public final static long LOGIN_EXPIRE = 14400L; // 4h
	/**token缓存key名前缀*/
	public final static String TOKEN_PREFIX = "t_";
	/**标记登陆用户token缓存key名前缀*/
	public final static String LOGIN_TOKEN_PREFIX = "login_";
	/**标记登陆用户菜单权限缓存key名前缀*/
	public final static String LOGIN_AUTH_PREFIX = "auth_";
	
	/**ContentType数据类型及编码*/
	public final static String TEXT_HTML_UTF8 = "text/html;charset=UTF-8";
	public final static String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";
	public final static String TEXT_EVENT_STREAM_UTF8 = "text/event-stream;charset=UTF-8";
	
	/**优客银行账户*/
	public final static String yk_bankName = "招商银行杭州申花小微企业专营支行";
	public final static String yk_accountHolder = "杭州优客互动网络科技有限公司";
	public final static String yk_accountNumber = "571912148010601";
	public final static String yk_payAccount = "公账";

	/**未创建过排期的公众号map在缓存中的key名*/
	public final static String UNUSED_OA = "unused_oa";



}
