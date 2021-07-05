package com.akarinti.preapproved.utils.apiresponse;

import lombok.Data;

@Data
public class BCAOauth2Response {
	private String access_token;
	private String token_type;
	private int expires_in;
	private String scope;
}
