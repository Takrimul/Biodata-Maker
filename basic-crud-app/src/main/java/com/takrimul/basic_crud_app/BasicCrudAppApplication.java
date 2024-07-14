package com.takrimul.basic_crud_app;

import com.takrimul.basic_crud_app.controller.UserController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.takrimul.basic_crud_app.repository")
public class BasicCrudAppApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(BasicCrudAppApplication.class, args);
	}

}
