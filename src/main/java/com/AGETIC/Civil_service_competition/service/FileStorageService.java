package com.AGETIC.Civil_service_competition.service;



import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileStorageService {
    String store(MultipartFile file, String directory) throws IOException;
}
