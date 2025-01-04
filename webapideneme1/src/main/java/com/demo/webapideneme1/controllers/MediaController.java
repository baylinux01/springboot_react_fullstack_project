package com.demo.webapideneme1.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.webapideneme1.models.ByteArrayMultipartFile;
import com.demo.webapideneme1.models.Group;
import com.demo.webapideneme1.models.User;
import com.demo.webapideneme1.models.Post;
import com.demo.webapideneme1.services.MediaService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/medias")
public class MediaController {

	MediaService mediaService;
	@Autowired
	public MediaController(MediaService mediaService) {
		super();
		this.mediaService = mediaService;
	}
	@PostMapping("/sendmediatoagroupasbytes")
	public String sendMediaToAGroupAsBytes(HttpServletRequest request,
			String name,String originalFileName,String contentType, byte[] multipartFileBytesToBeUploaded, Long groupId)
	{
		MultipartFile multipartFileToBeUploaded=new ByteArrayMultipartFile
				(name,originalFileName,contentType,multipartFileBytesToBeUploaded);
		return mediaService.sendMediaToAGroup(request,multipartFileToBeUploaded,groupId);
	}
	@PostMapping("/sendmediatoagroup/{groupId}")
	public String sendMediaToAGroup(HttpServletRequest request,MultipartFile multipartFileToBeUploaded,@PathVariable Long groupId)
	{
		
		return mediaService.sendMediaToAGroup(request,multipartFileToBeUploaded,groupId);
	}
	@PostMapping("/sendmediaasamessagepost/{receiverId}")
	public String sendMediaAsAMessagePost(HttpServletRequest request,MultipartFile multipartFileToBeUploaded,@PathVariable Long receiverId)
	{
		return mediaService.sendMediaAsAMessagePost(request,multipartFileToBeUploaded,receiverId);
	}
	@GetMapping("/getmediasofagroup")
	public List<Post> getMediasOfAGroup(HttpServletRequest request,Long groupId)
	{
		return mediaService.getMediasOfAGroup(request,groupId);
	}
	@DeleteMapping("/deletemedia")
	public String deleteMedia(HttpServletRequest request,Long id)
	{
		return mediaService.deleteMedia(request,id);
	}
	@GetMapping("/downloadfilefaster")
	ResponseEntity<Resource> downloadFileFaster(@RequestParam("fileName") String fileName) 
	{
		return mediaService.downloadFileFaster(fileName);
	}
}
