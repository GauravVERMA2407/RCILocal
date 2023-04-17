package gov.rci;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CrcApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrcApplication.class, args);
		System.out.println("App Started::");
	}
	
	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}


}
