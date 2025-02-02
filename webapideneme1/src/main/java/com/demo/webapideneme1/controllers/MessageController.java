package com.demo.webapideneme1.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.webapideneme1.models.Message;
import com.demo.webapideneme1.services.MessageService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/messages")
public class MessageController {
	
	MessageService messageService;
	
	@Autowired
	public MessageController(MessageService messageService) {
		super();
		this.messageService = messageService;
	}
	
	@GetMapping("/getonemessagebyid")
	public  Message getOneMessageById(Long messageId)
	{
		return messageService.getOneMessageById(messageId);
	}
	@PostMapping("/createmessage")
	public String createMessage(HttpServletRequest request,Long messageReceiverId,
			String messageContent,Long quotedMessageId,Long quotedMediaId)
	{
		
		return messageService.createMessage(request,messageReceiverId,
				messageContent,quotedMessageId,quotedMediaId);
	}
	@PutMapping("/editmessagecontent")
	public String editMessageContent(HttpServletRequest request,Long messageId,
			String newMessageContent)
	{
		return messageService.editMessageContent(request,messageId,newMessageContent);
	}
	
	@PutMapping("/editmessagestatus")
	public String editMessageContent(Long messageId,
			boolean newMessageStatus)
	{
		return messageService.editMessageStatus(messageId,newMessageStatus);
	}
	
	@PutMapping("/editquotedmessageofamessage")
	public String editQuotedMessageOfAMessage(HttpServletRequest request,Long messageId,
			Long newQuotedMessageId)
	{
		return messageService.editQuotedMessageOfAMessage(request,messageId,newQuotedMessageId);
	}
	
	@DeleteMapping("/deletemessage")
	public String deleteMessage(HttpServletRequest request,Long messageId)
	{
		return messageService.deleteMessage(request,messageId);
	}
	
	@GetMapping("/getmessagesbetweentwousers")
	public List<Message> getMessagesBetweenTwoUsers(HttpServletRequest request,Long user2Id)
	{
		return messageService.getMessagesBetweenTwoUsers(request,user2Id);
	}
	

}
