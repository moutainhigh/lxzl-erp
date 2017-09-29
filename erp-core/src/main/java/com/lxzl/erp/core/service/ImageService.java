package com.lxzl.erp.core.service;

import java.util.Map;

public interface ImageService {

    public String uploadImage(String filePath, String author, Map<String, String> extraInfo);
}
