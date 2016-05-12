package com.pilot.model;


import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.pilot.entity.Post;
import com.pilot.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostModel {
	
	private int id;
	private String content;
	private int indent;
	private String image;
	private Date regdate;
	private String type;
	private User user;
	
	public static PostModel create(Post post) {

		PostModel postDTO = new PostModel();
		BeanUtils.copyProperties(post, postDTO);
		
		return postDTO;
	}
}