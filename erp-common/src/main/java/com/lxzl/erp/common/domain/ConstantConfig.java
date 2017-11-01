package com.lxzl.erp.common.domain;

public class ConstantConfig {

    public static String uploadFileUrl;
    public static String imageDomain;
    public static String baseDomain;
    public static String staticCommonDomain;
    public static String staticDomain;
    public static String serviceDomain;

    public void setUploadFileUrl(String uploadFileUrl) {
        ConstantConfig.uploadFileUrl = uploadFileUrl;
    }

    public void setImageDomain(String imageDomain) {
        ConstantConfig.imageDomain = imageDomain;
    }

    public void setBaseDomain(String baseDomain) {
        ConstantConfig.baseDomain = baseDomain;
    }

    public void setStaticCommonDomain(String staticCommonDomain) {
        ConstantConfig.staticCommonDomain = staticCommonDomain;
    }

    public void setStaticDomain(String staticDomain) {
        ConstantConfig.staticDomain = staticDomain;
    }

    public void setServiceDomain(String serviceDomain) {
        ConstantConfig.serviceDomain = serviceDomain;
    }

    public String getBaseDomain() {
        return baseDomain;
    }

    public String getStaticCommonDomain() {
        return staticCommonDomain;
    }

    public String getStaticDomain() {
        return staticDomain;
    }

    public String getServiceDomain() {
        return serviceDomain;
    }
}
