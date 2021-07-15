package com.akarinti.preapproved.dto.authentication.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenSignInRequestDTO {
    @JsonProperty(value="username")
    private String username;

    @JsonProperty(value="password")
    private String password;
}
