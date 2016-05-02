package com.pilot.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
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

@Entity
@Table
public class Post implements Serializable, Cloneable {

	private Integer id;
	private String image;
	private String content;
	private Date regdate;
	private String password;
	private User user;
	private List<Reply> replies = new ArrayList<>();

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "image", nullable = true)
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	// 기본타입과 크기 varchar(6000)을 주어야한다.
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

	@ManyToOne(fetch = FetchType.EAGER)
	@Cascade(value = {CascadeType.SAVE_UPDATE})
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	// 하나의 Post는 다수의 Reply를 작성할수 있지만, 어느 Post에 대한 Reply인지를 알아야하므로 Reply(변수명 parent)와 맵핑되어야함.
	@OneToMany(mappedBy = "post", targetEntity = Reply.class)
	// 안해주면 detached entity passed to persist: com.pilot.domain.Reply 자꾸 뿜음.
	@Cascade(value = {CascadeType.SAVE_UPDATE})
	public List<Reply> getReplies() {
		return replies;
	}

	public void setReplies(List<Reply> replies) {
		this.replies.clear();
		this.replies.addAll(replies);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}