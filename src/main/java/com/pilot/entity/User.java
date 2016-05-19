package com.pilot.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class User {

	private Integer id;
	private String email;
	private String name;
	private String nickname;
	private String profileImage;
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

	@Column(name = "nickname", nullable = true)
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	@Column(name = "profile_image", nullable = true)
	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	
	@Column(name = "password", nullable = true)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@JsonIgnore // json변환시 불필요한 필드임을 알리는 어노테이션(안해주니 무한재귀호출이 일어남)
	@OneToMany(mappedBy = "user", targetEntity = Post.class)
	@Cascade(value = {CascadeType.SAVE_UPDATE})
	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts){
		this.posts = posts;
	}
	
}