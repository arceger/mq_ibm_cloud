package com.over.web5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
public class Web5Application {
	private static final Logger LOGGER = LoggerFactory.getLogger(Web5Application.class);
	@GetMapping("/hello")
	public String unInit(){
		return "executando...";
	}

	public static void main(String[] args) {
		SpringApplication.run(Web5Application.class, args);

		MqConect mq = new MqConect();
		try {
			mq.postAndReceive();
		}catch (Exception e){
			LOGGER.info("Erro mq "+e);
		}

		RestTemplate restTemplate = new RestTemplate();

		restTemplate.getInterceptors().add(new RestTemplateFilter());

		String url = "http://localhost:8080/hello";
		String response = restTemplate.getForObject(url, String.class);
		LOGGER.info("Response from {}: {}", url, response);

	}
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public ClientHttpRequestInterceptor restTemplateFilter() {
		return new RestTemplateFilter();
	}
}
