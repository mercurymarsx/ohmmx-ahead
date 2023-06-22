package com.ohmmx.steam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.ohmmx.steam.*", "com.ohmmx.common.*" }, lazyInit = true)
public class SteamApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SteamApplication.class, args);
	}
}
