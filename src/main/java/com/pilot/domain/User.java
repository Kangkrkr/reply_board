package com.pilot.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table
public class User implements Serializable{

	private Integer id;
	private String email;
	private String name;
	private String password;
	private List<Post> posts = new ArrayList<>();
	
	public User(String email, String name, String password) {
		this.email = email;
		this.name = name;
		this.password = password;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "email", unique = true, nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "password", nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// 하나의 유저는 다수의 Post를 가질수 있지만, 누가 썼는지를 알아야하기 때문에 User객체의 변수명인 user와 맵핑되어야 함.
	@OneToMany(mappedBy = "user", targetEntity = Post.class)
	// CascadeType.DELETE 까지 줬더니 Post row가 사라질때 해당 User도 같이 사라져버리는 문제 발생..
	@Cascade(value = {CascadeType.SAVE_UPDATE})
	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts){
		this.posts.clear();
		this.posts.addAll(posts);
	}
}