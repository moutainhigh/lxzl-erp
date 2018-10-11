package com.lxzl.erp.core.service.user;


import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.user.pojo.UserSysDataPrivilege;
import com.lxzl.se.core.service.BaseService;

import java.util.List;

public interface UserPrevService extends BaseService {



    /**
     * 取消授权
     * @param userSysDataPrivilege
     * @return
     */
    ServiceResult<String, Integer> deleteUserPrev(UserSysDataPrivilege userSysDataPrivilege);

    /**
     * 单个授权
     * @param userSysDataPrivilege
     * @return
     */
    ServiceResult<String, Integer> AddPrev(UserSysDataPrivilege userSysDataPrivilege);

    /**
     * 批量授权
     * @param userSysDataPrivilegeList
     * @return
     */

    ServiceResult<String, Integer> BatchPrev(List<UserSysDataPrivilege> userSysDataPrivilegeList);


}
