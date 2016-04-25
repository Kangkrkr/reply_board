package com.pilot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReplyBoardApplication {

	public static String ROOT = "upload-dir";
	
	public static void main(String[] args) {
		SpringApplication.run(ReplyBoardApplication.class, args);
	}
}
