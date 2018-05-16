package com.lxzl.erp.core.service.filterData.impl;

import com.lxzl.erp.core.service.filterData.FilterDataService;
import com.lxzl.erp.core.service.filterData.MatchModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/4/27
 * @Time : Created in 14:58
 */
@Service
public class FilterDataServiceImpl implements FilterDataService {
    @Override
    public void filterConformCustomer(List<MatchModel> matchModelList) {
//        List<MatchModel> matchModelList = initList(0.65);

        List<List<String>> arrayList = new ArrayList<>();
        Iterator<MatchModel> iterator = matchModelList.iterator();
        while (iterator.hasNext()) {
            MatchModel matchModel1 = iterator.next();
            List<String> list = new ArrayList<>();
            for (MatchModel matchModel2 : matchModelList) {
                if (matchModel1.equals(matchModel2) && !matchModel1.getName().equals(matchModel2.getName())) {
                    list.add(list.isEmpty() ? matchModel1.getName() + "----" + matchModel2.getName() : "----" + matchModel2.getName());
                }
            }
            if (!list.isEmpty()) {
                arrayList.add(list);
            }
            iterator.remove();
        }


        for (List<String> strings : arrayList) {
            String str = "";
            for (String string : strings) {
                str = str + string;
            }
            System.out.println(str.trim());
        }

    }


    public static List<MatchModel> initList(Double percent) {
        List<MatchModel> list = new ArrayList<>();
        list.add(new MatchModel(percent, "深圳一加一科技有限公司"));
        list.add(new MatchModel(percent, "深圳一加2科技有限公司"));
        list.add(new MatchModel(percent, "深圳哈哈哈科技有限公司"));
        list.add(new MatchModel(percent, "w'w'w'w'w"));
        return list;
    }
}
