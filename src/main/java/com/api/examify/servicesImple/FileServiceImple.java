package com.api.examify.servicesImple;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.examify.configs.Constants;
import com.api.examify.services.FileServices;

@Service
public class FileServiceImple implements FileServices {

    private Map<String, String> response = new HashMap<>();

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
        if (!(fileSizeInKb > 50) || !(fileSizeInKb < 1000)) {
            response.put("image", "Image size should be within 50KB and 1MB.");
            return response;
        }
        return null;
    }

    
    
    
	@Override
	public String uploadFile(MultipartFile file, int fileType) {
		// TODO Auto-generated method stub
		return null;
	}   
}
