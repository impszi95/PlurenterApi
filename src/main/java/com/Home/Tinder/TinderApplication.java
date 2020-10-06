package com.Home.Tinder;

import com.Home.Tinder.Repo.UserRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

	@SpringBootApplication
public class TinderApplication {

	public static void main(String[] args) {
		SpringApplication.run(TinderApplication.class, args);
	}

}
