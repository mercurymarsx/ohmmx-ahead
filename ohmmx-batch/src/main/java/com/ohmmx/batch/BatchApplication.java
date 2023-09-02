package com.ohmmx.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = { "com.ohmmx.common.entity" })
@EnableJpaRepositories(basePackages = { "com.ohmmx.common.mapper" })
@ComponentScan(basePackages = { "com.ohmmx.batch", "com.ohmmx.common" }, lazyInit = true)
public class BatchApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(BatchApplication.class, args);
	}
}
