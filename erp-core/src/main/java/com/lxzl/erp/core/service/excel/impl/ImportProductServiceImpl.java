package com.lxzl.erp.core.service.excel.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.product.pojo.ProductSkuProperty;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.excel.ImportProductService;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.basic.BrandMapper;
import com.lxzl.erp.dataaccess.dao.mysql.material.MaterialModelMapper;
import com.lxzl.erp.dataaccess.dao.mysql.product.*;
import com.lxzl.erp.dataaccess.domain.product.ProductCategoryPropertyValueDO;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-03-12 23:15
 */
@Service
public class ImportProductServiceImpl implements ImportProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductSkuPropertyMapper productSkuPropertyMapper;

    @Autowired
    private ProductCategoryPropertyMapper productCategoryPropertyMapper;

    @Autowired
    private ProductCategoryPropertyValueMapper productCategoryPropertyValueMapper;

    @Autowired
    private MaterialModelMapper materialModelMapper;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private GenerateNoSupport generateNoSupport;

    // 投影仪
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String importProjectorProduct(String filePath) throws Exception {
        Integer categoryId = 800010;
        FileInputStream fileIn = new FileInputStream(filePath);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileIn);
        XSSFSheet xssfSheet = xssfWorkbook.getSheet("投影仪");

        List<ProductSku> arrayList = new ArrayList<>();
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow.getCell(3) == null) {
                continue;
            }
            if (xssfRow.getCell(2) == null) {
                continue;
            }

            String k3ProductNo = null, productName = xssfRow.getCell(4).toString();

            if (xssfRow.getCell(0) != null) {
                k3ProductNo = xssfRow.getCell(0).toString();
            }

            List<Integer> propertyValueIdList = new ArrayList<>();
            propertyValueIdList.add(205);

            String brandName = xssfRow.getCell(2).toString();
            Integer brandId = brandMapper.findBrandIdByName(brandName);
            if (brandId == null) {
                System.out.println("投影仪品牌不存在，请查看, " + brandName);
                continue;
            }

            String productModel = xssfRow.getCell(3).toString();
            ProductDO productDO = productMapper.findExistsProduct(brandId, categoryId, productModel);
            // 商品不存在，直接添加，如果存在，直接判定SKU是否存在，如果存在则打印，如果不存在则添加进数据库即可
            if (productDO == null) {
                Product product = new Product();
                product.setProductName(productName);
                product.setK3ProductNo(k3ProductNo);
                product.setProductModel(productModel);
                product.setUnit(300016);
                product.setCategoryId(categoryId);
                product.setBrandId(brandId);
                product.setIsRent(CommonConstant.COMMON_CONSTANT_YES);
                product.setIsReturnAnyTime(CommonConstant.COMMON_CONSTANT_NO);

                List<ProductSkuProperty> productPropertyList = new ArrayList<>();
                ProductSkuProperty productSkuProperty = new ProductSkuProperty();
                productSkuProperty.setPropertyValueId(206);
                productPropertyList.add(productSkuProperty);
                product.setProductPropertyList(productPropertyList);

                List<ProductSku> productSkuList = new ArrayList<>();
                ProductSku productSku = new ProductSku();
                List<ProductSkuProperty> productSkuPropertyList = new ArrayList<>();
                for (Integer valueId : propertyValueIdList) {
                    ProductSkuProperty skuProperty = new ProductSkuProperty();
                    skuProperty.setPropertyValueId(valueId);
                    productSkuPropertyList.add(skuProperty);
                }
                productSku.setProductSkuPropertyList(productSkuPropertyList);

                productSku.setSkuPrice(new BigDecimal(xssfRow.getCell(5).toString()));
                productSku.setDayRentPrice(new BigDecimal(xssfRow.getCell(6).toString()));
                productSku.setMonthRentPrice(new BigDecimal(xssfRow.getCell(7).toString()));
                productSku.setNewSkuPrice(new BigDecimal(xssfRow.getCell(8).toString()));
                productSku.setNewDayRentPrice(new BigDecimal(xssfRow.getCell(9).toString()));
                productSku.setNewMonthRentPrice(new BigDecimal(xssfRow.getCell(10).toString()));


                productSkuList.add(productSku);

                product.setProductSkuList(productSkuList);

                ServiceResult<String, Integer> result = productService.addProduct(product);
                if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                    System.out.println("商品保存失败:" + productName);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            } else {
                Map<String, Object> maps = new HashMap<>();
                maps.put("productId", productDO.getId());
                maps.put("isSku", CommonConstant.COMMON_CONSTANT_YES);
                maps.put("propertyValueIdList", propertyValueIdList);
                maps.put("propertyValueIdCount", propertyValueIdList.size());
                Integer skuId = productSkuPropertyMapper.findSkuIdByParams(maps);
                if (skuId == null) {
                    Product product = productService.queryProductById(productDO.getId()).getResult();

                    List<ProductSku> productSkuList = product.getProductSkuList();
                    ProductSku productSku = new ProductSku();
                    List<ProductSkuProperty> productSkuPropertyList = new ArrayList<>();
                    for (Integer valueId : propertyValueIdList) {
                        ProductSkuProperty skuProperty = new ProductSkuProperty();
                        skuProperty.setPropertyValueId(valueId);
                        productSkuPropertyList.add(skuProperty);
                    }
                    productSku.setProductSkuPropertyList(productSkuPropertyList);

                    productSku.setProductId(product.getProductId());

                    productSku.setSkuPrice(new BigDecimal(xssfRow.getCell(5).toString()));
                    productSku.setDayRentPrice(new BigDecimal(xssfRow.getCell(6).toString()));
                    productSku.setMonthRentPrice(new BigDecimal(xssfRow.getCell(7).toString()));
                    productSku.setNewSkuPrice(new BigDecimal(xssfRow.getCell(8).toString()));
                    productSku.setNewDayRentPrice(new BigDecimal(xssfRow.getCell(9).toString()));
                    productSku.setNewMonthRentPrice(new BigDecimal(xssfRow.getCell(10).toString()));


                    productSkuList.add(productSku);

                    product.setProductSkuList(productSkuList);

                    ServiceResult<String, Integer> result = productService.updateProduct(product);
                    if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                        System.out.println("商品更新失败:" + productName);
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                }
            }
        }

        return ErrorCode.SUCCESS;
    }
    // 打印机
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String importPrinterProduct(String filePath) throws Exception {
        Integer categoryId = 800007;
        FileInputStream fileIn = new FileInputStream(filePath);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileIn);
        XSSFSheet xssfSheet = xssfWorkbook.getSheet("打印机");

        List<ProductSku> arrayList = new ArrayList<>();
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow.getCell(3) == null) {
                continue;
            }
            if (xssfRow.getCell(2) == null) {
                continue;
            }

            String k3ProductNo = null, productName = xssfRow.getCell(4).toString();

            if (xssfRow.getCell(0) != null) {
                k3ProductNo = xssfRow.getCell(0).toString();
            }

            List<Integer> propertyValueIdList = new ArrayList<>();
            propertyValueIdList.add(199);

            String brandName = xssfRow.getCell(2).toString();
            Integer brandId = brandMapper.findBrandIdByName(brandName);
            if (brandId == null) {
                System.out.println("打印机品牌不存在，请查看, " + brandName);
                continue;
            }

            String productModel = xssfRow.getCell(3).toString();
            ProductDO productDO = productMapper.findExistsProduct(brandId, categoryId, productModel);
            // 商品不存在，直接添加，如果存在，直接判定SKU是否存在，如果存在则打印，如果不存在则添加进数据库即可
            if (productDO == null) {
                Product product = new Product();
                product.setProductName(productName);
                product.setK3ProductNo(k3ProductNo);
                product.setProductModel(productModel);
                product.setUnit(300016);
                product.setCategoryId(categoryId);
                product.setBrandId(brandId);
                product.setIsRent(CommonConstant.COMMON_CONSTANT_YES);
                product.setIsReturnAnyTime(CommonConstant.COMMON_CONSTANT_NO);

                List<ProductSkuProperty> productPropertyList = new ArrayList<>();
                ProductSkuProperty productSkuProperty = new ProductSkuProperty();
                productSkuProperty.setPropertyValueId(200);
                productPropertyList.add(productSkuProperty);
                product.setProductPropertyList(productPropertyList);

                List<ProductSku> productSkuList = new ArrayList<>();
                ProductSku productSku = new ProductSku();
                List<ProductSkuProperty> productSkuPropertyList = new ArrayList<>();
                for (Integer valueId : propertyValueIdList) {
                    ProductSkuProperty skuProperty = new ProductSkuProperty();
                    skuProperty.setPropertyValueId(valueId);
                    productSkuPropertyList.add(skuProperty);
                }
                productSku.setProductSkuPropertyList(productSkuPropertyList);

                productSku.setSkuPrice(new BigDecimal(xssfRow.getCell(5).toString()));
                productSku.setDayRentPrice(new BigDecimal(xssfRow.getCell(6).toString()));
                productSku.setMonthRentPrice(new BigDecimal(xssfRow.getCell(7).toString()));
                productSku.setNewSkuPrice(new BigDecimal(xssfRow.getCell(8).toString()));
                productSku.setNewDayRentPrice(new BigDecimal(xssfRow.getCell(9).toString()));
                productSku.setNewMonthRentPrice(new BigDecimal(xssfRow.getCell(10).toString()));


                productSkuList.add(productSku);

                product.setProductSkuList(productSkuList);

                ServiceResult<String, Integer> result = productService.addProduct(product);
                if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                    System.out.println("商品保存失败:" + productName);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            } else {
                Map<String, Object> maps = new HashMap<>();
                maps.put("productId", productDO.getId());
                maps.put("isSku", CommonConstant.COMMON_CONSTANT_YES);
                maps.put("propertyValueIdList", propertyValueIdList);
                maps.put("propertyValueIdCount", propertyValueIdList.size());
                Integer skuId = productSkuPropertyMapper.findSkuIdByParams(maps);
                if (skuId == null) {
                    Product product = productService.queryProductById(productDO.getId()).getResult();

                    List<ProductSku> productSkuList = product.getProductSkuList();
                    ProductSku productSku = new ProductSku();
                    List<ProductSkuProperty> productSkuPropertyList = new ArrayList<>();
                    for (Integer valueId : propertyValueIdList) {
                        ProductSkuProperty skuProperty = new ProductSkuProperty();
                        skuProperty.setPropertyValueId(valueId);
                        productSkuPropertyList.add(skuProperty);
                    }
                    productSku.setProductSkuPropertyList(productSkuPropertyList);

                    productSku.setProductId(product.getProductId());

                    productSku.setSkuPrice(new BigDecimal(xssfRow.getCell(5).toString()));
                    productSku.setDayRentPrice(new BigDecimal(xssfRow.getCell(6).toString()));
                    productSku.setMonthRentPrice(new BigDecimal(xssfRow.getCell(7).toString()));
                    productSku.setNewSkuPrice(new BigDecimal(xssfRow.getCell(8).toString()));
                    productSku.setNewDayRentPrice(new BigDecimal(xssfRow.getCell(9).toString()));
                    productSku.setNewMonthRentPrice(new BigDecimal(xssfRow.getCell(10).toString()));


                    productSkuList.add(productSku);

                    product.setProductSkuList(productSkuList);

                    ServiceResult<String, Integer> result = productService.updateProduct(product);
                    if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                        System.out.println("商品更新失败:" + productName);
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                }
            }
        }

        return ErrorCode.SUCCESS;
    }

    // 显示器
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String importPhoneProduct(String filePath) throws Exception {
        Integer categoryId = 800015;
        FileInputStream fileIn = new FileInputStream(filePath);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileIn);
        XSSFSheet xssfSheet = xssfWorkbook.getSheet("手机");

        List<ProductSku> arrayList = new ArrayList<>();
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow.getCell(3) == null) {
                continue;
            }
            if (xssfRow.getCell(2) == null) {
                continue;
            }

            String k3ProductNo = null, productName = xssfRow.getCell(4).toString();

            if (xssfRow.getCell(0) != null) {
                k3ProductNo = xssfRow.getCell(0).toString();
            }

            List<Integer> propertyValueIdList = new ArrayList<>();
            propertyValueIdList.add(528);

            String brandName = xssfRow.getCell(2).toString();
            Integer brandId = brandMapper.findBrandIdByName(brandName);
            if (brandId == null) {
                System.out.println("手机品牌不存在，请查看, " + brandName);
                continue;
            }

            String productModel = xssfRow.getCell(3).toString();
            ProductDO productDO = productMapper.findExistsProduct(brandId, categoryId, productModel);
            // 商品不存在，直接添加，如果存在，直接判定SKU是否存在，如果存在则打印，如果不存在则添加进数据库即可
            if (productDO == null) {
                Product product = new Product();
                product.setProductName(productName);
                product.setK3ProductNo(k3ProductNo);
                product.setProductModel(productModel);
                product.setUnit(300016);
                product.setCategoryId(categoryId);
                product.setBrandId(brandId);
                product.setIsRent(CommonConstant.COMMON_CONSTANT_YES);
                product.setIsReturnAnyTime(CommonConstant.COMMON_CONSTANT_NO);

                List<ProductSkuProperty> productPropertyList = new ArrayList<>();
                ProductSkuProperty productSkuProperty = new ProductSkuProperty();
                productSkuProperty.setPropertyValueId(527);
                productPropertyList.add(productSkuProperty);
                product.setProductPropertyList(productPropertyList);

                List<ProductSku> productSkuList = new ArrayList<>();
                ProductSku productSku = new ProductSku();
                List<ProductSkuProperty> productSkuPropertyList = new ArrayList<>();
                for (Integer valueId : propertyValueIdList) {
                    ProductSkuProperty skuProperty = new ProductSkuProperty();
                    skuProperty.setPropertyValueId(valueId);
                    productSkuPropertyList.add(skuProperty);
                }
                productSku.setProductSkuPropertyList(productSkuPropertyList);

                productSku.setSkuPrice(new BigDecimal(xssfRow.getCell(5).toString()));
                productSku.setDayRentPrice(new BigDecimal(xssfRow.getCell(6).toString()));
                productSku.setMonthRentPrice(new BigDecimal(xssfRow.getCell(7).toString()));
                productSku.setNewSkuPrice(new BigDecimal(xssfRow.getCell(8).toString()));
                productSku.setNewDayRentPrice(new BigDecimal(xssfRow.getCell(9).toString()));
                productSku.setNewMonthRentPrice(new BigDecimal(xssfRow.getCell(10).toString()));


                productSkuList.add(productSku);

                product.setProductSkuList(productSkuList);

                ServiceResult<String, Integer> result = productService.addProduct(product);
                if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                    System.out.println("商品保存失败:" + productName);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            } else {
                Map<String, Object> maps = new HashMap<>();
                maps.put("productId", productDO.getId());
                maps.put("isSku", CommonConstant.COMMON_CONSTANT_YES);
                maps.put("propertyValueIdList", propertyValueIdList);
                maps.put("propertyValueIdCount", propertyValueIdList.size());
                Integer skuId = productSkuPropertyMapper.findSkuIdByParams(maps);
                if (skuId == null) {
                    Product product = productService.queryProductById(productDO.getId()).getResult();

                    List<ProductSku> productSkuList = product.getProductSkuList();
                    ProductSku productSku = new ProductSku();
                    List<ProductSkuProperty> productSkuPropertyList = new ArrayList<>();
                    for (Integer valueId : propertyValueIdList) {
                        ProductSkuProperty skuProperty = new ProductSkuProperty();
                        skuProperty.setPropertyValueId(valueId);
                        productSkuPropertyList.add(skuProperty);
                    }
                    productSku.setProductSkuPropertyList(productSkuPropertyList);

                    productSku.setProductId(product.getProductId());

                    productSku.setSkuPrice(new BigDecimal(xssfRow.getCell(5).toString()));
                    productSku.setDayRentPrice(new BigDecimal(xssfRow.getCell(6).toString()));
                    productSku.setMonthRentPrice(new BigDecimal(xssfRow.getCell(7).toString()));
                    productSku.setNewSkuPrice(new BigDecimal(xssfRow.getCell(8).toString()));
                    productSku.setNewDayRentPrice(new BigDecimal(xssfRow.getCell(9).toString()));
                    productSku.setNewMonthRentPrice(new BigDecimal(xssfRow.getCell(10).toString()));


                    productSkuList.add(productSku);

                    product.setProductSkuList(productSkuList);

                    ServiceResult<String, Integer> result = productService.updateProduct(product);
                    if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                        System.out.println("商品更新失败:" + productName);
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                }
            }
        }

        return ErrorCode.SUCCESS;
    }

    // 显示器
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String importMonitorProduct(String filePath) throws Exception {
        Integer categoryId = 800006;
        FileInputStream fileIn = new FileInputStream(filePath);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileIn);
        XSSFSheet xssfSheet = xssfWorkbook.getSheet("电视机");

        List<ProductSku> arrayList = new ArrayList<>();
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow.getCell(3) == null) {
                continue;
            }
            if (xssfRow.getCell(2) == null) {
                continue;
            }

            String k3ProductNo = null, productName = xssfRow.getCell(4).toString(), size = xssfRow.getCell(5).toString();

            if (xssfRow.getCell(0) != null) {
                k3ProductNo = xssfRow.getCell(0).toString();
            }

            List<Integer> propertyValueIdList = new ArrayList<>();
            // 判断尺寸是否存在，如果不存在，添加，否则直接用
            ProductCategoryPropertyValueDO sizeProductCategoryPropertyValueDO = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(size, categoryId);
            if (sizeProductCategoryPropertyValueDO == null) {
                ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(size, 12, categoryId, "显示器尺寸" + size);
                productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
            } else {
                propertyValueIdList.add(sizeProductCategoryPropertyValueDO.getId());
            }

            String brandName = xssfRow.getCell(2).toString();
            Integer brandId = brandMapper.findBrandIdByName(brandName);
            if (brandId == null) {
                System.out.println("显示器品牌不存在，请查看, " + brandName);
                continue;
            }

            String productModel = xssfRow.getCell(3).toString();
            ProductDO productDO = productMapper.findExistsProduct(brandId, categoryId, productModel);
            // 商品不存在，直接添加，如果存在，直接判定SKU是否存在，如果存在则打印，如果不存在则添加进数据库即可
            if (productDO == null) {
                Product product = new Product();
                product.setProductName(productName);
                product.setK3ProductNo(k3ProductNo);
                product.setProductModel(productModel);
                product.setUnit(300016);
                product.setCategoryId(categoryId);
                product.setBrandId(brandId);
                product.setIsRent(CommonConstant.COMMON_CONSTANT_YES);
                product.setIsReturnAnyTime(CommonConstant.COMMON_CONSTANT_NO);

                List<ProductSkuProperty> productPropertyList = new ArrayList<>();
                ProductSkuProperty productSkuProperty = new ProductSkuProperty();
                productSkuProperty.setPropertyValueId(84);
                productPropertyList.add(productSkuProperty);
                product.setProductPropertyList(productPropertyList);

                List<ProductSku> productSkuList = new ArrayList<>();
                ProductSku productSku = new ProductSku();
                List<ProductSkuProperty> productSkuPropertyList = new ArrayList<>();
                for (Integer valueId : propertyValueIdList) {
                    ProductSkuProperty skuProperty = new ProductSkuProperty();
                    skuProperty.setPropertyValueId(valueId);
                    productSkuPropertyList.add(skuProperty);
                }
                productSku.setProductSkuPropertyList(productSkuPropertyList);

                productSku.setSkuPrice(new BigDecimal(xssfRow.getCell(6).toString()));
                productSku.setDayRentPrice(new BigDecimal(xssfRow.getCell(7).toString()));
                productSku.setMonthRentPrice(new BigDecimal(xssfRow.getCell(8).toString()));
                productSku.setNewSkuPrice(new BigDecimal(xssfRow.getCell(9).toString()));
                productSku.setNewDayRentPrice(new BigDecimal(xssfRow.getCell(10).toString()));
                productSku.setNewMonthRentPrice(new BigDecimal(xssfRow.getCell(11).toString()));


                productSkuList.add(productSku);

                product.setProductSkuList(productSkuList);

                ServiceResult<String, Integer> result = productService.addProduct(product);
                if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                    System.out.println("商品保存失败:" + productName);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            } else {
                Map<String, Object> maps = new HashMap<>();
                maps.put("productId", productDO.getId());
                maps.put("isSku", CommonConstant.COMMON_CONSTANT_YES);
                maps.put("propertyValueIdList", propertyValueIdList);
                maps.put("propertyValueIdCount", propertyValueIdList.size());
                Integer skuId = productSkuPropertyMapper.findSkuIdByParams(maps);
                if (skuId == null) {
                    Product product = productService.queryProductById(productDO.getId()).getResult();

                    List<ProductSku> productSkuList = product.getProductSkuList();
                    ProductSku productSku = new ProductSku();
                    List<ProductSkuProperty> productSkuPropertyList = new ArrayList<>();
                    for (Integer valueId : propertyValueIdList) {
                        ProductSkuProperty skuProperty = new ProductSkuProperty();
                        skuProperty.setPropertyValueId(valueId);
                        productSkuPropertyList.add(skuProperty);
                    }
                    productSku.setProductSkuPropertyList(productSkuPropertyList);

                    productSku.setProductId(product.getProductId());

                    productSku.setSkuPrice(new BigDecimal(xssfRow.getCell(6).toString()));
                    productSku.setDayRentPrice(new BigDecimal(xssfRow.getCell(7).toString()));
                    productSku.setMonthRentPrice(new BigDecimal(xssfRow.getCell(8).toString()));
                    productSku.setNewSkuPrice(new BigDecimal(xssfRow.getCell(9).toString()));
                    productSku.setNewDayRentPrice(new BigDecimal(xssfRow.getCell(10).toString()));
                    productSku.setNewMonthRentPrice(new BigDecimal(xssfRow.getCell(11).toString()));


                    productSkuList.add(productSku);

                    product.setProductSkuList(productSkuList);

                    ServiceResult<String, Integer> result = productService.updateProduct(product);
                    if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                        System.out.println("商品更新失败:" + productName);
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                }
            }
        }

        return ErrorCode.SUCCESS;
    }

    // 台式机
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String importTVProduct(String filePath) throws Exception {
        Integer categoryId = 800009;
        FileInputStream fileIn = new FileInputStream(filePath);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileIn);
        XSSFSheet xssfSheet = xssfWorkbook.getSheet("电视机");

        List<ProductSku> arrayList = new ArrayList<>();
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow.getCell(3) == null) {
                continue;
            }
            if (xssfRow.getCell(2) == null) {
                continue;
            }

            String k3ProductNo = null, productName = xssfRow.getCell(4).toString(), size = xssfRow.getCell(5).toString();

            if (xssfRow.getCell(0) != null) {
                k3ProductNo = xssfRow.getCell(0).toString();
            }

            List<Integer> propertyValueIdList = new ArrayList<>();
            // 判断尺寸是否存在，如果不存在，添加，否则直接用
            ProductCategoryPropertyValueDO sizeProductCategoryPropertyValueDO = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(size, categoryId);
            if (sizeProductCategoryPropertyValueDO == null) {
                ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(size, 42, categoryId, "电视机尺寸" + size);
                productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
            } else {
                propertyValueIdList.add(sizeProductCategoryPropertyValueDO.getId());
            }

            String brandName = xssfRow.getCell(2).toString();
            Integer brandId = brandMapper.findBrandIdByName(brandName);
            if (brandId == null) {
                System.out.println("电视机品牌不存在，请查看, " + brandName);
                continue;
            }

            String productModel = xssfRow.getCell(3).toString();
            ProductDO productDO = productMapper.findExistsProduct(brandId, categoryId, productModel);
            // 商品不存在，直接添加，如果存在，直接判定SKU是否存在，如果存在则打印，如果不存在则添加进数据库即可
            if (productDO == null) {
                Product product = new Product();
                product.setProductName(productName);
                product.setK3ProductNo(k3ProductNo);
                product.setProductModel(productModel);
                product.setUnit(300016);
                product.setCategoryId(categoryId);
                product.setBrandId(brandId);
                product.setIsRent(CommonConstant.COMMON_CONSTANT_YES);
                product.setIsReturnAnyTime(CommonConstant.COMMON_CONSTANT_NO);

                List<ProductSkuProperty> productPropertyList = new ArrayList<>();
                ProductSkuProperty productSkuProperty = new ProductSkuProperty();
                productSkuProperty.setPropertyValueId(204);
                productPropertyList.add(productSkuProperty);
                product.setProductPropertyList(productPropertyList);

                List<ProductSku> productSkuList = new ArrayList<>();
                ProductSku productSku = new ProductSku();
                List<ProductSkuProperty> productSkuPropertyList = new ArrayList<>();
                for (Integer valueId : propertyValueIdList) {
                    ProductSkuProperty skuProperty = new ProductSkuProperty();
                    skuProperty.setPropertyValueId(valueId);
                    productSkuPropertyList.add(skuProperty);
                }
                productSku.setProductSkuPropertyList(productSkuPropertyList);

                productSku.setSkuPrice(new BigDecimal(xssfRow.getCell(6).toString()));
                productSku.setDayRentPrice(new BigDecimal(xssfRow.getCell(7).toString()));
                productSku.setMonthRentPrice(new BigDecimal(xssfRow.getCell(8).toString()));
                productSku.setNewSkuPrice(new BigDecimal(xssfRow.getCell(9).toString()));
                productSku.setNewDayRentPrice(new BigDecimal(xssfRow.getCell(10).toString()));
                productSku.setNewMonthRentPrice(new BigDecimal(xssfRow.getCell(11).toString()));


                productSkuList.add(productSku);

                product.setProductSkuList(productSkuList);

                ServiceResult<String, Integer> result = productService.addProduct(product);
                if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                    System.out.println("商品保存失败:" + productName);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            } else {
                Map<String, Object> maps = new HashMap<>();
                maps.put("productId", productDO.getId());
                maps.put("isSku", CommonConstant.COMMON_CONSTANT_YES);
                maps.put("propertyValueIdList", propertyValueIdList);
                maps.put("propertyValueIdCount", propertyValueIdList.size());
                Integer skuId = productSkuPropertyMapper.findSkuIdByParams(maps);
                if (skuId == null) {
                    Product product = productService.queryProductById(productDO.getId()).getResult();

                    List<ProductSku> productSkuList = product.getProductSkuList();
                    ProductSku productSku = new ProductSku();
                    List<ProductSkuProperty> productSkuPropertyList = new ArrayList<>();
                    for (Integer valueId : propertyValueIdList) {
                        ProductSkuProperty skuProperty = new ProductSkuProperty();
                        skuProperty.setPropertyValueId(valueId);
                        productSkuPropertyList.add(skuProperty);
                    }
                    productSku.setProductSkuPropertyList(productSkuPropertyList);

                    productSku.setProductId(product.getProductId());

                    productSku.setSkuPrice(new BigDecimal(xssfRow.getCell(6).toString()));
                    productSku.setDayRentPrice(new BigDecimal(xssfRow.getCell(7).toString()));
                    productSku.setMonthRentPrice(new BigDecimal(xssfRow.getCell(8).toString()));
                    productSku.setNewSkuPrice(new BigDecimal(xssfRow.getCell(9).toString()));
                    productSku.setNewDayRentPrice(new BigDecimal(xssfRow.getCell(10).toString()));
                    productSku.setNewMonthRentPrice(new BigDecimal(xssfRow.getCell(11).toString()));


                    productSkuList.add(productSku);

                    product.setProductSkuList(productSkuList);

                    ServiceResult<String, Integer> result = productService.updateProduct(product);
                    if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                        System.out.println("商品更新失败:" + productName);
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                }
            }
        }

        return ErrorCode.SUCCESS;
    }

    // 台式机
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String importAIOProduct(String filePath) throws Exception {
        Integer categoryId = 800003;
        FileInputStream fileIn = new FileInputStream(filePath);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileIn);
        XSSFSheet xssfSheet = xssfWorkbook.getSheet("一体机");

        List<ProductSku> arrayList = new ArrayList<>();
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow.getCell(3) == null) {
                continue;
            }
            if (xssfRow.getCell(2) == null) {
                continue;
            }

            String k3ProductNo = xssfRow.getCell(0).toString(), productName = xssfRow.getCell(4).toString(), cpu = xssfRow.getCell(5).toString(), memory = null, hdd = null, ssd = null, graphics = xssfRow.getCell(9).toString(), size = xssfRow.getCell(10).toString();
            if (xssfRow.getCell(7) != null) {
                hdd = xssfRow.getCell(7).toString();
            }
            if (xssfRow.getCell(8) != null) {
                ssd = xssfRow.getCell(8).toString();
            }
            if (xssfRow.getCell(6) != null) {
                memory = xssfRow.getCell(6).toString();
            }

            List<Integer> propertyValueIdList = new ArrayList<>();
            // 判断CPU是否存在，如果不存在，添加，否则直接用
            ProductCategoryPropertyValueDO cpuProductCategoryPropertyValueDO = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(cpu, categoryId);
            if (cpuProductCategoryPropertyValueDO == null) {
                ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(cpu, 23, categoryId, "一体机CPU" + cpu);
                productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
            } else {
                propertyValueIdList.add(cpuProductCategoryPropertyValueDO.getId());
            }
            if (memory != null) {
                ProductCategoryPropertyValueDO memoryProductCategoryPropertyValueDO = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(memory, categoryId);
                if (memoryProductCategoryPropertyValueDO == null) {
                    ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(memory, 24, categoryId, "一体机内存" + memory);
                    productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                } else {
                    propertyValueIdList.add(memoryProductCategoryPropertyValueDO.getId());
                }
            }
            if (hdd != null) {
                ProductCategoryPropertyValueDO hddProductCategoryPropertyValueDO = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(hdd, categoryId);
                if (hddProductCategoryPropertyValueDO == null) {
                    ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(hdd, 25, categoryId, "一体机械硬盘" + hdd);
                    productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                } else {
                    propertyValueIdList.add(hddProductCategoryPropertyValueDO.getId());
                }
            }
            if (ssd != null) {
                ProductCategoryPropertyValueDO ssdProductCategoryPropertyValueDO = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(ssd, categoryId);
                if (ssdProductCategoryPropertyValueDO == null) {
                    ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(ssd, 26, categoryId, "一体机固态硬盘" + ssd);
                    productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                } else {
                    propertyValueIdList.add(ssdProductCategoryPropertyValueDO.getId());
                }
            }

            ProductCategoryPropertyValueDO graphicsProductCategoryPropertyValueDO = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(graphics, categoryId);
            if (graphicsProductCategoryPropertyValueDO == null) {
                ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(graphics, 27, categoryId, "一体机显卡" + graphics);
                productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
            } else {
                propertyValueIdList.add(graphicsProductCategoryPropertyValueDO.getId());
            }

            ProductCategoryPropertyValueDO sizeProductCategoryPropertyValueDO = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(size, categoryId);
            if (sizeProductCategoryPropertyValueDO == null) {
                ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(size, 28, categoryId, "一体机尺寸" + size);
                productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
            } else {
                propertyValueIdList.add(sizeProductCategoryPropertyValueDO.getId());
            }

            String brandName = xssfRow.getCell(2).toString();
            Integer brandId = brandMapper.findBrandIdByName(brandName);
            if (brandId == null) {
                System.out.println("一体机品牌不存在，请查看, " + brandName);
                continue;
            }

            String productModel = xssfRow.getCell(3).toString();
            ProductDO productDO = productMapper.findExistsProduct(brandId, categoryId, productModel);
            // 商品不存在，直接添加，如果存在，直接判定SKU是否存在，如果存在则打印，如果不存在则添加进数据库即可
            if (productDO == null) {
                Product product = new Product();
                product.setProductName(productName);
                product.setK3ProductNo(k3ProductNo);
                product.setProductModel(productModel);
                product.setUnit(300016);
                product.setCategoryId(categoryId);
                product.setBrandId(brandId);
                product.setIsRent(CommonConstant.COMMON_CONSTANT_YES);
                product.setIsReturnAnyTime(CommonConstant.COMMON_CONSTANT_NO);

                List<ProductSkuProperty> productPropertyList = new ArrayList<>();
                ProductSkuProperty productSkuProperty = new ProductSkuProperty();
                productSkuProperty.setPropertyValueId(183);
                productPropertyList.add(productSkuProperty);
                product.setProductPropertyList(productPropertyList);

                List<ProductSku> productSkuList = new ArrayList<>();
                ProductSku productSku = new ProductSku();
                List<ProductSkuProperty> productSkuPropertyList = new ArrayList<>();
                for (Integer valueId : propertyValueIdList) {
                    ProductSkuProperty skuProperty = new ProductSkuProperty();
                    skuProperty.setPropertyValueId(valueId);
                    productSkuPropertyList.add(skuProperty);
                }
                productSku.setProductSkuPropertyList(productSkuPropertyList);

                productSku.setSkuPrice(new BigDecimal(xssfRow.getCell(11).toString()));
                productSku.setDayRentPrice(new BigDecimal(xssfRow.getCell(12).toString()));
                productSku.setMonthRentPrice(new BigDecimal(xssfRow.getCell(13).toString()));
                productSku.setNewSkuPrice(new BigDecimal(xssfRow.getCell(14).toString()));
                productSku.setNewDayRentPrice(new BigDecimal(xssfRow.getCell(15).toString()));
                productSku.setNewMonthRentPrice(new BigDecimal(xssfRow.getCell(16).toString()));


                productSkuList.add(productSku);

                product.setProductSkuList(productSkuList);

                ServiceResult<String, Integer> result = productService.addProduct(product);
                if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                    System.out.println("商品保存失败:" + productName);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            } else {
                Map<String, Object> maps = new HashMap<>();
                maps.put("productId", productDO.getId());
                maps.put("isSku", CommonConstant.COMMON_CONSTANT_YES);
                maps.put("propertyValueIdList", propertyValueIdList);
                maps.put("propertyValueIdCount", propertyValueIdList.size());
                Integer skuId = productSkuPropertyMapper.findSkuIdByParams(maps);
                if (skuId == null) {
                    Product product = productService.queryProductById(productDO.getId()).getResult();

                    List<ProductSku> productSkuList = product.getProductSkuList();
                    ProductSku productSku = new ProductSku();
                    List<ProductSkuProperty> productSkuPropertyList = new ArrayList<>();
                    for (Integer valueId : propertyValueIdList) {
                        ProductSkuProperty skuProperty = new ProductSkuProperty();
                        skuProperty.setPropertyValueId(valueId);
                        productSkuPropertyList.add(skuProperty);
                    }
                    productSku.setProductSkuPropertyList(productSkuPropertyList);

                    productSku.setProductId(product.getProductId());

                    productSku.setSkuPrice(new BigDecimal(xssfRow.getCell(11).toString()));
                    productSku.setDayRentPrice(new BigDecimal(xssfRow.getCell(12).toString()));
                    productSku.setMonthRentPrice(new BigDecimal(xssfRow.getCell(13).toString()));
                    productSku.setNewSkuPrice(new BigDecimal(xssfRow.getCell(14).toString()));
                    productSku.setNewDayRentPrice(new BigDecimal(xssfRow.getCell(15).toString()));
                    productSku.setNewMonthRentPrice(new BigDecimal(xssfRow.getCell(16).toString()));


                    productSkuList.add(productSku);

                    product.setProductSkuList(productSkuList);

                    ServiceResult<String, Integer> result = productService.updateProduct(product);
                    if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                        System.out.println("商品更新失败:" + productName);
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                }
            }
        }

        return ErrorCode.SUCCESS;
    }

    // 台式机
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String importNodeProduct(String filePath) throws Exception {
        Integer categoryId = 800002;
        FileInputStream fileIn = new FileInputStream(filePath);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileIn);
        XSSFSheet xssfSheet = xssfWorkbook.getSheet("笔记本");

        List<ProductSku> arrayList = new ArrayList<>();
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow.getCell(3) == null) {
                continue;
            }
            if (xssfRow.getCell(2) == null) {
                continue;
            }

            String k3ProductNo = xssfRow.getCell(0).toString(), productName = xssfRow.getCell(4).toString(), cpu = xssfRow.getCell(5).toString(), memory = null, hdd = null, ssd = null, graphics = xssfRow.getCell(9).toString(), size = xssfRow.getCell(10).toString();
            if (xssfRow.getCell(7) != null) {
                hdd = xssfRow.getCell(7).toString();
            }
            if (xssfRow.getCell(8) != null) {
                ssd = xssfRow.getCell(8).toString();
            }
            if (xssfRow.getCell(6) != null) {
                memory = xssfRow.getCell(6).toString();
            }

            List<Integer> propertyValueIdList = new ArrayList<>();
            // 判断CPU是否存在，如果不存在，添加，否则直接用
            ProductCategoryPropertyValueDO cpuProductCategoryPropertyValueDO = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(cpu, categoryId);
            if (cpuProductCategoryPropertyValueDO == null) {
                ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(cpu, 14, categoryId, "笔记本CPU" + cpu);
                productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
            } else {
                propertyValueIdList.add(cpuProductCategoryPropertyValueDO.getId());
            }
            if (memory != null) {
                ProductCategoryPropertyValueDO memoryProductCategoryPropertyValueDO = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(memory, categoryId);
                if (memoryProductCategoryPropertyValueDO == null) {
                    ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(memory, 15, categoryId, "笔记本内存" + memory);
                    productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                } else {
                    propertyValueIdList.add(memoryProductCategoryPropertyValueDO.getId());
                }
            }
            if (hdd != null) {
                ProductCategoryPropertyValueDO hddProductCategoryPropertyValueDO = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(hdd, categoryId);
                if (hddProductCategoryPropertyValueDO == null) {
                    ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(hdd, 16, categoryId, "笔记本械硬盘" + hdd);
                    productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                } else {
                    propertyValueIdList.add(hddProductCategoryPropertyValueDO.getId());
                }
            }
            if (ssd != null) {
                ProductCategoryPropertyValueDO ssdProductCategoryPropertyValueDO = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(ssd, categoryId);
                if (ssdProductCategoryPropertyValueDO == null) {
                    ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(ssd, 17, categoryId, "笔记本固态硬盘" + ssd);
                    productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                } else {
                    propertyValueIdList.add(ssdProductCategoryPropertyValueDO.getId());
                }
            }

            ProductCategoryPropertyValueDO graphicsProductCategoryPropertyValueDO = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(graphics, categoryId);
            if (graphicsProductCategoryPropertyValueDO == null) {
                ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(graphics, 18, categoryId, "笔记本显卡" + graphics);
                productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
            } else {
                propertyValueIdList.add(graphicsProductCategoryPropertyValueDO.getId());
            }

            ProductCategoryPropertyValueDO sizeProductCategoryPropertyValueDO = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(size, categoryId);
            if (sizeProductCategoryPropertyValueDO == null) {
                ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(size, 19, categoryId, "笔记本尺寸" + size);
                productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
            } else {
                propertyValueIdList.add(sizeProductCategoryPropertyValueDO.getId());
            }

            String brandName = xssfRow.getCell(2).toString();
            Integer brandId = brandMapper.findBrandIdByName(brandName);
            if (brandId == null) {
                System.out.println("笔记本品牌不存在，请查看, " + brandName);
                continue;
            }

            String productModel = xssfRow.getCell(3).toString();
            ProductDO productDO = productMapper.findExistsProduct(brandId, categoryId, productModel);
            // 商品不存在，直接添加，如果存在，直接判定SKU是否存在，如果存在则打印，如果不存在则添加进数据库即可
            if (productDO == null) {
                Product product = new Product();
                product.setProductName(productName);
                product.setK3ProductNo(k3ProductNo);
                product.setProductModel(productModel);
                product.setUnit(300016);
                product.setCategoryId(categoryId);
                product.setBrandId(brandId);
                product.setIsRent(CommonConstant.COMMON_CONSTANT_YES);
                product.setIsReturnAnyTime(CommonConstant.COMMON_CONSTANT_NO);

                List<ProductSkuProperty> productPropertyList = new ArrayList<>();
                ProductSkuProperty productSkuProperty = new ProductSkuProperty();
                productSkuProperty.setPropertyValueId(135);
                productPropertyList.add(productSkuProperty);
                product.setProductPropertyList(productPropertyList);

                List<ProductSku> productSkuList = new ArrayList<>();
                ProductSku productSku = new ProductSku();
                List<ProductSkuProperty> productSkuPropertyList = new ArrayList<>();
                for (Integer valueId : propertyValueIdList) {
                    ProductSkuProperty skuProperty = new ProductSkuProperty();
                    skuProperty.setPropertyValueId(valueId);
                    productSkuPropertyList.add(skuProperty);
                }
                productSku.setProductSkuPropertyList(productSkuPropertyList);

                productSku.setSkuPrice(new BigDecimal(xssfRow.getCell(11).toString()));
                productSku.setDayRentPrice(new BigDecimal(xssfRow.getCell(12).toString()));
                productSku.setMonthRentPrice(new BigDecimal(xssfRow.getCell(13).toString()));
                productSku.setNewSkuPrice(new BigDecimal(xssfRow.getCell(14).toString()));
                productSku.setNewDayRentPrice(new BigDecimal(xssfRow.getCell(15).toString()));
                productSku.setNewMonthRentPrice(new BigDecimal(xssfRow.getCell(16).toString()));


                productSkuList.add(productSku);

                product.setProductSkuList(productSkuList);

                ServiceResult<String, Integer> result = productService.addProduct(product);
                if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                    System.out.println("商品保存失败:" + productName);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            } else {
                Map<String, Object> maps = new HashMap<>();
                maps.put("productId", productDO.getId());
                maps.put("isSku", CommonConstant.COMMON_CONSTANT_YES);
                maps.put("propertyValueIdList", propertyValueIdList);
                maps.put("propertyValueIdCount", propertyValueIdList.size());
                Integer skuId = productSkuPropertyMapper.findSkuIdByParams(maps);
                if (skuId == null) {
                    Product product = productService.queryProductById(productDO.getId()).getResult();

                    List<ProductSku> productSkuList = product.getProductSkuList();
                    ProductSku productSku = new ProductSku();
                    List<ProductSkuProperty> productSkuPropertyList = new ArrayList<>();
                    for (Integer valueId : propertyValueIdList) {
                        ProductSkuProperty skuProperty = new ProductSkuProperty();
                        skuProperty.setPropertyValueId(valueId);
                        productSkuPropertyList.add(skuProperty);
                    }
                    productSku.setProductSkuPropertyList(productSkuPropertyList);

                    productSku.setProductId(product.getProductId());

                    productSku.setSkuPrice(new BigDecimal(xssfRow.getCell(11).toString()));
                    productSku.setDayRentPrice(new BigDecimal(xssfRow.getCell(12).toString()));
                    productSku.setMonthRentPrice(new BigDecimal(xssfRow.getCell(13).toString()));
                    productSku.setNewSkuPrice(new BigDecimal(xssfRow.getCell(14).toString()));
                    productSku.setNewDayRentPrice(new BigDecimal(xssfRow.getCell(15).toString()));
                    productSku.setNewMonthRentPrice(new BigDecimal(xssfRow.getCell(16).toString()));


                    productSkuList.add(productSku);

                    product.setProductSkuList(productSkuList);

                    ServiceResult<String, Integer> result = productService.updateProduct(product);
                    if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                        System.out.println("商品更新失败:" + productName);
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                }
            }
        }

        return ErrorCode.SUCCESS;
    }

    // 台式机
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String importAssemblyProduct(String filePath) throws Exception {
        Integer categoryId = 800004;
        FileInputStream fileIn = new FileInputStream(filePath);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileIn);
        XSSFSheet xssfSheet = xssfWorkbook.getSheet("台式机");

        List<ProductSku> arrayList = new ArrayList<>();
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow.getCell(3) == null) {
                continue;
            }
            if (xssfRow.getCell(2) == null) {
                continue;
            }

            String k3ProductNo = xssfRow.getCell(0).toString(), productName = xssfRow.getCell(4).toString(), cpu = xssfRow.getCell(5).toString(), memory = null, hdd = null, ssd = null, graphics = xssfRow.getCell(9).toString();
            if (xssfRow.getCell(7) != null) {
                hdd = xssfRow.getCell(7).toString();
            }
            if (xssfRow.getCell(8) != null) {
                ssd = xssfRow.getCell(8).toString();
            }
            if (xssfRow.getCell(6) != null) {
                memory = xssfRow.getCell(6).toString();
            }

            List<Integer> propertyValueIdList = new ArrayList<>();
            // 判断CPU是否存在，如果不存在，添加，否则直接用
            ProductCategoryPropertyValueDO cpuProductCategoryPropertyValueDO = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(cpu, categoryId);
            if (cpuProductCategoryPropertyValueDO == null) {
                ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(cpu, 1, categoryId, "台式机CPU" + cpu);
                productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
            } else {
                propertyValueIdList.add(cpuProductCategoryPropertyValueDO.getId());
            }
            if (memory != null) {
                ProductCategoryPropertyValueDO memoryProductCategoryPropertyValueDO = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(memory, categoryId);
                if (memoryProductCategoryPropertyValueDO == null) {
                    ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(memory, 2, categoryId, "台式机内存" + memory);
                    productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                } else {
                    propertyValueIdList.add(memoryProductCategoryPropertyValueDO.getId());
                }
            }
            if (hdd != null) {
                ProductCategoryPropertyValueDO hddProductCategoryPropertyValueDO = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(hdd, categoryId);
                if (hddProductCategoryPropertyValueDO == null) {
                    ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(hdd, 3, categoryId, "台式机机械硬盘" + hdd);
                    productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                } else {
                    propertyValueIdList.add(hddProductCategoryPropertyValueDO.getId());
                }
            }
            if (ssd != null) {
                ProductCategoryPropertyValueDO ssdProductCategoryPropertyValueDO = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(ssd, categoryId);
                if (ssdProductCategoryPropertyValueDO == null) {
                    ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(ssd, 4, categoryId, "台式机固态硬盘" + ssd);
                    productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
                } else {
                    propertyValueIdList.add(ssdProductCategoryPropertyValueDO.getId());
                }
            }

            ProductCategoryPropertyValueDO graphicsProductCategoryPropertyValueDO = productCategoryPropertyValueMapper.findByPropertyValueNameAndCategoryId(graphics, categoryId);
            if (graphicsProductCategoryPropertyValueDO == null) {
                ProductCategoryPropertyValueDO productCategoryPropertyValueDO = buildProductCategoryPropertyValueDO(graphics, 5, categoryId, "台式机显卡" + graphics);
                productCategoryPropertyValueMapper.save(productCategoryPropertyValueDO);
            } else {
                propertyValueIdList.add(graphicsProductCategoryPropertyValueDO.getId());
            }

            String brandName = xssfRow.getCell(2).toString();
            Integer brandId = brandMapper.findBrandIdByName(brandName);
            if (brandId == null) {
                System.out.println("台式机品牌不存在，请查看, " + brandName);
                continue;
            }

            String productModel = xssfRow.getCell(3).toString();
            ProductDO productDO = productMapper.findExistsProduct(brandId, categoryId, productModel);
            // 商品不存在，直接添加，如果存在，直接判定SKU是否存在，如果存在则打印，如果不存在则添加进数据库即可
            if (productDO == null) {
                Product product = new Product();
                product.setProductName(productName);
                product.setK3ProductNo(k3ProductNo);
                product.setProductModel(productModel);
                product.setUnit(300016);
                product.setCategoryId(categoryId);
                product.setBrandId(brandId);
                product.setIsRent(CommonConstant.COMMON_CONSTANT_YES);
                product.setIsReturnAnyTime(CommonConstant.COMMON_CONSTANT_NO);

                List<ProductSkuProperty> productPropertyList = new ArrayList<>();
                ProductSkuProperty productSkuProperty = new ProductSkuProperty();
                productSkuProperty.setPropertyValueId(46);
                productPropertyList.add(productSkuProperty);
                product.setProductPropertyList(productPropertyList);

                List<ProductSku> productSkuList = new ArrayList<>();
                ProductSku productSku = new ProductSku();
                List<ProductSkuProperty> productSkuPropertyList = new ArrayList<>();
                for (Integer valueId : propertyValueIdList) {
                    ProductSkuProperty skuProperty = new ProductSkuProperty();
                    skuProperty.setPropertyValueId(valueId);
                    productSkuPropertyList.add(skuProperty);
                }
                productSku.setProductSkuPropertyList(productSkuPropertyList);

                productSku.setSkuPrice(new BigDecimal(xssfRow.getCell(10).toString()));
                productSku.setDayRentPrice(new BigDecimal(xssfRow.getCell(11).toString()));
                productSku.setMonthRentPrice(new BigDecimal(xssfRow.getCell(12).toString()));
                productSku.setNewSkuPrice(new BigDecimal(xssfRow.getCell(13).toString()));
                productSku.setNewDayRentPrice(new BigDecimal(xssfRow.getCell(14).toString()));
                productSku.setNewMonthRentPrice(new BigDecimal(xssfRow.getCell(15).toString()));


                productSkuList.add(productSku);

                product.setProductSkuList(productSkuList);

                ServiceResult<String, Integer> result = productService.addProduct(product);
                if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                    System.out.println("商品保存失败:" + productName);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            } else {
                Map<String, Object> maps = new HashMap<>();
                maps.put("productId", productDO.getId());
                maps.put("isSku", CommonConstant.COMMON_CONSTANT_YES);
                maps.put("propertyValueIdList", propertyValueIdList);
                maps.put("propertyValueIdCount", propertyValueIdList.size());
                Integer skuId = productSkuPropertyMapper.findSkuIdByParams(maps);
                if (skuId == null) {
                    Product product = productService.queryProductById(productDO.getId()).getResult();

                    List<ProductSku> productSkuList = product.getProductSkuList();
                    ProductSku productSku = new ProductSku();
                    List<ProductSkuProperty> productSkuPropertyList = new ArrayList<>();
                    for (Integer valueId : propertyValueIdList) {
                        ProductSkuProperty skuProperty = new ProductSkuProperty();
                        skuProperty.setPropertyValueId(valueId);
                        productSkuPropertyList.add(skuProperty);
                    }
                    productSku.setProductSkuPropertyList(productSkuPropertyList);

                    productSku.setProductId(product.getProductId());
                    productSku.setSkuPrice(new BigDecimal(xssfRow.getCell(10).toString()));
                    productSku.setDayRentPrice(new BigDecimal(xssfRow.getCell(11).toString()));
                    productSku.setMonthRentPrice(new BigDecimal(xssfRow.getCell(12).toString()));
                    productSku.setNewSkuPrice(new BigDecimal(xssfRow.getCell(13).toString()));
                    productSku.setNewDayRentPrice(new BigDecimal(xssfRow.getCell(14).toString()));
                    productSku.setNewMonthRentPrice(new BigDecimal(xssfRow.getCell(15).toString()));


                    productSkuList.add(productSku);

                    product.setProductSkuList(productSkuList);

                    ServiceResult<String, Integer> result = productService.updateProduct(product);
                    if (!ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                        System.out.println("商品更新失败:" + productName);
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                }
            }
        }

        return ErrorCode.SUCCESS;
    }


    public static List removeDuplicate(List list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }
        return list;
    }

    ProductCategoryPropertyValueDO buildProductCategoryPropertyValueDO(String propertyValueName, Integer propertyId, Integer categoryId, String remark) {
        ProductCategoryPropertyValueDO productCategoryPropertyValueDO = new ProductCategoryPropertyValueDO();
        productCategoryPropertyValueDO.setPropertyValueName(propertyValueName);
        productCategoryPropertyValueDO.setCategoryId(categoryId);
        productCategoryPropertyValueDO.setPropertyId(propertyId);
        productCategoryPropertyValueDO.setDataOrder(0);
        productCategoryPropertyValueDO.setRemark(remark);
        productCategoryPropertyValueDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        productCategoryPropertyValueDO.setCreateTime(new Date());
        productCategoryPropertyValueDO.setUpdateTime(new Date());
        return productCategoryPropertyValueDO;
    }


}
