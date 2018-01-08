package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.ERPTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.PurchaseType;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductEquipment;
import com.lxzl.erp.common.domain.product.pojo.ProductMaterial;
import com.lxzl.erp.common.domain.purchase.*;
import com.lxzl.erp.common.domain.purchase.pojo.*;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.purchase.PurchaseOrderService;
import com.lxzl.erp.dataaccess.dao.mysql.product.ProductSkuMapper;
import com.lxzl.erp.dataaccess.domain.product.ProductMaterialDO;
import com.lxzl.erp.dataaccess.domain.product.ProductSkuDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderControllerTest extends ERPTransactionalTest {

    @Autowired
    private ProductSkuMapper productSkuMapper;

    /**
     * 测试自动过总公司的采购单
     * 条件：1.没有发票，2.收货库房为分公司，3.整机四大件
     *
     * @throws Exception
     */
    @Test
    public void addPurchaseOrder() throws Exception {

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setWarehouseNo("LXW07551");//仓库编号必填,这里为分公司
        purchaseOrder.setIsInvoice(CommonConstant.COMMON_CONSTANT_NO);//是否有发票字段必填
        purchaseOrder.setIsNew(CommonConstant.COMMON_CONSTANT_NO);//是否是全新机
        purchaseOrder.setPurchaseType(PurchaseType.PURCHASE_TYPE_ALL_OR_MAIN);
        purchaseOrder.setProductSupplierId(1);//商品供应商ID不能为空
        purchaseOrder.setTaxRate(0.22);

        List<PurchaseOrderProduct> purchaseOrderProductList = new ArrayList<>();//采购单商品项列表
//        purchaseOrderProductList.add(createPurchaseOrderProduct(65,100,new BigDecimal(5000)));
//        purchaseOrderProductList.add(createPurchaseOrderProduct(64,100,new BigDecimal(2200)));
        purchaseOrderProductList.add(createPurchaseOrderProduct(63, 100, new BigDecimal(2200)));
        purchaseOrder.setPurchaseOrderProductList(purchaseOrderProductList);

        List<PurchaseOrderMaterial> purchaseOrderMaterialList = new ArrayList<>();//小配件采购单物料项列表不能为空
//        purchaseOrderMaterialList.add(createPurchaseOrderMaterial("M201711201500267591516",100,new BigDecimal(875)));
        purchaseOrder.setPurchaseOrderMaterialList(purchaseOrderMaterialList);

        TestResult testResult = getJsonTestResult("/purchaseOrder/add", purchaseOrder);
    }

    @Test
    public void addPurchaseOrder3() throws Exception {

        PurchaseOrder purchaseOrder = JSON.parseObject("{\"productSupplierId\":\"11\",\"warehouseNo\":\"LXW07551\",\"isInvoice\":\"1\",\"isNew\":\"1\",\"purchaseType\":\"1\",\"taxRate\":\"0\",\"purchaseOrderMaterialList\":[{\"materialNo\":\"M201711171838059981292\",\"materialAmount\":\"500\",\"materialCount\":\"50\"}]}", PurchaseOrder.class);
        TestResult testResult = getJsonTestResult("/purchaseOrder/add", purchaseOrder);
    }

    private PurchaseOrderMaterial createPurchaseOrderMaterial(String materialNo, Integer materialCount, BigDecimal materialAmount) {
        PurchaseOrderMaterial purchaseOrderMaterial = new PurchaseOrderMaterial();
        purchaseOrderMaterial.setMaterialNo(materialNo);
        purchaseOrderMaterial.setMaterialCount(materialCount);
        purchaseOrderMaterial.setMaterialAmount(materialAmount);
        return purchaseOrderMaterial;
    }

    private PurchaseOrderProduct createPurchaseOrderProduct(Integer skuId, Integer productSkuCount, BigDecimal productSkuAmount) {
        ProductSkuDO productSkuDO = productSkuMapper.findById(skuId);
        List<ProductMaterialDO> productMaterialDOList = productSkuDO.getProductMaterialDOList();

        List<ProductMaterial> productMaterialList = new ArrayList<ProductMaterial>();
        for (ProductMaterialDO productMaterialDO : productMaterialDOList) {
            ProductMaterial productMaterial1 = new ProductMaterial();
            BeanUtils.copyProperties(productMaterialDO, productMaterial1);
            productMaterial1.setProductMaterialId(productMaterialDO.getId());
            productMaterialList.add(productMaterial1);
        }

        PurchaseOrderProduct purchaseOrderProduct = new PurchaseOrderProduct();
        purchaseOrderProduct.setProductSkuId(skuId); //采购单商品项SKU UD 不能为空
        purchaseOrderProduct.setProductAmount(productSkuAmount);//采购单商品单价不能为空且大于0
        purchaseOrderProduct.setProductCount(productSkuCount);//采购单商品数量不能为空且大于0
        purchaseOrderProduct.setProductMaterialList(productMaterialList);
        return purchaseOrderProduct;
    }

    /**
     * 测试小配件不过总公司
     * 条件：1.没有发票，2.收货库房为分公司，3.小配件
     *
     * @throws Exception
     */
    @Test
    public void addPurchaseOrder2() throws Exception {

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setWarehouseNo("W201708081508");//仓库编号必填,这里为分公司
        purchaseOrder.setIsInvoice(CommonConstant.COMMON_CONSTANT_NO);//是否有发票字段必填
        purchaseOrder.setIsNew(CommonConstant.COMMON_CONSTANT_NO);//是否是全新机
        purchaseOrder.setPurchaseType(PurchaseType.PURCHASE_TYPE_GADGET);
        purchaseOrder.setProductSupplierId(1);//商品供应商ID不能为空

        List<PurchaseOrderMaterial> purchaseOrderMaterialList = new ArrayList<>();//小配件采购单物料项列表不能为空
        purchaseOrderMaterialList.add(createPurchaseOrderMaterial("M201711171838059981293", 3, new BigDecimal(42)));

        purchaseOrder.setPurchaseOrderMaterialList(purchaseOrderMaterialList);
        TestResult testResult = getJsonTestResult("/purchaseOrder/add", purchaseOrder);
    }

    @Test
    public void updatePurchaseOrder() throws Exception {

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseNo("PO201711172026001085000051604");
        purchaseOrder.setWarehouseNo("W201708081508");//仓库编号必填,这里为分公司
        purchaseOrder.setIsInvoice(CommonConstant.COMMON_CONSTANT_NO);//是否有发票字段必填
        purchaseOrder.setIsNew(CommonConstant.COMMON_CONSTANT_NO);//是否是全新机
        purchaseOrder.setProductSupplierId(1);//商品供应商ID不能为空
        purchaseOrder.setPurchaseType(PurchaseType.PURCHASE_TYPE_ALL_OR_MAIN);

        List<PurchaseOrderProduct> purchaseOrderProductList = new ArrayList<>();//采购单商品项列表不能为空
        PurchaseOrderProduct purchaseOrderProduct = new PurchaseOrderProduct();
        purchaseOrderProduct.setProductSkuId(1); //采购单商品项SKU UD 不能为空
        purchaseOrderProduct.setProductAmount(new BigDecimal(800));//采购单商品单价不能为空且大于0
        purchaseOrderProduct.setProductCount(10);//采购单商品数量不能为空且大于0
        //添加物料配置
        List<ProductMaterial> productMaterialList1 = new ArrayList<ProductMaterial>();
        ProductMaterial productMaterial = new ProductMaterial();
        productMaterial.setMaterialId(1);
        productMaterialList1.add(productMaterial);
        purchaseOrderProduct.setProductMaterialList(productMaterialList1);

        PurchaseOrderProduct purchaseOrderProduct2 = new PurchaseOrderProduct();
        purchaseOrderProduct2.setProductSkuId(2); //采购单商品项SKU UD 不能为空，且不能重复
        purchaseOrderProduct2.setProductAmount(new BigDecimal(2000));//采购单商品单价不能为空且大于0
        purchaseOrderProduct2.setProductCount(8);//采购单商品数量不能为空且大于0
        //添加物料配置
        List<ProductMaterial> productMaterialList2 = new ArrayList<ProductMaterial>();
        productMaterial.setMaterialId(1);
        productMaterialList2.add(productMaterial);
        purchaseOrderProduct2.setProductMaterialList(productMaterialList2);

        purchaseOrderProductList.add(purchaseOrderProduct);
        purchaseOrderProductList.add(purchaseOrderProduct2);

        purchaseOrder.setPurchaseOrderProductList(purchaseOrderProductList);
        TestResult testResult = getJsonTestResult("/purchaseOrder/update", purchaseOrder);
    }

    @Test
    public void updatePurchaseOrder2() throws Exception {

        PurchaseOrder purchaseOrder = JSON.parseObject("{\n" +
                "\t\"purchaseReceiveNo\": \"PR2017121616283882360001101490\",\n" +
                "\t\"isNew\": \"1\",\n" +
                "\t\"purchaseReceiveOrderProductList\": [{\n" +
                "\t\t\"purchaseReceiveOrderProductId\": \"167\",\n" +
                "\t\t\"realProductSkuId\": \"75\",\n" +
                "\t\t\"realProductCount\": \"100\",\n" +
                "\t\t\"remark\": \"\",\n" +
                "\t\t\"productMaterialList\": [{\n" +
                "\t\t\t\"materialNo\": \"M201711211953291591494\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711171838059981292\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711201422478141693\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201712011508312131037\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711291737346371109\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711291912439731417\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"purchaseReceiveOrderProductId\": \"168\",\n" +
                "\t\t\"realProductSkuId\": \"73\",\n" +
                "\t\t\"realProductCount\": \"100\",\n" +
                "\t\t\"remark\": \"\",\n" +
                "\t\t\"productMaterialList\": [{\n" +
                "\t\t\t\"materialNo\": \"M201711211953291591494\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711201356145971009\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711291808329011520\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711201457288791418\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711291737346371109\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711291912439731417\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"purchaseReceiveOrderProductId\": \"169\",\n" +
                "\t\t\"realProductSkuId\": \"77\",\n" +
                "\t\t\"realProductCount\": \"100\",\n" +
                "\t\t\"remark\": \"\",\n" +
                "\t\t\"productMaterialList\": [{\n" +
                "\t\t\t\"materialNo\": \"M201711211953291591494\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711171838059981292\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711291808329011520\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201712011508312131037\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711291737346371109\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711291912439731417\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"purchaseReceiveOrderProductId\": \"170\",\n" +
                "\t\t\"realProductSkuId\": \"71\",\n" +
                "\t\t\"realProductCount\": \"100\",\n" +
                "\t\t\"remark\": \"\",\n" +
                "\t\t\"productMaterialList\": [{\n" +
                "\t\t\t\"materialNo\": \"M201711211953291591494\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711201356145971009\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711201422478141693\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711201457288791418\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711291737346371109\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711291912439731417\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"purchaseReceiveOrderProductId\": \"171\",\n" +
                "\t\t\"realProductSkuId\": \"76\",\n" +
                "\t\t\"realProductCount\": \"100\",\n" +
                "\t\t\"remark\": \"\",\n" +
                "\t\t\"productMaterialList\": [{\n" +
                "\t\t\t\"materialNo\": \"M201711211953291591494\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711171838059981292\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711291808329011520\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711201457288791418\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711291737346371109\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711291912439731417\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"purchaseReceiveOrderProductId\": \"172\",\n" +
                "\t\t\"realProductSkuId\": \"70\",\n" +
                "\t\t\"realProductCount\": \"100\",\n" +
                "\t\t\"remark\": \"\",\n" +
                "\t\t\"productMaterialList\": [{\n" +
                "\t\t\t\"materialNo\": \"M201711171838059981292\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711201422478141693\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711201457288791418\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711291737346371109\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711291912439731417\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711211953291591494\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"purchaseReceiveOrderProductId\": \"173\",\n" +
                "\t\t\"realProductSkuId\": \"74\",\n" +
                "\t\t\"realProductCount\": \"100\",\n" +
                "\t\t\"remark\": \"\",\n" +
                "\t\t\"productMaterialList\": [{\n" +
                "\t\t\t\"materialNo\": \"M201711211953291591494\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711201356145971009\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711291808329011520\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201712011508312131037\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711291737346371109\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711291912439731417\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"purchaseReceiveOrderProductId\": \"174\",\n" +
                "\t\t\"realProductSkuId\": \"72\",\n" +
                "\t\t\"realProductCount\": \"100\",\n" +
                "\t\t\"remark\": \"\",\n" +
                "\t\t\"productMaterialList\": [{\n" +
                "\t\t\t\"materialNo\": \"M201711211953291591494\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711201356145971009\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711201422478141693\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201712011508312131037\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711291737346371109\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"materialNo\": \"M201711291912439731417\",\n" +
                "\t\t\t\"materialCount\": \"1\"\n" +
                "\t\t}]\n" +
                "\t}],\n" +
                "\t\"purchaseReceiveOrderMaterialList\": [{\n" +
                "\t\t\"realMaterialNo\": \"M201711291745413251585\",\n" +
                "\t\t\"realMaterialCount\": \"20\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"realMaterialNo\": \"M201711291744581931681\",\n" +
                "\t\t\"realMaterialCount\": \"20\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"realMaterialNo\": \"M201711291737346371109\",\n" +
                "\t\t\"realMaterialCount\": \"20\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"realMaterialNo\": \"M201711251154081811299\",\n" +
                "\t\t\"realMaterialCount\": \"20\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"realMaterialNo\": \"M201711231853213681267\",\n" +
                "\t\t\"realMaterialCount\": \"20\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"realMaterialNo\": \"M201711212007516281673\",\n" +
                "\t\t\"realMaterialCount\": \"20\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"realMaterialNo\": \"M201711211953291591494\",\n" +
                "\t\t\"realMaterialCount\": \"20\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"realMaterialNo\": \"M201711201501547451898\",\n" +
                "\t\t\"realMaterialCount\": \"20\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"realMaterialNo\": \"M201711201500267591516\",\n" +
                "\t\t\"realMaterialCount\": \"20\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"realMaterialNo\": \"M201711201457288791418\",\n" +
                "\t\t\"realMaterialCount\": \"20\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"realMaterialNo\": \"M201711201422478141693\",\n" +
                "\t\t\"realMaterialCount\": \"20\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"realMaterialNo\": \"M201711201356145971009\",\n" +
                "\t\t\"realMaterialCount\": \"20\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"realMaterialNo\": \"M201711200917156471667\",\n" +
                "\t\t\"realMaterialCount\": \"20\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"realMaterialNo\": \"M201711171838059981293\",\n" +
                "\t\t\"realMaterialCount\": \"20\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"realMaterialNo\": \"M201711171838059981292\",\n" +
                "\t\t\"realMaterialCount\": \"20\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"realMaterialNo\": \"M201712011508312131037\",\n" +
                "\t\t\"realMaterialCount\": \"20\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"realMaterialNo\": \"M201711300902575761564\",\n" +
                "\t\t\"realMaterialCount\": \"20\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"realMaterialNo\": \"M201711291912454131862\",\n" +
                "\t\t\"realMaterialCount\": \"20\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"realMaterialNo\": \"M201711291912439731417\",\n" +
                "\t\t\"realMaterialCount\": \"20\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"realMaterialNo\": \"M201711291911003661287\",\n" +
                "\t\t\"realMaterialCount\": \"20\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"realMaterialNo\": \"M201711291808329011520\",\n" +
                "\t\t\"realMaterialCount\": \"20\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"realMaterialNo\": \"M201711291807447341672\",\n" +
                "\t\t\"realMaterialCount\": \"20\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"realMaterialNo\": \"M201711291753428101534\",\n" +
                "\t\t\"realMaterialCount\": \"20\",\n" +
                "\t\t\"remark\": \"\"\n" +
                "\t}]\n" +
                "}", PurchaseOrder.class);
        TestResult testResult = getJsonTestResult("/purchaseOrder/update", purchaseOrder);
    }


    @Test
    public void commit() throws Exception {
        PurchaseOrderCommitParam purchaseOrderCommitParam = new PurchaseOrderCommitParam();
        purchaseOrderCommitParam.setPurchaseNo("PO201712161659351135000061140");
        purchaseOrderCommitParam.setVerifyUserId(500002);
        purchaseOrderCommitParam.setRemark("给我好好审");
        TestResult testResult = getJsonTestResult("/purchaseOrder/commit", purchaseOrderCommitParam);
    }

    @Test
    public void delete() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
//        purchaseOrder.setPurchaseNo("PO201711181416106215000051764");
        TestResult testResult = getJsonTestResult("/purchaseOrder/delete", purchaseOrder);
    }

    @Test
    public void page() throws Exception {
        PurchaseOrderQueryParam purchaseOrderQueryParam = new PurchaseOrderQueryParam();
//        purchaseOrderQueryParam.setPurchaseNo("PO201711181544591335000051741");
//        purchaseOrderQueryParam.setProductSupplierId(1);
//        purchaseOrderQueryParam.setInvoiceSupplierId(1);
//        purchaseOrderQueryParam.setWarehouseNo("W201708081508");
//        purchaseOrderQueryParam.setIsInvoice(1);
//        purchaseOrderQueryParam.setIsNew(1);
//        purchaseOrderQueryParam.setCreateStartTime(new Date());
//        purchaseOrderQueryParam.setCreateEndTime(new Date());
//        purchaseOrderQueryParam.setPurchaseOrderStatus(PurchaseOrderStatus.PURCHASE_ORDER_STATUS_WAIT_COMMIT);
//        purchaseOrderQueryParam.setCommitStatus(CommonConstant.COMMON_CONSTANT_YES);
        TestResult testResult = getJsonTestResult("/purchaseOrder/page", purchaseOrderQueryParam);
    }

    @Test
    public void queryPurchaseOrderByNo() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseNo("LXPO0755201801050066");
        TestResult testResult = getJsonTestResult("/purchaseOrder/queryPurchaseOrderByNo", purchaseOrder);
    }

    @Test
    public void receiveVerifyTestResult() {
        boolean flag = purchaseOrderService.receiveVerifyResult(true, "");
    }

    @Test
    public void pagePurchaseDelivery() throws Exception {
        PurchaseDeliveryOrderQueryParam purchaseOrderDeliveryQueryParam = new PurchaseDeliveryOrderQueryParam();
//        purchaseOrderDeliveryQueryParam.setPurchaseNo("PO201711181544591335000051741");
//        purchaseOrderDeliveryQueryParam.setWarehouseId(4000001);
//        purchaseOrderDeliveryQueryParam.setIsInvoice(1);
//        purchaseOrderDeliveryQueryParam.setIsNew(1);
//        purchaseOrderDeliveryQueryParam.setCreateEndTime(new Date());
//        purchaseOrderDeliveryQueryParam.setCreateStartTime(new Date());
//        purchaseOrderDeliveryQueryParam.setPurchaseDeliveryOrderStatus(0);
        TestResult testResult = getJsonTestResult("/purchaseOrder/pagePurchaseDelivery", purchaseOrderDeliveryQueryParam);
    }

    @Test
    public void queryPurchaseDeliveryOrderByNo() throws Exception {
        PurchaseDeliveryOrder purchaseDeliveryOrder = new PurchaseDeliveryOrder();
        purchaseDeliveryOrder.setPurchaseDeliveryNo("PD2017111816030427860000471733");
        TestResult testResult = getJsonTestResult("/purchaseOrder/queryPurchaseDeliveryOrderByNo", purchaseDeliveryOrder);
    }

    @Test
    public void updatePurchaseReceiveOrder() throws Exception {
        PurchaseReceiveOrder purchaseReceiveOrder = new PurchaseReceiveOrder();
        purchaseReceiveOrder.setPurchaseReceiveNo("PR2017122613482675460001641534");
        purchaseReceiveOrder.setIsNew(CommonConstant.COMMON_CONSTANT_NO);

        List<PurchaseReceiveOrderProduct> purchaseOrderProductList = new ArrayList<>();

        PurchaseReceiveOrderProduct purchaseReceiveOrderProduct = new PurchaseReceiveOrderProduct();
        purchaseReceiveOrderProduct.setRealProductSkuId(1); //采购单商品项SKU ID 不能为空
        purchaseReceiveOrderProduct.setRealProductCount(5);//采购单商品数量不能为空且大于0
        purchaseOrderProductList.add(purchaseReceiveOrderProduct);


        List<PurchaseReceiveOrderMaterial> purchaseReceiveOrderMaterialList = new ArrayList<>();
        PurchaseReceiveOrderMaterial purchaseReceiveOrderMaterial = new PurchaseReceiveOrderMaterial();
        purchaseReceiveOrderMaterial.setMaterialId(17);
        purchaseReceiveOrderMaterial.setRemark("有3个坏的，17的好的");
        purchaseReceiveOrderMaterialList.add(purchaseReceiveOrderMaterial);


//        PurchaseReceiveOrderProduct purchaseReceiveOrderProduct = new PurchaseReceiveOrderProduct();
//        purchaseReceiveOrderProduct.setRealProductSkuId(2); //采购单商品项SKU ID 不能为空
//        purchaseReceiveOrderProduct.setRealProductCount(5);//采购单商品数量不能为空且大于0
//        purchaseOrderProductList.add(purchaseReceiveOrderProduct);
//
//        PurchaseReceiveOrderProduct purchaseReceiveOrderProduct2 = new PurchaseReceiveOrderProduct();
//        purchaseReceiveOrderProduct2.setRealProductSkuId(3); //采购单商品项SKU ID 不能为空
//        purchaseReceiveOrderProduct2.setRealProductCount(10);//采购单商品数量不能为空且大于0

//        purchaseOrderProductList.add(purchaseReceiveOrderProduct2);
//        purchaseReceiveOrder.setPurchaseReceiveOrderProductList(purchaseOrderProductList);
        purchaseReceiveOrder.setPurchaseReceiveOrderMaterialList(purchaseReceiveOrderMaterialList);

        TestResult testResult = getJsonTestResult("/purchaseOrder/updatePurchaseReceiveOrder", purchaseReceiveOrder);
    }

    @Test
    public void updatePurchaseReceiveOrder1() throws Exception {
        PurchaseReceiveOrder purchaseReceiveOrder = JSON.parseObject("{\"isNew\":1,\"purchaseReceiveNo\":\"LXPR1000201801020002\",\"purchaseReceiveOrderMaterialList\":[{\"realMaterialCount\":12,\"realMaterialNo\":\"M201712181610381101755\",\"remark\":\"\"}]}", PurchaseReceiveOrder.class);
        TestResult testResult = getJsonTestResult("/purchaseOrder/updatePurchaseReceiveOrder", purchaseReceiveOrder);
    }

    @Test
    public void confirmPurchaseReceiveOrder() throws Exception {
        PurchaseReceiveOrder purchaseReceiveOrder = new PurchaseReceiveOrder();
        purchaseReceiveOrder.setPurchaseReceiveNo("LXPR0755201801070185");
        TestResult testResult = getJsonTestResult("/purchaseOrder/confirmPurchaseReceiveOrder", purchaseReceiveOrder);
    }

    @Test
    public void pagePurchaseReceive() throws Exception {
        PurchaseReceiveOrderQueryParam purchaseReceiveOrderQueryParam = new PurchaseReceiveOrderQueryParam();
//        purchaseReceiveOrderQueryParam.setPurchaseNo("PR2017122613482675460001641534");
//        purchaseReceiveOrderQueryParam.setPurchaseOrderId(1);
//        purchaseReceiveOrderQueryParam.setPurchaseDeliveryOrderId(1);
//        purchaseReceiveOrderQueryParam.setWarehouseId(1);
//        purchaseReceiveOrderQueryParam.setProductSupplierId(1);//商品供应商ID
//        purchaseReceiveOrderQueryParam.setAutoAllotStatus(1);//分拨情况，0-未分拨，1-已分拨，2-被分拨，没发票的分公司仓库单，将生成一个总公司收货单，并生成分拨单号，自动分拨到分公司仓库
//        purchaseReceiveOrderQueryParam.setIsInvoice(0);//是否有发票，0否1是
//        purchaseReceiveOrderQueryParam.setIsNew(0);//是否全新机
//        purchaseReceiveOrderQueryParam.setPurchaseReceiveOrderStatus(0);//采购单收货状态，0待收货，1已签单
//
//        purchaseReceiveOrderQueryParam.setPurchaseNo("C201711091550440665000051897");//采购单编号
//        purchaseReceiveOrderQueryParam.setPurchaseDeliveryNo("D2017110915515787360000061537");//发货单编号
//        purchaseReceiveOrderQueryParam.setpurchaseReceiveNo("R2017110915515793660000061795"); //采购收货单编号
//        purchaseReceiveOrderQueryParam.setWarehouseNo("W201708091508"); //仓库编号
//        Calendar createStartTime  = Calendar.getInstance();
//        createStartTime.set(Calendar.DAY_OF_YEAR,-1);
//        Calendar createEndTime  = Calendar.getInstance();
//        Calendar confirmStartTime  = Calendar.getInstance();
//        confirmStartTime.set(Calendar.DAY_OF_YEAR,-1);
//        Calendar confirmEndTime  = Calendar.getInstance();
//        purchaseReceiveOrderQueryParam.setCreateStartTime(createStartTime.getTime());//创建收货单起始时间
//        purchaseReceiveOrderQueryParam.setCreateEndTime(createEndTime.getTime());//创建收货单结束时间
//        purchaseReceiveOrderQueryParam.setConfirmStartTime(confirmStartTime.getTime());//确认签单起始时间
//        purchaseReceiveOrderQueryParam.setConfirmEndTime(confirmEndTime.getTime());//确认签单结束时间
        TestResult testResult = getJsonTestResult("/purchaseOrder/pagePurchaseReceive", purchaseReceiveOrderQueryParam);
    }

    @Test
    public void queryPurchaseReceiveOrderByNo() throws Exception {
        PurchaseReceiveOrder purchaseReceiveOrder = new PurchaseReceiveOrder();
        purchaseReceiveOrder.setPurchaseReceiveNo("LXPR1000201801010001");
        TestResult testResult = getJsonTestResult("/purchaseOrder/queryPurchaseReceiveOrderByNo", purchaseReceiveOrder);
    }

    @Test
    public void endPurchaseOrder() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseNo("LXPO1000201801040041");
        TestResult testResult = getJsonTestResult("/purchaseOrder/endPurchaseOrder", purchaseOrder);
    }

    @Test
    public void continuePurchaseOrder() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseNo("LXPO0755201801050068");
        TestResult testResult = getJsonTestResult("/purchaseOrder/continuePurchaseOrder", purchaseOrder);
    }

    @Test
    public void pageReceiveOrderProductEquipment() throws Exception {
        PurchaseReceiveOrderProductEquipmentPageParam purchaseReceiveOrderProductEquipmentPageParam = new PurchaseReceiveOrderProductEquipmentPageParam();
        purchaseReceiveOrderProductEquipmentPageParam.setPurchaseReceiveOrderProductId(212);
        TestResult testResult = getJsonTestResult("/purchaseOrder/pageReceiveOrderProductEquipment", purchaseReceiveOrderProductEquipmentPageParam);
    }

    @Test
    public void pageReceiveOrderMaterialBulk() throws Exception {
        PurchaseReceiveOrderMaterialBulkPageParam purchaseReceiveOrderMaterialBulkPageParam = new PurchaseReceiveOrderMaterialBulkPageParam();
        purchaseReceiveOrderMaterialBulkPageParam.setPurchaseReceiveOrderMaterialId(134);
        TestResult testResult = getJsonTestResult("/purchaseOrder/pageReceiveOrderMaterialBulk", purchaseReceiveOrderMaterialBulkPageParam);
    }

    @Test
    public void updateReceiveEquipmentRemark() throws Exception {
        UpdateReceiveEquipmentRemarkParam updateReceiveEquipmentRemarkParam = new UpdateReceiveEquipmentRemarkParam();
        ProductEquipment productEquipment = new ProductEquipment();
        productEquipment.setEquipmentNo("LX-EQUIPMENT-4000002-2017122610015");
        productEquipment.setPurchaseReceiveRemark("这是个残次品，评级B");
        updateReceiveEquipmentRemarkParam.setProductEquipment(productEquipment);
        updateReceiveEquipmentRemarkParam.setPurchaseReceiveOrderNo("PR2017122613482675460001641534");
        TestResult testResult = getJsonTestResult("/purchaseOrder/updateReceiveEquipmentRemark", updateReceiveEquipmentRemarkParam);
    }

    @Test
    public void updateReceiveMaterialRemark() throws Exception {
        UpdateReceiveMaterialRemarkParam updateReceiveMaterialRemarkParam = new UpdateReceiveMaterialRemarkParam();

        updateReceiveMaterialRemarkParam.setPurchaseReceiveOrderNo("PR2017122613482675460001641534");
        PurchaseReceiveOrderMaterial purchaseReceiveOrderMaterial = new PurchaseReceiveOrderMaterial();
        purchaseReceiveOrderMaterial.setPurchaseReceiveOrderMaterialId(137);
        purchaseReceiveOrderMaterial.setRemark("有3个坏的,剩下的没问题");
        List<PurchaseReceiveOrderMaterial> purchaseReceiveOrderMaterialList = new ArrayList<>();
        purchaseReceiveOrderMaterialList.add(purchaseReceiveOrderMaterial);
        updateReceiveMaterialRemarkParam.setPurchaseReceiveOrderMaterialList(purchaseReceiveOrderMaterialList);
        TestResult testResult = getJsonTestResult("/purchaseOrder/updateReceiveMaterialRemark", updateReceiveMaterialRemarkParam);
    }

    @Test
    public void commitPurchaseReceiveOrder() throws Exception {
        PurchaseReceiveOrder purchaseReceiveOrder = new PurchaseReceiveOrder();
        purchaseReceiveOrder.setPurchaseReceiveNo("PR2017122613482675460001641534");
        TestResult testResult = getJsonTestResult("/purchaseOrder/commitPurchaseReceiveOrder", purchaseReceiveOrder);
    }

    @Test
    public void strongCancel() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseNo("LXPO1000201801080003");
        TestResult testResult = getJsonTestResult("/purchaseOrder/strongCancel", purchaseOrder);
    }

    @Test
    public void getPurchaseReceiveMaterialPriceList() throws Exception {
        PurchaseReceiveOrderMaterial purchaseReceiveOrderMaterial = new PurchaseReceiveOrderMaterial();
        purchaseReceiveOrderMaterial.setPurchaseReceiveOrderMaterialId(248);
        TestResult testResult = getJsonTestResult("/purchaseOrder/getPurchaseReceiveMaterialPriceList", purchaseReceiveOrderMaterial);
    }

    @Test
    public void updatePurchaseReceiveMaterialPrice() throws Exception {
        UpdatePurchaseReceiveMaterialPriceParam updatePurchaseReceiveMaterialPriceParam = new UpdatePurchaseReceiveMaterialPriceParam();
        updatePurchaseReceiveMaterialPriceParam.setPurchaseReceiveOrderMaterialId(137);
        List<PurchaseReceiveOrderMaterialPrice> purchaseReceiveOrderMaterialPriceList = new ArrayList<>();
        PurchaseReceiveOrderMaterialPrice purchaseReceiveOrderMaterialPrice = new PurchaseReceiveOrderMaterialPrice();
        purchaseReceiveOrderMaterialPrice.setCount(2);
        purchaseReceiveOrderMaterialPrice.setPrice(new BigDecimal(200));
        purchaseReceiveOrderMaterialPriceList.add(purchaseReceiveOrderMaterialPrice);

        PurchaseReceiveOrderMaterialPrice purchaseReceiveOrderMaterialPrice1 = new PurchaseReceiveOrderMaterialPrice();
        purchaseReceiveOrderMaterialPrice1.setCount(18);
        purchaseReceiveOrderMaterialPrice1.setPrice(new BigDecimal(100));
        purchaseReceiveOrderMaterialPriceList.add(purchaseReceiveOrderMaterialPrice1);
        updatePurchaseReceiveMaterialPriceParam.setPurchaseReceiveOrderMaterialPriceList(purchaseReceiveOrderMaterialPriceList);
        TestResult testResult = getJsonTestResult("/purchaseOrder/updatePurchaseReceiveMaterialPrice", updatePurchaseReceiveMaterialPriceParam);
    }

    @Test
    public void test() throws Exception {
        Product product = JSON.parseObject("{\"categoryId\":800003,\"dataStatus\":1,\"isRent\":1,\"listPrice\":100.00,\"productCategoryPropertyList\":[{\"categoryId\":800003,\"categoryPropertyId\":1,\"dataOrder\":11,\"dataStatus\":1,\"isCheckbox\":0,\"isInput\":0,\"isRequired\":0,\"productCategoryPropertyValueList\":[{\"categoryId\":800003,\"categoryPropertyValueId\":2,\"dataOrder\":1,\"dataStatus\":1,\"propertyId\":1,\"propertyValueName\":\"白色\"}],\"propertyName\":\"颜色\",\"propertyType\":1},{\"categoryId\":800003,\"categoryPropertyId\":2,\"dataOrder\":10,\"dataStatus\":1,\"isCheckbox\":0,\"isInput\":0,\"isRequired\":0,\"materialType\":1,\"productCategoryPropertyValueList\":[{\"categoryId\":800003,\"categoryPropertyValueId\":5,\"dataOrder\":0,\"dataStatus\":1,\"propertyId\":2,\"propertyValueName\":\"8G\"}],\"propertyName\":\"内存\",\"propertyType\":1},{\"categoryId\":800003,\"categoryPropertyId\":5,\"dataOrder\":7,\"dataStatus\":1,\"isCheckbox\":0,\"isInput\":0,\"isRequired\":0,\"materialType\":3,\"productCategoryPropertyValueList\":[{\"categoryId\":800003,\"categoryPropertyValueId\":11,\"dataOrder\":0,\"dataStatus\":1,\"propertyId\":5,\"propertyValueName\":\"I5-6400\"}],\"propertyName\":\"CPU\",\"propertyType\":1},{\"categoryId\":800003,\"categoryPropertyId\":6,\"dataOrder\":6,\"dataStatus\":1,\"isCheckbox\":0,\"isInput\":0,\"isRequired\":0,\"materialType\":4,\"productCategoryPropertyValueList\":[{\"categoryId\":800003,\"categoryPropertyValueId\":13,\"dataOrder\":0,\"dataStatus\":1,\"propertyId\":6,\"propertyValueName\":\"2T HDD\"}],\"propertyName\":\"机械硬盘\",\"propertyType\":1},{\"categoryId\":800003,\"categoryPropertyId\":7,\"dataOrder\":5,\"dataStatus\":1,\"isCheckbox\":0,\"isInput\":0,\"isRequired\":0,\"materialType\":5,\"productCategoryPropertyValueList\":[{\"categoryId\":800003,\"categoryPropertyValueId\":15,\"dataOrder\":0,\"dataStatus\":1,\"propertyId\":7,\"propertyValueName\":\"1060 3G\"}],\"propertyName\":\"显卡\",\"propertyType\":1}],\"productDesc\":\"thinkpad002 agagag\",\"productDescImgList\":[{\"dataStatus\":1,\"imgDomain\":\"http://192.168.10.200:8900/\",\"imgId\":128,\"imgOrder\":0,\"imgType\":2,\"imgUrl\":\"group1/M00/00/08/wKgKyFoNTJ2ATvaQAAClthmAlvA142.jpg\",\"isMain\":0,\"originalName\":\"002_01_500x500.jpg\",\"productId\":2000013},{\"dataStatus\":1,\"imgDomain\":\"http://192.168.10.200:8900/\",\"imgId\":130,\"imgOrder\":0,\"imgType\":2,\"imgUrl\":\"group1/M00/00/08/wKgKyFoNTJ2ARglUAADNYmKQUEw790.jpg\",\"isMain\":0,\"originalName\":\"002_03_500x500.jpg\",\"productId\":2000013},{\"dataStatus\":1,\"imgDomain\":\"http://192.168.10.200:8900/\",\"imgId\":132,\"imgOrder\":0,\"imgType\":2,\"imgUrl\":\"group1/M00/00/08/wKgKyFoNTJ2AIrubAACyKALuvoc015.jpg\",\"isMain\":0,\"originalName\":\"002_05_500x500.jpg\",\"productId\":2000013},{\"dataStatus\":1,\"imgDomain\":\"http://192.168.10.200:8900/\",\"imgId\":129,\"imgOrder\":0,\"imgType\":2,\"imgUrl\":\"group1/M00/00/08/wKgKyFoNTJ2AYa4xAAEfm778e6A667.jpg\",\"isMain\":0,\"originalName\":\"002_02_500x500.jpg\",\"productId\":2000013},{\"dataStatus\":1,\"imgDomain\":\"http://192.168.10.200:8900/\",\"imgId\":131,\"imgOrder\":0,\"imgType\":2,\"imgUrl\":\"group1/M00/00/08/wKgKyFoNTJ2ANE50AAA1jKJM1l8479.jpg\",\"isMain\":0,\"originalName\":\"002_04_500x500.jpg\",\"productId\":2000013}],\"productId\":2000013,\"productImgList\":[{\"dataStatus\":1,\"imgDomain\":\"http://192.168.10.200:8900/\",\"imgId\":126,\"imgOrder\":0,\"imgType\":1,\"imgUrl\":\"group1/M00/00/08/wKgKyFoNTJuAPwpLAAA1jKJM1l8134.jpg\",\"isMain\":0,\"originalName\":\"002_04_500x500.jpg\",\"productId\":2000013},{\"dataStatus\":1,\"imgDomain\":\"http://192.168.10.200:8900/\",\"imgId\":123,\"imgOrder\":0,\"imgType\":1,\"imgUrl\":\"group1/M00/00/07/wKgKyFoNTJuAbbnkAAClthmAlvA105.jpg\",\"isMain\":0,\"originalName\":\"002_01_500x500.jpg\",\"productId\":2000013},{\"dataStatus\":1,\"imgDomain\":\"http://192.168.10.200:8900/\",\"imgId\":124,\"imgOrder\":0,\"imgType\":1,\"imgUrl\":\"group1/M00/00/08/wKgKyFoNTJuAP9vHAAEfm778e6A622.jpg\",\"isMain\":0,\"originalName\":\"002_02_500x500.jpg\",\"productId\":2000013},{\"dataStatus\":1,\"imgDomain\":\"http://192.168.10.200:8900/\",\"imgId\":127,\"imgOrder\":0,\"imgType\":1,\"imgUrl\":\"group1/M00/00/08/wKgKyFoNTJuAKEYKAACyKALuvoc275.jpg\",\"isMain\":0,\"originalName\":\"002_05_500x500.jpg\",\"productId\":2000013},{\"dataStatus\":1,\"imgDomain\":\"http://192.168.10.200:8900/\",\"imgId\":125,\"imgOrder\":0,\"imgType\":1,\"imgUrl\":\"group1/M00/00/08/wKgKyFoNTJuAPzlsAADNYmKQUEw518.jpg\",\"isMain\":0,\"originalName\":\"002_03_500x500.jpg\",\"productId\":2000013}],\"productName\":\"thinkpad002\",\"productPropertyList\":[{\"dataStatus\":1,\"isSku\":0,\"productId\":2000013,\"propertyId\":3,\"propertyName\":\"机型\",\"propertyValueId\":7,\"propertyValueName\":\"E270DMG\",\"remark\":\"\",\"skuPropertyId\":261},{\"dataStatus\":1,\"isSku\":0,\"productId\":2000013,\"propertyId\":4,\"propertyName\":\"主板\",\"propertyValueId\":9,\"propertyValueName\":\"H110集显主板\",\"remark\":\"\",\"skuPropertyId\":262},{\"dataStatus\":1,\"isSku\":0,\"productId\":2000013,\"propertyId\":8,\"propertyName\":\"电源\",\"propertyValueId\":17,\"propertyValueName\":\"350W\",\"remark\":\"\",\"skuPropertyId\":263},{\"dataStatus\":1,\"isSku\":0,\"productId\":2000013,\"propertyId\":9,\"propertyName\":\"散热器\",\"propertyValueId\":19,\"propertyValueName\":\"CPU散热器\",\"remark\":\"\",\"skuPropertyId\":264},{\"dataStatus\":1,\"isSku\":0,\"productId\":2000013,\"propertyId\":11,\"propertyName\":\"机箱\",\"propertyValueId\":22,\"propertyValueName\":\"水冷机箱\",\"remark\":\"\",\"skuPropertyId\":265}],\"productSkuList\":[{\"customCode\":\"\",\"dataStatus\":1,\"dayRentPrice\":88.00,\"monthRentPrice\":77.00,\"productId\":2000013,\"productMaterialList\":[{\"materialCount\":1,\"materialName\":\"金士顿2G内存\",\"materialNo\":\"M201711251154081811299\",\"materialType\":1},{\"materialCount\":1,\"materialName\":\"金士顿2G内存\",\"materialNo\":\"M201711251154081811299\",\"materialType\":1},{\"materialCount\":1,\"materialName\":\"金士顿2G内存\",\"materialNo\":\"M201711251154081811299\",\"materialType\":1},{\"materialCount\":1,\"materialName\":\"金士顿2G内存\",\"materialNo\":\"M201711251154081811299\",\"materialType\":1},{\"materialCount\":1,\"materialModelId\":4,\"materialName\":\"CPU物料-I5-6400\",\"materialNo\":\"M201711201422478141693\",\"materialType\":3},{\"materialCount\":1,\"materialName\":\"机械硬盘物料/2T HDD\",\"materialNo\":\"M201711201457288791418\",\"materialType\":4},{\"materialCount\":1,\"materialModelId\":6,\"materialName\":\"显卡/1060 3G\",\"materialNo\":\"M201711201500267591516\",\"materialType\":5},{\"materialCount\":1,\"materialModelId\":2,\"materialName\":\"H110集显主板\",\"materialNo\":\"M201711211953291591494\",\"materialType\":2},{\"materialCount\":1,\"materialModelId\":8,\"materialName\":\"电源350W\",\"materialNo\":\"M201711291745413251585\",\"materialType\":6},{\"materialCount\":1,\"materialModelId\":10,\"materialName\":\"CPU散热器\",\"materialNo\":\"M201711291744581931681\",\"materialType\":7},{\"materialCount\":1,\"materialModelId\":11,\"materialName\":\"水冷机箱\",\"materialNo\":\"M201711171838059981293\",\"materialType\":9}],\"productSkuPropertyList\":[{\"dataStatus\":1,\"isSku\":1,\"productId\":2000013,\"propertyId\":1,\"propertyName\":\"颜色\",\"propertyValueId\":2,\"propertyValueName\":\"白色\",\"remark\":\"\",\"skuId\":40,\"skuPropertyId\":255},{\"dataStatus\":1,\"isSku\":1,\"productId\":2000013,\"propertyId\":2,\"propertyName\":\"内存\",\"propertyValueId\":5,\"propertyValueName\":\"8G\",\"remark\":\"\",\"skuId\":40,\"skuPropertyId\":256},{\"dataStatus\":1,\"isSku\":1,\"productId\":2000013,\"propertyId\":5,\"propertyName\":\"CPU\",\"propertyValueId\":11,\"propertyValueName\":\"I5-6400\",\"remark\":\"\",\"skuId\":40,\"skuPropertyId\":257},{\"dataStatus\":1,\"isSku\":1,\"productId\":2000013,\"propertyId\":6,\"propertyName\":\"机械硬盘\",\"propertyValueId\":13,\"propertyValueName\":\"2T HDD\",\"remark\":\"\",\"skuId\":40,\"skuPropertyId\":258},{\"dataStatus\":1,\"isSku\":1,\"productId\":2000013,\"propertyId\":7,\"propertyName\":\"显卡\",\"propertyValueId\":15,\"propertyValueName\":\"1060 3G\",\"remark\":\"\",\"skuId\":40,\"skuPropertyId\":260}],\"shouldProductCategoryPropertyValueList\":[{\"categoryId\":800003,\"categoryPropertyValueId\":5,\"materialType\":1,\"propertyCapacityValue\":8,\"propertyId\":2,\"propertyName\":\"内存\",\"propertyValueName\":\"8G\"},{\"categoryId\":800003,\"categoryPropertyValueId\":11,\"materialModelId\":4,\"materialType\":3,\"propertyId\":5,\"propertyName\":\"CPU\",\"propertyValueName\":\"I5-6400\"},{\"categoryId\":800003,\"categoryPropertyValueId\":13,\"materialType\":4,\"propertyCapacityValue\":2048,\"propertyId\":6,\"propertyName\":\"机械硬盘\",\"propertyValueName\":\"2T HDD\"},{\"categoryId\":800003,\"categoryPropertyValueId\":15,\"materialModelId\":6,\"materialType\":5,\"propertyId\":7,\"propertyName\":\"显卡\",\"propertyValueName\":\"1060 3G\"},{\"categoryId\":800003,\"categoryPropertyValueId\":9,\"materialModelId\":2,\"materialType\":2,\"propertyId\":4,\"propertyName\":\"主板\",\"propertyValueName\":\"H110集显主板\"},{\"categoryId\":800003,\"categoryPropertyValueId\":17,\"materialModelId\":8,\"materialType\":6,\"propertyId\":8,\"propertyName\":\"电源\",\"propertyValueName\":\"350W\"},{\"categoryId\":800003,\"categoryPropertyValueId\":19,\"materialModelId\":10,\"materialType\":7,\"propertyId\":9,\"propertyName\":\"散热器\",\"propertyValueName\":\"CPU散热器\"},{\"categoryId\":800003,\"categoryPropertyValueId\":22,\"materialModelId\":11,\"materialType\":9,\"propertyId\":11,\"propertyName\":\"机箱\",\"propertyValueName\":\"水冷机箱\"}],\"skuId\":40,\"skuName\":\"颜色:白色/内存:8G/CPU:I5-6400/机械硬盘:2T HDD/显卡:1060 3G/固态硬盘:256G SSD\",\"skuPrice\":100.00,\"stock\":100,\"timeRentPrice\":99.00}],\"subtitle\":\"thinkpad002\",\"unit\":303719}", Product.class);
        System.out.println();
    }

    @Test
    public void test1() throws Exception {
        UpdatePurchaseReceiveEquipmentPriceParam updatePurchaseReceiveEquipmentPriceParam = JSON.parseObject("{\n" +
                "  \"purchaseReceiveOrderNo\": \"PR2017122613482675460001641534\",\n" +
                "  \"equipmentList\": [{\n" +
                "    \"equipmentNo\": \"LX-EQUIPMENT-4000002-2017122610013\",\n" +
                "    \"purchasePrice\": \"10\"\n" +
                "  }]\n" +
                "}", UpdatePurchaseReceiveEquipmentPriceParam.class);
        TestResult testResult = getJsonTestResult("/purchaseOrder/updatePurchaseReceiveOrderPrice", updatePurchaseReceiveEquipmentPriceParam);
    }

    @Test
    public void testConvert() throws Exception {
//        PurchaseOrderDO purchaseOrderDO = new PurchaseOrderDO();
//        purchaseOrderDO.setCreateUser("500001");
//        purchaseOrderDO.setUpdateUser("500001");
//        purchaseOrderDO.setId(6000057);
        UserDO userDO = new UserDO();
        userDO.setCreateUser("500001");
        userDO.setUpdateUser("500001");
        userDO.setId(500001);
        userDO.setRemark("备注");


        User user = ConverterUtil.convert(userDO, User.class);
    }

    @Test
    public void test2() {

        TestResult result = JSON.parseObject("{\"purchaseReceiveNo\":\"LXPR1000201801020002\",\"isNew\":\"1\",\"purchaseReceiveOrderMaterialList\":[{\"realMaterialNo\":\"M201801011026586021729\",\"realMaterialCount\":\"10\",\"remark\":\"\"}]}", TestResult.class);
    }

    @Autowired
    private PurchaseOrderService purchaseOrderService;
}