package com.demo.webapideneme1.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileTransferService {

	private static final String fileStorageAddress="/home/baylinux/DosyaDeposu";
	
	
	
	public static String getFilestorageaddress() {
		return fileStorageAddress;
	}
	public void handleUpload(MultipartFile multipartFileToBeUploaded) throws IOException
	{
		if(multipartFileToBeUploaded==null) throw new FileNotFoundException("There is no file to upload");
	
	
	File fileToBeComposed=
			 new File(fileStorageAddress
					+File.separator
					+multipartFileToBeUploaded.getOriginalFilename());
	 if(!(fileToBeComposed.getParent()).equals(fileStorageAddress)) 
		 throw new SecurityException("insuitable filename");
	 Files.copy(multipartFileToBeUploaded.getInputStream()
			 ,fileToBeComposed.toPath()
			 ,StandardCopyOption.REPLACE_EXISTING);
	 
	 
	}
	public File getFileToBeDownloaded(String fileName) throws FileNotFoundException
	{
		if(fileName==null) throw new NullPointerException("filename is not suitable");
		File fileToBeDownloaded=new File(fileStorageAddress+File.separator+fileName);
		if(!(fileToBeDownloaded.getParent()).equals(fileStorageAddress)) 
			throw new SecurityException("insuitable fileName");
		if(!fileToBeDownloaded.exists()) throw new FileNotFoundException("file not found");
		return fileToBeDownloaded;
	}
}
