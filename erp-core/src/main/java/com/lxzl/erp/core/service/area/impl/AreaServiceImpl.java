package com.lxzl.erp.core.service.area.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.area.AreaCity;
import com.lxzl.erp.common.domain.area.AreaDistrict;
import com.lxzl.erp.common.domain.area.AreaProvince;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.area.AreaService;
import com.lxzl.erp.dataaccess.dao.mysql.area.AreaCityMapper;
import com.lxzl.erp.dataaccess.dao.mysql.area.AreaDistrictMapper;
import com.lxzl.erp.dataaccess.dao.mysql.area.AreaProvinceMapper;
import com.lxzl.erp.dataaccess.domain.area.AreaCityDO;
import com.lxzl.erp.dataaccess.domain.area.AreaDistrictDO;
import com.lxzl.erp.dataaccess.domain.area.AreaProvinceDO;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
    public ServiceResult<String, List<AreaProvince>> getAreaList() {
        ServiceResult<String, List<AreaProvince>> serviceResult = new ServiceResult<>();
        Map<String, Object> map = new HashMap<>();
        map.put("start", 0);
        map.put("pageSize", Integer.MAX_VALUE);
        List<AreaProvinceDO> areaProvinceDOList = areaProvinceMapper.listPage(map);
        List<AreaCityDO> areaCityDOList = areaCityMapper.listPage(map);
        List<AreaDistrictDO> areaDistrictDOList = areaDistrictMapper.listPage(map);

        List<AreaProvince> areaProvinceList = ConverterUtil.convertList(areaProvinceDOList, AreaProvince.class);
        List<AreaCity> areaCityList = ConverterUtil.convertList(areaCityDOList, AreaCity.class);
        List<AreaDistrict> areaDistrictList = ConverterUtil.convertList(areaDistrictDOList, AreaDistrict.class);

        Map<Integer, AreaProvince> provinceMap = new HashMap<>();
        for (AreaProvince areaProvince : areaProvinceList) {
            provinceMap.put(areaProvince.getAreaProvinceId(), areaProvince);
        }
        Map<Integer, AreaCity> cityMap = new HashMap<>();
        for (AreaCity areaCity : areaCityList) {
            cityMap.put(areaCity.getAreaCityId(), areaCity);
        }
        Map<Integer, AreaDistrict> districtMap = new HashMap<>();
        for (AreaDistrict areaDistrict : areaDistrictList) {
            districtMap.put(areaDistrict.getAreaDistrictId(), areaDistrict);
        }

        for (Integer districtId : districtMap.keySet()) {
            AreaDistrict areaDistrict = districtMap.get(districtId);
            AreaCity areaCity = cityMap.get(areaDistrict.getCityId());
            if (areaCity.getAreaDistrictList() == null) {
                areaCity.setAreaDistrictList(new ArrayList<AreaDistrict>());
            }
            areaCity.getAreaDistrictList().add(areaDistrict);
        }

        for (Integer cityId : cityMap.keySet()) {
            AreaCity areaCity = cityMap.get(cityId);
            AreaProvince areaProvince = provinceMap.get(areaCity.getProvinceId());
            if (areaProvince.getAreaCityList() == null) {
                areaProvince.setAreaCityList(new ArrayList<AreaCity>());
            }
            areaProvince.getAreaCityList().add(areaCity);
        }
        List<AreaProvince> provinceList = new ArrayList<>();
        for (Integer provinceId : provinceMap.keySet()) {
            provinceList.add(provinceMap.get(provinceId));
        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(provinceList);
        return serviceResult;
    }

    /**
     * 获取邮政编码
     */
    @Override
    public ServiceResult<String, List<String>> selectPostCodeAndSavaData2postCode() {
        //erp_area_district 地区名称
//        List<String> strings = areaDistrictMapper.selectAreaDistrictDOs();
//        List<String> districtNames = areaCityMapper.selectCityNames();
        //添加邮政编码
        List<String> strings = areaDistrictMapper.selectPostCodeIsNull();
        ServiceResult<String, List<String>> serviceResult = new ServiceResult<>();

        for (String areaDistrictDO : strings) {
            // 创建Httpclient对象


            CloseableHttpClient httpclient = HttpClients.createDefault();
//            if (districtName.equals("市辖区")) {
//                List<Integer> cityIds = areaDistrictMapper.findCityIdByDistrictName(districtName);
//                for (Integer cityId : cityIds) {

//                    AreaCityDO areaCityDO = areaCityMapper.findById(cityId);
//                    String cityName = areaCityDO.getCityName();

            String code = areaDistrictDO + "邮政编码";
            // 定义请求的参数
            URI uri = null;
            try {
                uri = new URIBuilder("http://www.baidu.com/s").setParameter("wd", code).build();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            CloseableHttpResponse response = null;
            try {
                // 执行请求
                try {
                    response = httpclient.execute(httpGet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 判断返回状态是否为200
                if (response.getStatusLine().getStatusCode() == 200) {
                    String content = null;
                    try {
                        content = EntityUtils.toString(response.getEntity(), "UTF-8");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Document document = Jsoup.parse(content);
                    Elements opExactqaSAnswer = document.select(".op_post_content");
                    for (Element element : opExactqaSAnswer) {
                        String text = element.select(".op_post_content").text();
                        String substring = text.substring(text.length() - 6, text.length());
                        Integer integer = areaDistrictMapper.savePostCode(substring, areaDistrictDO);
//                            areaCityMapper.savePostCode(text, districtName);
                        System.out.println(text);
                    }
                }

            } finally {
                if (response != null) {
                    try {
                        response.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//                }

        }

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(null);
        return serviceResult;
    }

    /**
     * 中英文简写
     */
    @Override
    public ServiceResult<String, Object> conversionType() {
        ServiceResult<String, Object> serviceResult = new ServiceResult<>();
//        ChineseCharToEn chineseCharToEn = new ChineseCharToEn();
//        List<AreaDistrictDO> areaDistrictDOS = areaDistrictMapper.selectAreaDistrictDOs();
//        for (AreaDistrictDO areaDistrictDO : areaDistrictDOS) {
//            String districtName = areaDistrictDO.getDistrictName();
//            String substring1 = districtName.substring(districtName.length() - 1, districtName.length());
//            switch (substring1) {
//                case "县":
//                case "区":
//                case "市":
//                case "旗":
//                    int i = districtName.indexOf(substring1);
//                    String abbCn = districtName.substring(0, i);
//                    String abbEn = chineseCharToEn.getAllFirstLetter(abbCn).toUpperCase();
//                    areaDistrictMapper.updateAbbCnAndAbbEn(abbCn, abbEn, districtName);
//            }
//        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    /**
     * 获取县级的邮政编码及插入
     */
    @Override
    public ServiceResult<String, Object> saveCode2erpAreaDistrict() {
        ServiceResult<String, Object> serviceResult = new ServiceResult<>();
//        List<String> districtNames = areaDistrictMapper.selectDistrictNames();
//        for (String districtName : districtNames) {
//            if (districtName.equals("市辖区")) {
//                List<Integer> cityIds = areaDistrictMapper.findCityIdByDistrictName(districtName);
//                for (Integer cityId : cityIds) {
//                    AreaCityDO areaCityDO = areaCityMapper.findById(cityId);
//                    String postCode = areaCityDO.getPostCode();
//                    areaDistrictMapper.savePostCode(postCode,districtName,areaCityDO.getId());
////                    areaDistrictMapper.update();
//                }
//            }
//        }
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

}
