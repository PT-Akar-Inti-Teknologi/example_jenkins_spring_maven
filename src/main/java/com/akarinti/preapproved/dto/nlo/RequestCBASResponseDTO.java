package com.akarinti.preapproved.dto.nlo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestCBASResponseDTO {

    @JsonProperty(value="request_id")
    private String requestId;

    @JsonProperty(value="cbas_id")
    private String cbasId;


}
