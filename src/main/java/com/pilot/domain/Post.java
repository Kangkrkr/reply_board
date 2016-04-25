package com.pilot.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AssociationOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.metamodel.relational.ForeignKey;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pilot.service.UserService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
@ToString(exclude = "replies")
public class Post {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;
	
	// 기본타입과 크기 varchar(6000)을 주어야한다.
	@Column(name = "content", nullable = false)
	private String content;
	
	@Column(name = "regdate")
	private Date regdate;
	
	@JsonIgnore
	@Column(name = "password", nullable = false)
	private String password;
	
	@JoinColumn(name = "id")
	private Integer user;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "post")
	private List<Reply> replies;
}
