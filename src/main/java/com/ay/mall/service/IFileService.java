package com.ay.mall.service;

import com.ay.mall.common.ServerResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface IFileService {
    ServerResponse<Map<String,String>> upload(MultipartFile file, String path);
}
