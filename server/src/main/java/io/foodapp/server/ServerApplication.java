package io.foodapp.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		// if (System.getenv("RAILWAY_ENVIRONMENT") == null) {
		// 	Dotenv dotenv = Dotenv.load();
		// 	dotenv.entries().forEach(entry -> {
		// 		System.setProperty(entry.getKey(), entry.getValue());
		// 	});
		// }
		SpringApplication.run(ServerApplication.class, args);
	}

}
