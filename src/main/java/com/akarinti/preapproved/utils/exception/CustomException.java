package com.akarinti.preapproved.utils.exception;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Data
@Slf4j
public class CustomException extends RuntimeException {

    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    private Object errorMessage;
    private StatusCode statusCode;

	public CustomException(){
		super();
	}

    public CustomException(Object message){
		super((String) message);
	}

    public CustomException(HttpStatus code, Object message, StatusCode statusCode) {
        super(message.toString());
        log.info("message:s "+ message.toString());
        this.status = code;
        this.errorMessage = message;
        this.statusCode = statusCode;
    }

}
