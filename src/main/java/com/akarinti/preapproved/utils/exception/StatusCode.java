package com.akarinti.preapproved.utils.exception;

import com.akarinti.preapproved.dto.StatusCodeMessageDTO;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

public enum StatusCode {
    OK(HttpStatus.OK, "BO-200", new StatusCodeMessageDTO("sukses", "success")),
    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "BO-400", new StatusCodeMessageDTO("argumen tidak valid", "invalid argument")),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "BO-401", new StatusCodeMessageDTO("kredensial yang diberikan salah", "invalid credentials")),
    FORBIDDEN(HttpStatus.FORBIDDEN, "BO-401", new StatusCodeMessageDTO("tidak berhak mengakses", "forbidden")),
    INVALID_JSON(HttpStatus.BAD_REQUEST, "BO-490", new StatusCodeMessageDTO("struktur atau format JSON tidak valid", "invalid JSON structure or format")),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "BO-500", new StatusCodeMessageDTO("terjadi kesalahan internal server", "internal server error")),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "BO-405", new StatusCodeMessageDTO("metode http tidak diijinkan", "http method not allowed")),
    NOT_FOUND(HttpStatus.NOT_FOUND, "BO-404", new StatusCodeMessageDTO("data tidak ditemukan", "data not found"));

    private final HttpStatus httpStatus;
    private final String codeDesc;
    private final Object message;

    StatusCode(HttpStatus httpStatus, String codeDesc, Object message) {
        this.httpStatus = httpStatus;
        this.codeDesc = codeDesc;
        this.message = message;
    }

    public HttpStatus httpStatus() {
        return this.httpStatus;
    }

    public String codeDesc() {
        return this.codeDesc;
    }
    public Object message() { return this.message; }
}
