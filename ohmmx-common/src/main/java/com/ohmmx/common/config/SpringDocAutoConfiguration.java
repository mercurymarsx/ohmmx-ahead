package com.ohmmx.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SpringDocAutoConfiguration {
	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI().info(apiInfo()) //
				.externalDocs(new ExternalDocumentation() //
						.description("SpringDoc Wiki Documentation") //
						.url("https://springshop.wiki.github.org/docs"));
	}

	private Info apiInfo() {
		return new Info() //
				.title("OhmMx Swagger API") // 标题
				.description("SpringDoc application swagger api") // 描述
				.version("1.0.0") // 版本
				.contact(new Contact().name("ohmmx").url("http://www.ohmmx.com").email("mercurymarsx@gmail.com")) // 联系人
				.license(new License().name("Apache 2.0").url("http://springdoc.org"));
	}
}
