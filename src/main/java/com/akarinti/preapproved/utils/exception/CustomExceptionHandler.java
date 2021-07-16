package com.akarinti.preapproved.utils.exception;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Data
@Slf4j
public class CustomExceptionHandler extends RuntimeException {

    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    private Object errorMessage;
    private StatusCode statusCode;

	public CustomExceptionHandler(){
		super();
	}

    public CustomExceptionHandler(Object message){
		super((String) message);
	}

    public CustomExceptionHandler(HttpStatus code, Object message, StatusCode statusCode) {
        super(message.toString());
        log.info("message: "+ message.toString());
        this.status = code;
        this.errorMessage = message;
        this.statusCode = statusCode;
    }

}
