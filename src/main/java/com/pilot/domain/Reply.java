package com.pilot.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "replies")
public class Reply {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	// 기본타입과 크기 varchar(6000)을 주어야한다.
	@Column(name = "content", nullable = false)
	private String content;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "regdate")
	private java.util.Date regdate;
	
	@JsonIgnore
	@Column(name = "password", nullable = false)
	private String password;
	
//	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id", nullable = false)
	private int post;
	
	/*
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reply_user", nullable = false)
	private User reply_user;
	*/
}
