package com.akarinti.preapproved.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SignInRequestDTO {

    @JsonProperty(value="session_id")
    private String sessionId;

    @JsonProperty(value="user_id")
    private String userId;
}
