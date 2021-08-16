package com.akarinti.preapproved.dto.BCA;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ErrorSchemaDTO {
    @JsonProperty(value = "error_code")
    private String errorCode;

    @JsonProperty(value = "error_message")
    private ErrorMessageDTO errorMessage;
}
