//package com.lxzl.demo.web.interceptor;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.regex.Pattern;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.hadoop.hdfs.web.URLUtils;
//import org.apache.http.HttpStatus;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import com.lxzl.erp.core.service.UserService;
//import com.lxzl.se.common.domain.ContextConstants;
//import com.lxzl.se.common.util.login.LoginUtil;
//import com.lxzl.se.web.config.LoginConfig;
//
///**
// * 
// * @author lxzl
// * 
// */
//public class AuthInterceptor extends HandlerInterceptorAdapter {
//
////	@Autowired
////	private ResourceService resourceService;
////
////	@Autowired
////	private RoleService roleService;
////
////	@Autowired
////	private UserService userService;
//
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//  
//		if (!LoginUtil.isLogined() || isExclude(request)) {
//			return true;
//		}
//
//		// 判断该请求是否被springmvc的controller处理，如果未被controller则转交给defaultServlet处理，直接返回true，跳出当前拦截
//		String controller = handler.getClass().getSimpleName();
//
//		if (controller.equals("DefaultServletHttpRequestHandler")) {
//			return true;
//		}
//
////		String uri = URLUtils.getURLWithoutContextPath(URLUtils.getURIWithoutSuffix(request.getRequestURI()), request);
////
////		// 判断当前用户是否有角色
////		List<RoleDO> roleList = roleService.findByUser(LoginUtil.currentUsername());
////		if (roleList == null || roleList.isEmpty()) {
////			processForbidden(request, response);
////			return false;
////		}
//
////		String warehouse = LoginUtil.currentWarehouseNo();
////		// 判断角色是否包含该资源
////		for (RoleDO role : roleList) {
////			if (resourceService.containsUrl(role, uri, LoginConfig.application, warehouse)) {
////				return true;
////			}
////		}
////
////		processForbidden(request, response);
//		return false;
//	}
//
////	private void processForbidden(HttpServletRequest request, HttpServletResponse response) throws IOException {
////		if (URLUtils.isAjaxUrl(request)) {
////			response.setStatus(HttpStatus.SC_FORBIDDEN);
////		} else {
////			response.sendRedirect(request.getContextPath() + ContextConstants.FORBIDDEN_PAGE);
////		}
////	}
////	
////	
//	private boolean isExclude(HttpServletRequest request) {
////		String currentUri = URLUtils.getURLWithoutContextPath(URLUtils.getURIWithoutSuffix(request.getRequestURI()), request);
////
////		if (AuthConfig.authExcludeUrls != null && AuthConfig.authExcludeUrls.length > 0) {
////			for (int i = 0; i < AuthConfig.authExcludeUrls.length; i++) {
////				Pattern p = Pattern.compile(AuthConfig.authExcludeUrls[i] + "$");
////
////				if (p.matcher(currentUri).find()) {
////					return true;
////				}
////			}
////		}
//
//		return false;
//	}
//}
