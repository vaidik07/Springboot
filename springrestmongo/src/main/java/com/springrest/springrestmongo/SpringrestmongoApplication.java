package com.springrest.springrestmongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.springrest.springrestmongo.repository")
public class SpringrestmongoApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringrestmongoApplication.class, args);
	}
}
