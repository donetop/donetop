package com.donetop.common.api;

import com.donetop.common.api.Response.BadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Arrays;
import java.util.List;

import static com.donetop.common.api.Message.ASK_ADMIN;
import static com.donetop.common.api.Message.FILE_SIZE_EXCEED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

	private static final List<String> messageFieldValues = Message.getFieldValues();

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BadRequest<String>> handleException(final Exception e) {
		final String message = e.getMessage();
		log.warn("[handleException] Error occurred. message : {}", message);
		final boolean hasMatchedField = messageFieldValues.stream().anyMatch(message::startsWith);
        return ResponseEntity.badRequest().contentType(APPLICATION_JSON).body(BadRequest.of(hasMatchedField ? message : ASK_ADMIN));
    }

	@ExceptionHandler(BindException.class)
	public ResponseEntity<BadRequest<String[]>> handleBindException(final BindException e) {
		final String[] defaultErrorMessages = e.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toArray(String[]::new);
		log.warn("[handleBindException] Error occurred. messages : {}", Arrays.toString(defaultErrorMessages));
		return ResponseEntity.badRequest().contentType(APPLICATION_JSON).body(BadRequest.of(defaultErrorMessages));
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<BadRequest<String>> handleMaxUploadSizeExceededException(final MaxUploadSizeExceededException e) {
		log.warn("[handleMaxUploadSizeExceededException] Error occurred. message : {}", e.getMessage());
		return ResponseEntity.badRequest().contentType(APPLICATION_JSON).body(BadRequest.of(FILE_SIZE_EXCEED));
	}

	@ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
	public ResponseEntity<BadRequest<String>> handleHttpMediaTypeNotAcceptableException(final HttpMediaTypeNotAcceptableException e) {
		return ResponseEntity.badRequest().contentType(APPLICATION_JSON).body(BadRequest.of(ASK_ADMIN));
	}

	@ExceptionHandler(InsufficientAuthenticationException.class)
	public ResponseEntity<BadRequest<String>> handleInsufficientAuthenticationException(final InsufficientAuthenticationException e) {
		return ResponseEntity.badRequest().contentType(APPLICATION_JSON).body(BadRequest.of(ASK_ADMIN));
	}

}
