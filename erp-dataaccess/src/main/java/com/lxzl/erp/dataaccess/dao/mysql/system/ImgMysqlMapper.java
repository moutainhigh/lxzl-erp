package com.lxzl.erp.dataaccess.dao.mysql.system;

import com.lxzl.erp.dataaccess.domain.system.ImageDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * ManagementUser: gaochao
 * Date: 2016/11/3.
 * Time: 10:03.
 */
@Repository
public interface ImgMysqlMapper extends BaseMysqlDAO<ImageDO> {

    List<ImageDO> findByRefId(String refId);
    ImageDO findByImgId(@Param("id") Integer id);
    List<ImageDO> findByRefIdAndType(@Param("refId") String refId,
                                     @Param("imgType") Integer imgType);
}
