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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoint para a Autenticação de usuário")
public class AuthController {
	
	@Autowired
	AuthService authService;
	
	@PostMapping(value = "/login")
	@Operation(summary = "FAZER LOGIN", description = "Faz login do usuário e gera um JWT utilizado para acessar recursos protegidos", tags = "Autenticação",
		responses = {
				@ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = TokenDto.class))),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
		})
	public ResponseEntity<?> login(@RequestBody AccountCredentialsDto credentials) {		
		if (ValidationUtil.isCredentialsNullOrBlank(credentials))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request. Missing username or password.");
		
		ResponseEntity<TokenDto> responseToken = authService.login(credentials);
		
		if (responseToken.getBody() == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");			
			
		return responseToken;
	}
	
	@PostMapping(value = "/refresh/{username}")
	@Operation(summary = "REFRESH TOKEN", description = "Gera um novo JWT utilizando o RefreshToken, sem a necessidade de realizar o login novamente.", tags = "Autenticação",
		responses = {
				@ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = TokenDto.class))),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
		})
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
