package com.pilot.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "REPLY")
public class Reply implements Serializable {	// Post와 유사한것이 대부분이라 상속받으려 했는데, 상속하니까 테이블이 안생김 -_-

	private Integer id;
	private Integer depth;
	private String image;
	private String content;
	private Date regdate;
	private String password;
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

	@Column(name = "password", nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	// 어째서인지 DB 테이블에 기본타입이 TINYBLOB으로 설정되는 이유로,
	// Data truncation: Data too long for column 에러가 떴음.
	// 기본타입으로 LONGBLOB 을 주어서 해결.
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