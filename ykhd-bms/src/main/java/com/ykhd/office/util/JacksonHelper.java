package com.ykhd.office.util;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Easy to use for Jackson
 */
public final class JacksonHelper {

	private static ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * obj -> jsonStr
	 */
	public static String toJsonStr(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("json parse error!");
		}
	}
	
	/**
	 * jsonStr --> obj
	 */
	public static <T> T toParseObj(String jsonStr, Class<T> clas) {
		try {
			return mapper.readValue(jsonStr, clas);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("json parse error!");
		}
		
	}
	
	/**
	 * jsonStr --> list<obj>
	 */
	public static <T> List<T> toParseObjArray(String jsonStr, Class<T> clas) {
		JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, clas);
		try {
			return mapper.readValue(jsonStr, javaType);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("json parse error!");
		}
	}
}
