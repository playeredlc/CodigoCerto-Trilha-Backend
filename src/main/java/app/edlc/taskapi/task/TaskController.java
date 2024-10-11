package app.edlc.taskapi.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/api/task/v1")
public class TaskController {
	
	@Autowired
	private TaskService service;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@GetMapping("/{id}")
	public ResponseEntity<TaskDto> findById(
			@PathVariable Long id,
			@RequestHeader(name = "Authorization") String accessToken) {
		String username = tokenProvider.extractSubject(accessToken);
		return service.findById(id, username);
	}
	
	@GetMapping
	public ResponseEntity<List<TaskDto>> findAll(@RequestHeader(name = "Authorization") String accessToken) {
		String username = tokenProvider.extractSubject(accessToken);
		
		return service.findAll(username);		
	}
	
	@PostMapping
	public ResponseEntity<TaskDto> create(
			@RequestBody TaskDto task,
			@RequestHeader(name = "Authorization") String accessToken) {		
		String username = tokenProvider.extractSubject(accessToken);
		
		return service.create(task, username);
	}
	
	@PutMapping
	public ResponseEntity<TaskDto> update(
			@RequestBody TaskDto task,
			@RequestHeader(name = "Authorization") String accessToken) {
		String username = tokenProvider.extractSubject(accessToken);
		
		return service.update(task, username);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(
			@PathVariable Long id,
			@RequestHeader(name = "Authorization") String accessToken) {
		String username = tokenProvider.extractSubject(accessToken);
		
		// TODO
		
		return null;
	}
}
