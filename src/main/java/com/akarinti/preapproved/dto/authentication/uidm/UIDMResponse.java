package com.akarinti.preapproved.dto.authentication.uidm;

import com.akarinti.preapproved.dto.authentication.uidm.userDetail.UserDetailResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UIDMResponse {
    @JsonProperty(value = "error_schema")
    private Object errorSchema;

    @JsonProperty(value = "output_schema")
    private Object outputSchema;
}
