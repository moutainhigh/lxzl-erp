package com.lxzl.erp.dataaccess.dao.mysql.product;

import com.lxzl.erp.common.domain.product.pojo.ProductEquipment;
import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProductEquipmentMapper extends BaseMysqlDAO<ProductEquipmentDO> {

    Integer saveList(List<ProductEquipmentDO> productEquipmentDOList);

    Integer updateList(@Param("productEquipmentDOList") List<ProductEquipmentDO> productEquipmentDOList);

    Integer updatePurchasePriceList(@Param("productEquipmentDOList") List<ProductEquipmentDO> productEquipmentDOList);

    ProductEquipmentDO findByEquipmentNo(@Param("equipmentNo") String equipmentNo);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    List<ProductEquipmentDO> listPage(@Param("maps") Map<String, Object> paramMap);

    List<ProductEquipmentDO> findRentByCustomerId(@Param("customerId") Integer customerId);

    Integer getRentEquipmentCountByOrderNo(@Param("orderNo") String orderNo);

    List<ProductEquipmentDO> findByPurchaseOrderNo(@Param("purchaseOrderNo") String purchaseOrderNo);

    List<ProductEquipmentDO> listByPurchaseReceiveOrderProductId(@Param("maps") Map<String, Object> paramMap);

    Integer listByPurchaseReceiveOrderProductIdCount(@Param("maps") Map<String, Object> paramMap);

    Integer updateStatusBatchByPeerDeploymentOrderId(@Param("maps") Map<String, Object> maps);

    List<ProductEquipmentDO> findBatchByPeerDeploymentOrderNo(@Param("maps") Map<String, Object> maps);

    List<ProductEquipmentDO> findBatchByTransferOrderNo(@Param("maps") Map<String, Object> maps);

    List<ProductEquipmentDO> findBatchByPeerDeploymentOrderId(@Param("maps") Map<String, Object> maps);

    Integer updateStatusBatchByTransferOrderId(@Param("maps") Map<String, Object> maps);

    List<ProductEquipmentDO> findByEquipmentList(@Param("productEquipmentList") List<ProductEquipment> productEquipmentList);
}
