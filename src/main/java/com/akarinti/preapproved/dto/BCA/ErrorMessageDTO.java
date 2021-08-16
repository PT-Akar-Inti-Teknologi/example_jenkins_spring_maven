package com.akarinti.preapproved.dto.BCA;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ErrorMessageDTO {
    @JsonProperty(value = "indonesian")
    private String indonesian;

    @JsonProperty(value = "english")
    private String english;
}
