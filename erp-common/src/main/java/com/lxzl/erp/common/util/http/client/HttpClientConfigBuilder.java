package com.lxzl.erp.common.util.http.client;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  xiaosc
 * @version  [版本号, 2015-11-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HttpClientConfigBuilder extends HttpClientBuilder{
	
	private boolean isSetPool=false;//记录是否设置了连接池
	private boolean isNewSSL=false;//记录是否设置了更新了ssl
	
	//用于配置ssl
	private SSLUtilBuilder ssls = SSLUtilBuilder.getInstance();
	
	private Builder requestConfigBuilder = RequestConfig.custom();
	
	private HttpClientContext clientContext = HttpClientContext.create();
	
	
	private HttpClientConfigBuilder(){
	
//		this.requestConfigBuilder.setRedirectsEnabled(true);
		this.setRedirectStrategy(new LaxRedirectStrategy());
		this.requestConfigBuilder.setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY);
		this.clientContext.setCookieStore(new BasicCookieStore());
	}
	
	public static HttpClientConfigBuilder custom(){
	
		
		return new HttpClientConfigBuilder().connectionRequestTimeout(1).connectTimeout(5).socketTimeout(30);
	}	
	
	/**
	 * 设置从connect Manager获取Connection 超时时间，单位秒
	 * <br>默认为1s
	 * @param connectionRequestTimeout
	 * @return
	 * @author xiaosc
	 * @date 2015-11-20 下午4:08:14 
	 * @see [类、类#方法、类#成员]
	 */
	public HttpClientConfigBuilder connectionRequestTimeout(int connectionRequestTimeout){
		
		this.requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeout * 1000);
		
		return this;
	}
	
	/**
	 * 设置连接超时时间，单位秒
	 * <br>默认为5s
	 * @param connectTimeout
	 * @return
	 * @author xiaosc
	 * @date 2015-11-20 下午4:08:20 
	 * @see [类、类#方法、类#成员]
	 */
	public HttpClientConfigBuilder connectTimeout(int connectTimeout){
		this.requestConfigBuilder.setConnectTimeout(connectTimeout * 1000);
		
		return this;
	}
	
	/**
	 * 请求获取数据的超时时间，单位秒
	 * <br>默认为30s
	 * @param socketTimeout
	 * @return
	 * @author xiaosc
	 * @date 2015-11-20 下午4:08:26 
	 * @see [类、类#方法、类#成员]
	 */
	public HttpClientConfigBuilder socketTimeout(int socketTimeout){
		this.requestConfigBuilder.setSocketTimeout(socketTimeout * 1000);
		
		return this;
	}
	
	/**
	 * 设置http请求重试次数
	 * <br>当请求失败时，执行重新访问的次数，该方法与retryHandler互斥，这两个方法最后一次调为有效
	 * @param retryTimes
	 * @return
	 * @author xiaosc
	 * @date 2015-11-23 下午7:19:18 
	 * @see HttpClientConfigBuilder#retryHandler(HttpRequestRetryHandler)
	 */
	public HttpClientConfigBuilder retryTimes(final int retryTimes){		
		this.setRetryHandler(new HttpRequestRetryHandler() {			
			@Override
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				if (executionCount <= retryTimes) {
					return true;
				}				
				return false;
			}
		});
		return this;
	}
	
	/**
	 * 设置http请求重试的handler
	 * <br>该方法与retryTimes互斥，这两个方法最后一次调为有效
	 * <功能详细描述>
	 * @param retryHandler
	 * @return
	 * @author xiaosc
	 * @date 2015-11-23 下午7:10:04 
	 * @see HttpClientConfigBuilder#retryTimes(int)
	 */
	public HttpClientConfigBuilder retryHandler(HttpRequestRetryHandler retryHandler){
		if (retryHandler != null) {
			this.setRetryHandler(retryHandler);
		}
		return this;
	}
	
	
	public HttpClientConfigBuilder ssl()  throws RuntimeException{
		return ssl(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	}
	
	
	
	/**
	 * 设置ssl安全链接
	 * 
	 * @return
	 * @throws HttpProcessException
	 */
	public HttpClientConfigBuilder ssl(X509HostnameVerifier hostnameVerifier) throws RuntimeException {
		
		if(isSetPool){//如果已经设置过线程池，那肯定也就是https链接了
			if(isNewSSL){
				throw new RuntimeException("请先设置ssl，后设置pool");
			}
			return this;
		}
		
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", ssls.getSSLCONNSF(hostnameVerifier)).build();
		//设置连接池大小
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		this.setConnectionManager(connManager);
		
		return this;
	}
	

	/**
	 * 设置自定义sslcontext
	 * 
	 * @param keyStorePath		密钥库路径
	 * @return
	 * @throws HttpProcessException
	 */
	public HttpClientConfigBuilder ssl(String keyStorePath) throws RuntimeException{
		return ssl(keyStorePath,"nopassword");
	}
	/**
	 * 设置自定义sslcontext
	 * 
	 * @param keyStorePath		密钥库路径
	 * @param keyStorepass		密钥库密码
	 * @return
	 * @throws HttpProcessException
	 */
	public HttpClientConfigBuilder ssl(String keyStorePath, String keyStorepass) throws RuntimeException{
		this.ssls = SSLUtilBuilder.custom().customSSL(keyStorePath, keyStorepass);
		this.isNewSSL = true;
		return ssl(SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
	}
	
	
	/**
	 * 设置连接池（默认开启https）
	 * 
	 * @param maxTotal					最大连接数
	 * @param defaultMaxPerRoute	每个路由默认连接数
	 * @return
	 * @throws HttpProcessException
	 */
//	public HttpClientConfigBuilder pool(int maxTotal, int defaultMaxPerRoute) throws RuntimeException{
//		
//		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
//				.<ConnectionSocketFactory> create()
//				.register("http", PlainConnectionSocketFactory.INSTANCE)
//				.register("https", ssls.getSSLCONNSF()).build();
//		
//		//设置连接池大小
//		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
//		connManager.setMaxTotal(maxTotal);
//		connManager.setDefaultMaxPerRoute(defaultMaxPerRoute);		
//		this.setConnectionManager(connManager);
//		this.isSetPool = true;
//		
//		return this;
//	}
	
	/**
	 * 返回http请求的上下文
	 * <p>当http请求后，服务器返回的cookie会保存在请求的上下文中，默认同一链接请求头会带上上次返回的cookie,一般在cookie需要做特殊处理时用到
	 * @return
	 * @author xiaosc
	 * @date 2015-11-23 下午7:23:41 
	 * @see [类、类#方法、类#成员]
	 */
	public HttpClientContext getClientContext() {
		return clientContext;
	}

	/**
	 * 设置代理
	 * 
	 * @param hostOrIP		代理host或者ip
	 * @param port			代理端口
	 * @return
	 */
	public HttpClientConfigBuilder proxy(String hostOrIP, int port){
		// 依次是代理地址，代理端口号，协议类型  
		HttpHost proxy = new HttpHost(hostOrIP, port, "http");  
		DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
		this.setRoutePlanner(routePlanner);
		return this;
	}

	public Builder getRequestConfigBuilder() {
		return requestConfigBuilder;
	}
	
	
	
}