package com.lxzl.erp.common.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2016/11/4.
 * Time: 8:38.
 */
public class FileUtil {

    //文件上传
    public static String uploadFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        File tempFile = new File(UPLOAD_FILE_URL, String.valueOf(fileName));
        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdir();
        }
        try {
            if (!tempFile.exists()) {
                tempFile.createNewFile();
            }
            file.transferTo(tempFile);
        } catch (IOException e) {

        }
        return UPLOAD_FILE_URL + tempFile.getName();
    }

    public static File getFile(String fileName) {
        return new File(UPLOAD_FILE_URL, fileName);
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }
    }

    static {
        try {
            Configuration configuration = new PropertiesConfiguration("config.properties");
            UPLOAD_FILE_URL = configuration.getString("UPLOAD_FILE_URL");
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private static String UPLOAD_FILE_URL;
}
