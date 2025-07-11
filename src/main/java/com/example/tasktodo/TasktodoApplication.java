package com.example.tasktodo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TasktodoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TasktodoApplication.class, args);
	}

}
