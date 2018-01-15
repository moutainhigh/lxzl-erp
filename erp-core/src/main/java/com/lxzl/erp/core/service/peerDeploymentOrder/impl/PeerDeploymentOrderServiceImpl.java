package com.lxzl.erp.core.service.peerDeploymentOrder.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.PeerDeploymentOrderRentType;
import com.lxzl.erp.common.constant.PeerDeploymentOrderStatus;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.peerDeploymentOrder.pojo.PeerDeploymentOrder;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.*;
import com.lxzl.erp.core.service.basic.impl.support.GenerateNoSupport;
import com.lxzl.erp.core.service.material.MaterialService;
import com.lxzl.erp.core.service.peerDeploymentOrder.PeerDeploymentOrderService;
import com.lxzl.erp.core.service.product.ProductService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.core.service.warehouse.impl.support.WarehouseSupport;
import com.lxzl.erp.dataaccess.dao.mysql.peer.PeerMapper;
import com.lxzl.erp.dataaccess.dao.mysql.peerDeploymentOrder.PeerDeploymentOrderConsignInfoMapper;
import com.lxzl.erp.dataaccess.dao.mysql.peerDeploymentOrder.PeerDeploymentOrderMapper;
import com.lxzl.erp.dataaccess.dao.mysql.peerDeploymentOrder.PeerDeploymentOrderMaterialMapper;
import com.lxzl.erp.dataaccess.dao.mysql.peerDeploymentOrder.PeerDeploymentOrderProductMapper;
import com.lxzl.erp.dataaccess.domain.peer.PeerDO;
import com.lxzl.erp.dataaccess.domain.peerDeploymentOrder.PeerDeploymentOrderConsignInfoDO;
import com.lxzl.erp.dataaccess.domain.peerDeploymentOrder.PeerDeploymentOrderDO;
import com.lxzl.erp.dataaccess.domain.peerDeploymentOrder.PeerDeploymentOrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.peerDeploymentOrder.PeerDeploymentOrderProductDO;
import com.lxzl.erp.dataaccess.domain.warehouse.WarehouseDO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: kai
 * @Description：
 * @Date: Created in 15:39 2018/1/13
 * @Modified By:
 */
@Service
public class PeerDeploymentOrderServiceImpl implements PeerDeploymentOrderService {

    private static Logger logger = LoggerFactory.getLogger(PeerDeploymentOrderServiceImpl.class);

    @Autowired
    private UserSupport userSupport;
    @Autowired
    private PeerDeploymentOrderMapper peerDeploymentOrderMapper;
    @Autowired
    private PeerMapper peerMapper;
    @Autowired
    private WarehouseSupport warehouseSupport;
    @Autowired
    private GenerateNoSupport generateNoSupport;
    @Autowired
    private PeerDeploymentOrderProductMapper peerDeploymentOrderProductMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private PeerDeploymentOrderMaterialMapper peerDeploymentOrderMaterialMapper;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private PeerDeploymentOrderConsignInfoMapper peerDeploymentOrderConsignInfoMapper;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ServiceResult<String, String> createPeerDeploymentOrder(PeerDeploymentOrder peerDeploymentOrder) {
        ServiceResult<String, String> result = new ServiceResult<>();
        User loginUser = userSupport.getCurrentUser();
        Date currentTime = new Date();

        PeerDO peerDO = peerMapper.findById(peerDeploymentOrder.getPeerId());

        //判断传入的仓库是否存在，同时查看当前操作是否有权操作此仓库
        WarehouseDO warehouseDO = warehouseSupport.getAvailableWarehouse(peerDeploymentOrder.getWarehouseId());
        if (warehouseDO == null) {
            result.setErrorCode(ErrorCode.WAREHOUSE_NOT_EXISTS);
            return result;
        }
        //判断天租还是月租--计算预计归还时间
//        Date rentStartTime = peerDeploymentOrder.getRentStartTime();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(rentStartTime);
//        if(peerDeploymentOrder.getRentType() == PeerDeploymentOrderRentType.RENT_TYPE_DAY){
//            calendar.add(Calendar.DATE,peerDeploymentOrder.getRentTimeLength());
//            rentStartTime = calendar.getTime();
//            peerDeploymentOrder.setExpectReturnTime(rentStartTime);
//        }else{
//            calendar.add(Calendar.MONTH,peerDeploymentOrder.getRentTimeLength());
//            calendar.add(Calendar.DATE,-1);
//            rentStartTime = calendar.getTime();
//            peerDeploymentOrder.setExpectReturnTime(rentStartTime);
//        }

        Date expectReturnTime = peerDeploymentOrderExpectReturnTime(peerDeploymentOrder.getRentStartTime(), peerDeploymentOrder.getRentType(), peerDeploymentOrder.getRentTimeLength());

        PeerDeploymentOrderDO peerDeploymentOrderDO = ConverterUtil.convert(peerDeploymentOrder,PeerDeploymentOrderDO.class);
        peerDeploymentOrderDO.setPeerDeploymentOrderNo(generateNoSupport.generatePeerDeploymentOrderNo(currentTime,peerDO.getId()));
        peerDeploymentOrderDO.setPeerDeploymentOrderStatus(PeerDeploymentOrderStatus.PEER_DEPLOYMENT_ORDER_STATUS_WAIT_COMMIT);
        peerDeploymentOrderDO.setExpectReturnTime(expectReturnTime);
        peerDeploymentOrderDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        peerDeploymentOrderDO.setCreateTime(currentTime);
        peerDeploymentOrderDO.setCreateUser(loginUser.getUserId().toString());
        peerDeploymentOrderDO.setUpdateTime(currentTime);
        peerDeploymentOrderDO.setUpdateUser(loginUser.getUserId().toString());
        peerDeploymentOrderMapper.save(peerDeploymentOrderDO);
        savePeerDeploymentOrderProductInfo(ConverterUtil.convertList(peerDeploymentOrder.getPeerDeploymentOrderProductList(),PeerDeploymentOrderProductDO.class),peerDeploymentOrderDO.getPeerDeploymentOrderNo(),loginUser,currentTime);
        savePeerDeploymentOrderMaterialInfo(ConverterUtil.convertList(peerDeploymentOrder.getPeerDeploymentOrderMaterialList(), PeerDeploymentOrderMaterialDO.class), peerDeploymentOrderDO.getPeerDeploymentOrderNo(),loginUser, currentTime);
        savePeerDeploymentOrderConsignInfo(ConverterUtil.convert(peerDeploymentOrder.getPeerDeploymentOrderConsignInfo(),PeerDeploymentOrderConsignInfoDO.class), peerDeploymentOrderDO.getId(), loginUser, currentTime);

        PeerDeploymentOrderDO newestPeerDeploymentOrderDO = peerDeploymentOrderMapper.findByPeerDeploymentOrderNo(peerDeploymentOrderDO.getPeerDeploymentOrderNo());
        for (PeerDeploymentOrderProductDO peerDeploymentOrderProductDO : newestPeerDeploymentOrderDO.getPeerDeploymentOrderProductDOList()) {
            peerDeploymentOrderDO.setTotalProductCount(peerDeploymentOrderDO.getTotalProductCount() == null ? peerDeploymentOrderProductDO.getProductSkuCount() : (peerDeploymentOrderDO.getTotalProductCount() + peerDeploymentOrderProductDO.getProductSkuCount()));
            peerDeploymentOrderDO.setTotalProductAmount(BigDecimalUtil.add(peerDeploymentOrderDO.getTotalProductAmount(), peerDeploymentOrderProductDO.getProductAmount()));
        }
        for (PeerDeploymentOrderMaterialDO peerDeploymentOrderMaterialDO : newestPeerDeploymentOrderDO.getPeerDeploymentOrderMaterialDOList()) {
            peerDeploymentOrderDO.setTotalMaterialCount(peerDeploymentOrderDO.getTotalMaterialCount() == null ? peerDeploymentOrderMaterialDO.getProductMaterialCount() : (peerDeploymentOrderDO.getTotalMaterialCount() + peerDeploymentOrderMaterialDO.getProductMaterialCount()));
            peerDeploymentOrderDO.setTotalMaterialAmount(BigDecimalUtil.add(peerDeploymentOrderDO.getTotalMaterialAmount(), peerDeploymentOrderMaterialDO.getMaterialAmount()));
        }
        peerDeploymentOrderDO.setTotalOrderAmount(BigDecimalUtil.add(peerDeploymentOrderDO.getTotalProductAmount(), peerDeploymentOrderDO.getTotalMaterialAmount()));
        peerDeploymentOrderDO.setTotalOrderAmount(BigDecimalUtil.sub(peerDeploymentOrderDO.getTotalOrderAmount(), peerDeploymentOrderDO.getTotalDiscountAmount()));
        peerDeploymentOrderMapper.update(peerDeploymentOrderDO);

        result.setResult(peerDeploymentOrderDO.getPeerDeploymentOrderNo());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, PeerDeploymentOrder> detailPeerDeploymentOrderNo(String peerDeploymentOrderNo) {
        return null;
    }

    @Override
    public boolean receiveVerifyResult(boolean verifyResult, String businessNo) {

        return false;
    }

    private void savePeerDeploymentOrderProductInfo(List<PeerDeploymentOrderProductDO> peerDeploymentOrderProductDOList, String peerDeploymentOrderNo, User loginUser, Date currentTime) {
        Map<Integer, PeerDeploymentOrderProductDO> savePeerDeploymentOrderProductDOMap = new HashMap<>();
        Map<Integer, PeerDeploymentOrderProductDO> updatePeerDeploymentOrderProductDOMap = new HashMap<>();
        List<PeerDeploymentOrderProductDO> dbPeerDeploymentOrderProductDOList = peerDeploymentOrderProductMapper.findByPeerDeploymentOrderNo(peerDeploymentOrderNo);
        Map<Integer, PeerDeploymentOrderProductDO> dbPeerDeploymentOrderProductDOMap = ListUtil.listToMap(dbPeerDeploymentOrderProductDOList, "id");
        if (CollectionUtil.isNotEmpty(peerDeploymentOrderProductDOList)) {
            for (PeerDeploymentOrderProductDO peerDeploymentOrderProductDO : peerDeploymentOrderProductDOList) {
                if (dbPeerDeploymentOrderProductDOMap.get(peerDeploymentOrderProductDO.getId()) != null) {
                    updatePeerDeploymentOrderProductDOMap.put(peerDeploymentOrderProductDO.getId(), peerDeploymentOrderProductDO);
                    dbPeerDeploymentOrderProductDOMap.remove(peerDeploymentOrderProductDO.getId());
                } else {
                    savePeerDeploymentOrderProductDOMap.put(peerDeploymentOrderProductDO.getId(), peerDeploymentOrderProductDO);
                }
            }
        }

        if (savePeerDeploymentOrderProductDOMap.size() > 0) {
            List<PeerDeploymentOrderProductDO> saveList = new ArrayList<>();
            for (Map.Entry<Integer, PeerDeploymentOrderProductDO> entry : savePeerDeploymentOrderProductDOMap.entrySet()) {
                PeerDeploymentOrderProductDO peerDeploymentOrderProductDO = entry.getValue();
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(peerDeploymentOrderProductDO.getProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                    throw new BusinessException(productServiceResult.getErrorCode());
                }
                Product product = productServiceResult.getResult();
                peerDeploymentOrderProductDO.setProductAmount(BigDecimalUtil.mul(peerDeploymentOrderProductDO.getProductUnitAmount(), new BigDecimal(peerDeploymentOrderProductDO.getProductSkuCount())));
                peerDeploymentOrderProductDO.setProductSkuSnapshot(FastJsonUtil.toJSONString(product));
                peerDeploymentOrderProductDO.setPeerDeploymentOrderNo(peerDeploymentOrderNo);
                peerDeploymentOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                peerDeploymentOrderProductDO.setUpdateUser(loginUser.getUserId().toString());
                peerDeploymentOrderProductDO.setCreateUser(loginUser.getUserId().toString());
                peerDeploymentOrderProductDO.setUpdateTime(currentTime);
                peerDeploymentOrderProductDO.setCreateTime(currentTime);
                saveList.add(peerDeploymentOrderProductDO);
            }
            peerDeploymentOrderProductMapper.saveList(saveList);
        }

        if (updatePeerDeploymentOrderProductDOMap.size() > 0) {
            for (Map.Entry<Integer, PeerDeploymentOrderProductDO> entry : updatePeerDeploymentOrderProductDOMap.entrySet()) {
                PeerDeploymentOrderProductDO peerDeploymentOrderProductDO = entry.getValue();
                PeerDeploymentOrderProductDO oldPeerDeploymentOrderProductDO = peerDeploymentOrderProductMapper.findByPeerDeploymentOrderNoAndSkuId(peerDeploymentOrderNo, peerDeploymentOrderProductDO.getProductSkuId());
                if (oldPeerDeploymentOrderProductDO == null) {
                    throw new BusinessException(ErrorCode.RECORD_NOT_EXISTS);
                }
                ServiceResult<String, Product> productServiceResult = productService.queryProductBySkuId(peerDeploymentOrderProductDO.getProductSkuId());
                if (!ErrorCode.SUCCESS.equals(productServiceResult.getErrorCode())) {
                    throw new BusinessException(productServiceResult.getErrorCode());
                }
                Product product = productServiceResult.getResult();
                peerDeploymentOrderProductDO.setId(oldPeerDeploymentOrderProductDO.getId());
                peerDeploymentOrderProductDO.setProductAmount(BigDecimalUtil.mul(peerDeploymentOrderProductDO.getProductUnitAmount(), new BigDecimal(peerDeploymentOrderProductDO.getProductSkuCount())));
                peerDeploymentOrderProductDO.setProductSkuSnapshot(FastJsonUtil.toJSONString(product));
                peerDeploymentOrderProductDO.setUpdateUser(loginUser.getUserId().toString());
                peerDeploymentOrderProductDO.setUpdateTime(currentTime);
                peerDeploymentOrderProductMapper.update(peerDeploymentOrderProductDO);
            }
        }

        if (dbPeerDeploymentOrderProductDOMap.size() > 0) {
            for (Map.Entry<Integer, PeerDeploymentOrderProductDO> entry : dbPeerDeploymentOrderProductDOMap.entrySet()) {
                PeerDeploymentOrderProductDO peerDeploymentOrderProductDO = entry.getValue();
                peerDeploymentOrderProductDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                peerDeploymentOrderProductDO.setUpdateUser(loginUser.getUserId().toString());
                peerDeploymentOrderProductDO.setUpdateTime(currentTime);
                peerDeploymentOrderProductMapper.update(peerDeploymentOrderProductDO);
            }
        }
    }

    private void savePeerDeploymentOrderMaterialInfo(List<PeerDeploymentOrderMaterialDO> peerDeploymentOrderMaterialDOList, String peerDeploymentOrderNo, User loginUser, Date currentTime) {
        Map<Integer, PeerDeploymentOrderMaterialDO> savePeerDeploymentOrderMaterialDOMap = new HashMap<>();
        Map<Integer, PeerDeploymentOrderMaterialDO> updatePeerDeploymentOrderMaterialDOMap = new HashMap<>();
        List<PeerDeploymentOrderMaterialDO> dbPeerDeploymentOrderMaterialDOList = peerDeploymentOrderMaterialMapper.findByPeerDeploymentOrderNo(peerDeploymentOrderNo);
        Map<Integer, PeerDeploymentOrderMaterialDO> dbPeerDeploymentOrderMaterialDOMap = ListUtil.listToMap(dbPeerDeploymentOrderMaterialDOList, "id");
        if (CollectionUtil.isNotEmpty(peerDeploymentOrderMaterialDOList)) {
            for (PeerDeploymentOrderMaterialDO peerDeploymentOrderMaterialDO : peerDeploymentOrderMaterialDOList) {
                if (dbPeerDeploymentOrderMaterialDOMap.get(peerDeploymentOrderMaterialDO.getId()) != null) {
                    updatePeerDeploymentOrderMaterialDOMap.put(peerDeploymentOrderMaterialDO.getId(), peerDeploymentOrderMaterialDO);
                    dbPeerDeploymentOrderMaterialDOMap.remove(peerDeploymentOrderMaterialDO.getId());
                } else {
                    savePeerDeploymentOrderMaterialDOMap.put(peerDeploymentOrderMaterialDO.getId(), peerDeploymentOrderMaterialDO);
                }
            }
        }

        if (savePeerDeploymentOrderMaterialDOMap.size() > 0) {
            List<PeerDeploymentOrderMaterialDO> saveList = new ArrayList<>();
            for (Map.Entry<Integer, PeerDeploymentOrderMaterialDO> entry : savePeerDeploymentOrderMaterialDOMap.entrySet()) {
                PeerDeploymentOrderMaterialDO peerDeploymentOrderMaterialDO = entry.getValue();
                ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(peerDeploymentOrderMaterialDO.getMaterialId());
                if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())) {
                    throw new BusinessException(materialServiceResult.getErrorCode());
                }
                Material material = materialServiceResult.getResult();
                peerDeploymentOrderMaterialDO.setMaterialAmount(BigDecimalUtil.mul(peerDeploymentOrderMaterialDO.getMaterialUnitAmount(), new BigDecimal(peerDeploymentOrderMaterialDO.getProductMaterialCount())));
                peerDeploymentOrderMaterialDO.setProductMaterialSnapshot(FastJsonUtil.toJSONString(material));
                peerDeploymentOrderMaterialDO.setPeerDeploymentOrderNo(peerDeploymentOrderNo);
                peerDeploymentOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                peerDeploymentOrderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                peerDeploymentOrderMaterialDO.setCreateUser(loginUser.getUserId().toString());
                peerDeploymentOrderMaterialDO.setUpdateTime(currentTime);
                peerDeploymentOrderMaterialDO.setCreateTime(currentTime);
                saveList.add(peerDeploymentOrderMaterialDO);
            }
            peerDeploymentOrderMaterialMapper.saveList(saveList);
        }

        if (updatePeerDeploymentOrderMaterialDOMap.size() > 0) {
            for (Map.Entry<Integer, PeerDeploymentOrderMaterialDO> entry : updatePeerDeploymentOrderMaterialDOMap.entrySet()) {
                PeerDeploymentOrderMaterialDO peerDeploymentOrderMaterialDO = entry.getValue();
                ServiceResult<String, Material> materialServiceResult = materialService.queryMaterialById(peerDeploymentOrderMaterialDO.getMaterialId());
                if (!ErrorCode.SUCCESS.equals(materialServiceResult.getErrorCode())) {
                    throw new BusinessException(materialServiceResult.getErrorCode());
                }
                Material material = materialServiceResult.getResult();
                peerDeploymentOrderMaterialDO.setMaterialAmount(BigDecimalUtil.mul(peerDeploymentOrderMaterialDO.getMaterialUnitAmount(), new BigDecimal(peerDeploymentOrderMaterialDO.getProductMaterialCount())));
                peerDeploymentOrderMaterialDO.setProductMaterialSnapshot(FastJsonUtil.toJSONString(material));
                peerDeploymentOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                peerDeploymentOrderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                peerDeploymentOrderMaterialDO.setUpdateTime(currentTime);
                peerDeploymentOrderMaterialMapper.update(peerDeploymentOrderMaterialDO);
            }
        }

        if (dbPeerDeploymentOrderMaterialDOMap.size() > 0) {
            for (Map.Entry<Integer, PeerDeploymentOrderMaterialDO> entry : dbPeerDeploymentOrderMaterialDOMap.entrySet()) {
                PeerDeploymentOrderMaterialDO peerDeploymentOrderMaterialDO = entry.getValue();
                peerDeploymentOrderMaterialDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                peerDeploymentOrderMaterialDO.setUpdateUser(loginUser.getUserId().toString());
                peerDeploymentOrderMaterialDO.setUpdateTime(currentTime);
                peerDeploymentOrderMaterialMapper.update(peerDeploymentOrderMaterialDO);
            }
        }
    }

    private void savePeerDeploymentOrderConsignInfo(PeerDeploymentOrderConsignInfoDO peerDeploymentOrderConsignInfoDO, Integer peerDeploymentOrderId, User loginUser, Date currentTime) {

        PeerDeploymentOrderConsignInfoDO dbPeerDeploymentOrderConsignInfoDO = peerDeploymentOrderConsignInfoMapper.findByPeerDeploymentOrderConsignInfoId(peerDeploymentOrderId);


        if (dbPeerDeploymentOrderConsignInfoDO == null) {
            peerDeploymentOrderConsignInfoDO.setPeerDeploymentOrderId(peerDeploymentOrderId);
            peerDeploymentOrderConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            peerDeploymentOrderConsignInfoDO.setCreateUser(loginUser.getUserId().toString());
            peerDeploymentOrderConsignInfoDO.setUpdateUser(loginUser.getUserId().toString());
            peerDeploymentOrderConsignInfoDO.setCreateTime(currentTime);
            peerDeploymentOrderConsignInfoDO.setUpdateTime(currentTime);
            peerDeploymentOrderConsignInfoMapper.save(peerDeploymentOrderConsignInfoDO);
        } else {
            dbPeerDeploymentOrderConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            dbPeerDeploymentOrderConsignInfoDO.setId(dbPeerDeploymentOrderConsignInfoDO.getId());
            dbPeerDeploymentOrderConsignInfoDO.setUpdateUser(loginUser.getUserId().toString());
            dbPeerDeploymentOrderConsignInfoDO.setUpdateTime(currentTime);
            peerDeploymentOrderConsignInfoMapper.update(dbPeerDeploymentOrderConsignInfoDO);

            peerDeploymentOrderConsignInfoDO.setPeerDeploymentOrderId(peerDeploymentOrderId);
            peerDeploymentOrderConsignInfoDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            peerDeploymentOrderConsignInfoDO.setCreateUser(loginUser.getUserId().toString());
            peerDeploymentOrderConsignInfoDO.setUpdateUser(loginUser.getUserId().toString());
            peerDeploymentOrderConsignInfoDO.setCreateTime(currentTime);
            peerDeploymentOrderConsignInfoDO.setUpdateTime(currentTime);
            peerDeploymentOrderConsignInfoMapper.save(peerDeploymentOrderConsignInfoDO);

        }
    }

    /**
     * 计算订单预计归还时间
     */
    private Date peerDeploymentOrderExpectReturnTime(Date rentStartTime, Integer rentType, Integer rentTimeLength) {
        if (PeerDeploymentOrderRentType.RENT_TYPE_DAY.equals(rentType)) {
            return DateUtil.dateInterval(rentStartTime, rentTimeLength - 1);
        } else if (PeerDeploymentOrderRentType.RENT_TYPE_MONTH.equals(rentType)) {
            return DateUtil.dateInterval(DateUtil.monthInterval(rentStartTime, rentTimeLength), -1);
        }
        return null;
    }

}
