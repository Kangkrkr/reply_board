package com.pilot.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class Reply implements Serializable {	// Post와 유사한 것이 대부분이라 상속처리가 가능한지 고려해봐야 겠다.

	private Integer id;
	private Integer depth;
	private String image;
	private String content;
	private Date regdate;
	private User user;
	private Post post;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "depth", nullable = false, columnDefinition = "int default 1")
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

	// 기본타입과 크기 varchar(6000)을 주어야한다 -> X 한글은 3, 영어는 2씩잡아먹으므로 틀림.
	// length 를 통해 설정하도록.
	@Column(name = "content", nullable = false, columnDefinition = "varchar(6000)")
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
	
	@Column(name = "user", nullable = false, columnDefinition = "LONGBLOB")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne
//	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "post_id")
	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

}