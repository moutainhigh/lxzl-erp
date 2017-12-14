package com.lxzl.erp.common.util.http.client;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.X509HostnameVerifier;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

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
public class SSLUtilBuilder {

    private static final AllTrustManager simpleVerifier = new AllTrustManager();
	private static SSLSocketFactory sslFactory ;
	private static SSLConnectionSocketFactory sslConnFactory ;
	private static SSLUtilBuilder sslutil = new SSLUtilBuilder();
	private SSLContext sslContext;
	
	public static SSLUtilBuilder getInstance(){
		return sslutil;
	}
	public static SSLUtilBuilder custom(){
		return new SSLUtilBuilder();
	}

    // 重写X509TrustManager类的三个方法,信任服务器证书
    private static class AllTrustManager implements  X509TrustManager{

		@Override
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		@Override
		public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
				String authType) throws CertificateException {
		}

		@Override
		public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
				String authType) throws CertificateException {
		}

	
	};
    
    
    public synchronized SSLSocketFactory getSSLSF() throws RuntimeException {
        if (sslFactory != null){
			return sslFactory;
		}
		try {
			SSLContext sc = getSSLContext();
//			sc.init(null, new TrustManager[] { simpleVerifier }, null);
			sslFactory = sc.getSocketFactory();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
        return sslFactory;
    }
    
    public synchronized SSLConnectionSocketFactory getSSLCONNSF(X509HostnameVerifier hostnameVerifier) throws RuntimeException {
    	if (sslConnFactory != null){
			return sslConnFactory;
		}
    	try {
	    	SSLContext sc = getSSLContext();
	    	
	    	if (hostnameVerifier == null) {
				hostnameVerifier = SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
			}
	    	
//	    	sslConnFactory = new SSLConnectionSocketFactory(sc, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
	    	sslConnFactory = new SSLConnectionSocketFactory(sc, new String[]{"TLSv1"}, null, hostnameVerifier);
	    	
    	} catch (Exception e) {
			throw new RuntimeException(e);
		}
    	return sslConnFactory;
    }
    
     SSLUtilBuilder customSSL(String keyStorePath, String keyStorepass) throws RuntimeException{
    	FileInputStream instream =null;
    	KeyStore trustStore = null; 
		try {
//			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore = KeyStore.getInstance("PKCS12");
			instream = new FileInputStream(new File(keyStorePath));
	     	trustStore.load(instream, keyStorepass.toCharArray());
            // 相信自己的CA和所有自签名的证书
	     	sslContext = SSLContexts.custom().loadKeyMaterial(trustStore, keyStorepass.toCharArray()) .build();	
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | KeyManagementException | UnrecoverableKeyException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}finally{
			try {
				instream.close();
			} catch (IOException e) {}
		}
		return this;
    }
    
    private SSLContext getSSLContext() throws RuntimeException{
    	try {
    		if(sslContext==null){
    			//
    			sslContext = SSLContext.getInstance("SSLv3");
    			sslContext.init(null, new TrustManager[] { simpleVerifier }, null);
    		}
			return sslContext;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}