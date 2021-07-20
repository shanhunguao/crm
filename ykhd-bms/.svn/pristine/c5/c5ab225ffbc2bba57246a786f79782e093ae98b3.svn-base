package com.ykhd.office;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.ykhd.office.component.interceptor.AuthorityInterceptor;
import com.ykhd.office.component.interceptor.LoginInterceptor;
import com.ykhd.office.domain.bean.common.ResponseMsg;
import com.ykhd.office.util.dictionary.SystemEnums.ResponseCode;

import cn.gjing.tools.excel.driven.EnableExcelDrivenMode;

/**
 * 启动类及配置项
 */
@SpringBootApplication
@MapperScan("com.ykhd.office.repository")
@EnableTransactionManagement
@EnableExcelDrivenMode
public class Application_ykhd implements WebMvcConfigurer {
	
	private static Logger log = LoggerFactory.getLogger(Application_ykhd.class);

	public static void main(String[] args) {
		SpringApplication.run(Application_ykhd.class, args);
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*");
	}
	
	@Bean
	public LoginInterceptor loginInterceptor() {
		return new LoginInterceptor();
	}
	@Bean
	public AuthorityInterceptor authorityInterceptor() {
		return new AuthorityInterceptor();
	}
	
	/**
	 *  注册拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginInterceptor()).addPathPatterns("/**").excludePathPatterns("/login/**")
			.excludePathPatterns("/*.html", "/img/**", "/css/**", "/js/**", "/resource/**");
		registry.addInterceptor(authorityInterceptor()).addPathPatterns("/**").excludePathPatterns("/login/**")
			.excludePathPatterns("/*.html", "/img/**", "/css/**", "/js/**", "/resource/**");
	}
	
	@RestControllerAdvice
	public class HandleControllerAdvice implements ResponseBodyAdvice<Object> {
		
		@ExceptionHandler(DataAccessException.class)
		public ResponseMsg DataAccess(Exception e) {
    		log.error("数据访问错误：----> " + e.getMessage());
			return new ResponseMsg(ResponseCode.FAILURE.getCode(), "数据访问错误，请联系管理员", null);
		}
		
		@ExceptionHandler(RuntimeException.class)
		public ResponseMsg Runtruntime(Exception e) {
			return new ResponseMsg(ResponseCode.FAILURE.getCode(), e.getMessage(), null);
		}
		
		@ExceptionHandler(BindException.class)
		public ResponseMsg bind(BindException e) {
			return new ResponseMsg(ResponseCode.FAILURE.getCode(), e.getBindingResult().getFieldError().getDefaultMessage(), null);
		}
		
		@Override
		public boolean supports(MethodParameter arg0, Class<? extends HttpMessageConverter<?>> arg1) {
			return true;
		}
		
		@Override
		public Object beforeBodyWrite(Object obj, MethodParameter arg1, MediaType arg2,
				Class<? extends HttpMessageConverter<?>> arg3, ServerHttpRequest arg4, ServerHttpResponse arg5) {
			if (obj instanceof ResponseMsg)
				return obj;
			else
				return new ResponseMsg(ResponseCode.SUCCESS.getCode(), null, obj);
		}
	}
	
	@Bean
	public FilterRegistrationBean<Filter> corsFilter() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin("*");
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);
		return new FilterRegistrationBean<Filter>(new CorsFilter(source));
	}
	
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		List<ISqlParser> sqlParserList = new ArrayList<>();
		sqlParserList.add(new BlockAttackSqlParser()); // 攻击SQL阻断解析器 ，加入解析链
		paginationInterceptor.setSqlParserList(sqlParserList);
		return paginationInterceptor;
	}

}
