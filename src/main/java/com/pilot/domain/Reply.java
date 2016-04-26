package com.pilot.domain;


import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "replies")
public class Reply {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "reply_id")
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
	
	
	@JoinColumn(name = "post_id", nullable = false)
	private int post;
	
	@ManyToOne(targetEntity = User.class)
	private User user;
}
