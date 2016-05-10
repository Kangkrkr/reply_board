package com.pilot.dto;


import com.pilot.entity.Post;
import com.pilot.entity.User;

//겟터와 셋터가 있어야 타임리프에서 접근이 가능하다.
public class PostDTO {
	private Post post;

	public Post getPost() {
		return post;
	}

	// dto 객체에 게시글(post)을 넣고, 해당 게시글의 작성자 정보도 넣는다.
	public void setPost(Post post) {
		this.post = post;
	}

	@Override
	public String toString() {
		return "PostDTO [" + post + "]";
	}
	
}