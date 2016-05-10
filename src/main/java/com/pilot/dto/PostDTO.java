package com.pilot.dto;


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
public class PostDTO {
	
	private int id;
	private String content;
	private int indent;
	private String image;
	private Date regdate;
	private String type;
	private User user;
	
	public static PostDTO create(Post post) {

		PostDTO postDTO = new PostDTO();
		BeanUtils.copyProperties(post, postDTO);
		
		return postDTO;
	}
}