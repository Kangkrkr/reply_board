package com.pilot.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.pilot.ReplyBoardApplication;
import com.pilot.entity.Post;
import com.pilot.entity.User;
import com.pilot.util.Message;
import com.pilot.valid.WriteForm;

@Service
public class UploadService {

	@Autowired
	HttpSession session;

	@Autowired
	PostService postService;
	
	private static final Logger logger = LoggerFactory.getLogger(UploadService.class);

	public String upload(MultipartRequest mr, WriteForm writeForm) {

		Post post = new Post();

		String type = writeForm.getType().split("#")[0];
		String fixedPath = imageUploadAndSavePath(mr);

		post.setType(type);
		post.setImage(fixedPath);
		post.setContent(writeForm.getContent());
		post.setUser((User) session.getAttribute("userInfo"));

		
		if (type.equals("post")) {
			Post saved = postService.write(post);	// path를 지정하기 위해 엔티티를 저장 후 그 저장된 정보를 받아온다.
			saved.setPath(saved.getId() + "/");		// path를 다음과 같이 수정한다.
			postService.update(saved);				// 수정된 정보 재반영.
			return Message.POST_UPLOAD_SUCCESS;
		} else if (type.equals("reply")) {

			Post saved = postService.write(post);	// 삽입할 엔티티를 저장후 역시 정보를 받아옴.
			
			// 게시글을 삽입할 대상의 id를 가져옴.
			Integer targetId = toInteger(writeForm.getType().split("#")[1]);
			Post rootPost = postService.findOne(targetId);
			
			saved.setIndent(rootPost.getIndent() + 1);
			saved.setRootPost(rootPost);
			saved.setPath(rootPost.getPath() + saved.getId() + "/");
			
			postService.update(saved);
			/*
			List<Post> posts = postService.findAll();	// 전체 게시물을 가져온다.
			int originalIdx = 0;	// 게시글을 삽입할 인덱스를 뽑아낸다.
			
			for (int i = 0; i < posts.size(); i++) {
				
				if (String.valueOf(posts.get(i).getId()).equals(String.valueOf(targetId))) {
					break;
				}
				originalIdx++;
			}

			// 테이블을 갱신하는 작업. 전체 게시물과 삽입할 위치, 삽입될 게시물이 인자로 들어간다.
			postService.addPost(posts, originalIdx, post);
			*/
			
			return Message.REPLY_UPLOAD_SUCCESS;
			
		} else if ((type.equals("edit"))) {
			
			Integer postId = toInteger(writeForm.getType().split("#")[1]);
			
			Post update = postService.findOne(postId);
			update.setImage(fixedPath);
			update.setContent(writeForm.getContent());
			update.setRegdate(new Date());
			update.setUser((User)session.getAttribute("userInfo"));
			
			postService.update(update);
			
			return Message.EDIT_SUCCESS;
		}
		
		return Message.ALERT_ERROR;
	}

	private Integer toInteger(String target) {
		return Integer.parseInt(target);
	}

	public String imageUploadAndSavePath(MultipartRequest mr) {

		MultipartFile photo = mr.getFile("photo");

		BufferedOutputStream stream = null;
		String fixedPath = null;

		// 같은 파일의 이름이 업로드 되려고할 때의 처리로직 추가 필요.
		if (null != photo && !photo.isEmpty()) {
			try {
				File imageFile = new File(ReplyBoardApplication.ROOT + "/" + photo.getOriginalFilename());
				String filePath = imageFile.getPath();

				// 순수 파일이름만 DB에 저장하는 방식을 취함.
				fixedPath = imageFile.getPath().substring(filePath.lastIndexOf('\\') + 1, filePath.length());

				stream = new BufferedOutputStream(new FileOutputStream(imageFile));
				FileCopyUtils.copy(photo.getInputStream(), stream);

			} catch (Exception e) {
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

		return fixedPath;
	}
}
