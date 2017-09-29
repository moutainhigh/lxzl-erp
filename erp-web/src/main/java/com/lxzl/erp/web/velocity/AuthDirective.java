package com.lxzl.erp.web.velocity;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.tools.view.ViewToolContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lxzl.se.web.context.WebContext;

/**
 * @author lxzl
 * 
 *         2014年4月10日
 */
public class AuthDirective extends Directive {

	/**
	 * 日志
	 */
	protected static final Logger LOG = LoggerFactory.getLogger(AuthDirective.class);

//	private ResourceService resourceService;
//
//	private RoleService roleService;

	private static String application;

	static {
		InputStream inputStream = AuthDirective.class.getClassLoader().getResourceAsStream("cooperation.properties");
		Properties p = new Properties();
		try {
			p.load(inputStream);
		} catch (IOException e) {
			LOG.error("读取cooperation.properties错误", e);
		}

		application = p.getProperty("application");
		if (StringUtils.isBlank(application)) {
			LOG.error("读取cooperation.properties中的application错误");
		}
	}

	@Override
	public String getName() {
		return "auth";
	}

	@Override
	public int getType() {
		return BLOCK;
	}

	private void init(ViewToolContext context) {
//		ServletContext servletContext = context.getServletContext();
//		ApplicationContext ac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
//		if (resourceService == null) {
//			resourceService = ac.getBean(ResourceService.class);
//		}
//
//		if (roleService == null) {
//			roleService = ac.getBean(RoleService.class);
//		}
	}

	@Override
	public boolean render(InternalContextAdapter internalContext, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
		ViewToolContext context = (ViewToolContext) internalContext.getInternalUserContext();
		init(context);
		return doRender(internalContext, context, writer, node);
	}

	protected boolean doRender(InternalContextAdapter internalContext, ViewToolContext context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException,
			MethodInvocationException {
		try {
			// 不做任何处理直接
			StringWriter sw = new StringWriter();
			Node body = node.jjtGetChild(1);
			body.render(internalContext, sw);
			String html = sw.toString();
			writer.write(html);
			return true;
			
//			int bodyIndex = 1;
//			
//			SimpleNode resourceCodeNode = (SimpleNode) node.jjtGetChild(0);
//			String resourceCode = (String) resourceCodeNode.value(internalContext);
//			
//			SimpleNode resourceTypeNode = (SimpleNode) node.jjtGetChild(1);
//			String resourceType = (String) resourceTypeNode.value(internalContext);
//			if(resourceType != null){
//				bodyIndex = 2;
//			}
//
//			if (!LoginUtil.isLogined()) {
//				return true;
//			}
//
//			List<RoleDO> roleDOList = roleService.findFromContext();
//
//			if (roleDOList == null || roleDOList.isEmpty()) {
//				return true;
//			}
//
//			ResourceDO resourceDO = new ResourceDO();
//			resourceDO.setCode(resourceCode);
//			
//			String warehouse = LoginUtil.currentWarehouseNo();
//			
//			for (RoleDO roleDO : roleDOList) {
//				boolean isContain = resourceService.containsResource(roleDO, resourceDO, application, warehouse);
//				boolean isField = ResourceType.RESOURCE_TYPE_FIELD.toString().equalsIgnoreCase(resourceType);
//
//				if ((isContain && !isField) || (!isContain && isField)) {
//					StringWriter sw = new StringWriter();
//					Node body = node.jjtGetChild(bodyIndex);
//					body.render(internalContext, sw);
//					String html = sw.toString();
//					writer.write(html);
//					return true;
//				}
//			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			HttpServletResponse response = WebContext.currentResponse();
			HttpServletRequest request = WebContext.currentRequest();
			
			response.setContentType("text/html");
			request.setAttribute("error", "权限标签解析错误\n" +  e.getMessage());
			try {
				request.getRequestDispatcher("/error.jsp").forward(request, response);
			} catch (ServletException e1) {
				LOG.error(e.getMessage(), e);
			}
			return false;
		}
		
//		return true;
	}
}
