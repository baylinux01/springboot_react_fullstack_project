package com.demo.webapideneme1.services;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.demo.webapideneme1.models.ConnectionRequest;
import com.demo.webapideneme1.models.Dto;
import com.demo.webapideneme1.models.User;
import com.demo.webapideneme1.repositories.UserRepository;

import io.jsonwebtoken.security.InvalidKeyException;
import jakarta.servlet.http.HttpServletRequest;


@Service
public class UserService {
	
	private UserRepository userRepository;
	private ConnectionRequestService connectionRequestService;
	private AuthenticationManager authenticationManager;
	//private JWTService jWTService;
	private PasswordEncoder passwordEncoder;
//	private BCryptPasswordEncoder bCPE=new BCryptPasswordEncoder(12);
	
	
	@Autowired
	public UserService(UserRepository userRepository, ConnectionRequestService connectionRequestService,
			AuthenticationManager authenticationManager, JWTService jWTService,PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.connectionRequestService = connectionRequestService;
		this.authenticationManager = authenticationManager;
		//this.jWTService = jWTService;
		this.passwordEncoder=passwordEncoder;
	}





	public String saveUser(User user) {
		if(user.getName()==null||user.getName().equals("")) return "Registration is unsuccessful name cannot be null";
		if(user.getSurname()==null||user.getSurname().equals("")) return "Registration is unsuccessful surname cannot be null";
		if(user.getUsername()==null||user.getUsername().equals("")) return "Registration is unsuccessful username cannot be null";
		if(user.getPassword()==null||user.getPassword().equals("")) return "Registration is unsuccessful password cannot be null";
		if(!user.getName().matches("^[öüÖÜĞğşŞçÇıİ|a-z|A-Z]{2,20}(\\s[öüÖÜĞğşŞçÇıİ|a-z|A-Z]{2,20})?$")) return "Name is not suitable to the format. Registration is unsuccessful";
		if(!user.getSurname().matches("^[öüÖÜĞğşŞçÇıİa-zA-Z]{2,20}$")) return "Surnam)e is not suitable to the format. Registration is unsuccessful";
		if(!user.getUsername().matches("^[öüÖÜĞğşŞçÇıİa-zA-Z0-9]{2,20}$")) return "Username is not suitable to the format. Registration is unsuccessful";
		if(!user.getPassword().matches("^[öüÖÜĞğşŞçÇıİa-zA-Z0-9]{2,20}$")) return "Password is not suitable to the format. Registration is unsuccessful";
		
		
		List<User> users= userRepository.findAll();
		
		for(User u : users)
		{
			if(user.getUsername().equals(u.getUsername())) return "Username cannot be the same as another one. Registration is unsuccessful";
		}
		
		userRepository.save(user);
		return "Registration is successful";
		
	}

	

	

	public String updateUser
	(HttpServletRequest request, String newname, String newsurname, String newusername,
			MultipartFile newuserimage,String newbirthdate) 
	throws IOException 
	{
		/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
		Principal pl=request.getUserPrincipal();
		String username=pl.getName();
		/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
		User user=userRepository.findByUsername(username);
		List<User> users;
		String newname2="";
		String newsurname2="";
		String newusername2="";
		byte[] newuserimage2=null;
		Date newbirthdate2;
		if(user!=null)
		{
		
		if(newname==null ||newname.equals(""))newname2=user.getName();
		else newname2=newname;
		if(newsurname==null||newsurname.equals(""))newsurname2=user.getSurname();
		else newsurname2=newsurname;
		if(newusername==null||newusername.equals(""))newusername2=user.getUsername();
		else newusername2=newusername;
		if(newbirthdate==null || newbirthdate.equals("")) newbirthdate2=user.getBirthDate();
		else
		{
			int day=0;
			int month=0;
			int year=0000;
			if(newbirthdate!=null)
			{
				String[] arr=new String[3];
				arr=newbirthdate.split("-");
				year=Integer.valueOf(arr[0]);
				month=Integer.valueOf(arr[1]);
				day=Integer.valueOf(arr[2]);
				
			}
			LocalDate localDate=LocalDate.of(year, month, day);
			//newbirthdate2 = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			newbirthdate2=java.sql.Date.valueOf(localDate);
		}
		
		if(!newname2.matches("^[öüÖÜĞğşŞçÇıİ|a-z|A-Z]{2,20}(\\s[öüÖÜĞğşŞçÇıİ|a-z|A-Z]{2,20})?$")) return "Name is not suitable to the format. Update is unsuccessful";
		if(!newsurname2.matches("^[öüÖÜĞğşŞçÇıİa-zA-Z]{2,20}$")) return "Surname is not suitable to the format. Update is unsuccessful";
		if(!newusername2.matches("^[öüÖÜĞğşŞçÇıİa-zA-Z0-9]{2,20}$")) return "Username is not suitable to the format. Update is unsuccessful";
		if(newuserimage!=null&&newuserimage.getContentType()!=null) 
		{
			if(!newuserimage.getContentType().equals("image/jpeg")&&!newuserimage.getContentType().equals("image/png"))
				return "Image file is not suitable to the format. Please load a jpeg or png file";
		}
		
		if(newuserimage==null||newuserimage.getContentType()==null
			||(!newuserimage.getContentType().equals("image/jpeg")
			&&!newuserimage.getContentType().equals("image/png"))) 
			newuserimage2=user.getUserImage();
		else newuserimage2=newuserimage.getBytes();
		
		users= userRepository.findAll();
		users.remove(user);
		for(User u : users)
		{
			if(newusername2.equals(u.getUsername())) return "Username cannot be the same as another one. Update is unsuccessful";
		}
		}else return "User not found. Update is unsuccessful";
		user.setName(newname2);
		user.setSurname(newsurname2);
		user.setUsername(newusername2);
		user.setUserImage(newuserimage2);
		user.setBirthDate(newbirthdate2);
		userRepository.save(user);
		return "Update is successful";
	}

	public List<User> getAllUsers() {
		List<User> users=userRepository.findAll();
		
		return users;
		
	}

	public String deleteUser(long id) {
		Optional<User> user=userRepository.findById(id);
		if(user.isPresent()) userRepository.delete(user.get());
		else return "User not found. User deletion is unsuccessful";
		return "User deletion is successful";
	}

	public User getOneUserById(Long userId) {
		User user=userRepository.findById(userId).orElse(null);
		if(user!=null)
		{
			
			return user;
		}
		return null;
	}

	public User getOneUserByUsername(String username) {
		User user= userRepository.findByUsername(username);
		return user;
	}

	public User enterUser(String username, String password) throws InvalidKeyException, NoSuchAlgorithmException {
		Authentication authentication =authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username,password));
		if(authentication.isAuthenticated()) 
		{
			//String token=jWTService.generateToken(username);
			User user=userRepository.findByUsername(username);
			if(user!=null)
			{
				
				return user;
			}return null;
			
		}
		else return null;
//		List<User> users= userRepository.findAll();
//		for(User u : users)
//		{
//			if( u.getUsername().equals(username) && u.getPassword().equals(password))
//			{
//				return true;
//			}
//		}
//		return false;
	}

	public List<User> getSearchedUsers(String searchedWords) {
		String[] array= searchedWords.split("\\s");
		int i=0;
		Set<User> searchedUsersSet=new HashSet<User>();
		List<User> prevUsers=new ArrayList<User>();
		while(i<array.length)
		{
			List<User> users=new ArrayList<User>();
			users=userRepository.getSearchedUsers(array[i]);
			
				
				int b=0;
				while(b<users.size())
				{
					if(!searchedUsersSet.contains(users.get(b)))
					searchedUsersSet.clear();
					b++;
					if(searchedUsersSet.size()>users.size())
					searchedUsersSet.clear();
					
				}
			
			
			
				int d=0;
				while(d<users.size())
				{
					if(prevUsers.size()>0 && !prevUsers.contains(users.get(d)))
					{
						users.remove(users.get(d));
						d--;
					}
					d++;
				}
			prevUsers.clear();
			prevUsers.addAll(users);
			searchedUsersSet.addAll(users);
			i++;
		}
		List<User> searchedUsersList=new ArrayList<User>(searchedUsersSet);
		Comparator<User> myComparator=Comparator.comparing(User::getName,String.CASE_INSENSITIVE_ORDER)
				.thenComparing(User::getSurname,String.CASE_INSENSITIVE_ORDER);
		List<User> sortedSearchedUsersList=searchedUsersList.stream()
				.sorted(myComparator).collect(Collectors.toList());
		return sortedSearchedUsersList;
	}

	public List<User> getBlockedUsersOfCurrentUser(HttpServletRequest request) {
		/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
		Principal pl=request.getUserPrincipal();
		String username=pl.getName();
		/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
		User user=userRepository.findByUsername(username);
		if(user!=null&&user.getBlockedUsers()!=null)
		return user.getBlockedUsers();
		else return null;
	}
	@Transactional
	public String blockUser(HttpServletRequest request, Long userToBeBlockedId) {
		/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
		Principal pl=request.getUserPrincipal();
		String username=pl.getName();
		/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
		User blockingUser=userRepository.findByUsername(username);
		User userToBeBlocked=userRepository.findById(userToBeBlockedId).orElse(null);
		if(blockingUser==userToBeBlocked) return "blockingUser and userToBeBlocked cannot be the same person";
		if(!blockingUser.getBlockedUsers().contains(userToBeBlocked))
		{
			if(blockingUser.getConnections().contains(userToBeBlocked))
				blockingUser.getConnections().remove(userToBeBlocked);
			if(userToBeBlocked.getConnections().contains(blockingUser))
				userToBeBlocked.getConnections().remove(blockingUser);
			
			List<ConnectionRequest> conreqs=
					connectionRequestService.getAllConnectionRequests();
			List<ConnectionRequest> conreqstoberemoved=new ArrayList<ConnectionRequest>();
			if(conreqs!=null&&conreqs.size()>0)
			{
				int i=0;
				while(i<conreqs.size())
				{
					if((conreqs.get(i).getConnectionRequestSender()==
							userToBeBlocked && conreqs.get(i).getConnectionRequestReceiver()==blockingUser)
							||
							(conreqs.get(i).getConnectionRequestReceiver()==
							userToBeBlocked && conreqs.get(i).getConnectionRequestSender()==blockingUser))
						{
							conreqstoberemoved.add(conreqs.get(i));
							
						}
							i++;
				}
			}
			blockingUser.getBlockedUsers().add(userToBeBlocked);
			connectionRequestService.removeAll(conreqstoberemoved);
			userRepository.save(blockingUser);
			userRepository.save(userToBeBlocked);
			return "success";
			
			
		}
		
		return "false";
	}
	@Transactional
	public String acceptConnection(HttpServletRequest request, Long userToBeAcceptedId) {
		/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
		Principal pl=request.getUserPrincipal();
		String username=pl.getName();
		/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
		User acceptingUser=userRepository.findByUsername(username);
		User userToBeAccepted=userRepository.findById(userToBeAcceptedId).orElse(null);
		List<ConnectionRequest> conreqs=
				connectionRequestService.getAllConnectionRequests();
		if(acceptingUser!=null && userToBeAccepted!=null && acceptingUser!=userToBeAccepted)
		{
			if(conreqs!=null&&conreqs.size()>0)
			{
				int i=0;
				while(i<conreqs.size())
				{
					if((conreqs.get(i).getConnectionRequestSender()==
							userToBeAccepted && conreqs.get(i).getConnectionRequestReceiver()==acceptingUser)
						)
						{
							connectionRequestService.removeConnectionRequest(conreqs.get(i));
							if(!acceptingUser.getConnections().contains(userToBeAccepted)
									&&!userToBeAccepted.getConnections().contains(acceptingUser))
							{
								acceptingUser.getConnections().add(userToBeAccepted);
								userToBeAccepted.getConnections().add(acceptingUser);
								userRepository.save(acceptingUser);
								userRepository.save(userToBeAccepted);
								return "success";
							}
							
						}
							i++;
				}
			}return "There is no conreq to accept";
		} return "user not found";
		
		
		
	}
	@Transactional
	public String deleteConnection( HttpServletRequest request,Long userToBeDeletedId) {
		/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
		Principal pl=request.getUserPrincipal();
		String username=pl.getName();
		/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
		User deletingUser=userRepository.findByUsername(username);
		User userToBeDeleted=userRepository.findById(userToBeDeletedId).orElse(null);
		if(deletingUser!=null&&userToBeDeleted!=null)
		{
			if(deletingUser.getConnections().contains(userToBeDeleted)
					&&userToBeDeleted.getConnections().contains(deletingUser))
			{
				deletingUser.getConnections().remove(userToBeDeleted);
				userToBeDeleted.getConnections().remove(deletingUser);
				userRepository.save(deletingUser);
				userRepository.save(userToBeDeleted);
				return "success";
			}
			else return "no connection to delete";
		}
		else
		return "one of the users not found";
	}

	public List<User> getConnectionsOfAUser(Long userId) {
		User user=userRepository.findById(userId).orElse(null);
		if(user!=null&&user.getConnections()!=null)
		return user.getConnections();
		else return null;
		
	}

	public String createUser(String name, String surname, String username, String password, MultipartFile userimage,
			String birthdate) throws IOException {
		User user=new User();
		user.setName(name);
		user.setSurname(surname);
		user.setUsername(username);
		user.setPassword(password);
		//password will be encoded before user is saved.(look end of the method).
		
		user.setRoles("USER");
		//user.setAuthorities(Collections.singleton(new SimpleGrantedAuthority("USER")));
		//user.setAuthorities(List.of(new SimpleGrantedAuthority("USER")));
		
		
		int day=0;
		int month=0;
		int year=0000;
		System.out.println(birthdate);
		if(birthdate!=null && !birthdate.equals(""))
		{
			if(birthdate.matches("^(19|20)\\d\\d\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$"))
			{
			
			String[] arr=new String[3];
			arr=birthdate.split("-");
			year=Integer.valueOf(arr[0]);
			month=Integer.valueOf(arr[1]);
			day=Integer.valueOf(arr[2]);
			Date dateuserentered;
			Date currentdate;
		if((month==2||month==4||month==6||month==9||month==11)&&day==31)
		{
			user.setBirthDate(null);
		}
		else if(month==2 && day==30)
		{
			user.setBirthDate(null);
		}
		else if(month==2 && day==29 && year%4!=0)
		{
			user.setBirthDate(null);
		}
		else
		{
		
		LocalDate localDate=LocalDate.of(year, month, day);
		//user.setBirthDate(new Date());
		//dateuserentered=Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		dateuserentered=java.sql.Date.valueOf(localDate);
		currentdate=new Date();
			if(dateuserentered.after(currentdate))
			{
				user.setBirthDate(null);
			}
			else user.setBirthDate(dateuserentered);
		}
		}}else user.setBirthDate(null);
		if(userimage!=null&&userimage.getContentType()!=null) 
		{
			if(!userimage.getContentType().equals("image/jpeg")&&!userimage.getContentType().equals("image/png")) 
			return "Image file is not suitable to the format. Please load a jpeg or png file";
			else user.setUserImage(userimage.getBytes());
		}
		if(user.getName()==null||user.getName().equals("")) return "Registration is unsuccessful name cannot be null";
		if(user.getSurname()==null||user.getSurname().equals("")) return "Registration is unsuccessful surname cannot be null";
		if(user.getUsername()==null||user.getUsername().equals("")) return "Registration is unsuccessful username cannot be null";
		if(user.getPassword()==null||user.getPassword().equals("")) return "Registration is unsuccessful password cannot be null";
		if(!user.getName().matches("^[öüÖÜĞğşŞçÇıİ|a-z|A-Z]{2,20}(\\s[öüÖÜĞğşŞçÇıİ|a-z|A-Z]{2,20})?$")) return "Name is not suitable to the format. Registration is unsuccessful";
		if(!user.getSurname().matches("^[öüÖÜĞğşŞçÇıİa-zA-Z]{2,20}$")) return "Surnam)e is not suitable to the format. Registration is unsuccessful";
		if(!user.getUsername().matches("^[öüÖÜĞğşŞçÇıİa-zA-Z0-9]{2,20}$")) return "Username is not suitable to the format. Registration is unsuccessful";
		if(!user.getPassword().matches("^[öüÖÜĞğşŞçÇıİa-zA-Z0-9]{2,20}$")) return "Password is not suitable to the format. Registration is unsuccessful";
		
		
		List<User> users= userRepository.findAll();
		
		for(User u : users)
		{
			if(user.getUsername().equals(u.getUsername())) return "Username cannot be the same as another one. Registration is unsuccessful";
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		return "Registration is successful";
	}

	public String changeUserPassword(HttpServletRequest request,String newPassword) {
		/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
		Principal pl=request.getUserPrincipal();
		String username=pl.getName();
		/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
		User user=userRepository.findByUsername(username);
		if(!newPassword.matches("^[öüÖÜĞğşŞçÇıİa-zA-Z0-9]{2,20}$")) 
			return "New password is not suitable to the format.";
		if(user!=null)
		{
			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);
			return "success";
		}else return "User not found";
		
	}





	public String changeUserImage(HttpServletRequest request, MultipartFile newuserimage) throws IOException {
		// TODO Auto-generated method stub
		/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
		Principal pl=request.getUserPrincipal();
		String username=pl.getName();
		/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
		User user=userRepository.findByUsername(username);
		if(newuserimage!=null&&newuserimage.getContentType()!=null) 
		{
			if(!newuserimage.getContentType().equals("image/jpeg")&&!newuserimage.getContentType().equals("image/png")) 
			return "Image file is not suitable to the format. Please load a jpeg or png file";
			else 
			{
				user.setUserImage(newuserimage.getBytes());
				userRepository.save(user);
				return "userimage successfully changed";
			}
		}
		return "user image not found";
	}





	public List<User> getBlockedUsersOfAUser(HttpServletRequest request, Long userId) {
		/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
		Principal pl=request.getUserPrincipal();
		String username=pl.getName();
		/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
		User user=userRepository.findByUsername(username);
		User user2=userRepository.findById(userId).orElse(null);
		if(user2!=null&&user2.getBlockedUsers()!=null)
		return user2.getBlockedUsers();
		else return null;
	}





	public String unblockUser(HttpServletRequest request, Long userToBeUnblockedId) {
		Principal pl=request.getUserPrincipal();
		String username=pl.getName();
		/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
		User unblockingUser=userRepository.findByUsername(username);
		User userToBeUnblocked=userRepository.findById(userToBeUnblockedId).orElse(null);
		if(unblockingUser==userToBeUnblocked) return "unblockingUser and userToBeUnblocked cannot be the same person";
		if(unblockingUser.getBlockedUsers().contains(userToBeUnblocked))
		{
			List<User> blockedUsers=unblockingUser.getBlockedUsers();
			blockedUsers.remove(userToBeUnblocked);
			unblockingUser.setBlockedUsers(blockedUsers);
			userRepository.save(unblockingUser);
			return "success";
		}
		return "there was an error";
	}





	





	

	

	
	

}
