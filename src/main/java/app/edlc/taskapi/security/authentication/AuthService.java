package app.edlc.taskapi.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import app.edlc.taskapi.security.data.AccountCredentialsDto;
import app.edlc.taskapi.security.data.TokenDto;
import app.edlc.taskapi.security.jwt.JwtTokenProvider;
import app.edlc.taskapi.user.UserRepository;
import app.edlc.taskapi.user.data.User;

@Service
public class AuthService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtTokenProvider tokenProvider;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public ResponseEntity<TokenDto> login(AccountCredentialsDto credentials) {
		try {			
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							credentials.getUsername(),
							credentials.getPassword())
			);			
			TokenDto tokenResponse = new TokenDto();			
			if(userRepository.existsByUsername(credentials.getUsername())) {
				User user = userRepository.findByUsername(credentials.getUsername());
				tokenResponse = tokenProvider.createAccessToken(user.getUsername(), user.getRoles());
			} else {
				throw new UsernameNotFoundException(credentials.getUsername() + " username does not exist.");
			}			
			return ResponseEntity.ok(tokenResponse);
			
		} catch (Exception e) {
			throw new BadCredentialsException("Invalid username or password.");
		}
	}
}
