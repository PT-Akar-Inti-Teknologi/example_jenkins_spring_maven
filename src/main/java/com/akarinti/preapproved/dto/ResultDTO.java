package com.akarinti.preapproved.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ResultDTO {

    private String message;

    private Object result;

    public ResultDTO() {
    }

    public ResultDTO(String message, Object result) {
        this.message = message;
        this.result = result;
    }
}
