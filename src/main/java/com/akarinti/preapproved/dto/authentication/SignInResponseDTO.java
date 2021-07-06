package com.akarinti.preapproved.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


@Data
public class SignInResponseDTO {

    @JsonProperty(value="access_token")
    private String accessToken;

    @JsonProperty(value="profile_internal")
    private ProfileUserDTO profileUserInternal;

    @JsonProperty(value="roles")
    private List<String> roles;

}
