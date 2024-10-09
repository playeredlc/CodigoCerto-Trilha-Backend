package app.edlc.taskapi.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.edlc.taskapi.security.data.AccountCredentialsDto;
import app.edlc.taskapi.security.data.TokenDto;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	AuthService authService;
	
	@PostMapping(value = "/login")
	public ResponseEntity<?> login(@RequestBody AccountCredentialsDto credentials) {		
		if (ValidationUtil.isCredentialsNullOrBlank(credentials))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request. Missing username or password.");
		
		ResponseEntity<TokenDto> responseToken = authService.login(credentials);
		
		if (responseToken.getBody() == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");			
			
		return responseToken;
	}
	
	@PostMapping(value = "/refresh/{username}")
	public ResponseEntity<?> refreshToken(
			@PathVariable String username,
			@RequestHeader(name = "Authorization") String refreshToken) {
		
		if (ValidationUtil.isStrNullOrBlank(username) || ValidationUtil.isStrNullOrBlank(refreshToken)) 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request. Missing username or refresh token.");
		
		ResponseEntity<TokenDto> responseToken = authService.refreshToken(username, refreshToken);
		
		if(responseToken == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or refresh token.");
			
		return responseToken;
	}
}
