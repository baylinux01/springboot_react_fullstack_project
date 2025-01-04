package com.demo.webapideneme1.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.webapideneme1.models.MessagePost;
import com.demo.webapideneme1.models.User;
import com.demo.webapideneme1.repositories.MessagePostRepository;
import com.demo.webapideneme1.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class MessagePostService {
	
	MessagePostRepository messagePostRepository;
	UserRepository userRepository;
	@Autowired
	public MessagePostService(MessagePostRepository messagePostRepository, UserRepository userRepository) {
		super();
		this.messagePostRepository = messagePostRepository;
		this.userRepository = userRepository;
	}
	public List<MessagePost> getMessagePostsBetweenTwoUsers(HttpServletRequest request, Long user2Id) {
		/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
		Principal pl=request.getUserPrincipal();
		String username=pl.getName();
		/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
		User user1=userRepository.findByUsername(username);
		User user2=userRepository.findById(user2Id).orElse(null);
		if(user1!=null&&user2!=null)
		{
			return messagePostRepository.findByTwoUsers(user1.getId(),user2.getId());
		}
		else
		{
			return new ArrayList<MessagePost>();
		}
	}
	
	

}
