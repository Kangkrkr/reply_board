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
		
		// 게시글에 단일 댓글만 다는 경우.
		if (typeDistinction.equals("reply")) {

			reply.setDepth(1);
			reply.setPost(postService.findOne(targetId));
		} else {
			// 댓글에 댓글을 달려고 하는 경우.
			
			Reply originalReply = replyService.findOne(targetId);
			Post post = originalReply.getPost();
			
			List<Reply> replies = replyService.findRepliesByPost(post);	//게시글의 댓글목록을 불러온다.
			
			// 댓글을 달려고하는 원본 댓글의 index를 뽑아온다.
			int originalIdx = 0;
			for(int i=0; i<replies.size(); i++){
				if(replies.get(i).getId() == originalReply.getId()){
					break;
				}
				++originalIdx;
			}
			
			// 달려고 하는 댓글의 기본 정보 설정.
			reply.setDepth(originalReply.getDepth() + 1);
			reply.setPost(post);
			
			replies.add(originalIdx + 1, reply);
			
			// 해당 게시글에 대한 모든 댓글을 삭제하고 갱신된 댓글들을 다시 삽입하는 방식.. (개선 필요)
			replyService.deleteAllByPost(post);
			for(Reply r : replies){
				replyService.write(r);
			}
			
			postService.write(post);
			return;
		}
		
		replyService.write(reply);
	}
}
