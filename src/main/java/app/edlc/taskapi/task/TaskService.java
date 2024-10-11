package app.edlc.taskapi.task;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import app.edlc.taskapi.task.data.Task;
import app.edlc.taskapi.task.data.TaskDto;
import app.edlc.taskapi.task.data.mapper.TaskMapper;
import app.edlc.taskapi.task.exception.RequiredObjectIsNullException;
import app.edlc.taskapi.task.exception.ResourceNotFoundException;
import app.edlc.taskapi.user.UserRepository;
import app.edlc.taskapi.user.data.User;

@Service
public class TaskService {
	
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TaskMapper mapper;
	
	public ResponseEntity<TaskDto> findById(Long id, String username) {
		Task taskEntity = taskRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());
		
		
		if (!taskEntity.getUser().getUsername().equals(username))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		
		TaskDto taskDto = mapper.toDto(taskEntity);
		taskDto.add(linkTo(
				methodOn(TaskController.class).findById(taskDto.getKey(), username)).withSelfRel());
		
		return ResponseEntity.ok(taskDto);
	}
	
	public ResponseEntity<List<TaskDto>> findAll(String username) {
		User taskOwner;
		if (userRepository.existsByUsername(username))
			taskOwner = userRepository.findByUsername(username);
		else
			throw new UsernameNotFoundException("User with " + username + " username not found.");
		
		List<TaskDto> dtoList = mapper.toDtoList(taskRepository.findByUser(taskOwner));
		for (TaskDto d : dtoList)
			d.add(linkTo(
				methodOn(TaskController.class).findById(d.getKey(), username)).withSelfRel());
		
		return ResponseEntity.ok(dtoList);
	}	
	
	public ResponseEntity<TaskDto> create(TaskDto task, String username) {		
		if (task == null) throw new RequiredObjectIsNullException();
		
		User taskOwner;
		if (userRepository.existsByUsername(username))
			taskOwner = userRepository.findByUsername(username);
		else
			throw new UsernameNotFoundException("User with " + username + " username not found.");
		
		Task taskEntity = mapper.toEntity(task);
		taskEntity.setUser(taskOwner);
		
		TaskDto createdDto = mapper.toDto(taskRepository.save(taskEntity));
		
		createdDto.add(linkTo(
				methodOn(TaskController.class).findById(createdDto.getKey(), username)).withSelfRel());
		
		return ResponseEntity.ok(createdDto);
	}
	
	public ResponseEntity<TaskDto> update(TaskDto task, String username) {
		if (task == null) throw new RequiredObjectIsNullException();
		
		Task taskEntity = taskRepository.findById(task.getKey())
				.orElseThrow(() -> new ResourceNotFoundException());
		
		if (!taskEntity.getUser().getUsername().equals(username))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		
		Task requestAsEntity = mapper.toEntity(task);
		
		taskEntity.setTitle(requestAsEntity.getTitle());
		taskEntity.setDescription(requestAsEntity.getDescription());
		taskEntity.setPriority(requestAsEntity.getPriority());
		taskEntity.setStatus(requestAsEntity.getStatus());
		taskEntity.setDeadline(requestAsEntity.getDeadline());
		
		TaskDto updatedDto = mapper.toDto(taskRepository.save(taskEntity));
		
		updatedDto.add(linkTo(
				methodOn(TaskController.class).findById(updatedDto.getKey(), username)).withSelfRel());
		
		return ResponseEntity.ok(updatedDto);
	}
	
	public ResponseEntity<?> delete(Long id) {
		
		// TODO
		return null;
	}
}
