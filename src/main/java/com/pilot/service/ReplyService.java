package com.pilot.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pilot.dao.PostDao;
import com.pilot.dao.ReplyDao;
import com.pilot.entity.Post;
import com.pilot.entity.Reply;
import com.pilot.validator.WriteForm;

@Service
public class ReplyService extends Writer {

	@Autowired 
	private PostDao postDao;
	
	@Autowired 
	private ReplyDao replyDao;
	
	@PersistenceContext(type = PersistenceContextType.TRANSACTION) 
	private EntityManager entityManager;
	
	public Reply findOne(Integer id){
		return replyDao.findOne(id);
	}
	
	public void write() {
		Reply reply = super.replyConstructor();

		String typeDistinction = getWriteForm().getType();
		int targetId = getExtraInfo().getTargetId();
		
		if (typeDistinction.equals("reply")) {

			reply.setDepth(1);
			reply.setPost(postDao.findOne(targetId));
			
			replyDao.write(reply);
		} else {
			Reply originalReply = replyDao.findOne(targetId);
			Post parentPost = originalReply.getPost();
			
			List<Reply> replies = replyDao.findRepliesByPost(parentPost);	// 일단 게시글의 댓글목록을 불러온다.
			
			// 댓글을 달려고하는 원본 댓글의 index를 뽑아온다.
			int originalIdx = 0;
			for(int i=0; i<replies.size(); i++){
				if(replies.get(i).getId() == originalReply.getId()){
					break;
				}
				++originalIdx;
			}
			
			reply.setDepth(originalReply.getDepth() + 1);
			reply.setPost(parentPost);
			
			// 새로운 댓글을 삽입하려는 위치에 삽입한다.
			replies.add(originalIdx + 1, reply);

			// 댓글목록을 갱신.
			replyDao.refreshReplies(replies);
		}
	}
	
	public List<Reply> findRepliesByPost(Post post){
		return replyDao.findRepliesByPost(post);
	}
	
	public void update(WriteForm writeForm, HttpSession session, String fixedPath){
		replyDao.update(writeForm, session, fixedPath);
	}
	
	public void delete(Integer replyId){
		replyDao.delete(replyDao.findOne(replyId));
	}
}
