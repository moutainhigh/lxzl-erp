package com.lxzl.erp.core.service.announcement.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.announcement.AnnouncementParam;
import com.lxzl.erp.common.domain.announcement.pojo.Announcement;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.announcement.AnnouncementService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.announcement.AnnouncementMapper;
import com.lxzl.erp.dataaccess.domain.announcement.AnnouncementDO;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("announcementService")
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Autowired
    private UserSupport userSupport;

    @Override
    public ServiceResult<String, Page<Announcement>> page(final AnnouncementParam param) {
        final PageQuery finalPageQuery = new PageQuery(param == null || param.getPageNo() == 0 ? 1 : param.getPageNo(),
                param == null || param.getPageSize() == 0 ? 10 : param.getPageSize());

        Map<String, Object> map = new HashMap<String, Object>() {{
            put("start", finalPageQuery.getStart());
            put("pageSize", finalPageQuery.getPageSize());
            if (param != null) {
                if (param.getTitle() != null)
                    put("title", param.getTitle());

                if (param.getContent() != null)
                    put("content", param.getContent());

                if (param.getRemark() != null)
                    put("remark", param.getRemark());
            }
        }};

        List<Announcement> announcements = ConverterUtil.convertList(announcementMapper.listPage(map), Announcement.class);
        int totalCount = announcementMapper.listCount(map);
        Page<Announcement> page = new Page<>(announcements, totalCount, finalPageQuery.getPageNo(), finalPageQuery.getPageSize());
        ServiceResult<String, Page<Announcement>> serviceResult = new ServiceResult<>();
        serviceResult.setResult(page);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Integer> save(AnnouncementParam param) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        if (!userSupport.isSuperUser()) {
            serviceResult.setErrorCode(ErrorCode.USER_ROLE_IS_NOT_SUPER_ADMIN);
            return serviceResult;
        }
        serviceResult.setResult(announcementMapper.save(initAnnouncementDO(param.getTitle(), param.getContent(), param.getRemark())));
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Integer> update(AnnouncementParam param) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        if (!userSupport.isSuperUser()) {
            serviceResult.setErrorCode(ErrorCode.USER_ROLE_IS_NOT_SUPER_ADMIN);
            return serviceResult;
        }
        if (param.getId() == null)
            throw new BusinessException(ErrorCode.ID_NOT_NULL);

        AnnouncementDO announcementDO = buildAnnouncementDO(param.getTitle(), param.getContent(), param.getRemark(), false);
        announcementDO.setId(param.getId());
        serviceResult.setResult(announcementMapper.update(announcementDO));
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Integer> delete(AnnouncementParam param) {
        ServiceResult<String, Integer> serviceResult = new ServiceResult<>();
        if (!userSupport.isSuperUser()) {
            serviceResult.setErrorCode(ErrorCode.USER_ROLE_IS_NOT_SUPER_ADMIN);
            return serviceResult;
        }
        if (param.getId() == null)
            throw new BusinessException(ErrorCode.ID_NOT_NULL);
        AnnouncementDO announcementDO = new AnnouncementDO();
        announcementDO.setId(param.getId());
        announcementDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        serviceResult.setResult(announcementMapper.update(announcementDO));
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    private AnnouncementDO initAnnouncementDO(String title, String content, String remark) {
        return buildAnnouncementDO(title, content, remark, true);
    }

    private AnnouncementDO buildAnnouncementDO(String title, String content, String remark, boolean isInit) {
        AnnouncementDO announcementDO = new AnnouncementDO();
        Date date = new Date();
        if (isInit) {
            announcementDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            announcementDO.setCreateUser(userSupport.getCurrentUserId().toString());
            announcementDO.setCreateTime(date);
            if (title == null)
                throw new BusinessException(ErrorCode.ANNOUNCEMENT_TITLE_NOT_NULL);

            if (content == null)
                throw new BusinessException(ErrorCode.ANNOUNCEMENT_CONTENT_NOT_NULL);
        }
        announcementDO.setTitle(title);
        announcementDO.setContent(content);
        announcementDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        announcementDO.setRemark(remark);
        announcementDO.setUpdateTime(date);

        return announcementDO;
    }
}
