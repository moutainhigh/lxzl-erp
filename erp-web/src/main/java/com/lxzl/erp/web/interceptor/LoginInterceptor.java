//package com.lxzl.demo.web.interceptor;
//
//import java.io.IOException;
//import java.util.regex.Pattern;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.http.HttpStatus;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import com.lxzl.se.common.domain.ContextConstants;
//import com.lxzl.se.common.util.http.URLUtil;
//import com.lxzl.se.common.util.login.LoginTicket;
//import com.lxzl.se.common.util.login.LoginUtil;
//import com.lxzl.se.web.config.LoginConfig;
//
///**
// * 
// * @author lxzl
// * 
// */
//public class LoginInterceptor extends HandlerInterceptorAdapter {
//
//	private static final Logger LOG = LoggerFactory.getLogger(LoginInterceptor.class);
//
////	@Autowired
////	private LoginService loginService;
//
//	// @Autowired
//	// private RoleService roleService;
//
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//		// 该URL是否需要登录
//		if (isExclude(request)) {
//			return processLogined(request, response);
//		}
//
//		LoginTicket loginTicket = null;
//
////		try {
////			loginTicket = LoginUtil.getLoginTicketByCookie(request, LoginConfig.loginCookieName, LoginConfig.loginAuthKey);
////		} catch (Exception e) {
////			LOG.error("Get login ticket error", e);
////			processUnLogin(request, response);
////			return false;
////		}
////
////		if (loginTicket == null) {
////			processUnLogin(request, response);
////			return false;
////		}
////
////		boolean notTimeOut = loginService.containsLoginTicket(loginTicket.getUsername(), loginTicket.getMd5Ticket(), LoginConfig.application);
////
////		if (!notTimeOut) {
////			processUnLogin(request, response);
////			return false;
////		}
////
////		String username = loginTicket.getUsername();
////		String ip = URLUtil.getRemoteIp(request);
////		LoginUtil.registryUser(username, ip);
////		Integer buildingId = loginTicket.getBuilding();
////		LoginUtil.registryBuilding(buildingId);
////		// List<RoleDO> roleList = roleService.findByUser(username);
////		// ObjectMapper objectMapper = new ObjectMapper();
////		// String roleStr = objectMapper.writeValueAsString(roleList);
////		// LoginUtil.registryRoleList(roleStr);
////
////		// 更新缓存
////		loginService.refreshLoginTicket(LoginUtil.currentUsername(), LoginConfig.maxKeepTime, LoginConfig.application);
//
//		return processLogined(request, response);
//	}
//
//	private boolean processLogined(HttpServletRequest request, HttpServletResponse response) throws IOException {
////		if ((request.getContextPath() + LoginConfig.validateLoginUrl).equals(request.getRequestURI())) {
////			setAjaxResponse(response);
////			return false;
////		}
//
//		return true;
//	}
//
//	private void processUnLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		if (URLUtil.isAjaxUrl(request)) {
//			if (URLUtil.isJsonp(request)) {
//				response.setContentType("application/javascript;charset=UTF-8");
//				String callback = request.getParameter("callback");
//				responseSb.append(callback).append("(").append(resultStr).append(")");
//				request.setAttribute("callback", callback);
//			} else {
//				response.setContentType("application/json;charset=UTF-8");
//				responseSb.append(resultStr);
//			}
//			
//			String responseStr = responseSb.toString();
//			response.getWriter().write(responseStr);
//			response.setStatus(HttpStatus.SC_UNAUTHORIZED);
//		} else {
//			response.setContentType("text/html");
//			if (Boolean.parseBoolean(request.getParameter("noRedirect"))) {
//				response.getWriter().write("401");
//			} else {
//				response.sendRedirect(request.getContextPath() + ContextConstants.LOGIN_PAGE);
//			}
//		}
//	}
//
//	/**
//	 * 
//	 * @param request
//	 * @return
//	 */
//	private boolean isExclude(HttpServletRequest request) {
//		String currentUri = URLUtil.getURLWithoutContextPath(URLUtil.getURIWithoutSuffix(request.getRequestURI()), request);
//
////		if (LoginConfig.loginExcludeUrls != null && LoginConfig.loginExcludeUrls.length > 0) {
////			for (int i = 0; i < LoginConfig.loginExcludeUrls.length; i++) {
////				Pattern p = Pattern.compile(LoginConfig.loginExcludeUrls[i] + "$");
////
////				if (p.matcher(currentUri).find()) {
////					return true;
////				}
////			}
////		}
//
//		return false;
//	}
//
//}
