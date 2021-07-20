package com.ykhd.office;

import java.io.IOException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {

	public static <T> T exchange(String url, HttpMethod httpMethod, MultiValueMap<String, String> headerParam, MultiValueMap<String, Object> bodyParam, Class<T> responseType) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseErrorHandler responseErrorHandler = new ResponseErrorHandler() {
			@Override
			public boolean hasError(ClientHttpResponse response) throws IOException {
				return true;
			}
			@Override
			public void handleError(ClientHttpResponse response) throws IOException {}
		};
		restTemplate.setErrorHandler(responseErrorHandler);
		
		HttpHeaders headers = new HttpHeaders();
		if (headerParam != null)
			headers.addAll(headerParam);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		//headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(bodyParam, headers);
		ResponseEntity<T> responseEntity = restTemplate.exchange(url, httpMethod, entity, responseType);
		Assert.state(responseEntity.getStatusCodeValue() == 200, url + " --> " + responseEntity.getStatusCode().toString());
		return responseEntity.getBody();
	}
	
	public static void main(String[] args) throws IOException {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
		header.add("auth-token", "612f4d156acb40a794129dc23ce36e42");
		
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		
//		String url = "http://192.168.31.226:9999/api4bms/login/account";
//		body.add("username", "薛雨方");
//		body.add("password", "GBaUFI5kx37MDiRxSzuJsQDnithOBU9b4xYJrTPmXixGjJ0AtFVMphwIpyC35D5GjzfCwc99Og0v3L8rPhW6oiTzaibz8pKftAqdhoxwSL+kAeZIYbvgt46TPQVoNWhaQMMx6DrR1bP/QpFlRoMDfRK+VbGYdsETsW9VvStIZIs=");
		
		String url = "http://localhost:9999/api4bms/xunhao";
		
//		body.add("medium", "1");
//		body.add("officeAccount", "2");
//		body.add("need", "需求~~~");
//		body.add("brand", "儿童教育~~~");
		
		String exchange = exchange(url, HttpMethod.GET, header, body, String.class);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.readValue(exchange, Object.class)));
	}
	
}