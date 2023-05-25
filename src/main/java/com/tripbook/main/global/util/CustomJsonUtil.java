package com.tripbook.main.global.util;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomJsonUtil {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static String JsonToString(Map<String, Object> value) throws JsonProcessingException {
		return objectMapper.writeValueAsString(value);
	}

	public static String ObjectToString(Object value) throws JsonProcessingException {
		return objectMapper.writeValueAsString(value);
	}

	public static Map<String, Object> StringToJson(String value) throws JsonProcessingException {
		return objectMapper.readValue(value, new TypeReference<HashMap<String, Object>>() {
		});

	}

}
