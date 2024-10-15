package app.edlc.taskapi.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.edlc.taskapi.security.jwt.JwtTokenProvider;
import app.edlc.taskapi.task.data.TaskDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/task/v1")
@Tag(name = "Tasks", description = "Endpoint para o gerenciamento de Tarefas")
@SecurityRequirement(name = "Authorization")
public class TaskController {
	
	@Autowired
	private TaskService service;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "BUSCAR TAREFA", description = "Busca uma tarefa utilizando o id", tags = "Tasks",
		responses = {
				@ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = TaskDto.class))),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
				@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
		})
	public ResponseEntity<TaskDto> findById(
			@PathVariable Long id,
			@RequestHeader(name = "Authorization") @Parameter(hidden = true) String accessToken) {
		String username = tokenProvider.extractSubject(accessToken);
		return service.findById(id, username);
	}
	
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "BUSCAR TODAS AS TAREFAS", description = "Busca todas as tarefas do usuário que realizou o request.", tags = "Tasks",
		responses = {
				@ApiResponse(description = "Success", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskDto.class)))),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)			
		})
	public ResponseEntity<List<TaskDto>> findAll(@RequestHeader(name = "Authorization") @Parameter(hidden = true) String accessToken) {
		String username = tokenProvider.extractSubject(accessToken);
		
		return service.findAll(username);		
	}
	
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "CRIAR TAREFA", description = "Cria uma nova tarefa associada ao usuário que realizou o request.", tags = "Tasks",
		responses = {
				@ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = TaskDto.class))),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
		})
	public ResponseEntity<TaskDto> create(
			@RequestBody TaskDto task,
			@RequestHeader(name = "Authorization") @Parameter(hidden = true) String accessToken) {		
		String username = tokenProvider.extractSubject(accessToken);
		
		return service.create(task, username);
	}
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "EDITAR TAREFA", description = "Edita uma tarefa existente. <br><b>OBS: Inserir o campo \"id\" ao Request</b>", tags = "Tasks",
		responses = {
				@ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = TaskDto.class))),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
				@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
		})
	public ResponseEntity<TaskDto> update(
			@RequestBody TaskDto task,
			@RequestHeader(name = "Authorization") @Parameter(hidden = true) String accessToken) {
		String username = tokenProvider.extractSubject(accessToken);
		
		return service.update(task, username);
	}
	
	
	@DeleteMapping("/{id}")
	@Operation(summary = "EXCLUIR TAREFA", description = "Excluir uma tarefa utilizando o id", tags = "Tasks",
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
