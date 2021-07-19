package com.akarinti.preapproved.utils.exception;

import com.akarinti.preapproved.dto.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<Object> customExceptionHandler(CustomException ex, WebRequest request) {
        final ResultDTO resultDTO = new ResultDTO(ex.getErrorMessage(), null, ex.getStatusCode().codeDesc());
        return new ResponseEntity(resultDTO, new HttpHeaders(), ex.getStatus());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        log.info("internal server error: "+ ex.getLocalizedMessage());
        if (ex instanceof NullPointerException) {
            final ResultDTO resultDTO = new ResultDTO(StatusCode.INVALID_ARGUMENT, ex.getMessage());
            return new ResponseEntity(resultDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        final ResultDTO resultDTO = new ResultDTO(StatusCode.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity(resultDTO, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final ResultDTO resultDTO = new ResultDTO(StatusCode.INVALID_JSON, null);
        return new ResponseEntity(resultDTO, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final ResultDTO resultDTO = new ResultDTO(StatusCode.INVALID_ARGUMENT, body);
        logger.info("class: "+ ex.getClass().getName());
        return new ResponseEntity(resultDTO, headers, status);
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final List<HashMap<String, String>> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            HashMap<String, String> fieldError = new HashMap<>();
            fieldError.put("field", error.getField());
            fieldError.put("message", error.getDefaultMessage());
            fieldError.put("message_summary", error.getField() + " " + error.getDefaultMessage());
            errors.add(fieldError);
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            HashMap<String, String> fieldError = new HashMap<>();
            fieldError.put("field", error.getObjectName());
            fieldError.put("message", error.getDefaultMessage());
            fieldError.put("message_summary", error.getObjectName() + " " + error.getDefaultMessage());
            errors.add(fieldError);
        }
        final ResultDTO resultDTO = new ResultDTO(StatusCode.INVALID_ARGUMENT, errors);
        return new ResponseEntity(resultDTO, headers, status);
    }
}
