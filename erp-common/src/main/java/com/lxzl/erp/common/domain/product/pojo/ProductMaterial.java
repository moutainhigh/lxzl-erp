package com.lxzl.erp.common.domain.product.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductMaterial extends BasePO {

	private Integer productMaterialId;   //唯一标识
	private Integer productId;   //商品ID
	private Integer productSkuId;   //商品SKU ID
	private Integer materialId;   //物料ID
	private Integer materialType;   //物料类型
	private Integer materialCount;   //物料总数
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注

	private String materialNo;
	private String materialName;
	private Double materialCapacityValue;
	private Integer materialModelId;

	public Integer getProductMaterialId(){
		return productMaterialId;
	}

	public void setProductMaterialId(Integer productMaterialId){
		this.productMaterialId = productMaterialId;
	}

	public Integer getProductId(){
		return productId;
	}

	public void setProductId(Integer productId){
		this.productId = productId;
	}

	public Integer getProductSkuId(){
		return productSkuId;
	}

	public void setProductSkuId(Integer productSkuId){
		this.productSkuId = productSkuId;
	}

	public Integer getMaterialId(){
		return materialId;
	}

	public void setMaterialId(Integer materialId){
		this.materialId = materialId;
	}

	public Integer getMaterialCount(){
		return materialCount;
	}

	public void setMaterialCount(Integer materialCount){
		this.materialCount = materialCount;
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

	public String getMaterialNo() {
		return materialNo;
	}

	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}

	public Integer getMaterialType() {
		return materialType;
	}

	public void setMaterialType(Integer materialType) {
		this.materialType = materialType;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public Double getMaterialCapacityValue() {
		return materialCapacityValue;
	}

	public void setMaterialCapacityValue(Double materialCapacityValue) {
		this.materialCapacityValue = materialCapacityValue;
	}

	public Integer getMaterialModelId() {
		return materialModelId;
	}

	public void setMaterialModelId(Integer materialModelId) {
		this.materialModelId = materialModelId;
	}
}