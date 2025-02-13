package br.com.denuncia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class DenuncIaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DenuncIaApplication.class, args);
	}

}
