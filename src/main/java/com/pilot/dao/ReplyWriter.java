package com.pilot.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pilot.domain.Post;
import com.pilot.domain.Reply;
import com.pilot.service.PostService;
import com.pilot.service.ReplyService;

@Component
public class ReplyWriter extends WriterImpl {

	@Autowired
	PostService postService;

	@Autowired
	ReplyService replyService;
	
	@Override
	public void write() {
		Reply reply = replyConstructor();

		String typeDistinction = getWriteForm().getType();
		int targetId = getExtraInfo().getTargetId();
		
		if (typeDistinction.equals("reply")) {

			//System.err.println("REPLY" + targetId);

			reply.setDepth(1);
			reply.setPost(postService.findOne(targetId));
			// replyService.write(reply);
			// post.setPost(originalPost);
			// 원본 게시글의 아이디를 댓글게시글에 저장.
		} else {
			//System.err.println("REPLY_ON_REPLY" + targetId);
			
			Reply originalReply = replyService.findOne(targetId);
			List<Reply> replies = replyService.findRepliesByPost(originalReply.getPost());	// 일단 게시글의 댓글목록을 불러온다.
			
			// 댓글을 달려고하는 원본 댓글의 index를 뽑아온다.
			int originalIdx = 0;
			for(int i=0; i<replies.size(); i++){
				if(replies.get(i).getId() == originalReply.getId()){
					//System.err.println("찾았당.");
					break;
				}
				++originalIdx;
			}
			
			//System.err.println("댓글 달려고 하는 대상의 idx : " + originalIdx);
			
			reply.setDepth(originalReply.getDepth() + 1);
			reply.setPost(originalReply.getPost());
			
			Post post = originalReply.getPost();
			replies.add(originalIdx + 1, reply);
			
			replyService.deleteAllByPost(post);
			for(Reply r : replies){
				replyService.write(r);
			}
			
			postService.write(post);
			return;
			
			/*
			// 원본댓글의 아랫번째 댓글이 있다면, 그 사이에 쓰려는 댓글을 삽입한다.
			if(replies.get(originalIdx + 1) != null){
				replies.add(originalIdx + 1, reply);
				originalReply.getPost().setReplies(replies);
			}else{
				replies.add(reply);
			}
			*/
		}
		
		replyService.write(reply);
	}
}
