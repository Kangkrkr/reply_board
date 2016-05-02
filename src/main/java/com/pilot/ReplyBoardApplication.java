package com.pilot;

import java.io.File;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * 해야할일 :
 * 
 * 3. 글 수정
 * 4. 리팩토링
 * 6. 그리고 나머지 ...
 * 
 */

@SpringBootApplication
public class ReplyBoardApplication implements CommandLineRunner {

	public static String ROOT = "C:/Users/Administrator/Desktop/개발툴/sts-bundle/workspace/reply_board/src/main/resources/static/images";
	
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
