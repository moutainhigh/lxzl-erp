package com.lxzl.erp.core.service.k3;

import com.lxzl.erp.common.util.http.client.HttpClientUtil;
import com.lxzl.erp.dataaccess.dao.mysql.k3.K3SendRecordMapper;
import com.lxzl.erp.dataaccess.domain.k3.K3SendRecordDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;

public class K3PostRunner implements Runnable
{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String url;
    private String postJson;
    private K3SendRecordMapper k3SendRecordMapper;

    public K3PostRunner(String url , String postJson,K3SendRecordMapper k3SendRecordMapper){
        this.url = url;
        this.postJson = postJson;
        this.k3SendRecordMapper = k3SendRecordMapper;
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
        try {
            //todo  创建推送记录
            System.out.println("推送消息");
            response = HttpClientUtil.post(url,postJson);
            System.out.println();

        } catch (UnsupportedEncodingException e) {
            logger.error("=============【PUSH DATA TO K3 ERROR】=============" );
            logger.error("【ERROR INFO】"+e.toString());
        }catch (Exception e) {
            logger.error("=============【PUSH DATA TO K3 ERROR】=============" );
            logger.error("【ERROR INFO】"+e.toString());
        }catch (Throwable t) {
            logger.error("=============【PUSH DATA TO K3 ERROR】=============" );
            logger.error("【ERROR INFO】"+t.toString());
        }finally {
            K3SendRecordDO k3SendRecordDO = new K3SendRecordDO();
            k3SendRecordDO.setRecordJson("123123123");
            k3SendRecordMapper.save(k3SendRecordDO);
            if(false){
                printErrorLog(response);
            }else{
                printSuccessLog(response);
            }
        }
    }
}
