package com.pilot;

import java.io.File;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReplyBoardApplication implements CommandLineRunner {

	public static String ROOT = "C:/dev/ide/sts/workspace/reply_board/src/main/resources/static/images";
	
	public static void main(String[] args) {
		SpringApplication.run(ReplyBoardApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		File rootPath = new File(ROOT);
		
		if(!rootPath.exists()){
			rootPath.mkdir();
		}
	}
}
