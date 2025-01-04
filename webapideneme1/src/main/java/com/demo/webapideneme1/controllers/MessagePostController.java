package com.demo.webapideneme1.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.webapideneme1.models.MessagePost;
import com.demo.webapideneme1.services.MessagePostService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/messageposts")
public class MessagePostController {
	
	MessagePostService messagePostService;

	@Autowired
	public MessagePostController(MessagePostService messagePostService) {
		super();
		this.messagePostService = messagePostService;
	}
	
	@GetMapping("/getmessagepostsbetweentwousers")
	public List<MessagePost> getMessagePostsBetweenTwoUsers(HttpServletRequest request,Long user2Id)
	{
		return messagePostService.getMessagePostsBetweenTwoUsers(request,user2Id);
	}

}
