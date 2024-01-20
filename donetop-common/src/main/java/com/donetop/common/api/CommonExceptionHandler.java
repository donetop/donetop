package com.donetop.common.api;

import com.donetop.common.api.Response.BadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Arrays;

import static com.donetop.common.api.Message.ASK_ADMIN;
import static com.donetop.common.api.Message.FILE_SIZE_EXCEED;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BadRequest<String>> handleException(final Exception e) {
		final String message = e.getMessage();
		log.warn("[handleException] Error occurred. message : {}", message);
		boolean hasMatchedField = Message.getFieldValues().stream().anyMatch(message::startsWith);
        return ResponseEntity.badRequest().body(BadRequest.of(hasMatchedField ? message : ASK_ADMIN));
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
		return ResponseEntity.badRequest().body(BadRequest.of(FILE_SIZE_EXCEED));
	}

}
