package app.edlc.taskapi.authentication.integrationtests;


import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
	public void testLogin() {
		AccountCredentialsDto credentials = new AccountCredentialsDto("tester_username", "tester123");
		
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
}
