package com.lxzl.erp.core.service.k3.converter.impl;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowLink;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
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
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.customer.CustomerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.k3.*;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.erp.dataaccess.domain.k3.*;
import com.lxzl.erp.dataaccess.domain.material.MaterialDO;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.se.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;


@Service
public class K3OrderConverter implements ConvertK3DataService {

    @Override
    public Object getK3PostWebServiceData(Integer postK3OperatorType , Object data) {

        Order erpOrder = (Order)data;
        ServiceResult<String,Order> orderResult = orderService.queryOrderByNo(erpOrder.getOrderNo());
        erpOrder = orderResult.getResult();
        FormSEOrder formSEOrder=new FormSEOrder();
        if(PostK3OperatorType.POST_K3_OPERATOR_TYPE_UPDATE.equals(postK3OperatorType)){
            formSEOrder.setIsReplace(true);// 是否覆盖
        }else if(PostK3OperatorType.POST_K3_OPERATOR_TYPE_ADD.equals(postK3OperatorType)){
            formSEOrder.setIsReplace(false);
        }
        K3MappingCustomerDO k3MappingCustomerDO = k3MappingCustomerMapper.findByErpCode(erpOrder.getBuyerCustomerNo());
        if(k3MappingCustomerDO==null){
            CustomerDO customerDO = customerMapper.findByNo(erpOrder.getBuyerCustomerNo());
            Integer subCompanyId = userSupport.getCompanyIdByUser(customerDO.getOwner());
            SubCompanyDO subCompanyDO = subCompanyMapper.findById(subCompanyId);
            String cityCode = k3Support.getK3CityCode(subCompanyDO.getSubCompanyCode());
            k3MappingCustomerDO = new K3MappingCustomerDO();
            k3MappingCustomerDO.setCustomerName(customerDO.getCustomerName());
            k3MappingCustomerDO.setErpCustomerCode(customerDO.getCustomerNo());

            String customerNumber = cityCode + "."+customerDO.getCustomerNo();
            k3MappingCustomerDO.setK3CustomerCode(customerNumber);
            k3MappingCustomerMapper.save(k3MappingCustomerDO);
            formSEOrder.setCustNumber(customerNumber);// 客户代码
            formSEOrder.setCustName(customerDO.getCustomerName());// 客户名称
        }else{
            formSEOrder.setCustNumber(k3MappingCustomerDO.getK3CustomerCode());// 客户代码
            formSEOrder.setCustName(k3MappingCustomerDO.getCustomerName());// 客户名称
        }

        formSEOrder.setFetchStyleNumber("");
        formSEOrder.setDeptNumber("");
        formSEOrder.setBillNO(erpOrder.getOrderNo());// 单据编号
        Calendar createTime= Calendar.getInstance();
        createTime.setTime(erpOrder.getCreateTime());
        formSEOrder.setDate(createTime);// 日期
        String fetchStyleNumber = null;
        if(DeliveryMode.DELIVERY_MODE_EXPRESS.equals(erpOrder.getDeliveryMode())){
            fetchStyleNumber = "FJH03";
        }else if(DeliveryMode.DELIVERY_MODE_SINCE.equals(erpOrder.getDeliveryMode())){
            fetchStyleNumber = "FJH01";
        }else if(DeliveryMode.DELIVERY_MODE_LX_EXPRESS.equals(erpOrder.getDeliveryMode())){
            fetchStyleNumber = "FJH02";
        }
        formSEOrder.setFetchStyleNumber(fetchStyleNumber);// 交货方式  FJH01 客户自提 FJH02 送货上门 FJH03 物流发货
        //todo 部门使用制单人部门还是业务员部门，一个人有两个部门的时候，该字段怎么填
        formSEOrder.setDeptNumber("01.06");// 部门代码
        formSEOrder.setDeptName("深圳-租赁业务部");// 部门名称
        //todo 业务代码是什么
        formSEOrder.setEmpNumber("00.0001");// 业务代码

        formSEOrder.setEmpName(erpOrder.getOrderSellerName());// 业务员名称
        formSEOrder.setBillerName(erpOrder.getCreateUserRealName());// 制单人
        //todo 我们取不到主管
        formSEOrder.setManagerNumber("00.0001");//  主管代码
        formSEOrder.setManagerName("胡祚雄");//  主管名称
        ServiceResult<String ,WorkflowLink> workflowLinkServiceResult = workflowService.getWorkflowLink(WorkflowType.WORKFLOW_TYPE_ORDER_INFO,erpOrder.getOrderNo());
        if(ErrorCode.SUCCESS.equals(workflowLinkServiceResult.getErrorCode())){
            WorkflowLink workflowLink = workflowLinkServiceResult.getResult();
            Calendar checkDate= Calendar.getInstance();
            checkDate.setTime(workflowLink.getUpdateTime());
            //todo 不用审核的话是否需要审核日期及审核人
            formSEOrder.setCheckDate(new GregorianCalendar(2018,1,28));// 审核日期
            formSEOrder.setCheckerName(workflowLink.getCurrentVerifyUserName());// 审核人
        }else{

        }

        formSEOrder.setExplanation(erpOrder.getRemark());// 摘要
        //todo 不知道怎么给,我们之分长租和短租
        formSEOrder.setOrderTypeNumber("L");// 订单类型 L	长租  R	短短租(天) X	销售   D	短租
        formSEOrder.setBusinessTypeNumber("ZY");// 经营类型  ZY	经营性租赁 RZ 融资性租赁
        formSEOrder.setOrderFromNumber("XX");// 订单来源 XS	线上 XX 线下
        formSEOrder.setDeliveryName(erpOrder.getOrderConsignInfo().getConsigneeName());// 提货人
        String address = erpOrder.getOrderConsignInfo().getProvinceName()+" "+erpOrder.getOrderConsignInfo().getCityName()+" "+
                erpOrder.getOrderConsignInfo().getDistrictName() + " "+erpOrder.getOrderConsignInfo().getAddress();
        formSEOrder.setDeliveryAddress(address);// 交货地址
        formSEOrder.setDeliverPhone(erpOrder.getOrderConsignInfo().getConsigneePhone());// 收货人电话
        //todo 我们先付信息是订单项信息
        formSEOrder.setPayMethodNumber("01");//  01	先付后用 02	先付后用(货到付款) 03	先用后付


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
        //todo 对账部门代码及对账部门的填写
        formSEOrder.setAcctDeptNumber("01.06");// 对账部门代码
        formSEOrder.setAcctDeptName("深圳-租赁业务部");// 对账部门
        if(erpOrder.getHighTaxRate()!=null&&erpOrder.getHighTaxRate()>0){
            formSEOrder.setInvoiceType("01");// 01:专票/02:普票/03:收据
        }else{
            formSEOrder.setInvoiceType("02");
        }


        int orderProductSize = CollectionUtil.isNotEmpty(erpOrder.getOrderProductList())? erpOrder.getOrderProductList().size():0;
        int orderMaterialSize = CollectionUtil.isNotEmpty(erpOrder.getOrderMaterialList())? erpOrder.getOrderMaterialList().size():0;
        int size = orderProductSize + orderMaterialSize;
        int index = 0;
        if(size>0){
            Calendar startTime = Calendar.getInstance();
            startTime.setTime(erpOrder.getRentStartTime());
            Calendar endTime = Calendar.getInstance();
            endTime.setTime(erpOrder.getExpectReturnTime());
            FormSeorderEntry list[]=new FormSeorderEntry[size];
            if(CollectionUtil.isNotEmpty(erpOrder.getOrderProductList())){
                for(OrderProduct orderProduct : erpOrder.getOrderProductList()){
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
                    if(OrderRentType.RENT_TYPE_DAY.equals(orderProduct.getRentType()))
                    {
                        formSeorderEntry.setLeaseMonthCount(BigDecimal.ZERO);//  租赁月数
                    }else if(OrderRentType.RENT_TYPE_MONTH.equals(orderProduct.getRentType())){
                        formSeorderEntry.setLeaseMonthCount(new BigDecimal(orderProduct.getRentTimeLength()));//  租赁月数
                    }
                    formSeorderEntry.setPrice(orderProduct.getProductUnitAmount());//  含税单价
                    //todo 我们税率是两种可以并存的，这样给数据会有偏差
                    if(erpOrder.getHighTaxRate()!=null&&erpOrder.getHighTaxRate()>0){
                        formSeorderEntry.setAddRate(new BigDecimal(17) );//  税率
                    }else{
                        formSeorderEntry.setAddRate(new BigDecimal(6) );//  税率
                    }
                    formSeorderEntry.setAmount(orderProduct.getProductAmount());//  含税租赁金额

                    formSeorderEntry.setDate(startTime);//  租赁开始日期
                    formSeorderEntry.setEndDate(endTime);//  租赁截止日期
                    formSeorderEntry.setYJMonthCount(new BigDecimal(orderProduct.getDepositCycle()));//  押金月数
                    formSeorderEntry.setSFMonthCount(new BigDecimal(orderProduct.getPaymentCycle()));//  首付月数
                    formSeorderEntry.setPayMonthCount(new BigDecimal(orderProduct.getPaymentCycle()) );// 付款月数
                    formSeorderEntry.setSFAmount(orderProduct.getFirstNeedPayAmount());//  首付租金
                    //todo 暂时与设备配置名称用一个值
                    formSeorderEntry.setEQConfigNumber(orderProduct.getProductSkuName());//  设备配置代码
                    formSeorderEntry.setEQConfigName(orderProduct.getProductSkuName());//  设备配置名称
                    formSeorderEntry.setStartDate(startTime);//  起算日期
                    formSeorderEntry.setYJAmount(BigDecimalUtil.add(orderProduct.getDepositAmount(),orderProduct.getRentDepositAmount()));//  押金金额
                    //todo 单项的首付和首付合计有什么区别
                    formSeorderEntry.setPayAmountTotal(new BigDecimal(1170));// 首付合计
                    ProductSkuDO productSkuDO = productSkuMapper.findById(orderProduct.getProductSkuId());
                    formSeorderEntry.setEQPrice(productSkuDO.getSkuPrice());//  单台设备价值
                    formSeorderEntry.setEQAmount( BigDecimalUtil.mul(productSkuDO.getSkuPrice(),new BigDecimal(orderProduct.getProductCount())));//  设备价值
                    formSeorderEntry.setSupplyNumber("");//  同行供应商
                    if(OrderRentType.RENT_TYPE_DAY.equals(orderProduct.getRentType())){
                        formSeorderEntry.setStdPrice(productSkuDO.getDayRentPrice());//  设备标准租金
                    }else if(OrderRentType.RENT_TYPE_MONTH.equals(orderProduct.getRentType())){
                        formSeorderEntry.setStdPrice(productSkuDO.getMonthRentPrice());//  设备标准租金
                    }
                    if(CommonConstant.COMMON_CONSTANT_YES.equals(orderProduct.getIsNewProduct())){
                        formSeorderEntry.setEQType("N");//  新旧属性 N	新   O 次新 （字母）
                    }else if(CommonConstant.COMMON_CONSTANT_NO.equals(orderProduct.getIsNewProduct())){
                        formSeorderEntry.setEQType("O");
                    }
                    if(StringUtil.isEmpty(orderProduct.getRemark())){
                        formSeorderEntry.setNote("无");//  备注
                    }else{
                        formSeorderEntry.setNote(orderProduct.getRemark());//  备注
                    }
                    list[index++] = formSeorderEntry;
                }
            }
            if(CollectionUtil.isNotEmpty(erpOrder.getOrderMaterialList())){
                for(OrderMaterial orderMaterial : erpOrder.getOrderMaterialList()){
                    MaterialDO materialDO = materialMapper.findById(orderMaterial.getMaterialId());
                    K3MappingMaterialTypeDO k3MappingMaterialTypeDO = k3MappingMaterialTypeMapper.findByErpCode(materialDO.getMaterialType().toString());
                    K3MappingBrandDO k3MappingBrandDO = k3MappingBrandMapper.findByErpCode(materialDO.getBrandId().toString());
                    String number = "20." + k3MappingMaterialTypeDO.getK3MaterialTypeCode() + "." + k3MappingBrandDO.getK3BrandCode() + "." + materialDO.getMaterialModel();
                    FormSeorderEntry formSeorderEntry=new FormSeorderEntry();
                    formSeorderEntry.setNumber(number);//  设备代码
                    formSeorderEntry.setName(materialDO.getMaterialName());//  设备名称
                    formSeorderEntry.setQty(new BigDecimal(orderMaterial.getMaterialCount()));// 数量
                    if(OrderRentType.RENT_TYPE_DAY.equals(orderMaterial.getRentType()))
                    {
                        formSeorderEntry.setLeaseMonthCount(BigDecimal.ZERO);//  租赁月数
                    }else if(OrderRentType.RENT_TYPE_MONTH.equals(orderMaterial.getRentType())){
                        formSeorderEntry.setLeaseMonthCount(new BigDecimal(orderMaterial.getRentTimeLength()));//  租赁月数
                    }
                    formSeorderEntry.setPrice(orderMaterial.getMaterialUnitAmount());//  含税单价
                    //todo 我们税率是两种可以并存的，这样给数据会有偏差
                    if(erpOrder.getHighTaxRate()!=null&&erpOrder.getHighTaxRate()>0){
                        formSeorderEntry.setAddRate(new BigDecimal(17) );//  税率
                    }else{
                        formSeorderEntry.setAddRate(new BigDecimal(6) );//  税率
                    }
                    formSeorderEntry.setAmount(orderMaterial.getMaterialAmount());//  含税租赁金额
                    formSeorderEntry.setDate(startTime);//  租赁开始日期
                    formSeorderEntry.setEndDate(endTime);//  租赁截止日期
                    formSeorderEntry.setYJMonthCount(new BigDecimal(orderMaterial.getDepositCycle()));//  押金月数
                    formSeorderEntry.setSFMonthCount(new BigDecimal(orderMaterial.getPaymentCycle()));//  首付月数
                    formSeorderEntry.setPayMonthCount(new BigDecimal(orderMaterial.getPaymentCycle()) );// 付款月数
                    formSeorderEntry.setSFAmount(orderMaterial.getFirstNeedPayAmount());//  首付租金
                    formSeorderEntry.setEQConfigNumber("");//  设备配置代码
                    formSeorderEntry.setEQConfigName("");//  设备配置名称
                    formSeorderEntry.setStartDate( startTime);//  起算日期
                    formSeorderEntry.setYJAmount(BigDecimalUtil.add(orderMaterial.getDepositAmount(),orderMaterial.getRentDepositAmount()));//  押金金额
                    //todo 单项的首付和首付合计有什么区别
                    formSeorderEntry.setPayAmountTotal(new BigDecimal(0));// 首付合计

                    formSeorderEntry.setEQPrice(materialDO.getMaterialPrice());//  单台设备价值
                    formSeorderEntry.setEQAmount(BigDecimalUtil.mul(new BigDecimal(orderMaterial.getMaterialCount()),materialDO.getMaterialPrice()));//  设备价值
                    formSeorderEntry.setSupplyNumber("");//  同行供应商
                    if(OrderRentType.RENT_TYPE_DAY.equals(orderMaterial.getRentType())){
                        formSeorderEntry.setStdPrice(materialDO.getDayRentPrice());//  设备标准租金
                    }else if(OrderRentType.RENT_TYPE_MONTH.equals(orderMaterial.getRentType())){
                        formSeorderEntry.setStdPrice(materialDO.getMonthRentPrice());//  设备标准租金
                    }
                    if(CommonConstant.COMMON_CONSTANT_YES.equals(orderMaterial.getIsNewMaterial())){
                        formSeorderEntry.setEQType("N");//  新旧属性 N	新   O 次新 （字母）
                    }else if(CommonConstant.COMMON_CONSTANT_NO.equals(orderMaterial.getIsNewMaterial())){
                        formSeorderEntry.setEQType("O");
                    }
                    if(StringUtil.isEmpty(orderMaterial.getRemark())){
                        formSeorderEntry.setNote("无");//  备注
                    }else{
                        formSeorderEntry.setNote(orderMaterial.getRemark());//  备注
                    }
                    list[index++] = formSeorderEntry;
                }
            }
            formSEOrder.setEntrys(list);
        }
        return formSEOrder;
    }

    @Autowired
    private K3Support k3Support;
    @Autowired
    private WorkflowService workflowService;
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
}
