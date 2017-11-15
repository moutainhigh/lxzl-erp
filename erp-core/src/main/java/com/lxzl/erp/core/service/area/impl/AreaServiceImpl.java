package com.lxzl.erp.core.service.area.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.area.AreaCity;
import com.lxzl.erp.common.domain.area.AreaDistrict;
import com.lxzl.erp.common.domain.area.AreaProvince;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.core.service.area.AreaService;
import com.lxzl.erp.core.service.area.impl.support.AreaConverter;
import com.lxzl.erp.dataaccess.dao.mysql.area.AreaCityMapper;
import com.lxzl.erp.dataaccess.dao.mysql.area.AreaDistrictMapper;
import com.lxzl.erp.dataaccess.dao.mysql.area.AreaProvinceMapper;
import com.lxzl.erp.dataaccess.domain.area.AreaCityDO;
import com.lxzl.erp.dataaccess.domain.area.AreaDistrictDO;
import com.lxzl.erp.dataaccess.domain.area.AreaProvinceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaProvinceMapper areaProvinceMapper;
    @Autowired
    private AreaCityMapper areaCityMapper;
    @Autowired
    private AreaDistrictMapper areaDistrictMapper;
    @Override
    public ServiceResult<String,  List<AreaProvince>> getAreaList() {
        ServiceResult<String,  List<AreaProvince>> serviceResult = new ServiceResult<>();
        Map<String,Object> map = new HashMap<>();
        map.put("start",0);
        map.put("pageSize",Integer.MAX_VALUE);
        List<AreaProvinceDO> areaProvinceDOList = areaProvinceMapper.listPage(map);
        List<AreaCityDO> areaCityDOList = areaCityMapper.listPage(map);
        List<AreaDistrictDO> areaDistrictDOList = areaDistrictMapper.listPage(map);

        List<AreaProvince> areaProvinceList = AreaConverter.convertAreaProvinceDOList(areaProvinceDOList);
        List<AreaCity> areaCityList = AreaConverter.convertAreaCityDOList(areaCityDOList);
        List<AreaDistrict> areaDistrictList = AreaConverter.convertAreaDistrictDOList(areaDistrictDOList);

        Map<Integer,AreaProvince> provinceMap = new HashMap<>();
        for(AreaProvince areaProvince : areaProvinceList){
            provinceMap.put(areaProvince.getAreaProvinceId(),areaProvince);
        }
        Map<Integer,AreaCity> cityMap = new HashMap<>();
        for(AreaCity areaCity : areaCityList){
            cityMap.put(areaCity.getAreaCityId(),areaCity);
        }
        Map<Integer,AreaDistrict> districtMap = new HashMap<>();
        for(AreaDistrict areaDistrict : areaDistrictList){
            districtMap.put(areaDistrict.getAreaDistrictId(),areaDistrict);
        }

        for(Integer districtId : districtMap.keySet()){
            AreaDistrict areaDistrict = districtMap.get(districtId);
            AreaCity areaCity = cityMap.get(areaDistrict.getCityId());
            if(areaCity.getAreaDistrictList()==null){
                areaCity.setAreaDistrictList(new ArrayList<AreaDistrict>());
            }
            areaCity.getAreaDistrictList().add(areaDistrict);
        }

        for(Integer cityId : cityMap.keySet()){
            AreaCity areaCity = cityMap.get(cityId);
            AreaProvince areaProvince = provinceMap.get(areaCity.getProvinceId());
            if(areaProvince.getAreaCityList()==null){
                areaProvince.setAreaCityList(new ArrayList<AreaCity>());
            }
            areaProvince.getAreaCityList().add(areaCity);
        }
        List<AreaProvince> provinceList = new ArrayList<>();
        for(Integer provinceId : provinceMap.keySet()){
            provinceList.add(provinceMap.get(provinceId));
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(provinceList);
        return serviceResult;
    }
}
