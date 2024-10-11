package app.edlc.taskapi.task.unittests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import app.edlc.taskapi.task.data.Task;
import app.edlc.taskapi.task.data.TaskDto;
import app.edlc.taskapi.task.data.enums.Priority;
import app.edlc.taskapi.task.data.enums.Status;
import app.edlc.taskapi.task.data.mapper.TaskMapper;
import app.edlc.taskapi.task.unittests.mocks.MockTask;

@ExtendWith(MockitoExtension.class)
public class TaskMapperTest {

	@InjectMocks
	TaskMapper taskMapper;
	
	MockTask mockTask;
	
	@BeforeEach
	public void setup() throws Exception {
		MockitoAnnotations.openMocks(this);
		mockTask = new MockTask();
	}
	
	@Test
    public void shouldConvertDtoToEntity() {
        TaskDto dto = mockTask.mockDto();
        Task entity = taskMapper.toEntity(dto);
        assertAll("Task Entity",
            () -> assertEquals(dto.getKey(), entity.getId()),
            () -> assertEquals("Title Test0", entity.getTitle()),
            () -> assertEquals("Description Test0", entity.getDescription()),
            () -> assertEquals(Status.PENDING, entity.getStatus()),
            () -> assertEquals(Priority.LOW, entity.getPriority()),
            () -> assertEquals(mockTask.getDate(), entity.getDeadline()),
            () -> assertNull(entity.getUser())
        );
    }
	
	@Test
	public void shouldSetDefaultWhenInvalidAttributes() {
		TaskDto dto = mockTask.mockDto();
		dto.setStatus("INVALID_STATUS");
		dto.setPriority("INVALID_PRIORITY");
		
		Task entity = taskMapper.toEntity(dto);
		assertAll("Task with invalid attributes",
			() -> assertEquals(Status.PENDING, entity.getStatus()),
			() -> assertEquals(Status.PENDING, entity.getStatus())
		);
	}
	
	@Test
	public void shouldSetDefaultWhenNullAttributes() {
		TaskDto dto = mockTask.mockDto();
		dto.setStatus(null);
		dto.setPriority(null);
		
		Task entity = taskMapper.toEntity(dto);
		assertAll("Task with invalid attributes",
			() -> assertEquals(Status.PENDING, entity.getStatus()),
			() -> assertEquals(Status.PENDING, entity.getStatus())
		);	
	}
	
	@Test
    public void shouldConvertEntityToDto() {
        Task entity = mockTask.mockEntity();        
        TaskDto dto = taskMapper.toDto(entity);
        assertAll("Task DTO",
			() -> assertEquals(entity.getId(), dto.getKey()),
			() -> assertEquals("Title Test0", dto.getTitle()),
			() -> assertEquals("Description Test0", dto.getDescription()),
			() -> assertEquals("PENDING", dto.getStatus()),
			() -> assertEquals("LOW", dto.getPriority()),
			() -> assertEquals(mockTask.getDate(), dto.getDeadline())
		);        
	}
	
    @Test
    public void shouldConvertDtoListToEntityList() {
        List<TaskDto> dtoList = mockTask.mockDtoList();        
        List<Task> entityList = taskMapper.toEntityList(dtoList);
        
        // first
        Task entity0 = entityList.getFirst();
        assertAll("Task 0",
    		() -> assertEquals(dtoList.getFirst().getKey(), entity0.getId()),
            () -> assertEquals("Title Test0", entity0.getTitle()),
            () -> assertEquals("Description Test0", entity0.getDescription()),
            () -> assertEquals(Status.PENDING, entity0.getStatus()),
            () -> assertEquals(Priority.LOW, entity0.getPriority()),
            () -> assertEquals(mockTask.getDate(), entity0.getDeadline()),
            () -> assertNull(entity0.getUser())
        );        
        // middle
        Task entity7 = entityList.get(7);
        assertAll("Task 7",
        		() -> assertEquals(dtoList.get(7).getKey(), entity7.getId()),
            () -> assertEquals("Title Test7", entity7.getTitle()),
            () -> assertEquals("Description Test7", entity7.getDescription()),
            () -> assertEquals(Status.PENDING, entity7.getStatus()),
            () -> assertEquals(Priority.LOW, entity7.getPriority()),
            () -> assertEquals(mockTask.getDate(), entity7.getDeadline()),
            () -> assertNull(entity7.getUser())
        );        
        // last
        Task entity13 = entityList.getLast();
        assertAll("Task 13",
    		() -> assertEquals(dtoList.get(13).getKey(), entity13.getId()),
            () -> assertEquals("Title Test13", entity13.getTitle()),
            () -> assertEquals("Description Test13", entity13.getDescription()),
            () -> assertEquals(Status.PENDING, entity13.getStatus()),
            () -> assertEquals(Priority.LOW, entity13.getPriority()),
            () -> assertEquals(mockTask.getDate(), entity13.getDeadline()),
            () -> assertNull(entity13.getUser())
        );
    }
    
    @Test
    public void shouldConvertEntityListToDtoList() {
		List<Task> entityList = mockTask.mockEntityList();
		List<TaskDto> dtoList = taskMapper.toDtoList(entityList);

		// first
		TaskDto dto0 = dtoList.getFirst();
        assertAll("Task 0",
    		() -> assertEquals(entityList.getFirst().getId(), dto0.getKey()),
			() -> assertEquals("Title Test0", dto0.getTitle()),
			() -> assertEquals("Description Test0", dto0.getDescription()),
			() -> assertEquals("PENDING", dto0.getStatus()),
			() -> assertEquals("LOW", dto0.getPriority()),
			() -> assertEquals(mockTask.getDate(), dto0.getDeadline())
		);
        // middle
		TaskDto dto7 = dtoList.get(7);
        assertAll("Task 7",
    		() -> assertEquals(entityList.get(7).getId(), dto7.getKey()),
			() -> assertEquals("Title Test7", dto7.getTitle()),
			() -> assertEquals("Description Test7", dto7.getDescription()),
			() -> assertEquals("PENDING", dto7.getStatus()),
			() -> assertEquals("LOW", dto7.getPriority()),
			() -> assertEquals(mockTask.getDate(), dto7.getDeadline())
		);
        // last
		TaskDto dto13 = dtoList.getLast();
        assertAll("Task 13",
    		() -> assertEquals(entityList.getLast().getId(), dto13.getKey()),
			() -> assertEquals("Title Test13", dto13.getTitle()),
			() -> assertEquals("Description Test13", dto13.getDescription()),
			() -> assertEquals("PENDING", dto13.getStatus()),
			() -> assertEquals("LOW", dto13.getPriority()),
			() -> assertEquals(mockTask.getDate(), dto13.getDeadline())
		);
    }
}
