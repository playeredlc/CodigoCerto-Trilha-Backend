package app.edlc.taskapi.security.jwt;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import app.edlc.taskapi.security.data.TokenDto;
import app.edlc.taskapi.security.exception.InvalidJwtAuthenticationException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

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
	
	public Authentication getAuthentication(String token) {
		DecodedJWT decodedJWT = validateAndDecodeToken(token);
		UserDetails userDetails = userDetailsService
				.loadUserByUsername(decodedJWT.getSubject());
		
		return new UsernamePasswordAuthenticationToken(decodedJWT, "", userDetails.getAuthorities());
	}
	
	// create TokenDto which will have the actual access and refresh tokens as attributes
	public TokenDto createAccessToken(String username, List<String> roles) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds);
		
		String accessToken = generateToken(username, roles, now, validity);
		String refreshToken = generateRefreshToken(username, roles, now);
		
		return new TokenDto(username, true, now, validity, accessToken, refreshToken);
	}
	
	// create a new TokenDTO given a valid refresh token
	public TokenDto refreshToken(String refreshToken) {
		if (refreshToken != null && refreshToken.startsWith("Bearer "))
			refreshToken = refreshToken.substring("Bearer ".length());
		
		DecodedJWT decodedRefresh = validateAndDecodeToken(refreshToken);
		
		return createAccessToken(
				decodedRefresh.getSubject(),
				decodedRefresh.getClaim("roles").asList(String.class));		
	}
	
	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		
		if(bearerToken != null && bearerToken.startsWith("Bearer "))
			return bearerToken.substring("Bearer ".length());
		
		return null;
	}
	
	public boolean isTokenValid(String token) {		
		try {
			DecodedJWT decodedJWT = validateAndDecodeToken(token);			
			if (decodedJWT.getExpiresAt().before(new Date()))
				return false;			
		} catch (Exception e) {
			throw new InvalidJwtAuthenticationException();
		}		
		return true;	
	}
	
	// Generate the actual AccessToken which is an attribute of TokenDto
	private String generateToken(String username, List<String> roles, Date now, Date validity) {
		String issuerUrl = ServletUriComponentsBuilder
				.fromCurrentContextPath()
				.build()
				.toUriString();
		
		return JWT.create()
				.withClaim("roles", roles)
				.withIssuedAt(now)
				.withExpiresAt(validity)
				.withSubject(username)
				.withIssuer(issuerUrl)
				.sign(algorithm)
				.strip();
	}
	
	// Generate the actual RefreshToken which is an attribute of TokenDto
	private String generateRefreshToken(String username, List<String> roles, Date now) {
		Date validityRefreshToken = new Date((now.getTime() + validityInMilliseconds) * refreshFactor);
		
		return JWT.create()
				.withClaim("roles", roles)
				.withIssuedAt(now)
				.withExpiresAt(validityRefreshToken)
				.withSubject(username)
				.sign(algorithm)
				.strip();
	}
	
	private DecodedJWT validateAndDecodeToken(String token) {
		JWTVerifier verifier = JWT.require(algorithm).build();
		try {
			return verifier.verify(token);
		} catch (Exception e) {
			throw new InvalidJwtAuthenticationException();
		}
	}
	
	public String extractSubject(String token) {
		if(token != null && token.startsWith("Bearer "))
			token = token.substring("Bearer ".length());
		
		return validateAndDecodeToken(token).getSubject();		
	}
}
