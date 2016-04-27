package com.pilot.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "post")
@Access(AccessType.FIELD)
public class Post {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "image", nullable = true)
	private String image;
	
	// 기본타입과 크기 varchar(6000)을 주어야한다.
	@Column(name = "content", nullable = false)
	private String content;
	
	@Column(name = "regdate")
	private Date regdate;
	
	@JsonIgnore
	@Column(name = "password", nullable = false)
	private String password;
	
	@JoinColumn(name = "user_id", nullable = false)
	private int user;

	@OneToMany(targetEntity = Reply.class, orphanRemoval = true)
	@Cascade(value = {CascadeType.SAVE_UPDATE, CascadeType.DELETE})
	@Fetch(FetchMode.SELECT)
	@JoinColumn(name="reply_id")
	private List<Reply> replies;
	
	public void setReplies(List<Reply> replies){
		this.replies.clear();
		this.replies.addAll(replies);
	}
}