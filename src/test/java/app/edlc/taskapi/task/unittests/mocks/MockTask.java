package app.edlc.taskapi.task.unittests.mocks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.edlc.taskapi.task.data.Task;
import app.edlc.taskapi.task.data.TaskDto;
import app.edlc.taskapi.task.data.enums.Priority;
import app.edlc.taskapi.task.data.enums.Status;
import app.edlc.taskapi.user.data.User;
import app.edlc.taskapi.user.unittests.mocks.MockUser;

public class MockTask {
	
	private final Date date;
	private final User ownerUser;
	
	public MockTask() throws Exception {
		date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2000-01-01 00:00:00");
		MockUser mockUser = new MockUser();
		ownerUser = mockUser.mockEntity();
	}
	
	public Task mockEntity() {
		return mockEntity(0);
	}
	
	public TaskDto mockDto() {
		return mockDto(0);
	}
	
	public Task mockEntity(int number) {
		Task entity = new Task();
		
		entity.setId(Long.valueOf(number));
		entity.setTitle("Title Test" + number);
		entity.setDescription("Description Test" + number);
		entity.setPriority(Priority.LOW);
		entity.setStatus(Status.PENDING);
		entity.setDeadline(date);
		entity.setUser(ownerUser);
		
		return entity;	
	}
	
	public TaskDto mockDto(int number) {
		TaskDto dto = new TaskDto();
		
		dto.setKey(Long.valueOf(number));
		dto.setTitle("Title Test" + number);
		dto.setDescription("Description Test" + number);
		dto.setPriority("LOW" + number);
		dto.setStatus("PENDING" + number);
		dto.setDeadline(date);
		
		return dto;
	}
	
	public List<Task> mockEntityList() {
		List<Task> entityList = new ArrayList<>();		
		for (int i=0; i<14; i++) {
			entityList.add(mockEntity(i));
		}		
		return entityList;
	}
	
	public List<TaskDto> mockDtoList() {
		List<TaskDto> dtoList = new ArrayList<>();
		for (int i=0; i<14; i++) {
			dtoList.add(mockDto(i));
		}
		return dtoList;
	}

	public Date getDate() {
		return date;
	}

	public User getOwnerUser() {
		return ownerUser;
	}
}
