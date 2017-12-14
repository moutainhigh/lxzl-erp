package com.lxzl.erp.common.util.http.client;

import com.lxzl.erp.common.util.XMLUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  xiaosc
 * @version  [版本号, 2015-11-23]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */

public class HttpClientUtil{
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	public static final String DEFAULT_ENCODING = "UTF-8";
	
	//默认采用的http协议的HttpClient对象
	private static  HttpClientConfigBuilder httpClientBuilder;
	
	//默认采用的https协议的HttpClient对象
	private static HttpClientConfigBuilder httpsClientBuilder;
	

	
	static{
		try {
			httpClientBuilder = HttpClientConfigBuilder.custom();
			httpsClientBuilder = HttpClientConfigBuilder.custom().ssl();
		} catch (RuntimeException e) {
			logger.error("创建https协议的HttpClient对象出错：{}", e);
		}
	}

	/**
	 * 判断url是http还是https，直接返回相应的默认client对象
	 * 
	 * @param url
	 * @return  返回对应默认的client对象
	 * @throws RuntimeException
	 * @author xiaosc
	 * @date 2015-11-23 下午3:03:04 
	 * @see [类、类#方法、类#成员]
	 */
	private static HttpClientConfigBuilder create(String url) throws RuntimeException  {
		if(url.toLowerCase().startsWith("https://")){
			return httpsClientBuilder;
		}else{
			return httpClientBuilder;
		}
	}

	
	/**
	 * http请求，自定义HttpClientConfigBuilder对象，传入HttpEntity请求参数，设置内容类型，并指定参数和返回数据的编码
	 * <br>该方法为HttpClientUtil类中http请求最原始的一个方法，即所有http请求最终都会调用此方法
	 * @param clientBuilder   HttpClientConfigBuilder对象
	 * @param url             资源地址
	 * @param httpMethod      请求方法
	 * @param requestEntity   请求参数
	 * @param headerBuilder   请求头信息
	 * @param encoding        编码
	 * @return
	 * @throws RuntimeException
	 * @author xiaosc
	 * @date 2015-11-23 下午3:10:00 
	 * 
	 */
	public static String send(HttpClientConfigBuilder clientBuilder, String url, HttpMethods httpMethod, HttpEntity requestEntity, HttpHeaderBuilder headerBuilder, String encoding) throws RuntimeException {
		String body = "";
		try {
			
			if (encoding == null || encoding.isEmpty()) {
				encoding = DEFAULT_ENCODING;
			}
			
			//创建请求对象
			HttpRequestBase request = getRequest(url, httpMethod);
			
			//设置header信息
			if (headerBuilder != null) {
				request.setHeaders(headerBuilder.build());
			}
			
			request.setConfig(clientBuilder.getRequestConfigBuilder().build());
			
			//判断是否支持设置entity(仅HttpPost、HttpPut、HttpPatch支持)
			if(HttpEntityEnclosingRequestBase.class.isAssignableFrom(request.getClass()) && requestEntity != null){				
				((HttpEntityEnclosingRequestBase)request).setEntity(requestEntity);				
			}
				
			int idx = url.indexOf("?");
			logger.info("请求地址：" + url.substring(0, (idx>0 ? idx-1:url.length())));
			if(idx>0){
				logger.info("请求参数：" + url.substring(idx+1));
			}else if (requestEntity != null) {
				logger.info("请求参数：" + EntityUtils.toString(requestEntity, encoding));
			}

			//调用发送请求
			body = execute(clientBuilder, request, url, encoding);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return body;
	}
	
	
	
	/**
	 * http请求，自定义HttpClientConfigBuilder对象，传入Map请求参数，设置内容类型，并指定参数和返回数据的编码
	 * 
	 * @param clientBuilder  HttpClientConfigBuilder对象
	 * @param url            资源地址
	 * @param httpMethod     请求方法
	 * @param headerBuilder  请求头信息
	 * @param parasMap       请求参数
	 * @param encoding       编码
	 * @return
	 * @throws RuntimeException
	 * @author xiaosc
	 * @date 2015-11-23 下午3:07:11 
	 * @see HttpClientUtil#send
	 */
	public static String send(HttpClientConfigBuilder clientBuilder, String url, HttpMethods httpMethod,  HttpHeaderBuilder headerBuilder, Map<String,String> parasMap, String encoding) throws RuntimeException {
		String body = "";
		HttpEntity requestEntity = null;
		
		try {
			
			if (encoding == null || encoding.isEmpty()) {
				encoding = DEFAULT_ENCODING;
			}
			
			//创建请求对象
			HttpRequestBase request = getRequest(url, httpMethod);
			
			//判断是否支持设置entity(仅HttpPost、HttpPut、HttpPatch支持)
			if(HttpEntityEnclosingRequestBase.class.isAssignableFrom(request.getClass())){
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				//检测url中是否存在参数
				url = HttpStringUtils.checkHasParas(url, nvps);				
				//装填参数
				HttpStringUtils.map2List(nvps, parasMap);				
				//设置参数到请求对象中
				requestEntity = new UrlEncodedFormEntity(nvps, encoding);	
			}			
			//调用发送请求
			body = send(clientBuilder, url, httpMethod, requestEntity, headerBuilder, encoding);			
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}	
		return body;
	}
	
	/**
	 * 以Get方式请求URL
	 * <P>请求头部信息默认，编码默认utf-8
	 * @param url    资源地址
	 * @return
	 * @throws RuntimeException
	 * @author xiaosc
	 * @date 2015-11-23 下午3:15:52 
	 * @see HttpClientUtil#send
	 */
	public static String get(String url) throws RuntimeException {
		return get(url, null);
	}
	
	/**
	 * 以GET方式自定义head请求url，编码默认utf-8
	 * 
	 * @param url              资源地址
	 * @param headerBuilder    请求头信息
	 * @return
	 * @throws RuntimeException
	 * @author xiaosc
	 * @date 2015-11-23 下午3:21:47 
	 * @see HttpClientUtil#send
	 */
	public static String get(String url, HttpHeaderBuilder headerBuilder) throws RuntimeException {
		return get(url, headerBuilder, DEFAULT_ENCODING);
	}
	
	/**
	 * 以Get方式，请求资源或服务
	 * 
	 * @param url
	 * @param headerBuilder
	 * @param encoding
	 * @return   String
	 * @throws RuntimeException
	 * @author xiaosc
	 * @date 2015-11-23 下午3:29:34 
	 * @see HttpClientUtil#send
	 */
	public static String get(String url, HttpHeaderBuilder headerBuilder,String encoding) throws RuntimeException {
		return get(create(url), url, headerBuilder, encoding);
	}

	/**
	 * 以Get方式，请求资源或服务
	 * 
	 * @param clientBuilder
	 * @param url
	 * @param headerBuilder
	 * @param encoding
	 * @return
	 * @throws RuntimeException
	 * @author xiaosc
	 * @date 2015-11-23 下午3:30:45 
	 * @see HttpClientUtil#send
	 */
	public static String get(HttpClientConfigBuilder clientBuilder, String url, HttpHeaderBuilder headerBuilder,String encoding) throws RuntimeException {

		return send(clientBuilder, url, HttpMethods.GET, null, headerBuilder, encoding);
		
	}
	
	public static String post(String url, Map<String, String> parasMap) throws RuntimeException{
		return post(url, parasMap, DEFAULT_ENCODING);
	}
	
	public static String post(String url, Map<String, String> parasMap,  String encoding) throws RuntimeException{
		return post(url, parasMap, null, encoding);
	}
	
	/**
	 * 以Post方式，请求资源或服务，传入请求参数，设置内容类型，并指定参数和返回数据的编码
	 * 
	 * @param url					资源地址
	 * @param parasMap		请求参数
	 * @param headerBuilder			请求头信息
	 * @param encoding		编码
	 * @return						返回处理结果
	 * @throws RuntimeException 
	 */
	public static String post(String url, Map<String, String> parasMap, HttpHeaderBuilder headerBuilder, String encoding) throws RuntimeException {
		return post(create(url), url, parasMap, headerBuilder, encoding);
	}
	
	
	public static String post(HttpClientConfigBuilder clientBuilder, String url, Map<String,String> parasMap, HttpHeaderBuilder headerBuilder, String encoding) throws RuntimeException {
		return send(clientBuilder, url, HttpMethods.POST, headerBuilder, parasMap, encoding);
	}
	
	public static String post(String url) throws RuntimeException,UnsupportedEncodingException {
		return post(url, "");
	}
	
	public static String post(String url, String paras) throws RuntimeException,UnsupportedEncodingException {
		return post(url, paras, DEFAULT_ENCODING);
	}
	
	public static String post(String url, String paras, String encoding) throws RuntimeException,UnsupportedEncodingException {
		return post(url, paras, null, encoding);
	}
	
	public static String post(String url, String paras, HttpHeaderBuilder headerBuilder, String encoding) throws RuntimeException,UnsupportedEncodingException {
		return post(create(url), url, paras, headerBuilder, encoding);
	}
	
	public static String post(HttpClientConfigBuilder clientBuilder, String url, String paras, HttpHeaderBuilder headerBuilder, String encoding) throws RuntimeException,UnsupportedEncodingException {
		HttpEntity requestEntity = null;
		
		if (encoding == null || encoding.isEmpty()) {
			encoding = DEFAULT_ENCODING;
		}
		
		if (paras != null && !paras.isEmpty()) {
			requestEntity = new StringEntity(paras, encoding);
		}
		
		return send(clientBuilder, url, HttpMethods.POST, requestEntity, headerBuilder, encoding);
	}
	
	
	/**
	 * 以Put方式，请求资源或服务，传入请求参数，设置内容类型，并指定参数和返回数据的编码
	 * 
	 * @param url					资源地址
	 * @param parasMap		请求参数
	 * @param headerBuilder			请求头信息
	 * @param encoding		编码
	 * @return						返回处理结果
	 * @throws RuntimeException 
	 */
	public static String put(String url, Map<String,String>parasMap,HttpHeaderBuilder headerBuilder,String encoding) throws RuntimeException {
		return put(create(url), url, parasMap, headerBuilder, encoding);
	}
	
	/**
	 * 以Put方式，请求资源或服务，自定义clientBuilder对象，传入请求参数，设置内容类型，并指定参数和返回数据的编码
	 * 
	 * @param clientBuilder				clientBuilder对象
	 * @param url					资源地址
	 * @param parasMap		请求参数
	 * @param headerBuilder			请求头信息
	 * @param encoding		编码
	 * @return						返回处理结果
	 * @throws RuntimeException 
	 */
	public static String put(HttpClientConfigBuilder clientBuilder, String url, Map<String,String>parasMap,HttpHeaderBuilder headerBuilder,String encoding) throws RuntimeException {
		return send(clientBuilder, url, HttpMethods.PUT, headerBuilder, parasMap, encoding);
	}
	
	/**
	 * 以Delete方式，请求资源或服务，传入请求参数，设置内容类型，并指定参数和返回数据的编码
	 * 
	 * @param url					资源地址
	 * @param headerBuilder			请求头信息
	 * @param encoding		编码
	 * @return						返回处理结果
	 * @throws RuntimeException 
	 */
	public static String delete(String url, HttpHeaderBuilder headerBuilder,String encoding) throws RuntimeException {
		return delete(create(url), url, headerBuilder, encoding);
	}
	
	/**
	 * 以Get方式，请求资源或服务，自定义clientBuilder对象，传入请求参数，设置内容类型，并指定参数和返回数据的编码
	 * 
	 * @param clientBuilder				clientBuilder对象
	 * @param url					资源地址
	 * @param headerBuilder			请求头信息
	 * @param encoding		编码
	 * @return						返回处理结果
	 * @throws RuntimeException 
	 */
	public static String delete(HttpClientConfigBuilder clientBuilder, String url, HttpHeaderBuilder headerBuilder,String encoding) throws RuntimeException {
		return send(clientBuilder, url, HttpMethods.DELETE, null, headerBuilder, encoding);
	}
	
	/**
	 * 以Patch方式，请求资源或服务，传入请求参数，设置内容类型，并指定参数和返回数据的编码
	 * 
	 * @param url					资源地址
	 * @param parasMap		请求参数
	 * @param headerBuilder			请求头信息
	 * @param encoding		编码
	 * @return						返回处理结果
	 * @throws RuntimeException 
	 */
	public static String patch(String url, Map<String,String>parasMap,HttpHeaderBuilder headerBuilder,String encoding) throws RuntimeException {
		return patch(create(url), url, parasMap, headerBuilder, encoding);
	}
	
	/**
	 * 以Patch方式，请求资源或服务，自定义clientBuilder对象，传入请求参数，设置内容类型，并指定参数和返回数据的编码
	 * 
	 * @param clientBuilder				clientBuilder对象
	 * @param url					资源地址
	 * @param parasMap		请求参数
	 * @param headerBuilder			请求头信息
	 * @param encoding		编码
	 * @return						返回处理结果
	 * @throws RuntimeException 
	 */
	public static String patch(HttpClientConfigBuilder clientBuilder, String url, Map<String,String>parasMap, HttpHeaderBuilder headerBuilder,String encoding) throws RuntimeException {
		return send(clientBuilder, url, HttpMethods.PATCH, headerBuilder, parasMap, encoding);
	}
	
	/**
	 * 以Head方式，请求资源或服务，传入请求参数，设置内容类型，并指定参数和返回数据的编码
	 * 
	 * @param url					资源地址
	 * @param headerBuilder			请求头信息
	 * @param encoding		编码
	 * @return						返回处理结果
	 * @throws RuntimeException 
	 */
	public static String head(String url, HttpHeaderBuilder headerBuilder,String encoding) throws RuntimeException {
		return head(create(url), url, headerBuilder, encoding);
	}
	
	/**
	 * 以Head方式，请求资源或服务，自定义clientBuilder对象，传入请求参数，设置内容类型，并指定参数和返回数据的编码
	 * 
	 * @param clientBuilder				clientBuilder对象
	 * @param url					资源地址
	 * @param headerBuilder			请求头信息
	 * @param encoding		编码
	 * @return						返回处理结果
	 * @throws RuntimeException 
	 */
	public static String head(HttpClientConfigBuilder clientBuilder, String url, HttpHeaderBuilder headerBuilder,String encoding) throws RuntimeException {
		return send(clientBuilder, url, HttpMethods.HEAD, null, headerBuilder, encoding);
	}
	
	/**
	 * 以Options方式，请求资源或服务，传入请求参数，设置内容类型，并指定参数和返回数据的编码
	 * 
	 * @param url					资源地址
	 * @param headerBuilder			请求头信息
	 * @param encoding		编码
	 * @return						返回处理结果
	 * @throws RuntimeException 
	 */
	public static String options(String url, HttpHeaderBuilder headerBuilder,String encoding) throws RuntimeException {
		return options(create(url), url, headerBuilder, encoding);
	}
	
	/**
	 * 以Options方式，请求资源或服务，自定义clientBuilder对象，传入请求参数，设置内容类型，并指定参数和返回数据的编码
	 * 
	 * @param clientBuilder				clientBuilder对象
	 * @param url					资源地址
	 * @param headerBuilder			请求头信息
	 * @param encoding		编码
	 * @return						返回处理结果
	 * @throws RuntimeException 
	 */
	public static String options(HttpClientConfigBuilder clientBuilder, String url, HttpHeaderBuilder headerBuilder,String encoding) throws RuntimeException {
		return send(clientBuilder, url, HttpMethods.OPTIONS, null, headerBuilder, encoding);
	}
	
	/**
	 * 以Trace方式，请求资源或服务，传入请求参数，设置内容类型，并指定参数和返回数据的编码
	 * 
	 * @param url					资源地址
	 * @param headerBuilder			请求头信息
	 * @param encoding		编码
	 * @return						返回处理结果
	 * @throws RuntimeException 
	 */
	public static String trace(String url, HttpHeaderBuilder headerBuilder,String encoding) throws RuntimeException {
		return trace(create(url), url, headerBuilder, encoding);
	}
	
	/**
	 * 以Trace方式，请求资源或服务，自定义clientBuilder对象，传入请求参数，设置内容类型，并指定参数和返回数据的编码
	 * 
	 * @param clientBuilder				clientBuilder对象
	 * @param url					资源地址
	 * @param headerBuilder			请求头信息
	 * @param encoding		编码
	 * @return						返回处理结果
	 * @throws RuntimeException 
	 */
	public static String trace(HttpClientConfigBuilder clientBuilder, String url, HttpHeaderBuilder headerBuilder,String encoding) throws RuntimeException {
		return send(clientBuilder, url, HttpMethods.TRACE, null, headerBuilder, encoding);
	}

	

	
	/**
	 * 请求资源或服务，自定义clientBuilder对象，设定请求方式，传入请求参数，设置内容类型，并指定参数和返回数据的编码
	 * 
	 * @param clientBuilder				clientBuilder对象
	 * @param request			请求对象
	 * @param url					资源地址
	 * @param encoding		编码
	 * @return						返回处理结果
	 * @throws RuntimeException 
	 */
	private static String execute(HttpClientConfigBuilder clientBuilder, HttpRequestBase request,String url, String encoding) throws RuntimeException {
		String body = "";
		HttpResponse response =null;
		try {
			
			HttpClient httpClient = clientBuilder.build();
			
			request.setConfig(clientBuilder.getRequestConfigBuilder().build());
			
			HttpContext context = clientBuilder.getClientContext();

			//执行请求操作，并拿到结果（同步阻塞）
			response = httpClient.execute(request, context);
			
			//获取结果实体
			HttpEntity entity = response.getEntity();
			
			if (entity != null) {
				//按指定编码转换结果实体为String类型
				body = EntityUtils.toString(entity, encoding);
				logger.debug(body);
			}
			EntityUtils.consume(entity);
		} catch (ParseException | IOException e) {
			throw new RuntimeException(e);
		} finally {
			close(response);
		}
		
		return body;
	}


	/**
	 * http提交post请求
	 *
	 * @param url
	 *            请求的地址
	 * @param parameterMap
	 *            请求的参数集合
	 * @param charSet
	 *            字符集
	 * @return 响应结果(字符串)
	 * @throws Exception
	 */
	public static String postXmlData(String url, Map<String, String> parameterMap, String charSet) throws Exception {
		try {
			return postXmlDataHttps(url, parameterMap, charSet);
		} catch (Exception e) {
			logger.error("Httpclient close error", e);
			throw new Exception("交易请求发送失败,请稍后重试.");
		}
	}


	/**
	 * 执行远程请求访问
	 * @param httpClient
	 * @param httpPost
	 * @param charSet
	 * @return
	 */
	private static String doPostExecute(CloseableHttpClient httpClient, HttpPost httpPost, String charSet) {
		String responseStr = null;
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				responseStr = EntityUtils.toString(entity, charSet);
			}
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		} finally {
			if (response != null){
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return responseStr;
	}

	@SuppressWarnings("unchecked")
	public static String postXmlDataHttps(String url, Map<String, String> parameterMap, String charSet) throws RuntimeException,UnsupportedEncodingException {
		String requestStr = XMLUtil.parseXML(XMLUtil.getParameterMap(parameterMap));
		if (logger.isDebugEnabled()){
			logger.debug("postXmlDataHttps url is {} and rquest data is {}", url, requestStr);
		}
		String responseStr = HttpClientUtil.post(url, requestStr, null, charSet);
		if (logger.isDebugEnabled()) {
			logger.debug("postXmlDataHttps url is {} and response data is {}", url, responseStr);
		}
		return responseStr;
	}
	
	/**
	 * 根据请求方法名，获取request对象
	 * 
	 * @param url					资源地址
	 * @param method			请求方式
	 * @return
	 */
	private static HttpRequestBase getRequest(String url, HttpMethods method) {
		HttpRequestBase request = null;
		switch (method.getCode()) {
			case 0:// HttpGet
				request = new HttpGet(url);
				break;
			case 1:// HttpPost
				request = new HttpPost(url);
				break;
			case 2:// HttpHead
				request = new HttpHead(url);
				break;
			case 3:// HttpPut
				request = new HttpPut(url);
				break;
			case 4:// HttpDelete
				request = new HttpDelete(url);
				break;
			case 5:// HttpTrace
				request = new HttpTrace(url);
				break;
			case 6:// HttpPatch
				request = new HttpPatch(url);
				break;
			case 7:// HttpOptions
				request = new HttpOptions(url);
				break;
			default:
				request = new HttpPost(url);
				break;
		}
		return request;
	}
	
	/**
	 * 尝试关闭response
	 * 
	 * @param resp				HttpResponse对象
	 */
	private static void close(HttpResponse resp) {
		try {
			if(resp == null){
				return;
			}
			//如果CloseableHttpResponse 是resp的父类，则支持关闭
			if(CloseableHttpResponse.class.isAssignableFrom(resp.getClass())){
				((CloseableHttpResponse)resp).close();
			}
		} catch (IOException e) {
			logger.error("", e);
		}
	}
	
	
	/**
	 * 枚举HttpMethods方法
	 * 
	 * @author  xiaosc
	 * @version  [版本号, 2015-11-23]
	 * @see  [相关类/方法]
	 * @since  [产品/模块版本]
	 */
	public enum HttpMethods{
		
		/**
		 * 求获取Request-URI所标识的资源
		 */
		GET(0, "GET"), 
		
		/**
		 * 向指定资源提交数据进行处理请求（例如提交表单或者上传文件）。数据被包含在请求体中。
		 * POST请求可能会导致新的资源的建立和/或已有资源的修改
		 */
		POST(1, "POST"),
		
		/**
		 * 向服务器索要与GET请求相一致的响应，只不过响应体将不会被返回。
		 * 这一方法可以在不必传输整个响应内容的情况下，就可以获取包含在响应消息头中的元信息
		 * 只获取响应信息报头
		 */
		HEAD(2, "HEAD"),
		
		/**
		 * 向指定资源位置上传其最新内容（全部更新，操作幂等）
		 */
		PUT	(3, "PUT"), 
		
		/**
		 * 请求服务器删除Request-URI所标识的资源
		 */
		DELETE	(4, "DELETE"), 
		
		/**
		 * 请求服务器回送收到的请求信息，主要用于测试或诊断
		 */
		TRACE(5, "TRACE"), 
		
		/**
		 * 向指定资源位置上传其最新内容（部分更新，非幂等）
		 */
		PATCH	(6, "PATCH"),
		
		/**
		 * 返回服务器针对特定资源所支持的HTTP请求方法。
		 * 也可以利用向Web服务器发送'*'的请求来测试服务器的功能性
		 */
		OPTIONS	(7, "OPTIONS"), 
		
//		/**
//		 * HTTP/1.1协议中预留给能够将连接改为管道方式的代理服务器
//		 */
//		CONNECT(99, "CONNECT"),
		;
		
		private int code;
		private String name;
		
		private HttpMethods(int code, String name){
			this.code = code;
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public int getCode() {
			return code;
		}
	}
}