package com.pilot.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
@Access(AccessType.FIELD)
public class User {
	@Id
	@GeneratedValue
	@Column(name = "user_id", updatable=false, nullable=false)
	private Integer id;
	
	@Column(name = "email", unique = true, nullable = false)
	private String email;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@OneToMany(targetEntity = Post.class, orphanRemoval = true)
	@Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
	@Fetch(FetchMode.SELECT)
    @JoinTable(name = "user_post", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "post_id") })
	private Set<Post> posts;
	
	// 롬복의 getter, setter 자동 생성 기능 때문에 이상현상 발생.
	// A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance 에러를 방지하기 위해 setter 재정의.
	public void setPosts(Set<Post> posts){
		this.posts.clear();
		this.posts.addAll(posts);
	}
}