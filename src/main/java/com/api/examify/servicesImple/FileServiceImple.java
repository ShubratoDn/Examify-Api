package com.api.examify.servicesImple;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.examify.configs.Constants;
import com.api.examify.services.FileServices;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileServiceImple implements FileServices {

    private Map<String, String> response = new HashMap<>();
    private String UPLOAD_DIR ; 
    
    
    @Override
    public Map<String, String> fileValidation(MultipartFile file, int fileType) {

        // User image validation
        if (fileType == Constants.FILE_USER_IMAGE) {
            Map<String, String> userImageValidation = userImageValidation(file);
            return userImageValidation;
        }

        return null;
    }

    
    
    // Validate user image
    public Map<String, String> userImageValidation(MultipartFile image) {
        int fileSizeInKb = (int) (image.getSize() / 1000);

        // Check if image is empty
        if (image.isEmpty()) {
            response.put("image", "Please upload your image.");
            return response;
        }

        // Check image format
        if (!image.getContentType().equals("image/png") && !image.getContentType().equals("image/jpeg")) {
            response.put("image", "Unsupported image format. Only JPG and PNG formats are supported.");
            return response;
        }

        // Check image size
        if (!(fileSizeInKb > 20) || !(fileSizeInKb < 6000)) {
            response.put("image", "Image size should be within 20KB and 6MB.");
            return response;
        }
        return null;
    }

    
    
    
    //uploading file
	@Override
	public String uploadFile(MultipartFile file, int fileType) {
		if(fileType == Constants.FILE_USER_IMAGE) {
			// Set the UPLOAD_DIR path using cross-platform compatible approach
			this.UPLOAD_DIR = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "UserImages";
			return uploadUserImage(file);
		}
		
		return null;
	}   
	
	
	
	
	
	
	//uploading user image
	public String uploadUserImage(MultipartFile image) {
	

		//Random text generate
		SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[20];
        random.nextBytes(randomBytes);

        StringBuilder sb = new StringBuilder();
        for (byte b : randomBytes) {
            sb.append(String.format("%02x", b));
        }
        
        String randomHexCode = sb.toString();        
        String fileExtension = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
        String fileName = "userimage_"+randomHexCode+"_"+fileExtension;
        
	
		try {
			File f = new File(UPLOAD_DIR);
			f.mkdir();
			
			InputStream inputStream = image.getInputStream();
			byte[] data = new byte[inputStream.available()];
			inputStream.read(data);
			
			FileOutputStream fos = new FileOutputStream(UPLOAD_DIR+File.separator+fileName);
			fos.write(data);
			
			fos.flush();
			fos.close();
			
			
			
			return fileName;
			
		}catch (Exception e) {
			log.error("File Upload fail Because : ");
			log.error(e.toString());
			return null;
		}		
	}
	
	
}
