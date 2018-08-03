package com.lxzl.erp.core.service.activity;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.activity.ActivityOrderParam;
import com.lxzl.se.common.domain.Result;

public interface ActivityService {

    ServiceResult<String, Result> getActivityOrder(ActivityOrderParam activityOrderParam);

}
