package com.pilot.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.pilot.entity.Post;
import com.pilot.entity.User;
import com.pilot.model.WriteModel;
import com.pilot.util.Message;

@Service
public class UploadService {

	@Autowired
	private PostService postService;
	
	@Autowired
	private RedisService redisService;
	
	private static final Logger logger = LoggerFactory.getLogger(UploadService.class);

	public String upload(MultipartRequest mr, WriteModel writeForm, String tk) {

		Post post = new Post();

		String type = writeForm.getType().split("#")[0];
		String fixedPath = imageUploadAndSavePath(mr);

		// cookie에 저장된 key(token)으로 유저 정보 불러오기.
		User uploader = redisService.getUserInfoByKey(tk);
		
		if(uploader == null){
			return "유효하지 않은 사용자 입니다.";
		}
		
		post.setType(type);
		post.setImage(fixedPath);
		post.setContent(writeForm.getContent());
		post.setUser(uploader);
		post.setProfileImage(uploader.getProfileImage());

		if (type.equals("post")) { 
			Post saved = postService.write(post);
			saved.setPath((999999 - saved.getId()) + "/");
			postService.update(saved);
			return Message.POST_UPLOAD_SUCCESS;
		} 
		else if (type.equals("reply")) {

			Post saved = postService.write(post);
			
			// 게시글을 삽입할 대상의 id를 가져옴.
			Integer targetId = toInteger(writeForm.getType().split("#")[1]);
			Post rootPost = postService.findOne(targetId);
			
			saved.setIndent(rootPost.getIndent() + 1);
			saved.setRootPost(rootPost);
			saved.setPath(rootPost.getPath() + (999999 - saved.getId()) + "/");
			
			postService.update(saved);
			
			return Message.REPLY_UPLOAD_SUCCESS;
			
		} 
		else if ((type.equals("edit"))) {
			
			Integer postId = toInteger(writeForm.getType().split("#")[1]);
			
			Post update = postService.findOne(postId);
			update.setImage(fixedPath);
			update.setContent(writeForm.getContent());
			update.setRegdate(new Date());
			update.setUser(uploader);
			
			postService.update(update);
			
			return Message.EDIT_SUCCESS;
		}
		
		return Message.ALERT_ERROR;
	}

	private Integer toInteger(String target) {
		return Integer.parseInt(target);
	}

	public String imageUploadAndSavePath(MultipartRequest mr) {

		final String path = "/nginx/root/images";
		
		File storePath = new File(path);
		if(!storePath.exists()){
			storePath.mkdir();
		}
		
		MultipartFile photo = mr.getFile("photo");

		BufferedOutputStream stream = null;
		String filename = null;

		if (null != photo && !photo.isEmpty()) {
			try {
				filename = photo.getOriginalFilename();
				
				// 크롬에선 파일원본명이 그대로 나오지만 익스플로러나 엣지에서는 전체경로가 붙어나오기 때문에 다음과 같이 처리.
				if(filename.contains("\\")){
					filename = filename.substring(filename.lastIndexOf('\\') + 1);
				}
				
				File imageFile = new File(path + "/" + filename);
				
				// 사용자가 업로드한 파일을 암호화한 값을 얻어온다.
				String uploadedCode = DigestUtils.shaHex(photo.getInputStream());
				
				// 이미 같은 이름의 파일이 존재한다면 존재하는 파일의 암호값을 구한다.
				if(imageFile.exists()){
					FileInputStream fis = new FileInputStream(imageFile);
					String savedCode = DigestUtils.shaHex(fis);
					
					// 두개의 암호값을 비교.
					if(uploadedCode.equals(savedCode)){
						logger.info("이미 존재하는 파일입니다.");
						return filename;
					}else{
						logger.info("파일이름이 같지만 다른 파일입니다.");
						
						filename = System.currentTimeMillis() + "_" + filename;
						imageFile = new File(path + "/" + filename);
					}
				}
				
				stream = new BufferedOutputStream(new FileOutputStream(imageFile));
				FileCopyUtils.copy(photo.getInputStream(), stream);
				
				return filename;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("upload error", e.toString());
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						logger.error("BufferedOutputStream close error.", e.toString());
					}
				}
			}
		}

		return filename;
	}
}
