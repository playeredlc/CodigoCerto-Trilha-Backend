package app.edlc.taskapi.task.data.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import app.edlc.taskapi.task.data.Task;
import app.edlc.taskapi.task.data.TaskDto;
import app.edlc.taskapi.task.data.enums.Priority;
import app.edlc.taskapi.task.data.enums.Status;

public class TaskMapper {
	private final ModelMapper modelMapper;
	
	public TaskMapper() {
		this.modelMapper = new ModelMapper();
		this.initializeMappings();
	}
	
	private void initializeMappings() {
		modelMapper
		.typeMap(TaskDto.class, Task.class)
		.addMapping(TaskDto::getKey, Task::setId)
		.addMappings(mapper -> {
			mapper.skip(Task::setPriority);
			mapper.skip(Task::setStatus);
		});
		
		modelMapper
		.typeMap(Task.class, TaskDto.class)
		.addMapping(Task::getId, TaskDto::setKey)
		.addMappings(mapper -> {
			mapper.skip(TaskDto::setPriority);
			mapper.skip(TaskDto::setStatus);
		});
	}

	public Task toEntity(TaskDto dto) {		
		Task entity = modelMapper.map(dto, Task.class);
		
		if (validatePriority(dto.getPriority()) == null)
			entity.setPriority(Priority.LOW);
		if (validateStatus(dto.getStatus()) == null)
			entity.setStatus(Status.PENDING);
		
		return entity;
	}
	
	public TaskDto toDto(Task entity) {
		TaskDto dto = modelMapper.map(entity, TaskDto.class);		
		dto.setPriority(entity.getPriority().name());
		dto.setStatus(entity.getStatus().name());
		
		return dto;
	}
	
	public List<Task> toEntityList(List<TaskDto> dtoList) {
		return dtoList.stream()
				.map(this::toEntity)
				.collect(Collectors.toList());		
	}
	
	public List<TaskDto> toDtoList(List<Task> entityList) {
		return entityList.stream()
				.map(this::toDto)
				.collect(Collectors.toList());
	}
	
	private Status validateStatus(String statusStr) {
		try {
            return Status.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // Invalid status
        }
	}
	
	private Priority validatePriority(String priorityStr) {
		try {
			return Priority.valueOf(priorityStr);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}
