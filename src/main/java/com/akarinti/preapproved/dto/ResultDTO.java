package com.akarinti.preapproved.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Data
public class ResultDTO {

    private String code;

    private Object message;

    private Object result;

    public ResultDTO() {
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
}
