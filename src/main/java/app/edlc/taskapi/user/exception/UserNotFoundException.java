package app.edlc.taskapi.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public UserNotFoundException (String message) {
		super(message);
	}	
	public UserNotFoundException () {
		super("Could not find user with this id.");
	}
}
