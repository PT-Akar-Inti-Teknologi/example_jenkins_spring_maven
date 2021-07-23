package com.akarinti.preapproved.utils.exception;

import com.akarinti.preapproved.dto.StatusCodeMessageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class CustomException extends RuntimeException {

    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    private Object errorMessage;
    private StatusCode statusCode;

	public CustomException(){
		super();
	}

    public CustomException(StatusCode statusCode) {
        super(statusCode.message().toString());
        this.status = statusCode.httpStatus();
        this.errorMessage = statusCode.message().toString();
        this.statusCode = statusCode;
    }

    public CustomException(StatusCode statusCode, StatusCodeMessageDTO message) {
        super(statusCode.message().toString());
        this.status = statusCode.httpStatus();
        this.errorMessage = message;
        this.statusCode = statusCode;
    }

}
