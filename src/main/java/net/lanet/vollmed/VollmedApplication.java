package net.lanet.vollmed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "net.lanet.vollmed")
public class VollmedApplication {

	public static void main(String[] args) {
		SpringApplication.run(VollmedApplication.class, args);
	}

}
