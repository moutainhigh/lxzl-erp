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

    public static DeploymentOrder convertDeploymentOrderDO(DeploymentOrderDO deploymentOrderDO) {
        DeploymentOrder deploymentOrder = new DeploymentOrder();
        if (deploymentOrderDO.getId() != null) {
            deploymentOrder.setDeploymentOrderId(deploymentOrderDO.getId());
        }
        BeanUtils.copyProperties(deploymentOrderDO, deploymentOrder);

        deploymentOrder.setDeploymentOrderProductList(convertDeploymentOrderProductDOList(deploymentOrderDO.getDeploymentOrderProductDOList()));
        deploymentOrder.setDeploymentOrderMaterialList(convertDeploymentOrderMaterialDOList(deploymentOrderDO.getDeploymentOrderMaterialDOList()));
        return deploymentOrder;
    }

    public static List<DeploymentOrder> convertDeploymentOrderDOList(List<DeploymentOrderDO> deploymentOrderDOList) {
        List<DeploymentOrder> deploymentOrderList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(deploymentOrderDOList)){
            for(DeploymentOrderDO deploymentOrderDO : deploymentOrderDOList){
                deploymentOrderList.add(convertDeploymentOrderDO(deploymentOrderDO));
            }
        }
        return deploymentOrderList;
    }

    public static DeploymentOrderDO convertDeploymentOrder(DeploymentOrder deploymentOrder) {
        DeploymentOrderDO deploymentOrderDO = new DeploymentOrderDO();
        if (deploymentOrder.getDeploymentOrderId() != null) {
            deploymentOrderDO.setId(deploymentOrder.getDeploymentOrderId());
        }
        BeanUtils.copyProperties(deploymentOrder, deploymentOrderDO);
        return deploymentOrderDO;
    }

    public static List<DeploymentOrderProduct> convertDeploymentOrderProductDOList(List<DeploymentOrderProductDO> deploymentOrderProductDOList) {
        List<DeploymentOrderProduct> deploymentOrderProductList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(deploymentOrderProductDOList)) {
            for (DeploymentOrderProductDO deploymentOrderProductDO : deploymentOrderProductDOList) {
                deploymentOrderProductList.add(convertDeploymentOrderProductDO(deploymentOrderProductDO));
            }
        }
        return deploymentOrderProductList;
    }

    public static DeploymentOrderProduct convertDeploymentOrderProductDO(DeploymentOrderProductDO deploymentOrderProductDO) {
        DeploymentOrderProduct deploymentOrderProduct = new DeploymentOrderProduct();
        if (deploymentOrderProductDO.getId() != null) {
            deploymentOrderProduct.setDeploymentOrderProductId(deploymentOrderProductDO.getId());
        }
        BeanUtils.copyProperties(deploymentOrderProductDO,deploymentOrderProduct);

        return deploymentOrderProduct;
    }


    public static List<DeploymentOrderMaterial> convertDeploymentOrderMaterialDOList(List<DeploymentOrderMaterialDO> deploymentOrderMaterialDOList) {
        List<DeploymentOrderMaterial> deploymentOrderMaterialList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(deploymentOrderMaterialDOList)) {
            for (DeploymentOrderMaterialDO deploymentOrderMaterialDO : deploymentOrderMaterialDOList) {
                deploymentOrderMaterialList.add(convertDeploymentOrderMaterialDO(deploymentOrderMaterialDO));
            }
        }
        return deploymentOrderMaterialList;
    }

    public static DeploymentOrderMaterial convertDeploymentOrderMaterialDO(DeploymentOrderMaterialDO deploymentOrderMaterialDO) {
        DeploymentOrderMaterial deploymentOrderMaterial = new DeploymentOrderMaterial();
        if (deploymentOrderMaterialDO.getId() != null) {
            deploymentOrderMaterial.setDeploymentMaterialId(deploymentOrderMaterialDO.getId());
        }
        BeanUtils.copyProperties(deploymentOrderMaterialDO,deploymentOrderMaterial);

        return deploymentOrderMaterial;
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
