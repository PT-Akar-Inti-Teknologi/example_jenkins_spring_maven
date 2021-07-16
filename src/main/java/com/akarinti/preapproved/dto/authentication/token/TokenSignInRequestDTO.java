package com.akarinti.preapproved.dto.authentication.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TokenSignInRequestDTO {
    @NotNull
    @JsonProperty(value="username")
    private String username;

    @NotNull
    @JsonProperty(value="password")
    private String password;
}
