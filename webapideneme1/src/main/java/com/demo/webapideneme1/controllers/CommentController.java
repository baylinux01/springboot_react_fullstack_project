package com.demo.webapideneme1.controllers;


import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.webapideneme1.models.Comment;
import com.demo.webapideneme1.models.Group;
import com.demo.webapideneme1.models.User;
import com.demo.webapideneme1.repositories.UserRepository;
import com.demo.webapideneme1.services.CommentService;
import com.demo.webapideneme1.services.GroupService;
import com.demo.webapideneme1.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
@CrossOrigin
@RestController
@RequestMapping("/comments")
public class CommentController {
	CommentService commentService;
	UserService userService;
	GroupService groupService;
	
	

//	@GetMapping("/{commentId}")
//	public Comment getOneCommentById(@PathVariable("commentId") long commentId)
//	{
//		Optional<Comment> comment=commentService.getOneCommentById(commentId);
//		if(comment.isPresent())
//		{
//			
//			return comment.get();
//		}else return null;
//	}
	@Autowired
	public CommentController(CommentService commentService, UserService userService, GroupService groupService) {
		super();
		this.commentService = commentService;
		this.userService = userService;
		this.groupService = groupService;
	}


	@GetMapping("/getonecommentbyid")
	public Comment getOneCommentById(long commentId)
	{
		Comment comment=commentService.getOneCommentById(commentId);
		if(comment!=null)
		{
			
			return comment;
		}else return null;
	}
	
	@GetMapping("/getcommentsofagroup")
	public List<Comment> getCommentsOfAGroup(HttpServletRequest request,long groupId)
	{
		
		List<Comment> comments=commentService.getCommentsOfAGroup(request,groupId);
		return comments;
	}
	@GetMapping("/getallcomments")
	public List<Comment> getAllComments()
	{
		List<Comment> comments=commentService.getAllComments();
		
		return comments;
	}
	
	@PostMapping("/createcomment")
	public String createComment(HttpServletRequest request, String content,Long mediaIdToBeQuoted,Long commentIdToBeQuoted,Long groupId)
	{
		return commentService.createComment(request,content,mediaIdToBeQuoted,commentIdToBeQuoted,groupId);
		
	}
	
	@PutMapping("/updatecomment")
	public String updateComment(HttpServletRequest request,Long commentId,String newcontent)
	{
		return commentService.updateComment(request,commentId,newcontent);
	}
	@DeleteMapping("/deletecomment")
	public String deleteComment(HttpServletRequest request,long commentId)
	{
		String result=commentService.deleteComment(request,commentId);
		return result;
	}
	
	

}
