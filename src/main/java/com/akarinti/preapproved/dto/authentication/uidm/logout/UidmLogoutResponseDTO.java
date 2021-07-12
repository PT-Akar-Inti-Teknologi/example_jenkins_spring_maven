package com.akarinti.preapproved.dto.authentication.uidm.logout;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UidmLogoutResponseDTO {

    @JsonProperty(value="logout_status")
    private String logoutStatus;
}
