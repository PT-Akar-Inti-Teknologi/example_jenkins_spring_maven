package com.akarinti.preapproved.dto.authentication.uidm.userDetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDetailRequestDTO {
    @JsonProperty(value="login_session_id")
    private String loginSessionId;
}
