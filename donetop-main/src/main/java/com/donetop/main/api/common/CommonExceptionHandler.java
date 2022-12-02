package com.donetop.main.api.common;

import com.donetop.main.api.common.Response.BadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BadRequest<String>> handleException(final Exception e) {
		log.warn("Error occurred. message : {}", e.getMessage());
        return ResponseEntity.badRequest().body(BadRequest.of(e.getMessage()));
    }

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<BadRequest<String[]>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
		final String[] defaultErrorMessages = e.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toArray(String[]::new);
		log.warn("Error occurred. messages : {}", Arrays.toString(defaultErrorMessages));
		return ResponseEntity.badRequest().body(BadRequest.of(defaultErrorMessages));
	}

}
