package com.donetop.main.api.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class CommonExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error<String>> handleException(final Exception e) {
        return ResponseEntity.badRequest().body(new Error<>(e.getMessage()));
    }

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Error<List<ObjectError>>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
		return ResponseEntity.badRequest().body(new Error<>(e.getAllErrors()));
	}

    @RequiredArgsConstructor
	@Getter @Setter
    public static class Error<INFO> {

        private final INFO info;

    }

}
