package com.akarinti.preapproved.utils.exception;

import com.akarinti.preapproved.dto.StatusCodeMessageDTO;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

public enum StatusCode {
    OK(200, "BO-200", new StatusCodeMessageDTO("sukses", "success")),
    INVALID_ARGUMENT(400, "BO-400", new StatusCodeMessageDTO("argumen tidak valid", "invalid argument")),
    UNAUTHORIZED(401, "BO-401", new StatusCodeMessageDTO("tidak punya akses", "Unauthorized")),
    INVALID_JSON(400, "BO-490", new StatusCodeMessageDTO("struktur atau format JSON tidak valid", "invalid JSON structure or format"));

    private final int code;
    private final String codeDesc;
    private final Object message;

    private StatusCode(int value, String codeDesc, Object message) {
        this.code = value;
        this.codeDesc = codeDesc;
        this.message = message;
    }

    public int code() {
        return this.code;
    }

    public String codeDesc() {
        return this.codeDesc;
    }
    public Object message() { return this.message; }
}
