package com.lxzl.erp.dataaccess.dao.mysql.message;

import com.lxzl.erp.dataaccess.domain.message.MessageDO;
import com.lxzl.erp.dataaccess.domain.product.ProductDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface MessageMapper extends BaseMysqlDAO<MessageDO> {

    void batchSave(@Param("messageDOList") List<MessageDO> messageDOList);

    Integer findSendMessageCountByParams(@Param("maps")Map<String, Object> maps);

    List<MessageDO> findSendMessageByParams(@Param("maps")Map<String, Object> maps);

    Integer findReceiveMessageCountByParams(@Param("maps")Map<String, Object> maps);

    List<MessageDO> findReceiveMessageByParams(@Param("maps")Map<String, Object> maps);

    Integer findNotReadCount(@Param("id")Integer id);

    void batchUpdate(@Param("list") List<MessageDO> messageDOList);
}
