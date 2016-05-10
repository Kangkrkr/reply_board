package com.pilot;

import java.io.File;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReplyBoardApplication implements CommandLineRunner {

	public static String ROOT = "C:/Users/강승윤/Desktop/백업/test_board_05_08/src/main/resources/static/images";

	public static void main(String[] args) {
		SpringApplication.run(ReplyBoardApplication.class, args);
		System.out.println("server on");
	}

	@Override
	public void run(String... args) throws Exception {
		File rootPath = new File(ROOT);
		
		if(!rootPath.exists()){
			rootPath.mkdir();
		}
		
	}
}
