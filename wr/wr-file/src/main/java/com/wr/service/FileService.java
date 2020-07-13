package com.wr.service;

import com.wr.vo.PageFile;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    PageFile fileUpload(MultipartFile uploadFile,String type);
}
