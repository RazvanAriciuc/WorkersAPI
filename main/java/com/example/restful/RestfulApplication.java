package com.example.restful;

import com.example.restful.worker.Worker;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestfulApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestfulApplication.class, args);
	}

	@Bean
	public Cache<Integer, Worker> workersCache() {
		return CacheBuilder.newBuilder()
//				.expireAfterWrite(10, TimeUnit.MINUTES) // let's go with a LRU approach
				.maximumSize(1000)
				.build();
	}
}
