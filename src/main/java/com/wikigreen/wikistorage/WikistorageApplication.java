package com.wikigreen.wikistorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class WikistorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(WikistorageApplication.class, args);
	}

}
