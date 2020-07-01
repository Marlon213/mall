package com.ay.mall.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public class PropertiesUtil {

    public static String getProperty(String fileName,String key)  {
        Properties properties = null;
        try {
            properties = PropertiesLoaderUtils.loadAllProperties(fileName);
        } catch (IOException e) {
            log.error("读取配置文件错误",e);
        }
        return properties.getProperty(key);
    }

    public static String getProperty(String fileName,String key,String defaultValue)  {
        Properties properties = null;
        try {
            properties = PropertiesLoaderUtils.loadAllProperties(fileName);
        } catch (IOException e) {
            log.error("读取配置文件错误",e);
        }
        return properties.getProperty(key,defaultValue);
    }

}
