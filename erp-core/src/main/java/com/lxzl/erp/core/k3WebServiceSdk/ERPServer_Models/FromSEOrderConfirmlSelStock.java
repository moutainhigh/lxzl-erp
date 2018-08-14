package com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models;

/**
 * @Author: Pengbinjie
 * @Description：
 * @Date: Created in 10:19 2018/8/7
 * @Modified By:
 */
public class FromSEOrderConfirmlSelStock {
    private String acctKey; //分公司号（00 总部 ；01 深圳；02 北京；03 上海；04 广州；05 武汉；06 南京；07 成都；08 厦门；10 电销）
    private Integer wareType; //库位类型（1 分公司仓； 2 借出仓；3 全部（默认））
    private String fNumber;//物料编码
    private String fName; //商品名称

    public String getAcctKey() {
        return acctKey;
    }

    public void setAcctKey(String acctKey) {
        this.acctKey = acctKey;
    }

    public Integer getWareType() {
        return wareType;
    }

    public void setWareType(Integer wareType) {
        this.wareType = wareType;
    }

    public String getfNumber() {
        return fNumber;
    }

    public void setfNumber(String fNumber) {
        this.fNumber = fNumber;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }
}
