package com.akarinti.preapproved.utils.exception;

import com.akarinti.preapproved.dto.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<Object> customExceptionHandler(CustomException ex, WebRequest request) {
        log.info("CustomException: "+ ex);
        final ResultDTO resultDTO = new ResultDTO(ex.getErrorMessage(), null);
        return new ResponseEntity<>(resultDTO, new HttpHeaders(), ex.getStatus());
    }
    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<Object> handle(HttpServletRequest req, Exception ex) {
        log.info("internal server error: ");
        log.info("ex: "+ ex.getClass());
        log.error(ex.getLocalizedMessage(), ex);
//        if (ex instanceof NullPointerException) {
//            final ResultDTO resultDTO = new ResultDTO(StatusCode.INVALID_ARGUMENT, ex.getMessage());
//            return new ResponseEntity(resultDTO, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
        final ResultDTO resultDTO = new ResultDTO(StatusCode.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(resultDTO, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.info("handleHttpMessageNotReadable: "+ ex);
        final ResultDTO resultDTO = new ResultDTO(StatusCode.INVALID_JSON, null);
        return new ResponseEntity<>(resultDTO, headers, status);
    }


    @Override
    @NonNull
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, @Nullable HttpHeaders headers, @Nullable HttpStatus status, @Nullable WebRequest request) {
        ResultDTO resultDTO;
        log.info("exception: "+ ex);
        log.info("body: "+ body);
        log.info("status: "+ status);
        if (ex.getClass().equals(HttpRequestMethodNotSupportedException.class)) {
            resultDTO = new ResultDTO(StatusCode.METHOD_NOT_ALLOWED, body);
        } else {
            resultDTO = new ResultDTO(StatusCode.INVALID_ARGUMENT, body);
        }
        logger.info("class: "+ ex.getClass().getName());
        return new ResponseEntity<>(resultDTO, headers, status);
    }
    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @Nullable final HttpHeaders headers, @Nullable final HttpStatus status, @Nullable final WebRequest request) {
        log.info("MethodArgumentNotValidException: "+ ex);
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
        return new ResponseEntity<>(resultDTO, headers, status);
    }
}
