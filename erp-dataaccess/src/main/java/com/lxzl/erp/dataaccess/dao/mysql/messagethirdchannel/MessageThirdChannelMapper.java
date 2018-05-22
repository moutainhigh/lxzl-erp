package com.lxzl.erp.dataaccess.dao.mysql.messagethirdchannel;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.messagethirdchannel.MessageThirdChannelDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface MessageThirdChannelMapper extends BaseMysqlDAO<MessageThirdChannelDO> {

	List<MessageThirdChannelDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}