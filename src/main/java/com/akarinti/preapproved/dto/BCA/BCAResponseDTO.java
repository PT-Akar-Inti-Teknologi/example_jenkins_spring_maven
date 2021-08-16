package com.akarinti.preapproved.dto.BCA;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BCAResponseDTO {

    @JsonProperty("error_schema")
    private ErrorSchemaDTO errorSchema;

    @JsonProperty("output_schema")
    private Object outputSchema;

}
