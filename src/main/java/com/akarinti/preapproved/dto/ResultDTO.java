package com.akarinti.preapproved.dto;

import com.akarinti.preapproved.utils.exception.StatusCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultDTO {

    @JsonProperty(value = "code")
    protected String code;

    @JsonProperty(value = "message")
    protected Object message;

    @JsonProperty(value = "meta_pagination")
    protected MetaPaginationDTO metaPaginationDTO;

    @JsonProperty(value = "result")
    protected Object result;

    public ResultDTO() {
    }

    public ResultDTO(Object result) {
        this.code = StatusCode.OK.codeDesc();
        this.message = StatusCode.OK.message();
        this.result = result;
    }


    public ResultDTO(StatusCode statusCode, Object result) {
        this.code = statusCode.codeDesc();
        this.message = statusCode.message();
        this.result = result;
    }

    public ResultDTO(StatusCode statusCode, Object message, Object result) {
        this.code = statusCode.codeDesc();
        this.message = message;
        this.result = result;
    }

    public ResultDTO(MetaPaginationDTO meta, Object result) {
        this.code = "BO-" + StatusCode.OK.httpStatus().value();
        this.metaPaginationDTO = meta;
        this.message = StatusCode.OK.message();
        this.result = result;
    }
}
