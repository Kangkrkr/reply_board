package com.pilot.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class Post {

	private int id;
	private int indent;
	private String path;
	private String type;
	private String image;
	private String profileImage;
	private String content;
	private Date regdate;
	private User user;
	
	private Post rootPost;
	private List<Post> branchPosts = new ArrayList<>();

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "indent", columnDefinition = "int default 0")
	public Integer getIndent() {
		return indent;
	}

	public void setIndent(Integer indent) {
		this.indent = indent;
	}
	
	@Column(name = "path", columnDefinition = "varchar(10000)")
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Column(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "image", nullable = true)
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	@Column(name = "profile_image", nullable = true)
	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

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

	@ManyToOne(targetEntity = User.class)
	@Cascade(value = {CascadeType.SAVE_UPDATE})
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(targetEntity = Post.class)
	@Cascade(value = {CascadeType.SAVE_UPDATE})
	@JoinColumn(name = "root_post_id")
	public Post getRootPost() {
		return rootPost;
	}

	public void setRootPost(Post rootPost) {
		this.rootPost = rootPost;
	}

	@OneToMany(targetEntity = Post.class, mappedBy = "rootPost")
	@Cascade(value = {CascadeType.SAVE_UPDATE, CascadeType.DELETE})
	public List<Post> getBranchPosts() {
		return branchPosts;
	}

	public void setBranchPosts(List<Post> branchPosts) {
		this.branchPosts.clear();
		this.branchPosts.addAll(branchPosts);
	}

}