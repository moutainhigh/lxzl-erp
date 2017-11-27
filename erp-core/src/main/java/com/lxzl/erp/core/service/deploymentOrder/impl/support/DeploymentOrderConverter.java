package com.lxzl.erp.core.service.deploymentOrder.impl.support;

import com.lxzl.erp.common.domain.deploymentOrder.pojo.DeploymentOrder;
import com.lxzl.erp.common.domain.deploymentOrder.pojo.DeploymentOrderMaterial;
import com.lxzl.erp.common.domain.deploymentOrder.pojo.DeploymentOrderProduct;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.dataaccess.domain.deploymentOrder.DeploymentOrderDO;
import com.lxzl.erp.dataaccess.domain.deploymentOrder.DeploymentOrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.deploymentOrder.DeploymentOrderProductDO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-27 16:40
 */
public class DeploymentOrderConverter {
    public static DeploymentOrderDO convertDeploymentOrder(DeploymentOrder deploymentOrder) {
        DeploymentOrderDO deploymentOrderDO = new DeploymentOrderDO();
        if (deploymentOrder.getDeploymentOrderId() != null) {
            deploymentOrderDO.setId(deploymentOrder.getDeploymentOrderId());
        }
        BeanUtils.copyProperties(deploymentOrder, deploymentOrderDO);
        return deploymentOrderDO;
    }

    public static List<DeploymentOrderProductDO> convertDeploymentOrderProductList(List<DeploymentOrderProduct> deploymentOrderProductList) {
        List<DeploymentOrderProductDO> deploymentOrderProductDOList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(deploymentOrderProductList)) {
            for (DeploymentOrderProduct deploymentOrderProduct : deploymentOrderProductList) {
                deploymentOrderProductDOList.add(convertDeploymentOrderProduct(deploymentOrderProduct));
            }
        }

        return deploymentOrderProductDOList;
    }

    public static DeploymentOrderProductDO convertDeploymentOrderProduct(DeploymentOrderProduct deploymentOrderProduct) {
        DeploymentOrderProductDO deploymentOrderProductDO = new DeploymentOrderProductDO();
        if (deploymentOrderProduct.getDeploymentOrderProductId() != null) {
            deploymentOrderProductDO.setId(deploymentOrderProduct.getDeploymentOrderProductId());
        }
        BeanUtils.copyProperties(deploymentOrderProduct,deploymentOrderProductDO);

        return deploymentOrderProductDO;
    }

    public static List<DeploymentOrderMaterialDO> convertDeploymentOrderMaterialList(List<DeploymentOrderMaterial> deploymentOrderMaterialList) {
        List<DeploymentOrderMaterialDO> deploymentOrderMaterialDOList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(deploymentOrderMaterialList)) {
            for (DeploymentOrderMaterial deploymentOrderMaterial : deploymentOrderMaterialList) {
                deploymentOrderMaterialDOList.add(convertDeploymentOrderMaterial(deploymentOrderMaterial));
            }
        }

        return deploymentOrderMaterialDOList;
    }

    public static DeploymentOrderMaterialDO convertDeploymentOrderMaterial(DeploymentOrderMaterial deploymentOrderMaterial) {
        DeploymentOrderMaterialDO deploymentOrderMaterialDO = new DeploymentOrderMaterialDO();
        if (deploymentOrderMaterial.getDeploymentMaterialId() != null) {
            deploymentOrderMaterialDO.setId(deploymentOrderMaterial.getDeploymentMaterialId());
        }
        BeanUtils.copyProperties(deploymentOrderMaterial,deploymentOrderMaterialDO);

        return deploymentOrderMaterialDO;
    }
}
