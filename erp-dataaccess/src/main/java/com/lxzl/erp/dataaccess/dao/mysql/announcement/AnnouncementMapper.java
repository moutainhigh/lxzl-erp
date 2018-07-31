package com.lxzl.erp.dataaccess.dao.mysql.announcement;

import com.lxzl.erp.dataaccess.domain.announcement.AnnouncementDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AnnouncementMapper extends BaseMysqlDAO<AnnouncementDO> {
    List<AnnouncementDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);
}
