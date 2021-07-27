package com.example.Mercearia;






import java.util.Collections;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class MerceariaApplication {

	public static void main(String[] args) {
		//SpringApplication.run(MerceariaApplication.class, args);
		
		SpringApplication app=new SpringApplication(MerceariaApplication.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", "7979"));
	app.run(args);
	}

}
