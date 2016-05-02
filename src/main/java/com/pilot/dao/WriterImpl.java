package com.pilot.dao;


import com.pilot.domain.Post;
import com.pilot.domain.Reply;
import com.pilot.util.ExtraInfo;
import com.pilot.validator.WriteForm;

public abstract class WriterImpl implements Writer {

	private WriteForm writeForm;
	private ExtraInfo info;
	
	public void setWriteForm(WriteForm writeForm) {
		this.writeForm = writeForm;
	}

	public WriteForm getWriteForm(){
		return this.writeForm;
	}
	
	public void setExtraInfo(ExtraInfo info) {
		this.info = info;
	}
	
	public ExtraInfo getExtraInfo(){
		return this.info;
	}

	public Post postConstructor(){
		Post post = new Post();
		post.setImage(info.getFixedPath());
		post.setContent(writeForm.getContent());
		post.setPassword(writeForm.getPassword());
		// 게시일자는 write 시점에서 생성해준다.
		post.setUser(info.getUploader());
		
		return post;
	}
	
	public Reply replyConstructor(){
		Reply reply = new Reply();
		reply.setImage(info.getFixedPath());
		reply.setContent(writeForm.getContent());
		reply.setPassword(writeForm.getPassword());
		reply.setUser(info.getUploader());
		
		return reply;
	}
	
}