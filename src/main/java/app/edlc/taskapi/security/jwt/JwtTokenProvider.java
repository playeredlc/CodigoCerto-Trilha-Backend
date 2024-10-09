package app.edlc.taskapi.security.jwt;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import app.edlc.taskapi.security.data.TokenDto;
import app.edlc.taskapi.security.exception.InvalidJwtAuthenticationException;
import jakarta.annotation.PostConstruct;

@Service
public class JwtTokenProvider {
	
	@Value("${ security.jwt.token.secret-key:secret }")
	private String secretKey;	
	
	@Value("${ security.jwt.token.expire-length:3600000 }")
	private long validityInMilliseconds;
	
	@Value("${ security.jwt.token.refresh_factor:3 }")
	private int refreshFactor;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	private Algorithm algorithm;
	
	@PostConstruct
	protected void init() {
		secretKey = KeyUtil.ensureBase64Key(secretKey);
		algorithm = Algorithm.HMAC256(secretKey.getBytes());
	}
	
	public TokenDto createAccessToken(String username, List<String> roles) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds);
		
		String accessToken = generateToken(username, roles, now, validity);
		String refreshToken = generateRefreshToken(username, roles, now);
		
		return new TokenDto(username, true, now, validity, accessToken, refreshToken);
	}
	
	public TokenDto refreshToken(String refreshToken) {
		if (refreshToken != null && refreshToken.startsWith("Bearer "))
			refreshToken = refreshToken.substring("Bearer ".length());
		
		DecodedJWT decodedRefresh = validateAndDecodeToken(refreshToken);
		
		return createAccessToken(
				decodedRefresh.getSubject(),
				decodedRefresh.getClaim("roles").asList(String.class));		
	}
	
	private DecodedJWT validateAndDecodeToken(String refreshToken) {
		JWTVerifier verifier = JWT.require(algorithm).build();
		try {
			return verifier.verify(refreshToken);
		} catch (Exception e) {
			throw new InvalidJwtAuthenticationException();
		}
	}
	
	private String generateToken(String username, List<String> roles, Date now, Date validity) {
		// TODO Auto-generated method stub
		return null;
	}

	private String generateRefreshToken(String username, List<String> roles, Date now) {
		// TODO Auto-generated method stub
		return null;
	}
}
