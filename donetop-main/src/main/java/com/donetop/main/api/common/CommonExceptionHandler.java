package com.donetop.main.api.common;

import com.donetop.main.api.common.Response.BadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BadRequest<String>> handleException(final Exception e) {
		log.warn("[handleException] Error occurred. message : {}", e.getMessage());
        return ResponseEntity.badRequest().body(BadRequest.of(e.getMessage()));
    }

	@ExceptionHandler(BindException.class)
	public ResponseEntity<BadRequest<String[]>> handleBindException(final BindException e) {
		final String[] defaultErrorMessages = e.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toArray(String[]::new);
		log.warn("[handleBindException] Error occurred. messages : {}", Arrays.toString(defaultErrorMessages));
		return ResponseEntity.badRequest().body(BadRequest.of(defaultErrorMessages));
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<BadRequest<String>> handleMaxUploadSizeExceededException(final MaxUploadSizeExceededException e) {
		log.warn("[handleMaxUploadSizeExceededException] Error occurred. message : {}", e.getMessage());
		return ResponseEntity.badRequest().body(BadRequest.of(e.getMessage()));
	}

}
