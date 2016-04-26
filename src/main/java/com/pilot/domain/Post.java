package com.pilot.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
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
@Table(name = "posts")
@Access(AccessType.FIELD)
public class Post {
	
	@Id
	@GeneratedValue
	@Column(name = "post_id")
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
	
	@ManyToOne(targetEntity = User.class)
	private User user;

	@OneToMany(targetEntity = Reply.class, orphanRemoval = true)
	@Cascade(value = {CascadeType.SAVE_UPDATE, CascadeType.REMOVE})
	@Fetch(FetchMode.SELECT)
	@JoinTable(name = "post_reply", joinColumns = { @JoinColumn(name = "post_id") }, inverseJoinColumns = { @JoinColumn(name = "reply_id") })
	private Set<Reply> replies;
	
	// A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance 에러를 방지하기 위해 setter 재정의.
	public void setReplies(Set<Reply> replies){
		this.replies.clear();
		this.replies.addAll(replies);
	}
}