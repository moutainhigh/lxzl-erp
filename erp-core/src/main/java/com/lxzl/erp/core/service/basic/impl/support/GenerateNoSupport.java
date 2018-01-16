package com.lxzl.erp.core.service.basic.impl.support;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.CustomerType;
import com.lxzl.erp.common.domain.assembleOder.AssembleOrderQueryParam;
import com.lxzl.erp.common.domain.changeOrder.ChangeOrderPageParam;
import com.lxzl.erp.common.domain.customer.CustomerQueryParam;
import com.lxzl.erp.common.domain.deploymentOrder.DeploymentOrderQueryParam;
import com.lxzl.erp.common.domain.material.BulkMaterialQueryParam;
import com.lxzl.erp.common.domain.material.MaterialQueryParam;
import com.lxzl.erp.common.domain.order.OrderQueryParam;
import com.lxzl.erp.common.domain.peerDeploymentOrder.PeerDeploymentOrderQueryParam;
import com.lxzl.erp.common.domain.product.ProductEquipmentQueryParam;
import com.lxzl.erp.common.domain.product.ProductQueryParam;
import com.lxzl.erp.common.domain.purchase.PurchaseDeliveryOrderQueryParam;
import com.lxzl.erp.common.domain.purchase.PurchaseOrderQueryParam;
import com.lxzl.erp.common.domain.purchase.PurchaseReceiveOrderQueryParam;
import com.lxzl.erp.common.domain.purchaseApply.PurchaseApplyOrderPageParam;
import com.lxzl.erp.common.domain.repairOrder.RepairOrderQueryParam;
import com.lxzl.erp.common.domain.returnOrder.ReturnOrderPageParam;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.domain.transferOrder.TransferOrderQueryParam;
import com.lxzl.erp.common.domain.warehouse.StockOrderQueryParam;
import com.lxzl.erp.common.domain.workflow.WorkflowLinkQueryParam;
import com.lxzl.erp.common.util.DateUtil;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.area.AreaCityMapper;
import com.lxzl.erp.dataaccess.dao.mysql.assembleOder.AssembleOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.ChangeOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.deploymentOrder.DeploymentOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.peer.PeerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.peerDeploymentOrder.PeerDeploymentOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.purchase.PurchaseDeliveryOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.purchase.PurchaseOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.purchase.PurchaseReceiveOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.purchaseApply.PurchaseApplyOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.repairOrder.RepairOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.supplier.SupplierMapper;
import com.lxzl.erp.dataaccess.dao.mysql.transferOrder.TransferOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.StockOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.WarehouseMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowLinkMapper;
import com.lxzl.erp.dataaccess.domain.area.AreaCityDO;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User : XiaoLuYu
 * Date : Created in ${Date}
 * Time : Created in ${Time}
 */
@Component
public class GenerateNoSupport {
    /**
     * 生成订单编号
     */
    public String generateOrderNo(Date currentTime, Integer buyerCustomerId) {
        if (buyerCustomerId == null || buyerCustomerId == 0) {
            buyerCustomerId = CommonConstant.SUPER_CUSTOMER_ID;
        }
        synchronized (this) {
            Map<String, Object> maps = new HashMap<>();
            OrderQueryParam orderQueryParam = new OrderQueryParam();
            orderQueryParam.setCreateStartTime(DateUtil.getMonthByOffset(0));
            orderQueryParam.setCreateEndTime(DateUtil.getMonthByOffset(1));
            maps.put("orderQueryParam", orderQueryParam);
            Integer orderCount = orderMapper.findOrderCountByParams(maps);
            StringBuilder builder = new StringBuilder();
            builder.append("LXO-");
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append("-");
            builder.append(buyerCustomerId);
            builder.append("-");
            builder.append(String.format("%05d", orderCount + 1));
            return builder.toString();
        }
    }

    /**
     * 生成商品编号
     */
    public String generateProductNo(String productModel) {
        Date currentTime = new Date();
        if (productModel == null) {
            productModel = "";
        }
        synchronized (this) {
            ProductQueryParam productQueryParam = new ProductQueryParam();
            Map<String, Object> maps = new HashMap<>();
            maps.put("start", 0);
            maps.put("pageSize", Integer.MAX_VALUE);
            productQueryParam.setCreateStartTime(DateUtil.getMonthByOffset(0));
            productQueryParam.setCreateEndTime(DateUtil.getMonthByOffset(1));
            maps.put("productQueryParam", productQueryParam);
            Integer productCount = productMapper.findProductCountByParams(maps);
            StringBuilder builder = new StringBuilder();
            builder.append("LXP-");
            builder.append(productModel);
            builder.append("-");
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append("-");
            builder.append(String.format("%05d", productCount + 1));
            return builder.toString();
        }
    }

    /**
     * 生成物料编号
     */
    public String generateEquipmentNo(String productModel, String cityCode, Integer counter) {
        Date currentTime = new Date();
        productModel = productModel == null ? "" : productModel;
        counter = counter == null ? 1 : counter;
        synchronized (this) {
            ProductEquipmentQueryParam productEquipmentQueryParam = new ProductEquipmentQueryParam();
            Map<String, Object> maps = new HashMap<>();
            maps.put("start", 0);
            maps.put("pageSize", Integer.MAX_VALUE);
            productEquipmentQueryParam.setCreateStartTime(DateUtil.getMonthByOffset(0));
            productEquipmentQueryParam.setCreateEndTime(DateUtil.getMonthByOffset(1));
            maps.put("productEquipmentQueryParam", productEquipmentQueryParam);
            Integer productCount = productEquipmentMapper.listCount(maps);
            StringBuilder builder = new StringBuilder();
            builder.append("LXE-");
            builder.append(cityCode);
            builder.append("-");
            builder.append(productModel);
            builder.append("-");
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append("-");
            builder.append(String.format("%05d", productCount + counter));
            return builder.toString();
        }
    }

    /**
     * 生成入库单号
     */
    public String generateStockOrderNo() {
        Date currentTime = new Date();

        synchronized (this) {
            StockOrderQueryParam stockOrderQueryParam = new StockOrderQueryParam();
            Map<String, Object> maps = new HashMap<>();
            maps.put("start", 0);
            maps.put("pageSize", Integer.MAX_VALUE);
            stockOrderQueryParam.setCreateStartTime(DateUtil.getMonthByOffset(0));
            stockOrderQueryParam.setCreateEndTime(DateUtil.getMonthByOffset(1));
            maps.put("stockOrderQueryParam", stockOrderQueryParam);
            Integer count = stockOrderMapper.listCount(maps);
            StringBuilder builder = new StringBuilder();
            builder.append("LXSO-");
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append("-");
            builder.append(String.format("%05d", count + 1));
            return builder.toString();
        }
    }

    /**
     * 生成散料编号
     */
    public String generateBulkMaterialNo(String materialModel, String cityCode, Integer counter) {
        Date currentTime = new Date();
        materialModel = materialModel == null ? "" : materialModel;
        counter = counter == null ? 1 : counter;
        synchronized (this) {
            BulkMaterialQueryParam bulkMaterialQueryParam = new BulkMaterialQueryParam();
            Map<String, Object> maps = new HashMap<>();
            maps.put("start", 0);
            maps.put("pageSize", Integer.MAX_VALUE);
            bulkMaterialQueryParam.setCreateStartTime(DateUtil.getMonthByOffset(0));
            bulkMaterialQueryParam.setCreateEndTime(DateUtil.getMonthByOffset(1));
            maps.put("bulkMaterialQueryParam", bulkMaterialQueryParam);
            Integer count = bulkMaterialMapper.listCount(maps);
            StringBuilder builder = new StringBuilder();
            builder.append("LXBM-");
            builder.append(cityCode);
            builder.append("-");
            builder.append(materialModel);
            builder.append("-");
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append("-");
            builder.append(String.format("%05d", count + counter));
            return builder.toString();
        }
    }

    /**
     * 生成物料编号
     */
    public String generateMaterialNo(String materialModel) {
        Date currentTime = new Date();
        materialModel = materialModel == null ? "" : materialModel;
        synchronized (this) {
            MaterialQueryParam materialQueryParam = new MaterialQueryParam();
            Map<String, Object> maps = new HashMap<>();
            maps.put("start", 0);
            maps.put("pageSize", Integer.MAX_VALUE);
            materialQueryParam.setCreateStartTime(DateUtil.getMonthByOffset(0));
            materialQueryParam.setCreateEndTime(DateUtil.getMonthByOffset(1));
            maps.put("materialQueryParam", materialQueryParam);
            Integer count = materialMapper.listCount(maps);
            StringBuilder builder = new StringBuilder();
            builder.append("LXM-");
            builder.append(materialModel);
            builder.append("-");
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append("-");
            builder.append(String.format("%05d", count + 1));
            return builder.toString();
        }
    }

    /**
     * 生成客户编号
     */
    public String generateCustomerNo(Date currentTime, Integer customerType) {
        synchronized (this) {
            StringBuilder builder = new StringBuilder();
            Integer subCompanyId = userSupport.getCurrentUserCompanyId();
            //业务员所在的city_code
            String subCompanyCode = subCompanyMapper.findById(subCompanyId).getSubCompanyCode();
            CustomerQueryParam customerQueryParam = new CustomerQueryParam();
            customerQueryParam.setCreateStartTime(DateUtil.getMonthByOffset(0));
            customerQueryParam.setCreateEndTime(DateUtil.getMonthByOffset(1));
            Map<String, Object> maps = new HashMap<>();
            maps.put("customerQueryParam", customerQueryParam);
            Integer count = customerMapper.listCount(maps);

            if (customerType.equals(CustomerType.CUSTOMER_TYPE_COMPANY)) {
                builder.append("LXCC-");
            }
            if (customerType.equals(CustomerType.CUSTOMER_TYPE_PERSON)) {
                builder.append("LXCD-");
            }
            builder.append(subCompanyCode);
            builder.append("-");
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append("-");
            builder.append(String.format("%05d", count + 1));
            return builder.toString();
        }
    }

    /**
     * 生成调拨单号
     */
    public String generateDeploymentOrderNo(Date currentTime, Integer srcWarehouseId, Integer targetWarehouseId) {
        synchronized (this) {
            StringBuilder builder = new StringBuilder();
            //获取入方的城市区号
            WarehouseDO srcWarehouseDO = warehouseMapper.findById(srcWarehouseId);
            SubCompanyDO srcSubCompanyDO = subCompanyMapper.findById(srcWarehouseDO.getSubCompanyId());
            //获取出方的城市区号
            WarehouseDO targetWarehouseDO = warehouseMapper.findById(targetWarehouseId);
            SubCompanyDO targetSubCompanyDO = subCompanyMapper.findById(targetWarehouseDO.getSubCompanyId());
            DeploymentOrderQueryParam deploymentOrderQueryParam = new DeploymentOrderQueryParam();
            deploymentOrderQueryParam.setCreateStartTime(DateUtil.getMonthByOffset(0));
            deploymentOrderQueryParam.setCreateEndTime(DateUtil.getMonthByOffset(1));
            Map<String, Object> maps = new HashMap<>();
            maps.put("deploymentOrderQueryParam", deploymentOrderQueryParam);
            Integer count = deploymentOrderMapper.listCount(maps);
            builder.append("LXD-");
            builder.append(srcSubCompanyDO.getSubCompanyCode());
            builder.append("-");
            builder.append(targetSubCompanyDO.getSubCompanyCode());
            builder.append("-");
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append("-");
            builder.append(String.format("%04d", count + 1));
            return builder.toString();
        }
    }

    /**
     * 生成采购单编号
     */
    public String generatePurchaseOrderNo(Date currentTime, Integer warehouseId) {
        synchronized (this) {
            WarehouseDO warehouseDO = warehouseMapper.findById(warehouseId);
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(warehouseDO.getSubCompanyId());
            //邮政编号
            String subCompanyCode = subCompanyDO.getSubCompanyCode();
            Map<String, Object> maps = new HashMap<>();
            PurchaseOrderQueryParam purchaseOrderQueryParam = new PurchaseOrderQueryParam();
            purchaseOrderQueryParam.setCreateStartTime(DateUtil.getMonthByOffset(0));
            purchaseOrderQueryParam.setCreateEndTime(DateUtil.getMonthByOffset(1));
            maps.put("purchaseOrderQueryParam", purchaseOrderQueryParam);
            Integer customerCompanyCount = purchaseOrderMapper.findPurchaseOrderCountByParams(maps);
            StringBuilder builder = new StringBuilder();
            builder.append("LXPO-");
            builder.append(subCompanyCode);
            builder.append("-");
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append("-");
            builder.append(String.format("%04d", customerCompanyCount + 1));
            return builder.toString();
        }
    }

    /**
     * 生成发货采购单编号
     */
    public String generatePurchaseDeliveryOrderNo(Date currentTime, Integer warehouseId) {
        synchronized (this) {
            WarehouseDO warehouseDO = warehouseMapper.findById(warehouseId);
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(warehouseDO.getSubCompanyId());
            //邮政编号

            Map<String, Object> maps = new HashMap<>();
            PurchaseDeliveryOrderQueryParam purchaseDeliveryOrderQueryParam = new PurchaseDeliveryOrderQueryParam();
            purchaseDeliveryOrderQueryParam.setCreateStartTime(DateUtil.getMonthByOffset(0));
            purchaseDeliveryOrderQueryParam.setCreateEndTime(DateUtil.getMonthByOffset(1));
            maps.put("purchaseDeliveryOrderQueryParam", purchaseDeliveryOrderQueryParam);
            Integer purchaseDeliveryOrderCount = purchaseDeliveryOrderMapper.findPurchaseDeliveryOrderCountByParams(maps);

            StringBuilder builder = new StringBuilder();
            builder.append("LXPD-");
            builder.append(subCompanyDO.getSubCompanyCode());
            builder.append("-");
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append("-");
            builder.append(String.format("%04d", purchaseDeliveryOrderCount + 1));
            return builder.toString();
        }
    }


    /**
     * 生成收货采购单编号
     */
    public String generatePurchaseReceiveOrderNo(Date currentTime, Integer warehouseId) {
        synchronized (this) {
            StringBuilder builder = new StringBuilder();
            WarehouseDO warehouseDO = warehouseMapper.findById(warehouseId);
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(warehouseDO.getSubCompanyId());

            Map<String, Object> maps = new HashMap<>();
            PurchaseReceiveOrderQueryParam purchaseReceiveOrderQueryParam = new PurchaseReceiveOrderQueryParam();
            purchaseReceiveOrderQueryParam.setCreateStartTime(DateUtil.getMonthByOffset(0));
            purchaseReceiveOrderQueryParam.setCreateEndTime(DateUtil.getMonthByOffset(1));
            maps.put("purchaseReceiveOrderQueryParam", purchaseReceiveOrderQueryParam);
            Integer purchaseReceiveOrderCount = purchaseReceiveOrderMapper.findPurchaseReceiveOrderCountByParams(maps);

            builder.append("LXPR-");
            builder.append(subCompanyDO.getSubCompanyCode());
            builder.append("-");
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append("-");
            builder.append(String.format("%04d", purchaseReceiveOrderCount + 1));
            return builder.toString();
        }
    }

    /**
     * 生成设备编号
     */
    public String generateProductEquipmentNo(String productModel, String cityCode) {
        Date currentTime = new Date();
        synchronized (this) {
            ProductEquipmentQueryParam param = new ProductEquipmentQueryParam();
            param.setCreateStartTime(DateUtil.getMonthByOffset(0));
            param.setCreateEndTime(DateUtil.getMonthByOffset(1));
            Map<String, Object> maps = new HashMap<>();
            maps.put("productEquipmentQueryParam", param);
            Integer listCount = productEquipmentMapper.listCount(maps);

            StringBuilder builder = new StringBuilder();
            builder.append("LXPE-");
            builder.append(cityCode);
            builder.append("-");
            builder.append(productModel);
            builder.append("-");
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append("-");
            builder.append(String.format("%06d", listCount + 1));
            return builder.toString();
        }
    }


    /**
     * 生成仓库编号
     */
    public String generateWarehouseNo(Integer subCompanyId, Integer warehouseTypeId) {
        synchronized (this) {
            //分公司
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(subCompanyId);
            StringBuilder builder = new StringBuilder();
            builder.append("LXW-");
            builder.append(subCompanyDO.getSubCompanyCode());
            builder.append("-");
            builder.append(warehouseTypeId);
            return builder.toString();
        }
    }

    /**
     * 生成工作流编号
     */
    public String generateWorkflowLinkNo(Date currentTime, Integer commitUserId) {
        synchronized (this) {
            WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
            workflowLinkQueryParam.setCreateStartTime(DateUtil.getMonthByOffset(0));
            workflowLinkQueryParam.setCreateEndTime(DateUtil.getMonthByOffset(1));
            Map<String, Object> maps = new HashMap<>();
            maps.put("workflowQueryParam", workflowLinkQueryParam);
            Integer count = workflowLinkMapper.listCount(maps);
            StringBuilder builder = new StringBuilder();
            builder.append("LXWF-");
            builder.append(commitUserId);
            builder.append("-");
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append("-");
            builder.append(String.format("%05d", count + 1));
            return builder.toString();
        }
    }

    /**
     * 生成退还单编号
     */
    public String generateReturnOrderNo(Date currentTime, Integer customerId) {
        synchronized (this) {
            ReturnOrderPageParam returnOrderPageParam = new ReturnOrderPageParam();
            returnOrderPageParam.setCreateStartTime(DateUtil.getMonthByOffset(0));
            returnOrderPageParam.setCreateEndTime(DateUtil.getMonthByOffset(1));
            Map<String, Object> maps = new HashMap<>();
            maps.put("returnOrderPageParam", returnOrderPageParam);
            Integer returnOrderCount = returnOrderMapper.findReturnOrderCountByParams(maps);
            StringBuilder builder = new StringBuilder();
            builder.append("LXRO-");
            builder.append(customerId);
            builder.append("-");
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append("-");
            builder.append(String.format("%05d", returnOrderCount + 1));
            return builder.toString();
        }

    }

    /**
     * 生成租赁换货单编号
     */
    public String generateChangeOrderNo(Date currentTime, Integer customerId) {
        synchronized (this) {
            ChangeOrderPageParam changeOrderPageParam = new ChangeOrderPageParam();
            changeOrderPageParam.setCreateStartTime(DateUtil.getMonthByOffset(0));
            changeOrderPageParam.setCreateEndTime(DateUtil.getMonthByOffset(1));
            Map<String, Object> maps = new HashMap<>();
            maps.put("changeOrderPageParam", changeOrderPageParam);
            Integer changeOrderCount = changeOrderMapper.findChangeOrderCountByParams(maps);
            StringBuilder builder = new StringBuilder();
            builder.append("LXCO-");
            builder.append(customerId);
            builder.append("-");
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append("-");
            builder.append(String.format("%05d", changeOrderCount + 1));
            return builder.toString();
        }
    }

    /**
     * 生成结算单编号
     */
    public String generateStatementOrderNo(Date expectPayTime, Integer customerId) {
        synchronized (this) {
            StatementOrderQueryParam statementOrderQueryParam = new StatementOrderQueryParam();
            statementOrderQueryParam.setCreateStartTime(DateUtil.getMonthByOffset(0));
            statementOrderQueryParam.setCreateEndTime(DateUtil.getMonthByOffset(1));
            Map<String, Object> maps = new HashMap<>();
            maps.put("statementOrderQueryParam", statementOrderQueryParam);
            Integer count = statementOrderMapper.listCount(maps);
            StringBuilder builder = new StringBuilder();
            builder.append("LXSO-");
            builder.append(customerId);
            builder.append("-");
            builder.append(new SimpleDateFormat("yyyyMMdd").format(expectPayTime));
            builder.append("-");
            builder.append(String.format("%05d", count + 1));
            return builder.toString();
        }
    }

    /**
     * 生成维修单编号
     */
    public String generateRepairOrderNo(Date currentTime, String warehouseNo) {
        synchronized (this) {
            RepairOrderQueryParam repairOrderQueryParam = new RepairOrderQueryParam();
            repairOrderQueryParam.setCreateStartTime(DateUtil.getMonthByOffset(0));
            repairOrderQueryParam.setCreateEndTime(DateUtil.getMonthByOffset(1));
            Map<String, Object> maps = new HashMap<>();
            maps.put("repairOrderQueryParam", repairOrderQueryParam);
            Integer count = repairOrderMapper.findRepairOrderCountByParams(maps);
            //仓库
            WarehouseDO warehouseDO = warehouseMapper.finByNo(warehouseNo);
            //分公司
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(warehouseDO.getSubCompanyId());
            StringBuilder builder = new StringBuilder();
            builder.append("LXRE-");
            builder.append(subCompanyDO.getSubCompanyCode());
            builder.append("-");
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append("-");
            builder.append(String.format("%05d", count + 1));
            return builder.toString();
        }
    }

    /**
     * 生成散料单编号
     **/
    public String generateBulkMaterialNo(Date currentTime, Integer warehouseId) {
        synchronized (this) {
            BulkMaterialQueryParam bulkMaterialQueryParam = new BulkMaterialQueryParam();
            bulkMaterialQueryParam.setCreateStartTime(DateUtil.getMonthByOffset(0));
            bulkMaterialQueryParam.setCreateEndTime(DateUtil.getMonthByOffset(1));
            Map<String, Object> maps = new HashMap<>();
            maps.put("bulkMaterialQueryParam", bulkMaterialQueryParam);
            Integer count = bulkMaterialMapper.listCount(maps);
            //仓库
            WarehouseDO warehouseDO = warehouseMapper.findById(warehouseId);
            //分公司
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(warehouseDO.getSubCompanyId());

            StringBuilder builder = new StringBuilder();
            builder.append("LXBM-");
            builder.append(subCompanyDO.getSubCompanyCode());
            builder.append("-");
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append("-");
            builder.append(String.format("%06d", count + 1));
            return builder.toString();
        }
    }

    /**
     * 生成批量设备编号
     */
    public List<String> generateProductEquipmentNoList(String productModel, String cityCode, Integer equipmentCount) {
        Date currentTime = new Date();
        synchronized (this) {
            ProductEquipmentQueryParam param = new ProductEquipmentQueryParam();
            param.setCreateStartTime(DateUtil.getMonthByOffset(0));
            param.setCreateEndTime(DateUtil.getMonthByOffset(1));
            Map<String, Object> maps = new HashMap<>();
            maps.put("productEquipmentQueryParam", param);
            Integer listCount = productEquipmentMapper.listCount(maps);

            ArrayList<String> ProductEquipmentNos = new ArrayList<>();
            for (int i = 0; i < equipmentCount; i++) {
                StringBuilder builder = new StringBuilder();
                builder.append("LXPE-");
                builder.append(cityCode);
                builder.append("-");
                builder.append(productModel);
                builder.append("-");
                builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
                builder.append("-");
                builder.append(String.format("%06d", listCount + 1));
                ProductEquipmentNos.add(builder.toString());
                listCount++;
            }
            return ProductEquipmentNos;
        }
    }

    /**
     * 生成批量散料单编号
     **/
    public List<String> generateBulkMaterialNoList(String materialModel, String cityCode, int bulkMaterialCount) {
        Date currentTime = new Date();
        synchronized (this) {
            BulkMaterialQueryParam bulkMaterialQueryParam = new BulkMaterialQueryParam();
            bulkMaterialQueryParam.setCreateStartTime(DateUtil.getMonthByOffset(0));
            bulkMaterialQueryParam.setCreateEndTime(DateUtil.getMonthByOffset(1));
            Map<String, Object> maps = new HashMap<>();
            maps.put("bulkMaterialQueryParam", bulkMaterialQueryParam);
            Integer count = bulkMaterialMapper.listCount(maps);

            ArrayList<String> BulkMaterialNos = new ArrayList<>();
            for (int i = 0; i < bulkMaterialCount; i++) {
                StringBuilder builder = new StringBuilder();
                builder.append("LXBM-");
                builder.append(cityCode);
                builder.append("-");
                builder.append(materialModel);
                builder.append("-");
                builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
                builder.append("-");
                builder.append(String.format("%06d", count + 1));
                BulkMaterialNos.add(builder.toString());
                count++;
            }
            return BulkMaterialNos;
        }
    }

    /**
     * 生成供应商编号
     */
    public String generateSupplierNo(String cityCode) {
        synchronized (this) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("supplierQueryParam", null);
            Integer count = supplierMapper.listCount(paramMap);
            StringBuilder builder = new StringBuilder();
            builder.append("LXS-");
            builder.append(cityCode);
            builder.append("-");
            builder.append(String.format("%05d", count + 1));
            return builder.toString();
        }
    }

    /**
     * 生成组装单编号
     */
    public String generateAssemblerOderNo(Date date, Integer warehouseId) {
        synchronized (this) {
            HashMap<String, Object> maps = new HashMap<>();
            AssembleOrderQueryParam assembleOrderQueryParam = new AssembleOrderQueryParam();
            assembleOrderQueryParam.setCreateStartTime(DateUtil.getMonthByOffset(0));
            assembleOrderQueryParam.setCreateEndTime(DateUtil.getMonthByOffset(1));
            maps.put("assembleOrderQueryParam", assembleOrderQueryParam);
            Integer count = assembleOrderMapper.listCount(maps);
            StringBuilder builder = new StringBuilder();
            builder.append("LXA-");
            builder.append(warehouseId);
            builder.append("-");
            builder.append(new SimpleDateFormat("yyyyMMdd").format(date));
            builder.append("-");
            builder.append(String.format("%05d", count + 1));
            return builder.toString();
        }
    }

    /**
     * 转移单编号
     */
    public String generateTransferOrderNo(Date date, Integer warehouseId) {
        synchronized (this) {
            HashMap<String, Object> maps = new HashMap<>();
            TransferOrderQueryParam transferOrderQueryParam = new TransferOrderQueryParam();
            transferOrderQueryParam.setCreateStartTime(DateUtil.getMonthByOffset(0));
            transferOrderQueryParam.setCreateEndTime(DateUtil.getMonthByOffset(1));
            maps.put("transferOrderQueryParam", transferOrderQueryParam);
            Integer count = transferOrderMapper.findTransferOrderCountByParams(maps);
            StringBuilder builder = new StringBuilder();
            builder.append("LXT-");
            builder.append(warehouseId);
            builder.append("-");
            builder.append(new SimpleDateFormat("yyyyMMdd").format(date));
            builder.append("-");
            builder.append(String.format("%05d", count + 1));
            return builder.toString();
        }
    }

    /**
     * 同行调拨单编号
     */
    public String generatePeerDeploymentOrderNo(Date date, String cityCode) {
        synchronized (this) {
            HashMap<String, Object> maps = new HashMap<>();
            PeerDeploymentOrderQueryParam peerDeploymentOrderQueryParam = new PeerDeploymentOrderQueryParam();
            peerDeploymentOrderQueryParam.setCreateStartTime(DateUtil.getMonthByOffset(0));
            peerDeploymentOrderQueryParam.setCreateEndTime(DateUtil.getMonthByOffset(1));
            maps.put("peerDeploymentOrderQueryParam", peerDeploymentOrderQueryParam);
            Integer count = peerDeploymentOrderMapper.listCount(maps);
            StringBuilder builder = new StringBuilder();
            builder.append("LXPDO-");
            builder.append(cityCode);
            builder.append("-");
            builder.append(new SimpleDateFormat("yyyyMMdd").format(date));
            builder.append("-");
            builder.append(String.format("%04d", count + 1));
            return builder.toString();
        }
    }


    /**
     * 生成采购申请单编号
     */
    public String generatePurchaseApplyOrderNo(String cityCode) {
        synchronized (this) {
            Date currentTime = new Date();
            PurchaseApplyOrderPageParam purchaseApplyOrderPageParam = new PurchaseApplyOrderPageParam();
            Map<String, Object> maps = new HashMap<>();
            maps.put("start", 0);
            maps.put("pageSize", Integer.MAX_VALUE);
            purchaseApplyOrderPageParam.setCreateStartTime(DateUtil.getMonthByOffset(0));
            purchaseApplyOrderPageParam.setCreateEndTime(DateUtil.getMonthByOffset(1));
            maps.put("purchaseApplyOrderPageParam", purchaseApplyOrderPageParam);
            Integer count = purchaseApplyOrderMapper.listCount(maps);
            StringBuilder builder = new StringBuilder();
            builder.append("LXPA-");
            builder.append(cityCode);
            builder.append("-");
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append("-");
            builder.append(String.format("%05d", count + 1));
            return builder.toString();
        }
    }

    /**
     * 生成同行供应商编号
     */
    public String generatePeerNo(String cityCode) {
        synchronized (this) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("peerQueryParam", null);
            Integer count = peerMapper.listCount(paramMap);
            StringBuilder builder = new StringBuilder();
            builder.append("LXPEER-");
            builder.append(cityCode);
            builder.append("-");
            builder.append(String.format("%05d", count + 1));
            return builder.toString();
        }
    }


    @Autowired
    private SupplierMapper supplierMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private AreaCityMapper areaCityMapper;
    @Autowired
    private WarehouseMapper warehouseMapper;
    @Autowired
    private SubCompanyMapper subCompanyMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;
    @Autowired
    private PurchaseDeliveryOrderMapper purchaseDeliveryOrderMapper;
    @Autowired
    private PurchaseReceiveOrderMapper purchaseReceiveOrderMapper;
    @Autowired
    private ProductEquipmentMapper productEquipmentMapper;
    @Autowired
    private WorkflowLinkMapper workflowLinkMapper;
    @Autowired
    private ReturnOrderMapper returnOrderMapper;
    @Autowired
    private ChangeOrderMapper changeOrderMapper;
    @Autowired
    private StatementOrderMapper statementOrderMapper;
    @Autowired
    private DeploymentOrderMapper deploymentOrderMapper;
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private AssembleOrderMapper assembleOrderMapper;
    @Autowired
    private TransferOrderMapper transferOrderMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private StockOrderMapper stockOrderMapper;
    @Autowired
    private BulkMaterialMapper bulkMaterialMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private PurchaseApplyOrderMapper purchaseApplyOrderMapper;
    @Autowired
    private PeerMapper peerMapper;
    @Autowired
    private PeerDeploymentOrderMapper peerDeploymentOrderMapper;
    @Autowired
    private RepairOrderMapper repairOrderMapper;
}
