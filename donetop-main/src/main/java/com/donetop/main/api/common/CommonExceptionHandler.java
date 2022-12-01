package com.donetop.main.api.common;

import com.donetop.main.api.common.Response.BadRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class CommonExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BadRequest<String>> handleException(final Exception e) {
        return ResponseEntity.badRequest().body(BadRequest.of(e.getMessage()));
    }

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<BadRequest<List<ObjectError>>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
		return ResponseEntity.badRequest().body(BadRequest.of(e.getAllErrors()));
	}

}
