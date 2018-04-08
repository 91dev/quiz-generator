package com.gmat.quiz.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.gmat.quiz.generator"})
public class QuizGeneratorApplication {
	public static void main(String[] args) {
		SpringApplication.run(QuizGeneratorApplication.class, args);
	}
}
