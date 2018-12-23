package com.yong.hystrix.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@EnableCircuitBreaker
@SpringBootApplication
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}

	@RestController
	static class Controller {
		@GetMapping("/consumer")
		@HystrixCommand(fallbackMethod = "fallback", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500"),
			@HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000"),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "10"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000")
		}, threadPoolProperties = @HystrixProperty(name = "coreSize", value = "100"))
		public String consumer(@RequestParam String path) {
			System.out.println("log :: " + path);

			ResponseEntity<String> entity = new RestTemplate().getForEntity("http://127.0.0.1:8090/" + path,
				String.class);

			if(entity.getStatusCode() == HttpStatus.OK) {
				return entity.getBody();
			}

			throw new RuntimeException("supplier is not OK");
		}

		private String fallback(String path) {
			return "hello fallback";
		}
	}
}
