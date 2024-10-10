package app.edlc.taskapi.authentication.integrationtests;


import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import app.edlc.taskapi.authentication.integrationtests.dto.AccountCredentialsDto;
import app.edlc.taskapi.authentication.integrationtests.dto.TokenDto;
import app.edlc.taskapi.configuration.TestConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class AuthControllerTest {

	private static TokenDto tokenDto;
	private static RequestSpecification specification;
	private static AccountCredentialsDto credentials;
	
	@BeforeAll
	public static void initSpec() {
		specification = new RequestSpecBuilder()
				.setPort(TestConfig.SERVER_PORT)
				.setContentType(TestConfig.CONTENT_TYPE_JSON)
				.setAccept(TestConfig.CONTENT_TYPE_JSON)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
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
	@Order(1)
	public void shouldLoginSuccessfullyWithValidCredentials() {
		credentials = new AccountCredentialsDto("tester_username", "tester123");
		
		tokenDto = given()
				.spec(specification)
				.basePath("/auth/login")
				.body(credentials)
				.when()
					.post()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.as(TokenDto.class);
		
		assertNotNull(tokenDto.getAccessToken());
		assertNotNull(tokenDto.getRefreshToken());
	}
	
	@Test
	@Order(2)
	public void shouldFailLoginWithInvalidUsername() {
		AccountCredentialsDto credentials = new AccountCredentialsDto("invalid_username", "tester123");
		
		String response = given()
					.spec(specification)
					.basePath("/auth/login")
					.body(credentials)
					.when()
						.post()
					.then()
						.statusCode(401)
					.extract()
						.body()
							.asString();
		
		assertTrue(response.contains("Invalid username or password."));		
	}
	
	@Test
	@Order(3)
	public void shouldFailLoginWithInvalidPassword() {
		AccountCredentialsDto credentials = new AccountCredentialsDto("tester_username", "invalid_password");
		
		String response = given()
					.spec(specification)
					.basePath("/auth/login")
					.body(credentials)
					.when()
						.post()
					.then()
						.statusCode(401)
					.extract()
						.body()
							.asString();
		
		assertTrue(response.contains("Invalid username or password."));
	}
	
	@Test
	@Order(4)
	public void shouldFailLoginWithEmptyCredentials() {
		AccountCredentialsDto credentials = new AccountCredentialsDto(" ", " ");
		
		String response = given()
					.spec(specification)
					.basePath("/auth/login")
					.body(credentials)
					.when()
						.post()
					.then()
						.statusCode(400)
					.extract()
						.body()
							.asString();
		
		assertTrue(response.contains("Invalid request. Missing username or password."));
	}
	
	@Test
	@Order(5)
	public void shouldFailLoginWithNullCredentials() {
		AccountCredentialsDto credentials = new AccountCredentialsDto();
		
		String response = given()
					.spec(specification)
					.basePath("/auth/login")
					.body(credentials)
					.when()
						.post()
					.then()
						.statusCode(400)
					.extract()
						.body()
							.asString();
		
		assertTrue(response.contains("Invalid request. Missing username or password."));
	}
	
	
	@Test
	@Order(6)
	public void shouldRefreshTokenSuccessfullyWithValidRefreshToken() {
		TokenDto refreshedToken = given()
				.spec(specification)
				.basePath("/auth/refresh/")
				.pathParam("username", "tester_username")
				.header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getRefreshToken())
				.when()
					.post("{username}")
				.then()
					.statusCode(200)
				.extract()
					.body()
						.as(TokenDto.class);
		
		assertNotNull(refreshedToken.getAccessToken());
		assertNotNull(refreshedToken.getRefreshToken());					
	}
	
	@Test
	@Order(7)
	public void shouldFailRefreshTokenWithNonExistentUsername() {
		String response = given()
				.spec(specification)
				.basePath("/auth/refresh/")
				.pathParam("username", "invalid_username")
				.header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getRefreshToken())
				.when()
					.post("{username}")
				.then()
					.statusCode(401)
				.extract()
					.body()
						.asString();
		
		assertTrue(response.contains("invalid_username username does not exist."));			
	}
	
	@Test
	@Order(8)
	public void shouldFailRefreshTokenWithInvalidRefreshToken() {
		String invalidRefreshToken = "Bearer xxx" + tokenDto.getRefreshToken();
		
		String response = given()
				.spec(specification)
				.basePath("/auth/refresh/")
				.pathParam("username", "invalid_username")
				.header(TestConfig.HEADER_PARAM_AUTHORIZATION, invalidRefreshToken)
				.when()
					.post("{username}")
				.then()
					.statusCode(401)
				.extract()
					.body()
						.asString();
		
		assertTrue(response.contains("Expired or invalid JWT token."));
	}
	
	@Test
	@Order(9)
	public void shouldFailRefreshTokenWithEmptyUsername() {
		String response = given()
				.spec(specification)
				.basePath("/auth/refresh/")
				.pathParam("username", "  ")
				.header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getRefreshToken())
				.when()
					.post("{username}")
				.then()
					.statusCode(400)
				.extract()
					.body()
						.asString();
		
		assertTrue(response.contains("Invalid request. Missing username or refresh token."));
	}
}
