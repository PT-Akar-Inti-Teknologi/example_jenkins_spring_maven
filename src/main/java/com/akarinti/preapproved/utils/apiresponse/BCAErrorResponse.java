package com.akarinti.preapproved.utils.apiresponse;

import lombok.Data;

import java.util.Map;

@Data
public class BCAErrorResponse {
	private String error_code;
	private Map<String, Object> error_message;
}
