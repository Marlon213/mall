package com.ay.mall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
@Slf4j
@Component
@PropertySource(value = {"classpath:src/main/resources.dev/mall.properties"})
public class FTPUtil {


    public static String ip;

    public static String user;

    public static String pwd;

    public static String remotePath;

    public static FTPClient ftpClient;

    @Value("${ftp.ip}")
    private void setIp(String ftpIp) {
        FTPUtil.ip = ftpIp;
    }
    @Value("${ftp.user}")
    public void setUser(String ftpUser) {
        user = ftpUser;
    }
    @Value("${ftp.pwd}")
    public void setPwd(String ftpPwd) {
        pwd = ftpPwd;
    }
    @Value("${ftp.remotePath}")
    public void setRemotePath(String remotePath) {
        FTPUtil.remotePath = remotePath;
    }

    public static boolean upload(List<File> fileList) throws IOException {
        boolean result = uploadFile(FTPUtil.remotePath,fileList);
        log.info("FTP文件上传完成");
        return result;
    }

    private static boolean uploadFile(String remotePath,List<File> fileList) throws IOException {
        boolean uploaded = false;
        FileInputStream fis = null;
        if (connectFTP(FTPUtil.ip,FTPUtil.user,FTPUtil.pwd)){
            try{
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                for(File fileItem : fileList){
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(),fis);
                }
                uploaded = true;
            }catch (IOException e){
                log.error("上传文件异常",e);
                uploaded = false;
            }finally {
                fis.close();
                ftpClient.disconnect();
            }
        }
        return uploaded;
    }


    private static boolean connectFTP(String ip,String username,String password){
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);

            isSuccess = ftpClient.login(username,password);
        } catch (IOException e) {
            log.error("连接登录FTP错误",e);
        }
        return isSuccess;
    }

    public static String getRemotePath() {
        return remotePath;
    }
}
