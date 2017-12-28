package com.lxzl.erp.core.service.basic.impl.support;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.CustomerType;
import com.lxzl.erp.common.domain.changeOrder.ChangeOrderPageParam;
import com.lxzl.erp.common.domain.deploymentOrder.DeploymentOrderQueryParam;
import com.lxzl.erp.common.domain.material.BulkMaterialQueryParam;
import com.lxzl.erp.common.domain.order.OrderQueryParam;
import com.lxzl.erp.common.domain.product.ProductEquipmentQueryParam;
import com.lxzl.erp.common.domain.purchase.PurchaseDeliveryOrderQueryParam;
import com.lxzl.erp.common.domain.purchase.PurchaseOrderQueryParam;
import com.lxzl.erp.common.domain.purchase.PurchaseReceiveOrderQueryParam;
import com.lxzl.erp.common.domain.repairOrder.RepairOrderQueryParam;
import com.lxzl.erp.common.domain.returnOrder.ReturnOrderPageParam;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.domain.workflow.WorkflowLinkQueryParam;
import com.lxzl.erp.dataaccess.dao.mysql.area.AreaCityMapper;
import com.lxzl.erp.dataaccess.dao.mysql.changeOrder.ChangeOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.company.DepartmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.deploymentOrder.DeploymentOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.BulkMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductEquipmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.purchase.PurchaseDeliveryOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.purchase.PurchaseOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.purchase.PurchaseReceiveOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.repairOrder.RepairOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.returnOrder.ReturnOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.statement.StatementOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.supplier.SupplierMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.RoleDepartmentDataMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserRoleMapper;
import com.lxzl.erp.dataaccess.dao.mysql.warehouse.WarehouseMapper;
import com.lxzl.erp.dataaccess.dao.mysql.workflow.WorkflowLinkMapper;
import com.lxzl.erp.dataaccess.domain.area.AreaCityDO;
import com.lxzl.erp.dataaccess.domain.company.DepartmentDO;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.user.RoleDepartmentDataDO;
import com.lxzl.erp.dataaccess.domain.user.UserRoleDO;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
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
            Map<String, Date> date = getMonthlongDate();
            Map<String, Object> maps = new HashMap<>();
            OrderQueryParam orderQueryParam = new OrderQueryParam();
            orderQueryParam.setCreateStartTime(date.get("firstdayDate"));
            orderQueryParam.setCreateEndTime(date.get("lastdayDate"));
            maps.put("orderQueryParam", orderQueryParam);
            Integer orderCount = orderMapper.findOrderCountByParams(maps);
            StringBuilder builder = new StringBuilder();
            builder.append("LXO");
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append(buyerCustomerId);
            builder.append(String.format("%05d", orderCount + 1));
            return builder.toString();
        }
    }

    /**
     * 生成客户编号
     */
    public String generateCustomerNo(Date currentTime, Integer owner, Integer customerType) {
        synchronized (this) {
            StringBuilder builder = new StringBuilder();
            UserRoleDO userRoleDO = userRoleMapper.findListByUserId(owner).get(0);
            //角色id
            RoleDepartmentDataDO roleDepartmentDataDO = roleDepartmentDataMapper.getRoleDepartmentDataListByRoleId(userRoleDO.getRoleId()).get(0);
            //部门id
            DepartmentDO departmentDO = departmentMapper.findById(roleDepartmentDataDO.getDepartmentId());
            //业务员所在的city_code
            String subCompanyCode = subCompanyMapper.findById(departmentDO.getSubCompanyId()).getSubCompanyCode();
            Map<String, Date> date = getMonthlongDate();
            ProductEquipmentQueryParam productEquipmentQueryParam = new ProductEquipmentQueryParam();
            productEquipmentQueryParam.setCreateStartTime(date.get("firstdayDate"));
            productEquipmentQueryParam.setCreateEndTime(date.get("lastdayDate"));
            Map<String, Object> maps = new HashMap<>();
            maps.put("productEquipmentQueryParam", productEquipmentQueryParam);
            Integer count = customerMapper.listCount(maps);

            if (customerType.equals(CustomerType.CUSTOMER_TYPE_COMPANY)) {
                builder.append("LXCC");
            }
            if (customerType.equals(CustomerType.CUSTOMER_TYPE_PERSON)) {
                builder.append("LXCD");
            }
            builder.append(subCompanyCode);
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
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
            AreaCityDO srcAreaCityDO = areaCityMapper.findById(srcSubCompanyDO.getCity());
            //获取出方的城市区号
            WarehouseDO targetWarehouseDO = warehouseMapper.findById(targetWarehouseId);
            SubCompanyDO targetSubCompanyDO = subCompanyMapper.findById(targetWarehouseDO.getSubCompanyId());
            AreaCityDO targetAreaCityDO = areaCityMapper.findById(targetSubCompanyDO.getCity());
            Map<String, Date> date = getMonthlongDate();
            DeploymentOrderQueryParam deploymentOrderQueryParam = new DeploymentOrderQueryParam();
            deploymentOrderQueryParam.setCreateStartTime(date.get("firstdayDate"));
            deploymentOrderQueryParam.setCreateEndTime(date.get("lastdayDate"));
            Map<String, Object> maps = new HashMap<>();
            maps.put("departmentQueryParam", deploymentOrderQueryParam);
            Integer count = deploymentOrderMapper.listCount(maps);
            builder.append("LXC");
            builder.append(srcAreaCityDO.getCityCode());
            builder.append(targetAreaCityDO.getCityCode());
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
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
            Map<String, Date> date = getMonthlongDate();
            Map<String, Object> maps = new HashMap<>();
            PurchaseOrderQueryParam purchaseOrderQueryParam = new PurchaseOrderQueryParam();
            purchaseOrderQueryParam.setCreateStartTime(date.get("firstdayDate"));
            purchaseOrderQueryParam.setCreateEndTime(date.get("lastdayDate"));
            maps.put("queryParam", purchaseOrderQueryParam);
            Integer customerCompanyCount = purchaseOrderMapper.findPurchaseOrderCountByParams(maps);
            StringBuilder builder = new StringBuilder();
            builder.append("LXPO");
            builder.append(subCompanyCode);
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
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
            Map<String, Date> date = getMonthlongDate();

            Map<String, Object> maps = new HashMap<>();
            PurchaseDeliveryOrderQueryParam purchaseDeliveryOrderQueryParam = new PurchaseDeliveryOrderQueryParam();
            purchaseDeliveryOrderQueryParam.setCreateStartTime(date.get("firstdayDate"));
            purchaseDeliveryOrderQueryParam.setCreateEndTime(date.get("lastdayDate"));
            maps.put("queryParam", purchaseDeliveryOrderQueryParam);
            Integer purchaseDeliveryOrderCount = purchaseDeliveryOrderMapper.findPurchaseDeliveryOrderCountByParams(maps);

            StringBuilder builder = new StringBuilder();
            builder.append("LXPD");
            builder.append(subCompanyDO.getSubCompanyCode());
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
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

            Map<String, Date> date = getMonthlongDate();
            Map<String, Object> maps = new HashMap<>();
            PurchaseReceiveOrderQueryParam purchaseReceiveOrderQueryParam = new PurchaseReceiveOrderQueryParam();
            purchaseReceiveOrderQueryParam.setCreateStartTime(date.get("firstdayDate"));
            purchaseReceiveOrderQueryParam.setCreateEndTime(date.get("lastdayDate"));
            maps.put("queryParam", purchaseReceiveOrderQueryParam);
            Integer purchaseReceiveOrderCount = purchaseReceiveOrderMapper.findPurchaseReceiveOrderCountByParams(maps);

            builder.append("LXPR");
            builder.append(subCompanyDO.getSubCompanyCode());
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append(String.format("%04d", purchaseReceiveOrderCount + 1));
            return builder.toString();
        }
    }

    /**
     * 生成设备编号
     */
    public String generateProductEquipmentNo(Date currentTime, Integer warehouseId) {
        synchronized (this) {
            Map<String, Date> date = getMonthlongDate();
            ProductEquipmentQueryParam param = new ProductEquipmentQueryParam();
            param.setCreateStartTime(date.get("firstdayDate"));
            param.setCreateEndTime(date.get("lastdayDate"));
            Map<String, Object> maps = new HashMap<>();
            maps.put("productEquipmentQueryParam", param);
            Integer listCount = productEquipmentMapper.listCount(maps);
            //仓库
            WarehouseDO warehouseDO = warehouseMapper.findById(warehouseId);
            //分公司
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(warehouseDO.getSubCompanyId());

            StringBuilder builder = new StringBuilder();
            builder.append("LXE");
            builder.append(subCompanyDO.getSubCompanyCode());
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
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
            builder.append("LXW");
            builder.append(subCompanyDO.getSubCompanyCode());
            builder.append(warehouseTypeId);
            return builder.toString();
        }
    }

    /**
     * 生成工作流编号
     */
    public String generateWorkflowLinkNo(Date currentTime, Integer commitUserId) {
        synchronized (this) {
            Map<String, Date> date = getMonthlongDate();
            WorkflowLinkQueryParam workflowLinkQueryParam = new WorkflowLinkQueryParam();
            workflowLinkQueryParam.setCreateStartTime(date.get("firstdayDate"));
            workflowLinkQueryParam.setCreateEndTime(date.get("lastdayDate"));
            Map<String, Object> maps = new HashMap<>();
            maps.put("queryParam", workflowLinkQueryParam);
            Integer count = workflowLinkMapper.listCount(maps);
            StringBuilder builder = new StringBuilder();
            builder.append("LXWF");
            builder.append(commitUserId);
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append(String.format("%05d", count + 1));
            return builder.toString();
        }
    }

    /**
     * 生成退还单编号
     */
    public String generateReturnOrderNo(Date currentTime, Integer customerId) {
        synchronized (this) {
            Map<String, Date> date = getMonthlongDate();
            ReturnOrderPageParam returnOrderPageParam = new ReturnOrderPageParam();
            returnOrderPageParam.setCreateStartTime(date.get("firstdayDate"));
            returnOrderPageParam.setCreateEndTime(date.get("lastdayDate"));
            Map<String, Object> maps = new HashMap<>();
            maps.put("queryParam", returnOrderPageParam);
            Integer returnOrderCount = returnOrderMapper.findReturnOrderCountByParams(maps);
            StringBuilder builder = new StringBuilder();
            builder.append("LXRO");
            builder.append(customerId);
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append(String.format("%05d", returnOrderCount + 1));
            return builder.toString();
        }

    }

    /**
     * 生成租赁换货单编号
     */
    public String generateChangeOrderNo(Date currentTime, Integer customerId) {
        synchronized (this) {
            Map<String, Date> date = getMonthlongDate();
            ChangeOrderPageParam changeOrderPageParam = new ChangeOrderPageParam();
            changeOrderPageParam.setCreateStartTime(date.get("firstdayDate"));
            changeOrderPageParam.setCreateEndTime(date.get("lastdayDate"));
            Map<String, Object> maps = new HashMap<>();
            maps.put("queryParam", changeOrderPageParam);
            Integer changeOrderCount = changeOrderMapper.findChangeOrderCountByParams(maps);
            StringBuilder builder = new StringBuilder();
            builder.append("LXCO");
            builder.append(customerId);
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append(String.format("%05d", changeOrderCount + 1));
            return builder.toString();
        }
    }

    /**
     * 生成结算单编号
     */
    public String generateStatementOrderNo(Date expectPayTime, Integer customerId) {
        synchronized (this) {
            Map<String, Date> date = getMonthlongDate();
            StatementOrderQueryParam statementOrderQueryParam = new StatementOrderQueryParam();
            statementOrderQueryParam.setCreateStartTime(date.get("firstdayDate"));
            statementOrderQueryParam.setCreateEndTime(date.get("lastdayDate"));
            Map<String, Object> maps = new HashMap<>();
            maps.put("queryParam", statementOrderQueryParam);
            Integer count = statementOrderMapper.listCount(maps);
            StringBuilder builder = new StringBuilder();
            builder.append("LXSO");
            builder.append(customerId);
            builder.append(new SimpleDateFormat("yyyyMMdd").format(expectPayTime));
            builder.append(String.format("%05d", count + 1));
            return builder.toString();
        }
    }

    /**
     * 生成维修单编号
     */
    public String generateRepairOrderNo(Date currentTime, String warehouseNo) {
        synchronized (this) {
            Map<String, Date> date = getMonthlongDate();
            RepairOrderQueryParam repairOrderQueryParam = new RepairOrderQueryParam();
            repairOrderQueryParam.setCreateStartTime(date.get("firstdayDate"));
            repairOrderQueryParam.setCreateEndTime(date.get("lastdayDate"));
            Map<String, Object> maps = new HashMap<>();
            maps.put("queryParam", repairOrderQueryParam);
            Integer count = statementOrderMapper.listCount(maps);
            //仓库
            WarehouseDO warehouseDO = warehouseMapper.finByNo(warehouseNo);
            //分公司
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(warehouseDO.getSubCompanyId());
            StringBuilder builder = new StringBuilder();
            builder.append("LXRE");
            builder.append(subCompanyDO.getSubCompanyCode());
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append(String.format("%05d", count + 1));
            return builder.toString();
        }
    }

    /**
     * 生成散料单编号
     **/
    public String generateBulkMaterialNo(Date currentTime, Integer warehouseId) {
        synchronized (this) {
            Map<String, Date> date = getMonthlongDate();
            BulkMaterialQueryParam bulkMaterialQueryParam = new BulkMaterialQueryParam();
            bulkMaterialQueryParam.setCreateStartTime(date.get("firstdayDate"));
            bulkMaterialQueryParam.setCreateEndTime(date.get("lastdayDate"));
            Map<String, Object> maps = new HashMap<>();
            maps.put("queryParam", bulkMaterialQueryParam);
            Integer count = bulkMaterialMapper.listCount(maps);
            //仓库
            WarehouseDO warehouseDO = warehouseMapper.findById(warehouseId);
            //分公司
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(warehouseDO.getSubCompanyId());

            StringBuilder builder = new StringBuilder();
            builder.append("LXBM");
            builder.append(subCompanyDO.getSubCompanyCode());
            builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
            builder.append(String.format("%06d", count + 1));
            return builder.toString();
        }
    }

    /**
     * 生成批量设备编号
     */
    public List<String> generateProductEquipmentNo(Date currentTime, List<Integer> warehouseIds) {
        synchronized (this) {
            Map<String, Date> date = getMonthlongDate();
            ProductEquipmentQueryParam param = new ProductEquipmentQueryParam();
            param.setCreateStartTime(date.get("firstdayDate"));
            param.setCreateEndTime(date.get("lastdayDate"));
            Map<String, Object> maps = new HashMap<>();
            maps.put("productEquipmentQueryParam", param);
            Integer listCount = productEquipmentMapper.listCount(maps);

            ArrayList<String> ProductEquipmentNos = new ArrayList<>();
            for (Integer warehouseId : warehouseIds) {
                //仓库
                WarehouseDO warehouseDO = warehouseMapper.findById(warehouseId);
                //分公司
                SubCompanyDO subCompanyDO = subCompanyMapper.findById(warehouseDO.getSubCompanyId());
                StringBuilder builder = new StringBuilder();
                builder.append("LXE");
                builder.append(subCompanyDO.getSubCompanyCode());
                builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
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
    public List<String> generateBulkMaterialNo(Date currentTime, List<Integer> warehouseIds) {
        synchronized (this) {
            Map<String, Date> date = getMonthlongDate();
            BulkMaterialQueryParam bulkMaterialQueryParam = new BulkMaterialQueryParam();
            bulkMaterialQueryParam.setCreateStartTime(date.get("firstdayDate"));
            bulkMaterialQueryParam.setCreateEndTime(date.get("lastdayDate"));
            Map<String, Object> maps = new HashMap<>();
            maps.put("queryParam", bulkMaterialQueryParam);
            Integer count = bulkMaterialMapper.listCount(maps);

            ArrayList<String> BulkMaterialNos = new ArrayList<>();
            for (Integer warehouseId : warehouseIds) {
                //仓库
                WarehouseDO warehouseDO = warehouseMapper.findById(warehouseId);
                //分公司
                SubCompanyDO subCompanyDO = subCompanyMapper.findById(warehouseDO.getSubCompanyId());
                StringBuilder builder = new StringBuilder();
                builder.append("LXBM");
                builder.append(subCompanyDO.getSubCompanyCode());
                builder.append(new SimpleDateFormat("yyyyMMdd").format(currentTime));
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
    public String generateSupplierNo(Integer cityId) {
        synchronized (this) {
            AreaCityDO areaCityDO = areaCityMapper.findById(cityId);
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("supplierQueryParam", null);
            Integer count = supplierMapper.listCount(paramMap);
            StringBuilder builder = new StringBuilder();
            builder.append("LXS");
            builder.append(areaCityDO.getCityCode());
            builder.append(count+1);
            return builder.toString();
        }
    }

    /**
     * 获取当月的时间
     */
    public Map<String, Date> getMonthlongDate() {
        HashMap<String, Date> map = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String firstday, lastday;
        // 获取前月的第一天
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        firstday = format.format(cale.getTime());
        Date firstdayDate = null;
        try {
            firstdayDate = format.parse(firstday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 下个月的第一天
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        lastday = format.format(cale.getTime());
        Date lastdayDate = null;
        try {
            lastdayDate = format.parse(lastday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        map.put("firstdayDate", firstdayDate);
        map.put("lastdayDate", lastdayDate);
        return map;
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
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleDepartmentDataMapper roleDepartmentDataMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
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
    private RepairOrderMapper repairOrderMapper;
    @Autowired
    private BulkMaterialMapper bulkMaterialMapper;
    @Autowired
    private DeploymentOrderMapper deploymentOrderMapper;

}
