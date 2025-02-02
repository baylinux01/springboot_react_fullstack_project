package com.demo.webapideneme1.services;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.webapideneme1.models.Media;
import com.demo.webapideneme1.models.Message;
import com.demo.webapideneme1.models.MessagePost;
import com.demo.webapideneme1.models.User;
import com.demo.webapideneme1.repositories.MediaRepository;
import com.demo.webapideneme1.repositories.MessagePostRepository;
import com.demo.webapideneme1.repositories.MessageRepository;
import com.demo.webapideneme1.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class MessageService {
	UserRepository userRepository;
	MessageRepository messageRepository;
	MessagePostRepository messagePostRepository;
	MediaRepository mediaRepository;
	
	@Autowired
	public MessageService(UserRepository userRepository, MessageRepository messageRepository
			,MessagePostRepository messagePostRepository,MediaRepository mediaRepository) {
		super();
		this.userRepository=userRepository;
		this.messageRepository = messageRepository;
		this.messagePostRepository=messagePostRepository;
		this.mediaRepository=mediaRepository;
	}

	public Message getOneMessageById(Long messageId) {
		
		return messageRepository.findById(messageId).orElse(null);
	}
	@Transactional
	public String createMessage(HttpServletRequest request, Long messageReceiverId, 
			String messageContent, Long quotedMessageId, Long quotedMediaId) 
	{
		/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
		Principal pl=request.getUserPrincipal();
		String username=pl.getName();
		/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
		User messageSender=userRepository.findByUsername(username);
		User messageReceiver=userRepository.findById(messageReceiverId).orElse(null);
		Message quotedMessage=null;
		Media quotedMedia=null;
		if(quotedMessageId!=null)
		{
			 quotedMessage=messageRepository
				.findById(quotedMessageId).orElse(null);
		}
		else if(quotedMessageId==null&&quotedMediaId!=null)
		{
			quotedMedia=mediaRepository
					.findById(quotedMediaId).orElse(null);
		}
		if(!messageReceiver.getBlockedUsers().contains(messageSender)
				&&!messageSender.getBlockedUsers().contains(messageReceiver)
				&&messageSender.getConnections().contains(messageReceiver)
				&&messageReceiver.getConnections().contains(messageSender))
		{
			Message message=new Message();
			message.setMessageSender(messageSender);
			message.setMessageReceiver(messageReceiver);
			if(quotedMessage!=null
					&&((quotedMessage.getMessageSender()==messageSender
					&&quotedMessage.getMessageReceiver()==messageReceiver)
							||(quotedMessage.getMessageSender()==messageReceiver
									&&quotedMessage.getMessageReceiver()==messageSender)))
			message.setQuotedMessage(quotedMessage);
			if(message.getQuotedMessage()==null&&quotedMedia!=null)
			message.setQuotedMedia(quotedMedia);
			message.setMessageContent(messageContent);
			messageRepository.save(message);
			MessagePost messagePost=new MessagePost();
			messagePost.setSender(messageSender);
			messagePost.setReceiver(messageReceiver);
			messagePost.setMessage(message);
			messagePostRepository.save(messagePost);
			return "success";
			
		}else
		return "fail";
	}

	public String editMessageContent(HttpServletRequest request,Long messageId, String newMessageContent) {
		/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
		Principal pl=request.getUserPrincipal();
		String username=pl.getName();
		/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
		User user=userRepository.findByUsername(username);
		Message message=messageRepository.findById(messageId).orElse(null);
		if(message!=null&&message.getMessageSender()==user)
		{
			message.setMessageContent(newMessageContent);
			message.setMessageEditDate(LocalDateTime.now(ZoneId.of("Turkey")));
			messageRepository.save(message);
			return "success";
		}
		return "fail";
	}

	public String editQuotedMessageOfAMessage(HttpServletRequest request,
			Long messageId, 
			Long newQuotedMessageId) {
		/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
		Principal pl=request.getUserPrincipal();
		String username=pl.getName();
		/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
		User user=userRepository.findByUsername(username);
		Message message=messageRepository.findById(messageId).orElse(null);
		Message newQuotedMessage=messageRepository.findById(newQuotedMessageId).orElse(null);
		if(message!=null&&message.getQuotedMessage().getMessageSender()==user)
		{
			message.setQuotedMessage(newQuotedMessage);
			message.setMessageEditDate(LocalDateTime.now(ZoneId.of("Turkey")));
			messageRepository.save(message);
			return "success";
		}
		return "fail";
	}

	public String deleteMessage(HttpServletRequest request,Long messageId) {
		/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
		Principal pl=request.getUserPrincipal();
		String username=pl.getName();
		/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
		User user=userRepository.findByUsername(username);
		Message message=messageRepository.findById(messageId).orElse(null);
		
		if(message!=null&&(message.getMessageSender()==user||user.getRoles().contains("ADMIN")))
		{
			MessagePost messagePost=messagePostRepository.findByMessage(message);
			List<Message> quotingMessages=messageRepository.findByQuotedMessage(message);
			if(quotingMessages!=null)
			{
				for(Message quotingMessage: quotingMessages)
				{
					quotingMessage.setQuotedMessage(null);
					messageRepository.save(quotingMessage);
				}
			}
			if(messagePost!=null)
			{
				messagePost.setMessage(null);
				messagePostRepository.save(messagePost);
				messagePostRepository.delete(messagePost);
			}
			messageRepository.delete(message);
			return "message deleted";
		}
			return "fail";
		
		
	}

	public List<Message> getMessagesBetweenTwoUsers(HttpServletRequest request, Long user2Id) {
		/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
		Principal pl=request.getUserPrincipal();
		String username=pl.getName();
		/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
		User user1=userRepository.findByUsername(username);
		User user2=userRepository.findById(user2Id).orElse(null);
		List<Message> messages=null;
		if(user1!=null && user2!=null)
		{
			messages=messageRepository
					.findByMessageSenderandMessageReceiver(user1.getId(),user2Id);
			return messages;
		}else
		
		return null;
	}

	public String editMessageStatus(Long messageId, boolean newMessageStatus) {
		Message message=messageRepository.findById(messageId).orElse(null);
		if(message!=null)
		{
			message.setRead(newMessageStatus);
			messageRepository.save(message);
			return "success";
		}
		
		return "fail";
	}

	

	
	
	

}
