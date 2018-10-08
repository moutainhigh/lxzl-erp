package com.lxzl.erp.core.service.user;


import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.dataaccess.domain.user.UserPrevDO;
import com.lxzl.se.core.service.BaseService;

import java.util.List;

public interface UserPrevService extends BaseService {

    ServiceResult<String, Integer> insetUserPrev(UserPrevDO userPrevDO);


    ServiceResult<String, Integer> AddPrev(UserPrevDO userPrevDO);


    ServiceResult<String, Integer> BatchPrev(List<UserPrevDO> userPrevDOs);


}
