package com.lxzl.erp.web.controller;


import com.lxzl.erp.common.domain.announcement.AnnouncementParam;
import com.lxzl.erp.common.domain.announcement.pojo.Announcement;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.announcement.AnnouncementService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@ControllerLog
@RequestMapping("/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private ResultGenerator resultGenerator;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result list(@RequestBody(required = false) AnnouncementParam pageQuery) {
        return resultGenerator.generate(announcementService.page(pageQuery));
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Result create(@RequestBody @Validated({AddGroup.class}) Announcement announcement, BindingResult validResult) {
        return resultGenerator.generate(announcementService.save(announcement));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(@RequestBody @Validated({UpdateGroup.class}) Announcement announcement, BindingResult validResult) {
        return resultGenerator.generate(announcementService.update(announcement));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result delete(@RequestBody @Validated({IdGroup.class}) Announcement announcement, BindingResult validResult) {
        return resultGenerator.generate(announcementService.delete(announcement));
    }
}
