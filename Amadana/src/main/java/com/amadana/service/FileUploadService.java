package com.amadana.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileUploadService {
    Map<String,Object> uploadPicture(MultipartFile file) throws IOException;
    Map<String,Object> getPath();
    List<String> getFileNames();
}
