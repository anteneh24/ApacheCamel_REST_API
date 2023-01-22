package com.example.first;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SecondApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context =  SpringApplication.run(SecondApplication.class, args);
		First first = context.getBean(First.class);
		first.print();
		System.out.println("print this");
	}

}
