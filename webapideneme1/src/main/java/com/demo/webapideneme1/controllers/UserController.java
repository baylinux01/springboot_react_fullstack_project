package com.demo.webapideneme1.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.webapideneme1.models.Dto;
import com.demo.webapideneme1.models.User;
import com.demo.webapideneme1.services.UserService;

import io.jsonwebtoken.security.InvalidKeyException;
import jakarta.servlet.http.HttpServletRequest;
@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
	UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	
	
	@DeleteMapping("/deleteconnection")
	public String deleteConnection(HttpServletRequest request, Long userToBeDeletedId) 
	{
		
		String result=userService.deleteConnection(request,userToBeDeletedId);
		return result;
	}
	
	@PostMapping("/acceptconnection")
	public String acceptConnection(HttpServletRequest request,Long userToBeAcceptedId) 
	{
		String result=userService.acceptConnection(request,userToBeAcceptedId);
		return result;
	}
	
	@PostMapping("/blockuser")
	public String blockUser(HttpServletRequest request,Long userToBeBlockedId) 
	{ 
		
		String result=userService.blockUser(request,userToBeBlockedId);
		return result;
	}
	@PostMapping("/unblockuser")
	public String unblockUser(HttpServletRequest request,Long userToBeUnblockedId) 
	{ 
		
		String result=userService.unblockUser(request,userToBeUnblockedId);
		return result;
	}
	
	@GetMapping("/getconnectionsofauser")
	public List<User> getConnectionsOfAUser(Long userId)
	{
		return userService.getConnectionsOfAUser(userId);
	}
	
	@GetMapping("/getblockedusersofcurrentuser")
	public List<User> getBlockedUsersOfCurrentUser(HttpServletRequest request)
	{
		return userService.getBlockedUsersOfCurrentUser(request);
	}
	@GetMapping("/getblockedusersofauser")
	public List<User> getBlockedUsersOfAUser(HttpServletRequest request,Long userId)
	{
		return userService.getBlockedUsersOfAUser(request,userId);
	}

	@GetMapping("/getsearchedusers")
	public List<User> getSearchedUsers(String searchedWords)
	{
		return userService.getSearchedUsers(searchedWords);
	}
	@GetMapping("/getallusers")
	public List<User> getAllUsers()
	{
		
		List<User> users= userService.getAllUsers();
		return users;
		
	}
	@GetMapping("/getoneuserbyid")
	public User getOneUserById(long userId)
	{
		User user=userService.getOneUserById(userId);
		if(user!=null)
		{
			return user;
			
		}else return null;
		
	}
	@GetMapping("/getoneuserbyusername")
	public User getOneUserByUsername(String username)
	{
		User user=userService.getOneUserByUsername(username);
		return user;
	}
	@PostMapping("/createuser")
	public  String createUser(String name, String surname, String username,  
			String password, MultipartFile userimage, String birthdate) throws IOException 
	{
		
		
		String result=userService.createUser( name,surname,username,  
				password, userimage,birthdate);
		return result;
		
		
	}
	@PostMapping("/enteruser")
	public User enterUser(String username,String password) throws InvalidKeyException, NoSuchAlgorithmException 
	{
		return userService.enterUser(username,password);
		
	}
	@PostMapping("/exituser")
	public void exitUser(HttpServletRequest request,String username)
	{
		
	}
	@PutMapping("/changeuserimage")
	public String changeUserImage(HttpServletRequest request,MultipartFile newuserimage) throws IOException
	{
		return userService.changeUserImage(request,newuserimage);
	}
	@PutMapping("/updateuser")
	public String updateUser
	(HttpServletRequest request, String newname,String newsurname, String newusername,
		MultipartFile newuserimage,String newbirthdate) 
	throws IOException
	{
		String result=userService.updateUser(request, newname,newsurname, newusername,newuserimage,newbirthdate);
		return result;
	}
	@DeleteMapping
	public String deleteUser(long id)
	{
		String result=userService.deleteUser(id);
		return result;
	}
	
	@PutMapping("/changeuserpassword")
	public String changeUserPassword(HttpServletRequest request,String newPassword)
	{
		
		String result=userService.changeUserPassword(request,newPassword);
			return result;
		
	}
	

}
