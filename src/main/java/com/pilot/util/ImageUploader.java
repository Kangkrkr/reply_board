package com.pilot.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.pilot.ReplyBoardApplication;
import com.pilot.validator.WriteForm;

public class ImageUploader {

	public static String uploadAndSavePath(MultipartRequest mr, WriteForm writeForm){
		
		MultipartFile photo = (MultipartFile)mr.getFile("photo");
		
		BufferedOutputStream stream = null;
		String fixedPath = null;
		
		if (null != photo && !photo.isEmpty()) {
			try {
				File imageFile = new File(ReplyBoardApplication.ROOT + "/" + photo.getOriginalFilename());
				String filePath = imageFile.getPath();
				
				// 순수 파일이름만 DB에 저장하는 방식을 취함.
				fixedPath = imageFile.getPath().substring(filePath.lastIndexOf('\\') + 1, filePath.length());
				
				stream = new BufferedOutputStream(
						new FileOutputStream(imageFile));
                FileCopyUtils.copy(photo.getInputStream(), stream);
                
                if(stream != null){
        			try {
        				stream.close();
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
        		}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return fixedPath;
	}
	
}
