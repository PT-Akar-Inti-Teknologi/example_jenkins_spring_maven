package com.akarinti.preapproved.dto.authentication.uidm.logout;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UidmLogoutRequestDTO {

    @JsonProperty(value="user_id")
    private String userId;
}
