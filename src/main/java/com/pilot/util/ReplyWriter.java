package com.pilot.util;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pilot.domain.Post;
import com.pilot.domain.Reply;
import com.pilot.service.PostService;
import com.pilot.service.ReplyService;
import com.pilot.validator.WriteForm;

@Component
public class ReplyWriter extends WriterImpl {

	@Autowired
	PostService postService;
	
	@Autowired
	ReplyService replyService;

	@Override
	public void write() {
		
		String type = super.getWriteForm().getType().split("#")[0];
		
		int targetId = getExtraInfo().getTargetId();
		
		Reply reply = super.replyConstructor();
		reply.setRegdate(new Date());
		
		if(type.equals("reply")){
			Post post = postService.findOne(targetId);
			reply.setDepth(1);
			reply.setPost(post.getId());
		}else if(type.equals("reply_on_reply")){
			Reply originalReply = replyService.findOne(targetId);
			reply.setDepth(originalReply.getDepth() + 1);
			reply.setPost(originalReply.getPost());
		}
		
		replyService.write(reply);
	}
}
