package com.pilot.domain;

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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Data;

@Data
@Entity
@Table(name = "user")
@Access(AccessType.FIELD)
public class User {
	@Id
	@GeneratedValue
	@Column(name = "id", updatable=false, nullable=false)
	private Integer id;
	
	@Column(name = "email", unique = true, nullable = false)
	private String email;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	// JoinTable을 사용하면 그 테이블의 이름인 post_reply 라는 테이블에 포스트의 아이디와, 댓글의 아이디가 함께 들어가버려서 삭제에 실패하는 경우가 생김.
	// 그래서, JoinColumn을 사용하여 외래키가 낑기지 않게 만들었다.
	@OneToMany(targetEntity = Post.class, orphanRemoval = true)
	@Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
	@Fetch(FetchMode.SELECT)
    @JoinColumn(name="post_id")
	private List<Post> posts;
	
	// 롬복의 getter, setter 자동 생성 기능 때문에 이상현상 발생.
	// A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance 에러를 방지하기 위해 setter 재정의.
	public void setPosts(List<Post> posts){
		this.posts.clear();
		this.posts.addAll(posts);
	}
}