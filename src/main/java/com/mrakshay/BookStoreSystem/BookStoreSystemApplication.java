package com.mrakshay.BookStoreSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BookStoreSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookStoreSystemApplication.class, args);
	}

}
