package app.edlc.taskapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@SecurityScheme(
		name = "Authorization",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		scheme = "bearer"
)
public class OpenApiConfig {
	
	@Bean
	OpenAPI customOpenAPI() {
		
		return new OpenAPI()
				.info(new Info()
					.title("TASK API - Código Certo-Trilha Backend")
					.description("API para gerenciamento de Tarefas desenvolvida para o desafio da trilha de Backend da Código Certo."
							+ "<br><br> Eduardo Corrêa"
							+ "<br> eduardodlcorrea@gmail.com")
					.version("v1")
				);
	}
}
