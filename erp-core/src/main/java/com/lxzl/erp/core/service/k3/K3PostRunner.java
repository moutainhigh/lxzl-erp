package com.lxzl.erp.core.service.k3;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.util.http.client.HttpClientUtil;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3SendRecordMapper;
import com.lxzl.erp.dataaccess.domain.k3.K3SendRecordDO;
import org.apache.http.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.List;

public class K3PostRunner implements Runnable
{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String url;
    private List<NameValuePair> params;
    private K3SendRecordMapper k3SendRecordMapper;
    private Integer postK3Type;

    public K3PostRunner(Integer postK3Type , String url , List<NameValuePair> params, K3SendRecordMapper k3SendRecordMapper){
        this.url = url;
        this.params = params;
        this.k3SendRecordMapper = k3SendRecordMapper;
        this.postK3Type = postK3Type;
    }
    private void printSuccessLog(String response){
        logger.info("【PUSH DATA TO K3 RESPONSE SUCCESS】 ： "+response );
    }
    private void printErrorLog(String response){
        logger.info("【PUSH DATA TO K3 RESPONSE FAIL】 ： "+response );
    }

    @Override
    public void run() {
        String response = null;
        K3Response k3Response = null;
        try {
            //创建推送记录，此时发送状态失败，接收状态失败
            K3SendRecordDO k3SendRecordDO = new K3SendRecordDO();
            k3SendRecordDO.setRecordType(postK3Type);
            k3SendRecordDO.setRecordJson(JSON.toJSONString(params));
            k3SendRecordDO.setSendResult(CommonConstant.COMMON_CONSTANT_NO);
            k3SendRecordDO.setReceiveResult(CommonConstant.COMMON_CONSTANT_NO);
            k3SendRecordDO.setSendTime(new Date());
            k3SendRecordMapper.save(k3SendRecordDO);
            logger.info("【推送消息】"+JSON.toJSONString(params));
            response = HttpClientUtil.post(url,params,"UTF-8");
            //修改推送记录
            k3Response = JSON.parseObject(response,K3Response.class);
            if("0".equals(k3Response.getStatus())){
                k3SendRecordDO.setReceiveResult(CommonConstant.COMMON_CONSTANT_YES);
            }else{
                k3SendRecordDO.setReceiveResult(CommonConstant.COMMON_CONSTANT_NO);
            }
            k3SendRecordDO.setSendResult(CommonConstant.COMMON_CONSTANT_YES);
            k3SendRecordDO.setResponseJson(response);
            k3SendRecordMapper.update(k3SendRecordDO);
            logger.info("【返回结果】"+response);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("=============【PUSH DATA TO K3 ERROR】=============" );
            logger.error("【ERROR INFO】"+e);
        }catch (Throwable t) {
            logger.error("=============【PUSH DATA TO K3 ERROR】=============" );
            logger.error("【ERROR INFO】"+t);
        }finally {

            if(k3Response==null||!"0".equals(k3Response.getStatus())){
                printErrorLog(response);
            }else{
                printSuccessLog(response);
            }
        }
    }
}
