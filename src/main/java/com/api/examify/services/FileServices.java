package com.api.examify.services;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;


public interface FileServices {

	public Map<String, String> fileValidation(MultipartFile file, int fileType);
	
	public String uploadFile(MultipartFile file, int fileType);
	
}
