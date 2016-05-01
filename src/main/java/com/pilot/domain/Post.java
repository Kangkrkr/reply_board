package com.pilot.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Target;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "POST")
public class Post implements Serializable {

	private Integer id;
	private Integer depth;
	private String image;
	private String content;
	private Date regdate;
	private String password;
	private User user;
	private Post post;
	//private List<Reply> replies;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "depth", nullable = false, columnDefinition = "int default 0")
	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}
	
	@Column(name = "image", nullable = true)
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	// 기본타입과 크기 varchar(6000)을 주어야한다.
	@Column(name = "content", nullable = false)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "regdate", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	public Date getRegdate() {
		return regdate;
	}

	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}

	@Column(name = "password", nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@ManyToOne
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Post.class)
	@JoinColumn(name = "post_id")
	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	
	/*
	@OneToMany(targetEntity = Reply.class, orphanRemoval = true)
	@Cascade(value = { CascadeType.SAVE_UPDATE, CascadeType.DELETE })
	@Fetch(FetchMode.SELECT)
	@JoinColumn(name = "reply_id")
	public List<Reply> getReplies() {
		return replies;
	}
	
	public void setReplies(List<Reply> replies) {
		this.replies.clear();
		this.replies.addAll(replies);
	}
	*/
}