package app.edlc.taskapi.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.edlc.taskapi.security.jwt.JwtTokenProvider;
import app.edlc.taskapi.user.data.UserRequestDto;
import app.edlc.taskapi.user.data.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/user")
@Tag(name = "Usuário", description = "Endpoint para a criação de usuário")
public class UserController {

	@Autowired
	private UserService service;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@PostMapping(
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "CRIAR USUÁRIO", description = "Registra um novo usuário", tags = "Usuário",
		responses = {
				@ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Conflict", responseCode = "409", content = @Content),
				@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
		})
	public UserResponseDto create(@RequestBody UserRequestDto userDto) {
		return service.create(userDto);
	}
	
	@DeleteMapping("/{id}")
	@SecurityRequirement(name = "Authorization")
	@Operation(summary = "DELETAR USUÁRIO", description = "Deleta um usuário utilizando o id", tags = "Usuário",
		responses = {
				@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
				@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
		})
	public ResponseEntity<?> delete(
			@PathVariable Long id,
			@RequestHeader(name = "Authorization") @Parameter(hidden = true) String accessToken) {
		String username = tokenProvider.extractSubject(accessToken);
		
		return service.delete(id, username);
	}	
}
