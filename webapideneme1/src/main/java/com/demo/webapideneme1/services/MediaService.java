package com.demo.webapideneme1.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.demo.webapideneme1.models.Group;
import com.demo.webapideneme1.models.Media;
import com.demo.webapideneme1.models.MessagePost;
import com.demo.webapideneme1.models.User;
import com.demo.webapideneme1.models.Post;
import com.demo.webapideneme1.repositories.GroupRepository;
import com.demo.webapideneme1.repositories.MediaRepository;
import com.demo.webapideneme1.repositories.MessagePostRepository;
import com.demo.webapideneme1.repositories.PostRepository;
import com.demo.webapideneme1.repositories.UserGroupPermissionRepository;
import com.demo.webapideneme1.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class MediaService {

	PostRepository postRepository;
	MessagePostRepository messagePostRepository;
	UserRepository userRepository;
	GroupRepository groupRepository;
	FileTransferService fileTransferService;
	MediaRepository mediaRepository;
	@Autowired
	public MediaService(PostRepository postRepository, UserRepository userRepository,
			GroupRepository groupRepository, FileTransferService fileTransferService
			,MediaRepository mediaRepository,MessagePostRepository messagePostRepository) {
		super();
		this.postRepository = postRepository;
		this.messagePostRepository=messagePostRepository;
		this.userRepository = userRepository;
		this.groupRepository = groupRepository;
		this.fileTransferService = fileTransferService;
		this.mediaRepository=mediaRepository;
	}
	public String sendMediaToAGroup(HttpServletRequest request, MultipartFile multipartFileToBeUploaded, Long groupId) {
		/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
		Principal pl=request.getUserPrincipal();
		String username=pl.getName();
		/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
		User user1=userRepository.findByUsername(username);
		Group group=groupRepository.findById(groupId).orElse(null);
		if(user1!=null&&group!=null&&multipartFileToBeUploaded!=null)
		{
			
			try {
				fileTransferService.handleUpload(multipartFileToBeUploaded);
				Post ugm=new Post();
				ugm.setUser(user1);
				ugm.setGroup(group);
				Media media=new Media();
				media.setMedia_address(fileTransferService.getFilestorageaddress()+File.separator+multipartFileToBeUploaded.getOriginalFilename());
				media.setContent_type(multipartFileToBeUploaded.getContentType());
				media.setName(multipartFileToBeUploaded.getOriginalFilename());
				media.setGroup(group);
				media.setOwner(user1);
				mediaRepository.save(media);
				ugm.setMedia(media);
				ugm.setUser(user1);
				ugm.setGroup(group);
				postRepository.save(ugm);
				
				return "success";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "there was an error";
			}
		}
		else
		{
			return "user, group or file not found";
		}
	}
	public List<Post> getMediasOfAGroup(HttpServletRequest request, Long groupId) {
		/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
		Principal pl=request.getUserPrincipal();
		String username=pl.getName();
		/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
		User user1=userRepository.findByUsername(username);
		Group group=groupRepository.findById(groupId).orElse(null);
		if(user1!=null&&group!=null)
		{
			List<Post> list=postRepository.findByGroup(group);
			return list;
		}
		else
		{
			return new ArrayList();
		}
	}
	@Transactional
	public String deleteMedia(HttpServletRequest request, Long id) {
		/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
		Principal pl=request.getUserPrincipal();
		String username=pl.getName();
		/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
		User user1=userRepository.findByUsername(username);
		Media media=mediaRepository.findById(id).orElse(null);
		Post post=postRepository.findByMedia(media);
		MessagePost messagePost=messagePostRepository.findByMedia(media);
		if(messagePost==null&&post!=null&&user1!=null&&(post.getUser()==user1||user1.getRoles().contains("ADMIN"))&&media!=null)
		{
			post.setMedia(null);
			post.setUser(null);
			post.setGroup(null);
			postRepository.save(post);
			mediaRepository.delete(media);
			postRepository.delete(post);
			return "media successfully deleted";
		}
		else if(messagePost!=null&&post==null&&user1!=null&&(messagePost.getSender()==user1||user1.getRoles().contains("ADMIN"))&&media!=null)
		{
			messagePost.setMedia(null);
			messagePost.setSender(null);
			messagePost.setReceiver(null);
			messagePostRepository.save(messagePost);
			mediaRepository.delete(media);
			messagePostRepository.delete(messagePost);
			return "media successfully deleted";
		}
		else
		{
			return "error";
		}
	}
	public ResponseEntity<Resource> downloadFileFaster(String fileName) {
		try {
			File fileToBeDownloaded=fileTransferService.getFileToBeDownloaded(fileName);
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+fileName+"\"")
					.contentLength(fileToBeDownloaded.length())
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					.body(new FileSystemResource(fileToBeDownloaded));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.notFound().build();
		}
	}
	public String sendMediaAsAMessagePost(HttpServletRequest request, MultipartFile multipartFileToBeUploaded,Long receiverId) {
		/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
		Principal pl=request.getUserPrincipal();
		String username=pl.getName();
		/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
		User user1=userRepository.findByUsername(username);
		User receiver=userRepository.findById(receiverId).orElse(null);
		if(user1!=null&&receiver!=null&&multipartFileToBeUploaded!=null)
		{
			try {
				fileTransferService.handleUpload(multipartFileToBeUploaded);
				MessagePost ugm=new MessagePost();
				ugm.setSender(user1);
				ugm.setReceiver(receiver);
				Media media=new Media();
				media.setMedia_address(fileTransferService.getFilestorageaddress()+File.separator+multipartFileToBeUploaded.getOriginalFilename());
				media.setContent_type(multipartFileToBeUploaded.getContentType());
				media.setName(multipartFileToBeUploaded.getOriginalFilename());
				media.setOwner(user1);
				mediaRepository.save(media);
				ugm.setMedia(media);
				messagePostRepository.save(ugm);
				
				return "success";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "there was an error";
			}
		}
		else
		{
			return "user, receiver or file not found";
		}
	}
	
	
	
}
