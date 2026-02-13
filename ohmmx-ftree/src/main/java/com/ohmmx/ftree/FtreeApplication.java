package com.ohmmx.ftree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching
// @EnableEurekaClient
@EntityScan(basePackages = { "com.ohmmx.common.entity", "com.ohmmx.ftree.entity" })
@EnableJpaRepositories(basePackages = { "com.ohmmx.common.mapper", "com.ohmmx.ftree.mapper" })
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.ohmmx.ftree", "com.ohmmx.common" }, lazyInit = true)
public class FtreeApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(FtreeApplication.class, args);
	}
}
