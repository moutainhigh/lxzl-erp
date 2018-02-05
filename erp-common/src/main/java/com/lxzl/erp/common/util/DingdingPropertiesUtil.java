package com.lxzl.erp.common.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-10-25 19:57
 */
public class DingdingPropertiesUtil {

    static {
        try {
            Configuration configuration = new PropertiesConfiguration("dingding.properties");
            DINGDING_USER_GROUP_DEPARTMENT_DEVELOPER = configuration.getString("DINGDING_USER_GROUP_DEPARTMENT_DEVELOPER");
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static String DINGDING_USER_GROUP_DEPARTMENT_DEVELOPER;
}
