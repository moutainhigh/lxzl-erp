package com.lxzl.erp.core.service.k3;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.K3ReturnOrderCommitParam;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderQueryParam;
import com.lxzl.erp.core.service.VerifyReceiver;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 20:00 2018/4/10
 * @Modified By:
 */
public interface K3ReturnOrderService extends VerifyReceiver {
    /**
     * 创建K3退货单
     * @param k3ReturnOrder
     * @return
     */
    ServiceResult<String, String> createReturnOrder(K3ReturnOrder k3ReturnOrder);

    /**
     * 加入K3退货单
     * @param k3ReturnOrder
     * @return
     */
    ServiceResult<String, String> addReturnOrder(K3ReturnOrder k3ReturnOrder);

    /**
     * 删除K3退货单
     * @param k3ReturnOrderDetailId
     * @return
     */
    ServiceResult<String, String> deleteReturnOrder(Integer k3ReturnOrderDetailId);

    /**
     * 发送退货单到K3
     * @param returnOrderNo
     * @return
     */
    ServiceResult<String, String> sendReturnOrderToK3(String returnOrderNo);

    /**
     * 查询退货单信息
     * @param k3ReturnOrderQueryParam
     * @return
     */
    ServiceResult<String, Page<K3ReturnOrder>> queryReturnOrder(K3ReturnOrderQueryParam k3ReturnOrderQueryParam);

    /**
     * 根据编号查询退货信息
     * @param returnOrderNo
     * @return
     */
    ServiceResult<String, K3ReturnOrder> queryReturnOrderByNo(String returnOrderNo);

    /**
     * 编辑K3退货单
     * @param k3ReturnOrder
     * @return
     */
    ServiceResult<String, String> updateReturnOrder(K3ReturnOrder k3ReturnOrder);

    /**
     * 取消k3退货单
     * @param k3ReturnOrder
     * @return
     */
    ServiceResult<String, String> cancelK3ReturnOrder(K3ReturnOrder k3ReturnOrder);

    /**
     * 提交k3退货单
     * @param k3ReturnOrderCommitParam
     * @return
     */
    ServiceResult<String, String> commitK3ReturnOrder(K3ReturnOrderCommitParam k3ReturnOrderCommitParam);

    /**
     * 强制取消
     * @param returnOrderNo
     * @return
     */
    ServiceResult<String,String> strongCancelReturnOrder(String returnOrderNo);

    /**
     * 取消k3退货单（K3接口）
     *
     * @param returnOrderNo
     * @return
     */
    ServiceResult<String,String> revokeReturnOrder(String returnOrderNo);

	
    /**  
     * <p>
     * 导入k3历史退货单
     * </p>
     * <pre>
     *     所需参数示例及其说明
     *     参数名称 : 示例值 : 说明 : 是否必须
     *     pageNo : 1 : 当前导入的页数 : 是
     *     pageSize : 10 : 每页显示数量 : 是
     * </pre>
     * @author daiqi  
     * @date 2018/4/18 16:32
     * @param  k3ReturnOrderQueryParam
      
     * @return com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>  
     */  
    ServiceResult<String, Integer> importK3HistoricalRefundList(K3ReturnOrderQueryParam k3ReturnOrderQueryParam);


    ServiceResult<String, String> batchImportK3HistoricalRefundList(Integer startPage);
}
