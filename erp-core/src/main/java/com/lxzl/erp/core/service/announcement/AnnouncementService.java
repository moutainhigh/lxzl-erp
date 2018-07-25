package com.lxzl.erp.core.service.announcement;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.announcement.AnnouncementParam;
import com.lxzl.erp.common.domain.announcement.pojo.Announcement;
public interface AnnouncementService {

    ServiceResult<String,Page<Announcement>> page(AnnouncementParam announcementParam);

    ServiceResult<String, Integer> save(AnnouncementParam announcementDO);

    ServiceResult<String, Integer> update(AnnouncementParam announcementDO);

    ServiceResult<String, Integer> delete(AnnouncementParam announcementDO);

}
