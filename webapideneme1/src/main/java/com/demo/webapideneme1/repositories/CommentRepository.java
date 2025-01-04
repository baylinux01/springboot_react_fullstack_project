package com.demo.webapideneme1.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.webapideneme1.models.Comment;
import com.demo.webapideneme1.models.Group;
@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

	List<Comment> findByGroupId(long groupId);

	List<Comment> findByQuotedComment(Comment comment);

	

}
