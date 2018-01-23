package com.lxzl.erp.web.controller;

import com.lxzl.erp.dataaccess.domain.user.UserDO;
import com.lxzl.se.common.domain.ContextConstants;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.common.domain.ResultCode;
import com.lxzl.se.common.util.image.ImageUtil;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequestMapping("/example")
@Controller
public class ExampleController extends BaseController {

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
		// 返回页面
		Result result = new Result(ResultCode.COMMON_SUCCESS, true);
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
		return null;
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
