package com.lxzl.erp.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: huahongbin
 * @Date: 2018/4/28 14:43
 * @Description: 业务员统计查询字段定义
 */
public class StatisticsSalesmanOrderBy {
	private static Map<String, String> MAP = new HashMap<String, String>();

	// 数据库对应排序字段
	public static final String SUB_COMPNAY_ID = "sub_company_id";
	public static final String SUB_COMPANY_NAME = "sub_company_name";
	public static final String SALESMAN_ID = "salesman_id";
	public static final String SALESMAN_NAME = "salesman_name";
	public static final String DEALS_COUNT = "deals_count";
	public static final String DEALS_PRODUCT_COUNT = "deals_product_count";
	public static final String DEALS_AMOUNT = "deals_amount";
	public static final String AWAIT_RECEIVABLE = "await_receivable";
	public static final String INCOME = "income";

	static {
		MAP.put("subCompanyName", SUB_COMPANY_NAME);
		MAP.put("salesmanName", SALESMAN_NAME);
		MAP.put("dealsCount", DEALS_COUNT);
		MAP.put("dealsProductCount", DEALS_PRODUCT_COUNT);
		MAP.put("dealsAmount", DEALS_AMOUNT);
		MAP.put("awaitReceivable", AWAIT_RECEIVABLE);
		MAP.put("income", INCOME);
	}

	public static String getDataFiled(String name) {return MAP.get(name);}

	public static Boolean isValid(String name) {return MAP.containsKey(name);}
}
