package com.pilot.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pilot.entity.Post;
import com.pilot.entity.Reply;

@Component
public class ReplyWriter extends WriterImpl {

	@Autowired PostDao postDao;
	@Autowired ReplyDao replyDao;
	
	
	@Override
	public void write() {
		Reply reply = replyConstructor();

		String typeDistinction = getWriteForm().getType();
		int targetId = getExtraInfo().getTargetId();
		
		if (typeDistinction.equals("reply")) {

			reply.setDepth(1);
			reply.setPost(postDao.findOne(targetId));
		} else {
			Reply originalReply = replyDao.findOne(targetId);
			Post parnetPost = originalReply.getPost();
			
			List<Reply> replies = replyDao.findRepliesByPost(parnetPost);	// 일단 게시글의 댓글목록을 불러온다.
			
			// 댓글을 달려고하는 원본 댓글의 index를 뽑아온다.
			int originalIdx = 0;
			for(int i=0; i<replies.size(); i++){
				if(replies.get(i).getId() == originalReply.getId()){
					break;
				}
				++originalIdx;
			}
			
			reply.setDepth(originalReply.getDepth() + 1);
			reply.setPost(parnetPost);
			
			replies.add(originalIdx + 1, reply);
			
			replyDao.deleteAllByPost(parnetPost);
			for(Reply r : replies){
				replyDao.write(r);
			}
			
			postDao.write(parnetPost);
			return;
		}
		
		replyDao.write(reply);
	}
}
