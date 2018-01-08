package com.lxzl.erp.dataaccess.domain.assembleOder;

import com.lxzl.erp.common.domain.assembleOder.pojo.AssembleOrderMaterial;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.util.List;


public class AssembleOrderDO  extends BaseDO {

	private Integer id;
	private String assembleOrderNo;
	private Integer assembleProductId;
	private Integer assembleProductSkuId;
	private Integer assembleProductCount;
	private Integer warehouseId;
	private Integer assembleOrderStatus;
	private Integer dataStatus;
	private String remark;
	@Transient
	private List<AssembleOrderMaterialDO> assembleOrderMaterialDOList;  //组装单配件

	public List<AssembleOrderMaterialDO> getAssembleOrderMaterialDOList() {
		return assembleOrderMaterialDOList;
	}

	public void setAssembleOrderMaterialDOList(List<AssembleOrderMaterialDO> assembleOrderMaterialDOList) {
		this.assembleOrderMaterialDOList = assembleOrderMaterialDOList;
	}

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getAssembleOrderNo(){
		return assembleOrderNo;
	}

	public void setAssembleOrderNo(String assembleOrderNo){
		this.assembleOrderNo = assembleOrderNo;
	}

	public Integer getAssembleProductId(){
		return assembleProductId;
	}

	public void setAssembleProductId(Integer assembleProductId){
		this.assembleProductId = assembleProductId;
	}

	public Integer getAssembleProductSkuId(){
		return assembleProductSkuId;
	}

	public void setAssembleProductSkuId(Integer assembleProductSkuId){
		this.assembleProductSkuId = assembleProductSkuId;
	}

	public Integer getAssembleProductCount(){
		return assembleProductCount;
	}

	public void setAssembleProductCount(Integer assembleProductCount){
		this.assembleProductCount = assembleProductCount;
	}

	public Integer getWarehouseId(){
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId){
		this.warehouseId = warehouseId;
	}

	public Integer getAssembleOrderStatus(){
		return assembleOrderStatus;
	}

	public void setAssembleOrderStatus(Integer assembleOrderStatus){
		this.assembleOrderStatus = assembleOrderStatus;
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

}