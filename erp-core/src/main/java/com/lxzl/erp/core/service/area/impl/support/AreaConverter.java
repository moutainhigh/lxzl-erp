package com.lxzl.erp.core.service.area.impl.support;

import com.lxzl.erp.common.domain.area.AreaCity;
import com.lxzl.erp.common.domain.area.AreaDistrict;
import com.lxzl.erp.common.domain.area.AreaProvince;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.dataaccess.domain.area.AreaCityDO;
import com.lxzl.erp.dataaccess.domain.area.AreaDistrictDO;
import com.lxzl.erp.dataaccess.domain.area.AreaProvinceDO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;


public class AreaConverter {

    public static AreaDistrict convertDistrictCityDO(AreaDistrictDO areaDistrictDO){
        AreaDistrict areaDistrict = new AreaDistrict();
        BeanUtils.copyProperties(areaDistrictDO,areaDistrict);
        areaDistrict.setAreaDistrictId(areaDistrictDO.getId());
        return areaDistrict;
    }
    public static List<AreaDistrict> convertAreaDistrictDOList(List<AreaDistrictDO> areaDistrictDOList){
        List<AreaDistrict> areaDistrictList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(areaDistrictDOList)){
            for(AreaDistrictDO areaDistrictDO :areaDistrictDOList){
                areaDistrictList.add(convertDistrictCityDO(areaDistrictDO));
            }
        }
        return areaDistrictList;
    }

    public static AreaCity convertAreaCityDO(AreaCityDO areaCityDO){
        AreaCity areaCity = new AreaCity();
        BeanUtils.copyProperties(areaCityDO,areaCity);
        areaCity.setAreaCityId(areaCityDO.getId());
        List<AreaDistrictDO> areaDistrictDOList = areaCityDO.getAreaDistrictDOList();
        if(CollectionUtil.isNotEmpty(areaDistrictDOList)){
            areaCity.setAreaDistrictList(convertAreaDistrictDOList(areaDistrictDOList));
        }
        return areaCity;
    }
    public static List<AreaCity> convertAreaCityDOList(List<AreaCityDO> areaCityDOList){
        List<AreaCity> areaCityList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(areaCityDOList)){
            for(AreaCityDO areaCityDO :areaCityDOList){
                areaCityList.add(convertAreaCityDO(areaCityDO));
            }
        }
        return areaCityList;
    }

    public static AreaProvince convertAreaProvinceDO(AreaProvinceDO areaProvinceDO){
        AreaProvince areaProvince = new AreaProvince();
        BeanUtils.copyProperties(areaProvinceDO,areaProvince);
        areaProvince.setAreaProvinceId(areaProvinceDO.getId());
        List<AreaCityDO> areaCityDOList = areaProvinceDO.getAreaCityDOList();
        List<AreaCity> areaCityList = convertAreaCityDOList(areaCityDOList);
        areaProvince.setAreaCityList(areaCityList);
        return areaProvince;
    }
    public static List<AreaProvince> convertAreaProvinceDOList(List<AreaProvinceDO> areaProvinceDOList){
        List<AreaProvince> areaProvinceList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(areaProvinceDOList)) {
            for (AreaProvinceDO areaProvinceDO : areaProvinceDOList) {
                areaProvinceList.add(convertAreaProvinceDO(areaProvinceDO));
            }
        }
        return areaProvinceList;
    }
}
