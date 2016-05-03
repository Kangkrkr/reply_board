package com.pilot.dto;

import java.util.List;

import com.pilot.entity.Post;
import com.pilot.entity.Reply;
import com.pilot.entity.User;

//겟터와 셋터가 있어야 타임리프에서 접근이 가능하다.
public class PostDTO {
	private User user;
	private Post post;
	private List<Reply> replies;
	private int replySize;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Post getPost() {
		return post;
	}

	public List<Reply> getReplies() {
		return replies;
	}

	public void setReplies(List<Reply> replies) {
		this.replies = replies;
	}

	public int getReplySize() {
		return replySize;
	}

	public void setReplySize(int replySize) {
		this.replySize = replySize;
	}

	// dto 객체에 게시글(post)을 넣고, 해당 게시글의 작성자 정보도 넣는다.
	public void setPost(Post post) {
		this.post = post;
		this.user = post.getUser();
	}
}