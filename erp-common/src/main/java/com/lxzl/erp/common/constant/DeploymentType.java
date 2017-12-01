package com.lxzl.erp.common.constant;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-27 14:14
 */
public class DeploymentType {
    public static final Integer DEPLOYMENT_TYPE_BORROW = 1;
    public static final Integer DEPLOYMENT_TYPE_SELL = 2;

    public static boolean inThisScope(Integer deploymentType) {
        if (deploymentType != null
                && (DEPLOYMENT_TYPE_BORROW.equals(deploymentType)
                || DEPLOYMENT_TYPE_SELL.equals(deploymentType))) {
            return true;
        }
        return false;
    }
}
