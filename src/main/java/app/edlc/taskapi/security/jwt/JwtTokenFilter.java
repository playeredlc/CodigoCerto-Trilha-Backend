package app.edlc.taskapi.security.jwt;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import app.edlc.taskapi.security.exception.InvalidJwtAuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenFilter extends GenericFilterBean {
	
	private final JwtTokenProvider tokenProvider;
	
	public JwtTokenFilter(JwtTokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		try {
			String token = tokenProvider.resolveToken( (HttpServletRequest) request);
			
			if (token != null && tokenProvider.isTokenValid(token)) {
				Authentication auth = tokenProvider.getAuthentication(token);
				if (auth != null)
					SecurityContextHolder.getContext().setAuthentication(auth);
			}		
		} catch (InvalidJwtAuthenticationException e) {
			// this exception must be handled here because it never gets to controller handler.
			HttpServletResponse res = (HttpServletResponse) response;
			res.setStatus(HttpStatus.UNAUTHORIZED.value());
			res.getWriter().write(e.getMessage());
			res.getWriter().flush();
			return;
		}
		
		chain.doFilter(request, response);
	}
}
