package com.akarinti.preapproved.dto;

import com.akarinti.preapproved.utils.exception.StatusCode;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Data
public class ResultDTO {

    protected String code;

    protected Object message;

    protected Object result;

    public ResultDTO() {
    }

    public ResultDTO(Object result) {
    }

    public ResultDTO(Object message, Object result) {
        this.code = "BO-"+ HttpStatus.OK.value();
        this.message = message;
        this.result = result;
    }

    public ResultDTO(Object message, Object result, String code) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public ResultDTO(StatusCode statusCode, Object result) {
        this.code = "BO-"+ statusCode.httpStatus().value();
        this.message = statusCode.message();
        this.result = result;
    }
}
