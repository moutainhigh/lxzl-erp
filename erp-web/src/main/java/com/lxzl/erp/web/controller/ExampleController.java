package com.lxzl.erp.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.lxzl.erp.common.domain.ApplicationConfig;
import com.lxzl.erp.core.service.CaptchaService;
import com.lxzl.erp.core.service.FileService;
import com.lxzl.erp.core.service.LogService;
import com.lxzl.erp.core.service.MessageService;
import com.lxzl.erp.core.service.RpcTestService;
import com.lxzl.erp.core.service.TokenService;
import com.lxzl.erp.core.service.UserService;
import com.lxzl.erp.dataaccess.domain.LogDO;
import com.lxzl.erp.dataaccess.domain.UserDO;
import com.lxzl.se.common.domain.ContextConstants;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.common.domain.ResultCode;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.date.DateUtil;
import com.lxzl.se.common.util.file.FileInfo;
import com.lxzl.se.common.util.image.ImageUtil;
import com.lxzl.se.common.util.poi.ExcelResult;
import com.lxzl.se.web.controller.BaseController;

@RequestMapping("/example")
@Controller
public class ExampleController extends BaseController {

	@Autowired
	private UserService userService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private CaptchaService captchaService;

	@Autowired
	private FileService fileService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private RpcTestService rpcTestService;

	@Autowired
	private LogService logService;

	@RequestMapping(value = "/testAll")
	public Result testAll(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		String username = getStringParameter("username");
		String password = getStringParameter("password");
		String nick = getStringParameter("nick");
		Integer sex = getIntegerParameter("sex");
		Integer age = getIntegerParameter("age");
		Date birthday = getDateParameter("birthday");
		String address = getStringParameter("address");
		String tel = getStringParameter("tel");
		String email = getStringParameter("email");

		userService.addUser(username, password, nick, sex, age, birthday, address, tel, email, ApplicationConfig.authKey);
		userService.findByUsername(username);

		String value = tokenService.generateToken(1000l);

		rpcTestService.testRpc();

		String message1 = "{\"itemHost\":\"item.lxzl.com\",\"itemId\":\"121345325\",\"abtestKey\":\"A\",\"pageContent\":\"<html>商品</html>\", \"action\":\"add\"}";
		messageService.sendCommentMsg(message1);

		String message2 = "{\"itemHost\":\"shop.lxzl.com\",\"shopId\":\"121345325\",\"abtestKey\":\"A\",\"pageContent\":\"<html>店铺</html>\", \"action\":\"add\"}";
		messageService.sendCommentMsg(message2);

		// 返回页面
		Result result = new Result(ResultCode.COMMON_SUCCESS, true);
		result.setProperty("value", value);
		return result;
	}

	@RequestMapping(value = "/testMysql")
	public Result testMysql(HttpServletRequest request, HttpServletResponse response, Model model) {
		String username = getStringParameter("username");
		String password = getStringParameter("password");
		String nick = getStringParameter("nick");
		Integer sex = getIntegerParameter("sex");
		Integer age = getIntegerParameter("age");
		Date birthday = getDateParameter("birthday");
		String address = getStringParameter("address");
		String tel = getStringParameter("tel");
		String email = getStringParameter("email");
		userService.addUser(username, password, nick, sex, age, birthday, address, tel, email, ApplicationConfig.authKey);
		UserDO user = userService.findByUsername(username);
		// 返回页面
		Result result = new Result(ResultCode.COMMON_SUCCESS, true);
		result.setProperty("user", user);
		return result;
	}

	@RequestMapping(value = "/testHbase")
	public Result testHbase(HttpServletRequest request, HttpServletResponse response, Model model) {
		Integer id = 123;
		String content = "aa哈哈";
		Integer executeTimes = 1;
		Integer status = 2;
		Date createTime = DateUtil.parse("2016-08-03 15:25:36", DateUtil.LONG_DATE_FORMAT_STR);
		Date updateTime = createTime;
		String createUser = "张三";
		String updateUser = createUser;
		logService.dumpLog(id, content, executeTimes, status, createUser, createTime, updateUser, updateTime);
		LogDO log = logService.findHistroyLog(id);
		// 返回页面
		Result result = new Result(ResultCode.COMMON_SUCCESS, true);
		result.setProperty("log", log);
		return result;
	}

	@RequestMapping(value = "/testRedis")
	public Result testRedis(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		String value = tokenService.generateToken(1000l);
		// 返回页面
		Result result = new Result(ResultCode.COMMON_SUCCESS, true);
		result.setProperty("value", value);
		return result;
	}

	@RequestMapping(value = "/testMemcache")
	public Result testMemcache(HttpServletRequest request, HttpServletResponse response, Model model) {
		String username = getStringParameter("username");
		String value = captchaService.generateCaptcha(username, 60);
		// 返回页面
		Result result = new Result(ResultCode.COMMON_SUCCESS, true);
		result.setProperty("value", value);
		return result;
	}

	@RequestMapping(value = "/testRpc")
	public Result testRpc(HttpServletRequest request, Model model) {
		String m = rpcTestService.testRpc();
		// 返回页面
		Result result = new Result(ResultCode.COMMON_SUCCESS, true);
		result.setProperty("value", m);
		return result;
	}

	@RequestMapping(value = "/testMq")
	public Result testMq(HttpServletRequest request, Model model) {
		String message = "{\"itemHost\":\"item.lxzl.com\",\"itemId\":\"121345325\",\"abtestKey\":\"A\",\"pageContent\":\"<html>啊啊啊啊</html>\", \"action\":\"add\"}";
		String messageId = messageService.sendCommentMsg(message);
		// 返回页面
		Result result = new Result(ResultCode.COMMON_SUCCESS, true);
		result.setProperty("messageId", messageId);
		result.setProperty("message", message);
		return result;
	}

	@RequestMapping(value = "/testImage")
	public Result testImage(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		String imagePath = getStringParameter("imagePath");
		String newImagePath = getStringParameter("newImagePath");
		Integer width = getIntegerParameter("width");
		Integer height = getIntegerParameter("height");
		boolean value = ImageUtil.zoomImage(imagePath, newImagePath, width, height, 30);
		// 返回页面
		Result result = new Result(ResultCode.COMMON_SUCCESS, true);
		result.setProperty("value", value);
		return result;
	}

	@RequestMapping(value = "/testFile", method = { RequestMethod.GET })
	public String testFile(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		return "/example/file";
	}

	@RequestMapping(value = "/testUploadFile", method = { RequestMethod.POST })
	public Result testUploadFile(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		MultipartFile multipartFile = getMultipartFile(request, "file");
		String fileName = multipartFile.getOriginalFilename();
		byte[] fileBytes = multipartFile.getBytes();
		long size = multipartFile.getSize();
		InputStream inputStream = multipartFile.getInputStream();
		Map<String, String> extraInfo = new HashMap<String, String>();
		extraInfo.put("color", "red");
		String fileId = fileService.uploadFile(fileName, fileBytes, size, "lxzl", extraInfo, inputStream);

		// 返回页面
		Result result = new Result(ResultCode.COMMON_SUCCESS, true);
		result.setProperty("value", fileId);
		return result;
	}

	@RequestMapping(value = "/testDownloadFile")
	public void testDownloadFile(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		String fileId = getStringParameter("fileId");
		FileInfo fileInfo = fileService.downloadFile(fileId);

		if (fileInfo == null) {
			throw new BusinessException("文件fileId=" + fileId + "不存在");
		}

		byte[] content = fileInfo.getContent();
		String fileName = fileInfo.getFileName();
		download(fileName, content, "UTF-8", request, response);
	}

	@RequestMapping(value = "/testDownload")
	public void testDownload(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		String fileName = getStringParameter("file");
		download(fileName, "UTF-8", request, response);
	}

	@RequestMapping(value = "/testExcel", method = { RequestMethod.GET })
	public String testExcel(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		return "/example/excel";
	}

	@RequestMapping(value = "/testImportExcel", method = { RequestMethod.POST })
	public Result testImportExcel(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		MultipartFile multipartFile = getMultipartFile(request, "excelFile");
		InputStream inputStream = multipartFile.getInputStream();
		String file = multipartFile.getOriginalFilename();
		ExcelResult excelResult = userService.importUser(file, inputStream);
		// 返回页面
		Result result = null;

		if (excelResult.isSuccess()) {
			result = new Result(ResultCode.COMMON_SUCCESS, true);
			result.setProperty("totalNum", excelResult.getTotalNum());
			result.setProperty("successNum", excelResult.getSuccessNum());
			result.setProperty("failureNum", excelResult.getFailureNum());
		} else {
			result = new Result(ResultCode.COMMON_BUSINESS_EXCEPTION, false);
			result.setProperty("totalNum", excelResult.getTotalNum());
			result.setProperty("successNum", excelResult.getSuccessNum());
			result.setProperty("failureNum", excelResult.getFailureNum());
		}

		return result;
	}

	@RequestMapping(value = "/testExportExcel")
	public void testExportExcel(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		// String excelFieldDefinition =
		// "[{\"0\": \"表名\"},{\"1\": \"分表字段\"},{\"2\": \"表个数\"},{\"3\": \"数据库\"}]";
		String excelFieldDefinition = getStringParameter(ContextConstants.EXPORT_FIELD);
		String file = getStringParameter("file");
		List<UserDO> objects = new ArrayList<UserDO>();
		excelExport(file, objects, excelFieldDefinition, true, 1, response, UserDO.class);
	}

}
