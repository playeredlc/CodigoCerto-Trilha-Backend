package app.edlc.taskapi.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import app.edlc.taskapi.security.exception.InvalidJwtAuthenticationException;
import app.edlc.taskapi.user.exception.UserNotFoundException;
import app.edlc.taskapi.user.exception.UsernameAlreadyExistsException;

/*
 *	Global Exception Handler
 */

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionResponse> handleGenericExceptions(Exception e, WebRequest request) {		
		ExceptionResponse exceptionResponse = new ExceptionResponse(
				new Date(),
				e.getMessage(),
				request.getDescription(false));
		
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);		
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public final ResponseEntity<ExceptionResponse> handleNotFoundException(Exception e, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(
				new Date(),
				e.getMessage(),
				request.getDescription(false));
		
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);		
	}
	
	@ExceptionHandler(UsernameAlreadyExistsException.class)
	public final ResponseEntity<ExceptionResponse> handleConflictException(Exception e, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(
				new Date(),
				e.getMessage(),
				request.getDescription(false));
		
		return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);		
	}
	
	@ExceptionHandler(InvalidJwtAuthenticationException.class)
	public final ResponseEntity<ExceptionResponse> handleInvalidJwtAuthenticationException(Exception e, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(
				new Date(),
				e.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public final ResponseEntity<ExceptionResponse> handleBadCredentialsException(Exception e, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(
				new Date(),
				e.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
	}	
}
