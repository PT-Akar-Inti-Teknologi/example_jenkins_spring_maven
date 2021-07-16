package com.akarinti.preapproved.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

@Data
public class StatusCodeMessageDTO {

    private String indonesian;
    private String english;

    public StatusCodeMessageDTO() {
    }

    public StatusCodeMessageDTO(String indonesian, String english) {
        this.indonesian = indonesian;
        this.english = english;
    }

    @Override
    public String toString() {
        String result = this.indonesian + "," + this.english;
        return result;
    }
}
