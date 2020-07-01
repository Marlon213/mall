package com.ay.mall.service.Impl;

import com.ay.mall.common.ServerResponse;
import com.ay.mall.service.IFileService;
import com.ay.mall.util.FTPUtil;
import com.ay.mall.util.PropertiesUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
@Slf4j
@Service("iFileService")
public class FileServiceImpl implements IFileService {


    public ServerResponse<Map<String,String>> upload(MultipartFile file,String path){

        File fileDir = new File(path);
        if (!fileDir.exists()){
            fileDir.mkdirs();
            fileDir.setWritable(true);
        }

        String clientName = file.getOriginalFilename();

        String fileExtensionName = clientName.substring(clientName.lastIndexOf("."));
        //把名字改了，以免重复
        String newName = UUID.randomUUID().toString()+fileExtensionName;

        File targetFile = new File(path,newName);
        try {
            //上传tomcat服务器
            file.transferTo(targetFile);
            //上传到ftp服务器
            System.out.println(FTPUtil.upload(Lists.newArrayList(targetFile)));

            targetFile.delete();
        } catch (IOException e) {
            log.error("文件上传tomcat服务器失败",e);
        }
        Map<String,String> map = Maps.newHashMap();
        String url = PropertiesUtil.getProperty("mall.properties","ftp.server.http.prefix")+FTPUtil.getRemotePath()+targetFile.getName();
        map.put("uri",targetFile.getName());
        map.put("url",url);
        return ServerResponse.createBySuccess(map);
    }


}
