package com.lxzl.erp.common.constant;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-29 14:04
 */
public class MessageContant {
    static {
        try {
            Configuration configuration = new PropertiesConfiguration("message.properties");
            WORKFLOW_COMMIT_TITLE = configuration.getString("WORKFLOW_COMMIT_TITLE");
            WORKFLOW_COMMIT_CONTENT = configuration.getString("WORKFLOW_COMMIT_CONTENT");
            WORKFLOW_VERIFY_PASS_TITLE = configuration.getString("WORKFLOW_VERIFY_PASS_TITLE");
            WORKFLOW_VERIFY_PASS_CONTENT = configuration.getString("WORKFLOW_VERIFY_PASS_CONTENT");
            WORKFLOW_VERIFY_BACK_TITLE = configuration.getString("WORKFLOW_VERIFY_BACK_TITLE");
            WORKFLOW_VERIFY_BACK_CONTENT = configuration.getString("WORKFLOW_VERIFY_BACK_CONTENT");
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static String WORKFLOW_COMMIT_TITLE;
    public static String WORKFLOW_COMMIT_CONTENT;
    public static String WORKFLOW_VERIFY_PASS_TITLE;
    public static String WORKFLOW_VERIFY_PASS_CONTENT;
    public static String WORKFLOW_VERIFY_BACK_TITLE;
    public static String WORKFLOW_VERIFY_BACK_CONTENT;
}
