package com.lxzl.erp.core.service.k3.converter.impl;

import com.lxzl.erp.common.cache.CommonCache;
import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.order.pojo.OrderTimeAxis;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowLink;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.FormICItem;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.FormSEOrder;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.FormSeorderEntry;
import com.lxzl.erp.core.service.k3.K3Support;
import com.lxzl.erp.core.service.k3.converter.ConvertK3DataService;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.workflow.WorkflowService;
import com.lxzl.erp.dataaccess.dao.mysql.company.DepartmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.*;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.order.OrderTimeAxisMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.RoleMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserRoleMapper;
import com.lxzl.erp.dataaccess.domain.company.DepartmentDO;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.k3.*;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderTimeAxisDO;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.erp.dataaccess.domain.user.RoleDO;
import com.lxzl.erp.dataaccess.domain.user.RoleDepartmentDataDO;
import com.lxzl.erp.dataaccess.domain.user.UserRoleDO;
import com.lxzl.se.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


@Service
public class K3OrderConverter implements ConvertK3DataService {

    @Override
    public Object getK3PostWebServiceData(Integer postK3OperatorType, Object data) {

        Order erpOrder = (Order) data;
        ServiceResult<String, Order> orderResult = orderService.queryOrderByNo(erpOrder.getOrderNo());
        erpOrder = orderResult.getResult();
        if(erpOrder == null){
            return null;
        }
        // 先查该订单下有几种订单类型类型，有长租，短租，短短租~，以及先付后用和先用后付
        // 长租-先付后用，短租-先付后用，短短租-先付后用，长租-先用后付，短租-先用后付，短短租-先用后付
        String longFirstKey = "L-F";            // 长租先付key
        String shortFirstKey = "D-F";           // 短租先付key
        String bigShortFirstKey = "R-F";      // 短短租先付key
        String longEndKey = "L-E";            // 长租后付key
        String shortEndKey = "D-E";           // 短租后付key
        String bigShortEndKeyKey = "R-E";      // 短短租后付key
        Map<String, List<OrderProduct>> orderProductMap = new HashMap<>();
        orderProductMap.put(longFirstKey, new ArrayList<OrderProduct>());
        orderProductMap.put(shortFirstKey, new ArrayList<OrderProduct>());
        orderProductMap.put(bigShortFirstKey, new ArrayList<OrderProduct>());
        orderProductMap.put(longEndKey, new ArrayList<OrderProduct>());
        orderProductMap.put(shortEndKey, new ArrayList<OrderProduct>());
        orderProductMap.put(bigShortEndKeyKey, new ArrayList<OrderProduct>());
        Map<String, List<OrderMaterial>> orderMaterialMap = new HashMap<>();
        orderMaterialMap.put(longFirstKey, new ArrayList<OrderMaterial>());
        orderMaterialMap.put(shortFirstKey, new ArrayList<OrderMaterial>());
        orderMaterialMap.put(bigShortFirstKey, new ArrayList<OrderMaterial>());
        orderMaterialMap.put(longEndKey, new ArrayList<OrderMaterial>());
        orderMaterialMap.put(shortEndKey, new ArrayList<OrderMaterial>());
        orderMaterialMap.put(bigShortEndKeyKey, new ArrayList<OrderMaterial>());

        if (CollectionUtil.isNotEmpty(erpOrder.getOrderProductList())) {
            for (OrderProduct orderProduct : erpOrder.getOrderProductList()) {
                String startKey;
                if (OrderRentType.RENT_TYPE_DAY.equals(orderProduct.getRentType())) {
                    // 短短租的情况
                    startKey = "R";
                } else {
                    // 短租和长租的情况
                    if (orderProduct.getRentTimeLength() >= 6) {
                        // 长租
                        startKey = "L";
                    } else {
                        // 短租
                        startKey = "D";
                    }
                }
                if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(orderProduct.getPayMode())
                        || OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(orderProduct.getPayMode())) {
                    startKey = startKey + "-F";
                } else {
                    startKey = startKey + "-E";
                }

                List<OrderProduct> orderProductList = orderProductMap.get(startKey);
                orderProductList.add(orderProduct);
                orderProductMap.put(startKey, orderProductList);
            }
        }

        if (CollectionUtil.isNotEmpty(erpOrder.getOrderMaterialList())) {
            for (OrderMaterial orderMaterial : erpOrder.getOrderMaterialList()) {
                String startKey;
                if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterial.getRentType())) {
                    // 短短租的情况
                    startKey = "R";
                } else {
                    // 短租和长租的情况
                    if (orderMaterial.getRentTimeLength() >= 6) {
                        // 长租
                        startKey = "L";
                    } else {
                        // 短租
                        startKey = "D";
                    }
                }
                if (OrderPayMode.PAY_MODE_PAY_BEFORE.equals(orderMaterial.getPayMode())
                        || OrderPayMode.PAY_MODE_PAY_BEFORE_PERCENT.equals(orderMaterial.getPayMode())) {
                    startKey = startKey + "-F";
                } else {
                    startKey = startKey + "-E";
                }

                List<OrderMaterial> orderMaterialList = orderMaterialMap.get(startKey);
                orderMaterialList.add(orderMaterial);
                orderMaterialMap.put(startKey, orderMaterialList);
            }
        }

        // ==================================== 以上封装为多种情况的订单项 ====================================

        List<FormSEOrder> formSEOrderList = new ArrayList<>();
        if (orderProductMap.size() > 0) {
            for (Map.Entry<String, List<OrderProduct>> entry : orderProductMap.entrySet()) {
                String key = entry.getKey();
                List<OrderProduct> orderProductList = entry.getValue();
                if (CollectionUtil.isEmpty(orderProductList)) {
                    continue;
                }
                // 如果配件有同种类型的一并取
                List<OrderMaterial> orderMaterialList = orderMaterialMap.get(key);
                if (CollectionUtil.isNotEmpty(orderMaterialList)) {
                    orderMaterialMap.put(key, new ArrayList<OrderMaterial>());
                }
                FormSEOrder formSEOrder = new FormSEOrder();
                if (PostK3OperatorType.POST_K3_OPERATOR_TYPE_UPDATE.equals(postK3OperatorType)) {
                    formSEOrder.setIsReplace(true);// 是否覆盖
                } else if (PostK3OperatorType.POST_K3_OPERATOR_TYPE_ADD.equals(postK3OperatorType)) {
                    formSEOrder.setIsReplace(false);
                }
                K3MappingCustomerDO k3MappingCustomerDO = k3MappingCustomerMapper.findByErpCode(erpOrder.getBuyerCustomerNo());
                if (k3MappingCustomerDO == null) {
                    CustomerDO customerDO = customerMapper.findByNo(erpOrder.getBuyerCustomerNo());
                    Integer subCompanyId = userSupport.getCompanyIdByUser(customerDO.getOwner());
                    SubCompanyDO subCompanyDO = subCompanyMapper.findById(subCompanyId);
                    String cityCode = k3Support.getK3CityCode(subCompanyDO.getSubCompanyCode());
                    k3MappingCustomerDO = new K3MappingCustomerDO();
                    k3MappingCustomerDO.setCustomerName(customerDO.getCustomerName());
                    k3MappingCustomerDO.setErpCustomerCode(customerDO.getCustomerNo());

                    String customerNumber = cityCode + "." + customerDO.getId();
                    k3MappingCustomerDO.setK3CustomerCode(customerNumber);
                    k3MappingCustomerMapper.save(k3MappingCustomerDO);
                    formSEOrder.setCustNumber(customerNumber);// 客户代码
                    formSEOrder.setCustName(customerDO.getCustomerName());// 客户名称
                } else {
                    formSEOrder.setCustNumber(k3MappingCustomerDO.getK3CustomerCode());// 客户代码
                    formSEOrder.setCustName(k3MappingCustomerDO.getCustomerName());// 客户名称
                }

                formSEOrder.setFetchStyleNumber("");
                formSEOrder.setDeptNumber("");
                formSEOrder.setBillNO(erpOrder.getOrderNo() + "-" + key);// 单据编号
                Calendar createTime = Calendar.getInstance();
                createTime.setTime(erpOrder.getCreateTime());
                formSEOrder.setDate(createTime);// 日期
                String fetchStyleNumber = null;
                if (DeliveryMode.DELIVERY_MODE_EXPRESS.equals(erpOrder.getDeliveryMode())) {
                    fetchStyleNumber = "FJH03";
                } else if (DeliveryMode.DELIVERY_MODE_SINCE.equals(erpOrder.getDeliveryMode())) {
                    fetchStyleNumber = "FJH01";
                } else if (DeliveryMode.DELIVERY_MODE_LX_EXPRESS.equals(erpOrder.getDeliveryMode())) {
                    fetchStyleNumber = "FJH02";
                }
                formSEOrder.setFetchStyleNumber(fetchStyleNumber);// 交货方式  FJH01 客户自提 FJH02 送货上门 FJH03 物流发货
                List<RoleDO> roleDOList = roleMapper.findByUserId(erpOrder.getOrderSellerId());
                for (int i = 0; i < roleDOList.size(); i++) {
                    RoleDO roleDO = roleDOList.get(i);
                    K3MappingDepartmentDO k3MappingDepartmentDO = k3MappingDepartmentMapper.findByErpId(roleDO.getDepartmentId());
                    if (i == 0) {
                        if (k3MappingDepartmentDO != null) {
                            formSEOrder.setDeptNumber(k3MappingDepartmentDO.getK3DepartmentCode());// 部门代码
                            formSEOrder.setDeptName(k3MappingDepartmentDO.getDepartmentName());
                        }
                    } else {
                        DepartmentDO departmentDO = departmentMapper.findById(roleDO.getDepartmentId());
                        if (DepartmentType.DEPARTMENT_TYPE_BUSINESS.equals(departmentDO.getDepartmentType())) {
                            formSEOrder.setDeptNumber(k3MappingDepartmentDO.getK3DepartmentCode());// 部门代码
                            formSEOrder.setDeptName(k3MappingDepartmentDO.getSubCompanyName() + "-" + k3MappingDepartmentDO.getDepartmentName());
                        }
                    }
                }

//        formSEOrder.setDeptNumber("01.06");// 部门代码
//        formSEOrder.setDeptName("深圳-租赁业务部");// 部门名称
                Integer subCompanyId = userSupport.getCompanyIdByUser(erpOrder.getOrderSellerId());
                SubCompanyDO sellerSubCompanyDO = subCompanyMapper.findById(subCompanyId);
                String empNumber = k3Support.getK3CityCode(sellerSubCompanyDO.getSubCompanyCode()) + "." + erpOrder.getOrderSellerId();
                formSEOrder.setEmpNumber(empNumber);// 业务员代码

                formSEOrder.setEmpName(erpOrder.getOrderSellerName());// 业务员名称
                formSEOrder.setBillerName(erpOrder.getCreateUserRealName());// 制单人
                //主管用业务员代替
                formSEOrder.setManagerNumber(empNumber);//  主管代码
                formSEOrder.setManagerName(erpOrder.getOrderSellerName());//  主管名称

                //取待发货状态的订单的时间轴节点
                List<OrderTimeAxisDO> orderTimeAxisDOList = orderTimeAxisMapper.findByOrderId(erpOrder.getOrderId());
                if (CollectionUtil.isNotEmpty(orderTimeAxisDOList)) {
                    for (OrderTimeAxisDO orderTimeAxisDO : orderTimeAxisDOList) {
                        if (OrderStatus.ORDER_STATUS_WAIT_DELIVERY.equals(orderTimeAxisDO.getOrderStatus())) {
                            Calendar checkDate = Calendar.getInstance();
                            checkDate.setTime(orderTimeAxisDO.getCreateTime());
                            formSEOrder.setCheckDate(checkDate);// 审核日期
                            User user = CommonCache.userMap.get(Integer.parseInt(String.valueOf(orderTimeAxisDO.getCreateUser())));
                            formSEOrder.setCheckerName(user.getRealName());// 审核人
                        }
                    }
                }


                formSEOrder.setExplanation(erpOrder.getRemark());// 摘要
                String orderTypeNumber = key.startsWith("L") ? "L" : key.startsWith("R") ? "R" : key.startsWith("D") ? "D" : null;
                formSEOrder.setOrderTypeNumber(orderTypeNumber);// 订单类型 L	长租  R	短短租(天) X	销售   D	短租

                formSEOrder.setBusinessTypeNumber("ZY");// 经营类型  ZY	经营性租赁 RZ 融资性租赁
                formSEOrder.setOrderFromNumber("XX");// 订单来源 XS	线上 XX 线下
                formSEOrder.setDeliveryName(erpOrder.getOrderConsignInfo().getConsigneeName());// 提货人
                String address = erpOrder.getOrderConsignInfo().getProvinceName() + " " + erpOrder.getOrderConsignInfo().getCityName() + " " +
                        erpOrder.getOrderConsignInfo().getDistrictName() + " " + erpOrder.getOrderConsignInfo().getAddress();
                formSEOrder.setDeliveryAddress(address);// 交货地址
                formSEOrder.setDeliverPhone(erpOrder.getOrderConsignInfo().getConsigneePhone());// 收货人电话
                String payMode = key.endsWith("F") ? "01" : key.endsWith("E") ? "03" : null;
                formSEOrder.setPayMethodNumber(payMode);//  01	先付后用 02	先付后用(货到付款) 03	先用后付


                /// <summary>
                /// 分公司
                /// 00	总部
                /// 01	深圳
                /// 02	北京
                /// 03	上海
                /// 04	广州
                /// 05	武汉
                /// 06	南京
                /// 07	成都
                /// 08	厦门
                /// 10	电销
                /// 20	生产中心
                /// </summary>

                SubCompanyDO subCompanyDO = subCompanyMapper.findById(erpOrder.getOrderSubCompanyId());
                K3MappingSubCompanyDO k3MappingSubCompanyDO = k3MappingSubCompanyMapper.findByErpCode(subCompanyDO.getSubCompanyCode());
                formSEOrder.setCompanyNumber(k3MappingSubCompanyDO.getK3SubCompanyCode());//

                formSEOrder.setAreaPS("租赁");// 销售/租赁
                //对账部门代码填写订单部门
                formSEOrder.setAcctDeptNumber(formSEOrder.getDeptNumber());// 对账部门代码
                formSEOrder.setAcctDeptName(formSEOrder.getDeptName());// 对账部门
//        formSEOrder.setAcctDeptNumber("01.06");// 对账部门代码
//        formSEOrder.setAcctDeptName("深圳-租赁业务部");// 对账部门
                if (erpOrder.getHighTaxRate() != null && erpOrder.getHighTaxRate() > 0) {
                    formSEOrder.setInvoiceType("01");// 01:专票/02:普票/03:收据
                } else {
                    formSEOrder.setInvoiceType("02");
                }


                int orderProductSize = CollectionUtil.isNotEmpty(orderProductList) ? orderProductList.size() : 0;
                int orderMaterialSize = CollectionUtil.isNotEmpty(orderMaterialList) ? orderMaterialList.size() : 0;
                int size = orderProductSize + orderMaterialSize;
                int index = 0;
                if (size > 0) {
                    Calendar startTime = Calendar.getInstance();
                    startTime.setTime(erpOrder.getRentStartTime());
                    Calendar endTime = Calendar.getInstance();
                    endTime.setTime(erpOrder.getExpectReturnTime());
                    FormSeorderEntry list[] = new FormSeorderEntry[size];
                    if (CollectionUtil.isNotEmpty(orderProductList)) {
                        for (OrderProduct orderProduct : orderProductList) {
                            ProductDO productDO = productMapper.findByProductId(orderProduct.getProductId());
                            K3MappingCategoryDO k3MappingCategoryDO = k3MappingCategoryMapper.findByErpCode(productDO.getCategoryId().toString());
                            K3MappingBrandDO k3MappingBrandDO = k3MappingBrandMapper.findByErpCode(productDO.getBrandId().toString());

                            FormICItem formICItem = new FormICItem();
                            formICItem.setModel(productDO.getProductModel());//型号名称
                            formICItem.setName(productDO.getProductName());//商品名称
                            String number = "10." + k3MappingCategoryDO.getK3CategoryCode() + "." + k3MappingBrandDO.getK3BrandCode() + "." + productDO.getProductModel();
                            FormSeorderEntry formSeorderEntry = new FormSeorderEntry();

                            formSeorderEntry.setNumber(number);//  设备代码
                            formSeorderEntry.setName(productDO.getProductName());//  设备名称
                            formSeorderEntry.setQty(new BigDecimal(orderProduct.getProductCount()));// 数量
                            if (OrderRentType.RENT_TYPE_DAY.equals(orderProduct.getRentType())) {
                                formSeorderEntry.setLeaseMonthCount(BigDecimal.ZERO);//  租赁月数
                            } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderProduct.getRentType())) {
                                formSeorderEntry.setLeaseMonthCount(new BigDecimal(orderProduct.getRentTimeLength()));//  租赁月数
                            }
                            formSeorderEntry.setPrice(orderProduct.getProductUnitAmount());//  含税单价
                            //计算平均税率
                            BigDecimal rate = BigDecimalUtil.add(BigDecimalUtil.mul(new BigDecimal(erpOrder.getHighTaxRate()), new BigDecimal(0.17d)), BigDecimalUtil.mul(new BigDecimal(erpOrder.getLowTaxRate()), new BigDecimal(0.06d)));
                            formSeorderEntry.setAddRate(rate);//  税率
                            formSeorderEntry.setAmount(orderProduct.getProductAmount());//  含税租赁金额

                            formSeorderEntry.setDate(startTime);//  租赁开始日期
                            formSeorderEntry.setEndDate(endTime);//  租赁截止日期
                            formSeorderEntry.setYJMonthCount(new BigDecimal(orderProduct.getDepositCycle()));//  押金月数
                            formSeorderEntry.setSFMonthCount(new BigDecimal(orderProduct.getPaymentCycle()));//  首付月数
                            formSeorderEntry.setPayMonthCount(new BigDecimal(orderProduct.getPaymentCycle()));// 付款月数
                            formSeorderEntry.setSFAmount(orderProduct.getFirstNeedPayAmount());//  首付租金
                            //暂时与设备配置名称用一个值
                            formSeorderEntry.setEQConfigNumber(orderProduct.getProductSkuName());//  设备配置代码
                            formSeorderEntry.setEQConfigName(orderProduct.getProductSkuName());//  设备配置名称
                            formSeorderEntry.setStartDate(startTime);//  起算日期
                            formSeorderEntry.setYJAmount(BigDecimalUtil.add(orderProduct.getDepositAmount(), orderProduct.getRentDepositAmount()));//  押金金额
                            //todo 单项的首付和首付合计有什么区别
                            formSeorderEntry.setPayAmountTotal(new BigDecimal(1170));// 首付合计
                            ProductSkuDO productSkuDO = productSkuMapper.findById(orderProduct.getProductSkuId());
                            formSeorderEntry.setEQPrice(productSkuDO.getSkuPrice());//  单台设备价值
                            formSeorderEntry.setEQAmount(BigDecimalUtil.mul(productSkuDO.getSkuPrice(), new BigDecimal(orderProduct.getProductCount())));//  设备价值
                            formSeorderEntry.setSupplyNumber("");//  同行供应商
                            if (OrderRentType.RENT_TYPE_DAY.equals(orderProduct.getRentType())) {
                                formSeorderEntry.setStdPrice(productSkuDO.getDayRentPrice());//  设备标准租金
                            } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderProduct.getRentType())) {
                                formSeorderEntry.setStdPrice(productSkuDO.getMonthRentPrice());//  设备标准租金
                            }
                            if (CommonConstant.COMMON_CONSTANT_YES.equals(orderProduct.getIsNewProduct())) {
                                formSeorderEntry.setEQType("N");//  新旧属性 N	新   O 次新 （字母）
                            } else if (CommonConstant.COMMON_CONSTANT_NO.equals(orderProduct.getIsNewProduct())) {
                                formSeorderEntry.setEQType("O");
                            }
                            if (StringUtil.isEmpty(orderProduct.getRemark())) {
                                formSeorderEntry.setNote("无");//  备注
                            } else {
                                formSeorderEntry.setNote(orderProduct.getRemark());//  备注
                            }
                            list[index++] = formSeorderEntry;
                        }
                    }
                    if (CollectionUtil.isNotEmpty(orderMaterialList)) {
                        for (OrderMaterial orderMaterial : orderMaterialList) {
                            MaterialDO materialDO = materialMapper.findById(orderMaterial.getMaterialId());
                            K3MappingMaterialTypeDO k3MappingMaterialTypeDO = k3MappingMaterialTypeMapper.findByErpCode(materialDO.getMaterialType().toString());
                            K3MappingBrandDO k3MappingBrandDO = k3MappingBrandMapper.findByErpCode(materialDO.getBrandId().toString());
                            String number = "20." + k3MappingMaterialTypeDO.getK3MaterialTypeCode() + "." + k3MappingBrandDO.getK3BrandCode() + "." + materialDO.getMaterialModel();
                            FormSeorderEntry formSeorderEntry = new FormSeorderEntry();
                            formSeorderEntry.setNumber(number);//  设备代码
                            formSeorderEntry.setName(materialDO.getMaterialName());//  设备名称
                            formSeorderEntry.setQty(new BigDecimal(orderMaterial.getMaterialCount()));// 数量
                            if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterial.getRentType())) {
                                formSeorderEntry.setLeaseMonthCount(BigDecimal.ZERO);//  租赁月数
                            } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderMaterial.getRentType())) {
                                formSeorderEntry.setLeaseMonthCount(new BigDecimal(orderMaterial.getRentTimeLength()));//  租赁月数
                            }
                            formSeorderEntry.setPrice(orderMaterial.getMaterialUnitAmount());//  含税单价
                            //计算平均税率
                            BigDecimal rate = BigDecimalUtil.add(BigDecimalUtil.mul(new BigDecimal(erpOrder.getHighTaxRate()), new BigDecimal(0.17d)), BigDecimalUtil.mul(new BigDecimal(erpOrder.getLowTaxRate()), new BigDecimal(0.06d)));

                            formSeorderEntry.setAmount(orderMaterial.getMaterialAmount());//  含税租赁金额
                            formSeorderEntry.setDate(startTime);//  租赁开始日期
                            formSeorderEntry.setEndDate(endTime);//  租赁截止日期
                            formSeorderEntry.setYJMonthCount(new BigDecimal(orderMaterial.getDepositCycle()));//  押金月数
                            formSeorderEntry.setSFMonthCount(new BigDecimal(orderMaterial.getPaymentCycle()));//  首付月数
                            formSeorderEntry.setPayMonthCount(new BigDecimal(orderMaterial.getPaymentCycle()));// 付款月数
                            formSeorderEntry.setSFAmount(orderMaterial.getFirstNeedPayAmount());//  首付租金
                            formSeorderEntry.setEQConfigNumber("");//  设备配置代码
                            formSeorderEntry.setEQConfigName("");//  设备配置名称
                            formSeorderEntry.setStartDate(startTime);//  起算日期
                            formSeorderEntry.setYJAmount(BigDecimalUtil.add(orderMaterial.getDepositAmount(), orderMaterial.getRentDepositAmount()));//  押金金额
                            //todo 单项的首付和首付合计有什么区别
                            formSeorderEntry.setPayAmountTotal(new BigDecimal(0));// 首付合计
                            formSeorderEntry.setAddRate(rate);//  税率
                            formSeorderEntry.setEQPrice(materialDO.getMaterialPrice());//  单台设备价值
                            formSeorderEntry.setEQAmount(BigDecimalUtil.mul(new BigDecimal(orderMaterial.getMaterialCount()), materialDO.getMaterialPrice()));//  设备价值
                            formSeorderEntry.setSupplyNumber("");//  同行供应商
                            if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterial.getRentType())) {
                                formSeorderEntry.setStdPrice(materialDO.getDayRentPrice());//  设备标准租金
                            } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderMaterial.getRentType())) {
                                formSeorderEntry.setStdPrice(materialDO.getMonthRentPrice());//  设备标准租金
                            }
                            if (CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterial.getIsNewMaterial())) {
                                formSeorderEntry.setEQType("N");//  新旧属性 N	新   O 次新 （字母）
                            } else if (CommonConstant.COMMON_CONSTANT_NO.equals(orderMaterial.getIsNewMaterial())) {
                                formSeorderEntry.setEQType("O");
                            }
                            if (StringUtil.isEmpty(orderMaterial.getRemark())) {
                                formSeorderEntry.setNote("无");//  备注
                            } else {
                                formSeorderEntry.setNote(orderMaterial.getRemark());//  备注
                            }
                            list[index++] = formSeorderEntry;
                        }
                    }
                    formSEOrder.setEntrys(list);
                }
                formSEOrderList.add(formSEOrder);
            }
        }
        if (orderMaterialMap.size() > 0) {
            for (Map.Entry<String, List<OrderMaterial>> entry : orderMaterialMap.entrySet()) {
                String key = entry.getKey();
                // 如果配件有同种类型的一并取
                List<OrderMaterial> orderMaterialList = entry.getValue();
                if (CollectionUtil.isEmpty(orderMaterialList)) {
                    continue;
                }
                FormSEOrder formSEOrder = new FormSEOrder();
                if (PostK3OperatorType.POST_K3_OPERATOR_TYPE_UPDATE.equals(postK3OperatorType)) {
                    formSEOrder.setIsReplace(true);// 是否覆盖
                } else if (PostK3OperatorType.POST_K3_OPERATOR_TYPE_ADD.equals(postK3OperatorType)) {
                    formSEOrder.setIsReplace(false);
                }
                K3MappingCustomerDO k3MappingCustomerDO = k3MappingCustomerMapper.findByErpCode(erpOrder.getBuyerCustomerNo());
                if (k3MappingCustomerDO == null) {
                    CustomerDO customerDO = customerMapper.findByNo(erpOrder.getBuyerCustomerNo());
                    Integer subCompanyId = userSupport.getCompanyIdByUser(customerDO.getOwner());
                    SubCompanyDO subCompanyDO = subCompanyMapper.findById(subCompanyId);
                    String cityCode = k3Support.getK3CityCode(subCompanyDO.getSubCompanyCode());
                    k3MappingCustomerDO = new K3MappingCustomerDO();
                    k3MappingCustomerDO.setCustomerName(customerDO.getCustomerName());
                    k3MappingCustomerDO.setErpCustomerCode(customerDO.getCustomerNo());

                    String customerNumber = cityCode + "." + customerDO.getId();
                    k3MappingCustomerDO.setK3CustomerCode(customerNumber);
                    k3MappingCustomerMapper.save(k3MappingCustomerDO);
                    formSEOrder.setCustNumber(customerNumber);// 客户代码
                    formSEOrder.setCustName(customerDO.getCustomerName());// 客户名称
                } else {
                    formSEOrder.setCustNumber(k3MappingCustomerDO.getK3CustomerCode());// 客户代码
                    formSEOrder.setCustName(k3MappingCustomerDO.getCustomerName());// 客户名称
                }

                formSEOrder.setFetchStyleNumber("");
                formSEOrder.setDeptNumber("");
                formSEOrder.setBillNO(erpOrder.getOrderNo() + "-" + key);// 单据编号
                Calendar createTime = Calendar.getInstance();
                createTime.setTime(erpOrder.getCreateTime());
                formSEOrder.setDate(createTime);// 日期
                String fetchStyleNumber = null;
                if (DeliveryMode.DELIVERY_MODE_EXPRESS.equals(erpOrder.getDeliveryMode())) {
                    fetchStyleNumber = "FJH03";
                } else if (DeliveryMode.DELIVERY_MODE_SINCE.equals(erpOrder.getDeliveryMode())) {
                    fetchStyleNumber = "FJH01";
                } else if (DeliveryMode.DELIVERY_MODE_LX_EXPRESS.equals(erpOrder.getDeliveryMode())) {
                    fetchStyleNumber = "FJH02";
                }
                formSEOrder.setFetchStyleNumber(fetchStyleNumber);// 交货方式  FJH01 客户自提 FJH02 送货上门 FJH03 物流发货
                List<RoleDO> roleDOList = roleMapper.findByUserId(erpOrder.getOrderSellerId());
                for (int i = 0; i < roleDOList.size(); i++) {
                    RoleDO roleDO = roleDOList.get(i);
                    K3MappingDepartmentDO k3MappingDepartmentDO = k3MappingDepartmentMapper.findByErpId(roleDO.getDepartmentId());
                    if (i == 0) {
                        if (k3MappingDepartmentDO != null) {
                            formSEOrder.setDeptNumber(k3MappingDepartmentDO.getK3DepartmentCode());// 部门代码
                            formSEOrder.setDeptName(k3MappingDepartmentDO.getDepartmentName());
                        }
                    } else {
                        DepartmentDO departmentDO = departmentMapper.findById(roleDO.getDepartmentId());
                        if (DepartmentType.DEPARTMENT_TYPE_BUSINESS.equals(departmentDO.getDepartmentType())) {
                            formSEOrder.setDeptNumber(k3MappingDepartmentDO.getK3DepartmentCode());// 部门代码
                            formSEOrder.setDeptName(k3MappingDepartmentDO.getSubCompanyName() + "-" + k3MappingDepartmentDO.getDepartmentName());
                        }
                    }
                }

//        formSEOrder.setDeptNumber("01.06");// 部门代码
//        formSEOrder.setDeptName("深圳-租赁业务部");// 部门名称
                Integer subCompanyId = userSupport.getCompanyIdByUser(erpOrder.getOrderSellerId());
                SubCompanyDO sellerSubCompanyDO = subCompanyMapper.findById(subCompanyId);
                String empNumber = k3Support.getK3CityCode(sellerSubCompanyDO.getSubCompanyCode()) + "." + erpOrder.getOrderSellerId();
                formSEOrder.setEmpNumber(empNumber);// 业务员代码

                formSEOrder.setEmpName(erpOrder.getOrderSellerName());// 业务员名称
                formSEOrder.setBillerName(erpOrder.getCreateUserRealName());// 制单人
                //主管用业务员代替
                formSEOrder.setManagerNumber(empNumber);//  主管代码
                formSEOrder.setManagerName(erpOrder.getOrderSellerName());//  主管名称

                //取待发货状态的订单的时间轴节点
                List<OrderTimeAxisDO> orderTimeAxisDOList = orderTimeAxisMapper.findByOrderId(erpOrder.getOrderId());
                if (CollectionUtil.isNotEmpty(orderTimeAxisDOList)) {
                    for (OrderTimeAxisDO orderTimeAxisDO : orderTimeAxisDOList) {
                        if (OrderStatus.ORDER_STATUS_WAIT_DELIVERY.equals(orderTimeAxisDO.getOrderStatus())) {
                            Calendar checkDate = Calendar.getInstance();
                            checkDate.setTime(orderTimeAxisDO.getCreateTime());
                            formSEOrder.setCheckDate(checkDate);// 审核日期
                            User user = CommonCache.userMap.get(Integer.parseInt(String.valueOf(orderTimeAxisDO.getCreateUser())));
                            formSEOrder.setCheckerName(user.getRealName());// 审核人
                        }
                    }
                }


                formSEOrder.setExplanation(erpOrder.getRemark());// 摘要
                String orderTypeNumber = key.startsWith("L") ? "L" : key.startsWith("R") ? "R" : key.startsWith("D") ? "D" : null;
                formSEOrder.setOrderTypeNumber(orderTypeNumber);// 订单类型 L	长租  R	短短租(天) X	销售   D	短租

                formSEOrder.setBusinessTypeNumber("ZY");// 经营类型  ZY	经营性租赁 RZ 融资性租赁
                formSEOrder.setOrderFromNumber("XX");// 订单来源 XS	线上 XX 线下
                formSEOrder.setDeliveryName(erpOrder.getOrderConsignInfo().getConsigneeName());// 提货人
                String address = erpOrder.getOrderConsignInfo().getProvinceName() + " " + erpOrder.getOrderConsignInfo().getCityName() + " " +
                        erpOrder.getOrderConsignInfo().getDistrictName() + " " + erpOrder.getOrderConsignInfo().getAddress();
                formSEOrder.setDeliveryAddress(address);// 交货地址
                formSEOrder.setDeliverPhone(erpOrder.getOrderConsignInfo().getConsigneePhone());// 收货人电话
                String payMode = key.endsWith("F") ? "01" : key.endsWith("E") ? "03" : null;
                formSEOrder.setPayMethodNumber(payMode);//  01	先付后用 02	先付后用(货到付款) 03	先用后付

                /// <summary>
                /// 分公司
                /// 00	总部
                /// 01	深圳
                /// 02	北京
                /// 03	上海
                /// 04	广州
                /// 05	武汉
                /// 06	南京
                /// 07	成都
                /// 08	厦门
                /// 10	电销
                /// 20	生产中心
                /// </summary>

                SubCompanyDO subCompanyDO = subCompanyMapper.findById(erpOrder.getOrderSubCompanyId());
                K3MappingSubCompanyDO k3MappingSubCompanyDO = k3MappingSubCompanyMapper.findByErpCode(subCompanyDO.getSubCompanyCode());
                formSEOrder.setCompanyNumber(k3MappingSubCompanyDO.getK3SubCompanyCode());//

                formSEOrder.setAreaPS("租赁");// 销售/租赁
                //对账部门代码填写订单部门
                formSEOrder.setAcctDeptNumber(formSEOrder.getDeptNumber());// 对账部门代码
                formSEOrder.setAcctDeptName(formSEOrder.getDeptName());// 对账部门
//        formSEOrder.setAcctDeptNumber("01.06");// 对账部门代码
//        formSEOrder.setAcctDeptName("深圳-租赁业务部");// 对账部门
                if (erpOrder.getHighTaxRate() != null && erpOrder.getHighTaxRate() > 0) {
                    formSEOrder.setInvoiceType("01");// 01:专票/02:普票/03:收据
                } else {
                    formSEOrder.setInvoiceType("02");
                }

                int size = CollectionUtil.isNotEmpty(orderMaterialList) ? orderMaterialList.size() : 0;
                int index = 0;
                if (size > 0) {
                    Calendar startTime = Calendar.getInstance();
                    startTime.setTime(erpOrder.getRentStartTime());
                    Calendar endTime = Calendar.getInstance();
                    endTime.setTime(erpOrder.getExpectReturnTime());
                    FormSeorderEntry list[] = new FormSeorderEntry[size];
                    if (CollectionUtil.isNotEmpty(orderMaterialList)) {
                        for (OrderMaterial orderMaterial : orderMaterialList) {
                            MaterialDO materialDO = materialMapper.findById(orderMaterial.getMaterialId());
                            K3MappingMaterialTypeDO k3MappingMaterialTypeDO = k3MappingMaterialTypeMapper.findByErpCode(materialDO.getMaterialType().toString());
                            K3MappingBrandDO k3MappingBrandDO = k3MappingBrandMapper.findByErpCode(materialDO.getBrandId().toString());
                            String number = "20." + k3MappingMaterialTypeDO.getK3MaterialTypeCode() + "." + k3MappingBrandDO.getK3BrandCode() + "." + materialDO.getMaterialModel();
                            FormSeorderEntry formSeorderEntry = new FormSeorderEntry();
                            formSeorderEntry.setNumber(number);//  设备代码
                            formSeorderEntry.setName(materialDO.getMaterialName());//  设备名称
                            formSeorderEntry.setQty(new BigDecimal(orderMaterial.getMaterialCount()));// 数量
                            if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterial.getRentType())) {
                                formSeorderEntry.setLeaseMonthCount(BigDecimal.ZERO);//  租赁月数
                            } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderMaterial.getRentType())) {
                                formSeorderEntry.setLeaseMonthCount(new BigDecimal(orderMaterial.getRentTimeLength()));//  租赁月数
                            }
                            formSeorderEntry.setPrice(orderMaterial.getMaterialUnitAmount());//  含税单价
                            //计算平均税率
                            BigDecimal rate = BigDecimalUtil.add(BigDecimalUtil.mul(new BigDecimal(erpOrder.getHighTaxRate()), new BigDecimal(0.17d)), BigDecimalUtil.mul(new BigDecimal(erpOrder.getLowTaxRate()), new BigDecimal(0.06d)));

                            formSeorderEntry.setAmount(orderMaterial.getMaterialAmount());//  含税租赁金额
                            formSeorderEntry.setDate(startTime);//  租赁开始日期
                            formSeorderEntry.setEndDate(endTime);//  租赁截止日期
                            formSeorderEntry.setYJMonthCount(new BigDecimal(orderMaterial.getDepositCycle()));//  押金月数
                            formSeorderEntry.setSFMonthCount(new BigDecimal(orderMaterial.getPaymentCycle()));//  首付月数
                            formSeorderEntry.setPayMonthCount(new BigDecimal(orderMaterial.getPaymentCycle()));// 付款月数
                            formSeorderEntry.setSFAmount(orderMaterial.getFirstNeedPayAmount());//  首付租金
                            formSeorderEntry.setEQConfigNumber("");//  设备配置代码
                            formSeorderEntry.setEQConfigName("");//  设备配置名称
                            formSeorderEntry.setStartDate(startTime);//  起算日期
                            formSeorderEntry.setYJAmount(BigDecimalUtil.add(orderMaterial.getDepositAmount(), orderMaterial.getRentDepositAmount()));//  押金金额
                            //todo 单项的首付和首付合计有什么区别
                            formSeorderEntry.setPayAmountTotal(new BigDecimal(0));// 首付合计
                            formSeorderEntry.setAddRate(rate);//  税率
                            formSeorderEntry.setEQPrice(materialDO.getMaterialPrice());//  单台设备价值
                            formSeorderEntry.setEQAmount(BigDecimalUtil.mul(new BigDecimal(orderMaterial.getMaterialCount()), materialDO.getMaterialPrice()));//  设备价值
                            formSeorderEntry.setSupplyNumber("");//  同行供应商
                            if (OrderRentType.RENT_TYPE_DAY.equals(orderMaterial.getRentType())) {
                                formSeorderEntry.setStdPrice(materialDO.getDayRentPrice());//  设备标准租金
                            } else if (OrderRentType.RENT_TYPE_MONTH.equals(orderMaterial.getRentType())) {
                                formSeorderEntry.setStdPrice(materialDO.getMonthRentPrice());//  设备标准租金
                            }
                            if (CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterial.getIsNewMaterial())) {
                                formSeorderEntry.setEQType("N");//  新旧属性 N	新   O 次新 （字母）
                            } else if (CommonConstant.COMMON_CONSTANT_NO.equals(orderMaterial.getIsNewMaterial())) {
                                formSeorderEntry.setEQType("O");
                            }
                            if (StringUtil.isEmpty(orderMaterial.getRemark())) {
                                formSeorderEntry.setNote("无");//  备注
                            } else {
                                formSeorderEntry.setNote(orderMaterial.getRemark());//  备注
                            }
                            list[index++] = formSeorderEntry;
                        }
                    }
                    formSEOrder.setEntrys(list);
                }
                formSEOrderList.add(formSEOrder);
            }
        }
        return formSEOrderList;
    }

    @Autowired
    private K3Support k3Support;
    @Autowired
    private SubCompanyMapper subCompanyMapper;
    @Autowired
    private K3MappingSubCompanyMapper k3MappingSubCompanyMapper;
    @Autowired
    private K3MappingCategoryMapper k3MappingCategoryMapper;
    @Autowired
    private K3MappingBrandMapper k3MappingBrandMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private K3MappingMaterialTypeMapper k3MappingMaterialTypeMapper;
    @Autowired
    private K3MappingCustomerMapper k3MappingCustomerMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private K3MappingDepartmentMapper k3MappingDepartmentMapper;
    @Autowired
    private OrderTimeAxisMapper orderTimeAxisMapper;
}
