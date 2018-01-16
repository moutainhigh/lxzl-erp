package com.lxzl.erp.common.domain.peerDeploymentOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.common.util.validate.constraints.In;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PeerDeploymentOrderProduct extends BasePO {

	private Integer peerDeploymentOrderProductId;   //唯一标识
	private Integer peerDeploymentOrderId;   //调拨单ID
	private String peerDeploymentOrderNo;   //货物调拨单编号
	@NotNull(message = ErrorCode.PRODUCT_SKU_NOT_NULL, groups = {AddGroup.class, UpdateGroup.class})
	private Integer productSkuId;   //货物调拨单商品SKU_ID
	@NotNull(message = ErrorCode.PEER_DEPLOYMENT_ORDER_UNIT_AMOUNT_NOT_NULL, groups = {AddGroup.class, UpdateGroup.class})
	private BigDecimal productUnitAmount;   //商品单价
	private BigDecimal productAmount;   //商品总价格
	@NotNull(message = ErrorCode.PEER_DEPLOYMENT_ORDER_COUNT_NOT_NULL, groups = {AddGroup.class, UpdateGroup.class})
	private Integer productSkuCount;   //货物调拨单商品SKU数量
	private String productSkuSnapshot;   //货物调拨单商品SKU快照
	@NotNull(message = ErrorCode.PEER_DEPLOYMENT_ORDER_IS_NEW_NOT_NULL, groups = {AddGroup.class, UpdateGroup.class})
	@In(value = {CommonConstant.YES, CommonConstant.NO}, message = ErrorCode.IS_NEW_VALUE_ERROR, groups = {AddGroup.class, UpdateGroup.class})
	private Integer isNew;   //是否全新机
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人

	private List<PeerDeploymentOrderProductEquipment> peerDeploymentOrderProductEquipmentList;


	public Integer getPeerDeploymentOrderProductId(){
		return peerDeploymentOrderProductId;
	}

	public void setPeerDeploymentOrderProductId(Integer peerDeploymentOrderProductId){ this.peerDeploymentOrderProductId = peerDeploymentOrderProductId; }

	public Integer getPeerDeploymentOrderId(){
		return peerDeploymentOrderId;
	}

	public void setPeerDeploymentOrderId(Integer peerDeploymentOrderId){ this.peerDeploymentOrderId = peerDeploymentOrderId; }

	public String getPeerDeploymentOrderNo(){
		return peerDeploymentOrderNo;
	}

	public void setPeerDeploymentOrderNo(String peerDeploymentOrderNo){ this.peerDeploymentOrderNo = peerDeploymentOrderNo; }

	public Integer getProductSkuId(){
		return productSkuId;
	}

	public void setProductSkuId(Integer productSkuId){
		this.productSkuId = productSkuId;
	}

	public BigDecimal getProductUnitAmount(){
		return productUnitAmount;
	}

	public void setProductUnitAmount(BigDecimal productUnitAmount){
		this.productUnitAmount = productUnitAmount;
	}

	public BigDecimal getProductAmount(){
		return productAmount;
	}

	public void setProductAmount(BigDecimal productAmount){
		this.productAmount = productAmount;
	}

	public Integer getProductSkuCount(){
		return productSkuCount;
	}

	public void setProductSkuCount(Integer productSkuCount){
		this.productSkuCount = productSkuCount;
	}

	public String getProductSkuSnapshot(){
		return productSkuSnapshot;
	}

	public void setProductSkuSnapshot(String productSkuSnapshot){
		this.productSkuSnapshot = productSkuSnapshot;
	}

	public Integer getIsNew(){
		return isNew;
	}

	public void setIsNew(Integer isNew){
		this.isNew = isNew;
	}

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
	}

	public String getRemark(){
		return remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public String getCreateUser(){
		return createUser;
	}

	public void setCreateUser(String createUser){
		this.createUser = createUser;
	}

	public Date getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}

	public String getUpdateUser(){
		return updateUser;
	}

	public void setUpdateUser(String updateUser){
		this.updateUser = updateUser;
	}

}