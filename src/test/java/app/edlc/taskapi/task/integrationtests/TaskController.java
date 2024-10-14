package app.edlc.taskapi.task.integrationtests;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.edlc.taskapi.authentication.integrationtests.dto.AccountCredentialsDto;
import app.edlc.taskapi.authentication.integrationtests.dto.TokenDto;
import app.edlc.taskapi.configuration.TestConfig;
import app.edlc.taskapi.task.integrationtests.data.TaskDto;
import app.edlc.taskapi.task.integrationtests.util.MockDtoUtil;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class TaskController {
	
	private static ObjectMapper objectMapper;
	private static RequestSpecification specification;
	private static RequestSpecification specWithoutPermission;
	private static TaskDto task;
	
	@BeforeAll
	public static void initSpec() {
		objectMapper = new ObjectMapper(new JsonFactory())
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		specification = new RequestSpecBuilder()
				.setPort(TestConfig.SERVER_PORT)
				.setBasePath("/api/task/v1")
				.setContentType(TestConfig.CONTENT_TYPE_JSON)
				.setAccept(TestConfig.CONTENT_TYPE_JSON)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		specWithoutPermission = new RequestSpecBuilder()
				.setPort(TestConfig.SERVER_PORT)
				.setBasePath("/api/task/v1")
				.setContentType(TestConfig.CONTENT_TYPE_JSON)
				.setAccept(TestConfig.CONTENT_TYPE_JSON)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		task = new TaskDto();
	}
	
    @AfterAll
    public static void tearDown() {
        // Delete the database file after all tests are done
        File dbFile = new File(TestConfig.DB_FILE_PATH);
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }
    
    @Test
    @Order(0)
    public void authorization() {
    	AccountCredentialsDto credentials = new AccountCredentialsDto("tester_username", "tester123");
    	
    	String accessToken = given()
    			.port(TestConfig.SERVER_PORT)
				.contentType(TestConfig.CONTENT_TYPE_JSON)
				.accept(TestConfig.CONTENT_TYPE_JSON)    			
    			.basePath("/auth/login")
    			.body(credentials)
    			.when()
    				.post()
    			.then()
    				.statusCode(200)
    			.extract()
    				.body()
    					.as(TokenDto.class)
    				.getAccessToken();
    	
    	assertNotNull(accessToken);
    	specification.header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken);
    	
    	// Set specWithoutPermission accessToken
    	credentials.setUsername("second_tester_username");
    	credentials.setPassword("tester123");
    	accessToken = given()
    			.port(TestConfig.SERVER_PORT)
				.contentType(TestConfig.CONTENT_TYPE_JSON)
				.accept(TestConfig.CONTENT_TYPE_JSON)    			
    			.basePath("/auth/login")
    			.body(credentials)
    			.when()
    				.post()
    			.then()
    				.statusCode(200)
    			.extract()
    				.body()
    					.as(TokenDto.class)
    				.getAccessToken();
    	
    	assertNotNull(accessToken);
    	specWithoutPermission.header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken);    
    }
    
    @Test
    @Order(1)
    public void create_ValidTaskDto_ReturnsCreatedTaskDto() throws Exception {
    	MockDtoUtil.mockTaskAttributes(task);
    	
    	var response = given()
    			.spec(specification)
    			.body(task)
    			.when()
    				.post()
    			.then()
    				.statusCode(200)
    			.extract()
    				.body()
    					.asString();
    	
    	assertNotNull(response);
    	TaskDto responseDto = objectMapper.readValue(response, TaskDto.class);    	
    	
    	assertAll("Task Creation",
    		() -> assertNotNull(responseDto),
    		() -> assertNotNull(responseDto.getId()),
    		() -> assertTrue(responseDto.getId() > 0),
    		() -> assertEquals(task.getTitle(), responseDto.getTitle()),
    		() -> assertEquals(task.getDescription(), responseDto.getDescription()),
    		() -> assertEquals("MEDIUM", responseDto.getPriority()),
    		() -> assertEquals("PENDING", responseDto.getStatus()),
    		() -> assertEquals(task.getDeadline(), responseDto.getDeadline())
    	);
    	
    	task = responseDto;
    }
    
    @Test
    @Order(2)
    public void findAll_ValidUsername_ReturnsListOfTaskDtos() throws JsonMappingException, JsonProcessingException {
    	var response = given()
    			.spec(specification)
    			.when()
    				.get()
    			.then()
    				.statusCode(200)
    			.extract()
    				.body()
    					.asString();
    	
    	assertNotNull(response);
    	List<TaskDto> taskList = objectMapper.readValue(response, new TypeReference<List<TaskDto>>(){});
    	
    	assertNotNull(taskList);
    	taskList.stream()
    		.forEach(t -> {
    			assertNotNull(t);
    			assertTrue(t.getId() > 0);
    	});    	
    }
    
    @Test
    @Order(3)
    public void findById_ValidId_ReturnsTaskDto() throws Exception {    	
    	MockDtoUtil.mockTaskAttributes(task);
    	
    	var response = given()
    			.spec(specification)
    			.pathParam("id", task.getId())
    			.when()
    				.get("{id}")
    			.then()
    				.statusCode(200)
    			.extract()
    				.body()
    					.asString();
    	
    	assertNotNull(response);
    	TaskDto responseDto = objectMapper.readValue(response, TaskDto.class);    	
    	
    	assertAll("Task Find by ID",
    		() -> assertNotNull(responseDto),
    		() -> assertNotNull(responseDto.getId()),
    		() -> assertEquals(task.getId(), responseDto.getId()),
    		() -> assertEquals(task.getTitle(), responseDto.getTitle()),
    		() -> assertEquals(task.getDescription(), responseDto.getDescription()),
    		() -> assertEquals("MEDIUM", responseDto.getPriority()),
    		() -> assertEquals("PENDING", responseDto.getStatus()),
    		() -> assertEquals(task.getDeadline(), responseDto.getDeadline())
    	);    	
    }
    
    @Test
    @Order(4)
    public void findById_UserDoestNotOwnTask_ReturnForbidden() {
    	given()
			.spec(specWithoutPermission)
			.pathParam("id", task.getId())
			.when()
				.get("{id}")
			.then()
				.statusCode(403)
			.extract()
				.body()
					.asString();
    }
    
    @Test
    @Order(5)
    public void findById_InvalidId_ThrowsResourceNotFoundException() {
    	Long invalidId = Long.valueOf(-1);
    	
    	var response = given()
    			.spec(specification)
    			.pathParam("id", invalidId)
    			.when()
    				.get("{id}")
    			.then()
    				.statusCode(404)
    			.extract()
    				.body()
    					.asString();
    	
    	assertTrue(response.contains("Could not find any records for this id."));
    }
    
    @Test
    @Order(6)
    public void update_ValidTaskDto_ReturnsUpdatedTaskDto() throws JsonMappingException, JsonProcessingException {
    	MockDtoUtil.updateTaskAttributes(task);
    	
    	var response = given()
    			.spec(specification)
    			.body(task)
    			.when()
    				.put()
    			.then()
    				.statusCode(200)
    			.extract()
    				.body()
    					.asString();
    	
    	assertNotNull(response);
    	TaskDto responseDto = objectMapper.readValue(response, TaskDto.class);
    	
    	assertAll("Task Update",
        		() -> assertNotNull(responseDto),
        		() -> assertNotNull(responseDto.getId()),
        		() -> assertEquals(task.getId(), responseDto.getId()),
        		() -> assertEquals("Updated Title Test", responseDto.getTitle()),
        		() -> assertEquals("Updated Description Test", responseDto.getDescription()),
        		() -> assertEquals("HIGH", responseDto.getPriority()),
        		() -> assertEquals("COMPLETED", responseDto.getStatus()),
        		() -> assertNull(responseDto.getDeadline())
        	);
    }
    
    @Test
    @Order(7)
    public void update_UserDoestNotOwnTask_ReturnForbidden() {
    	given()
		.spec(specWithoutPermission)
		.body(task)
		.when()
			.put()
		.then()
			.statusCode(403)
		.extract()
			.body()
				.asString();
    }
    
    @Test
    @Order(8)
    public void delete_UserDoestNotOwnTask_ReturnForbidden() {
    	given()
		.spec(specWithoutPermission)
		.pathParam("id", task.getId())
		.when()
			.delete("{id}")
		.then()
			.statusCode(403);    	
    }
    
    @Test
    @Order(9)
    public void delete_ValidId_ReturnsNoContent() {
    	given()
    		.spec(specification)
    		.pathParam("id", task.getId())
    		.when()
    			.delete("{id}")
    		.then()
    			.statusCode(204);    	
    }
    
    @Test
    @Order(10)
    public void delete_InvalidId_ThrowsResourceNotFoundException() {
    	String response = given()
				.spec(specification)
				.pathParam("id", task.getId())
				.when()
					.delete("{id}")
				.then()
					.statusCode(404)
				.extract()
					.body()
						.asString();
    	
    	assertTrue(response.contains("Could not find any records for this id."));
    }
}
