package com.ldz.util.commonUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class HttpUtil {
	private HttpUtil() {	}
	private static final String AJAX_REQUEST_HEADER_NAME = "X-Requested-With";
	private static final String AJAX_REQUEST_HEADER_VALUE = "XMLHttpRequest";
	private static final String AJAX_REQUEST_URI_PREFIX= "ajax-";

	
	public static final String DEFAULT_CHARSET = "utf-8";

	/**
	     * 连接超时时间
	     */
	    public final static int CONNECT_TIMEOUT = 60000;
	    /**
	     * 读取超时时间
	     */
	    public final static int READ_TIMEOUT = 60000;
	
	
		public static boolean isAjax(HttpServletRequest request) {
			String uri = request.getRequestURI();
			uri = uri.substring(uri.lastIndexOf("/")+1);
			return AJAX_REQUEST_HEADER_VALUE.equals(request.getHeader(AJAX_REQUEST_HEADER_NAME)) || uri.startsWith(AJAX_REQUEST_URI_PREFIX);
		}
	    
	public static String post(String url  ) {
		return post(url, null  );
	}
	 
	public static String get(String url  ) {
		return get(url, null  );
	}
	
	
	public static String post(String url, Map<String, String> params) {
		return post(url, params, DEFAULT_CHARSET);
	}
	public static String get(String url, Map<String, String> params) {
		return get(url, params, DEFAULT_CHARSET);
	}
	
	public static String postJson(String postUrl,Map<String, String> postHeaders, String postEntity) throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(postUrl);
		if(postHeaders != null) {
			for(String pKey : postHeaders.keySet()) {
				post.setHeader(pKey, postHeaders.get(pKey));
			}
		}
		try {
			//发送数据对象，转化为JSON格式
			StringEntity entity = new StringEntity(postEntity, "UTF-8");
			
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			//返回数据结果
			HttpEntity entity2 = response.getEntity();
			
			return EntityUtils.toString(entity2);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}  finally {
			post.releaseConnection();
			try {
				client.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	} 

	public static String post(String url, Map<String, String> params,  String charset) {
		
		CloseableHttpClient client = getClient() ;
		
		HttpPost post =    new HttpPost(url);
		post.setConfig(getRequsetConfig());
		try {
			if (params != null && params.size() > 0) {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				for (Entry<String, String> param : params.entrySet()) {
					nameValuePairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
				}
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs,charset));
			}
			HttpResponse response = client.execute(post);
			return handleResponse(url,response, charset);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}  finally {
			post.releaseConnection();
			try {
				client.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
public static String postExtral(String url, Map<String, String> params,List<Object> extarlList,String extarlName,  String charset) {
		
		CloseableHttpClient client = getClient() ;
		
		HttpPost post =    new HttpPost(url);
		post.setConfig(getRequsetConfig());
		try {
			if (params != null && params.size() > 0) {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				for (Entry<String, String> param : params.entrySet()) {
					nameValuePairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
				}
				if(extarlList!=null&&extarlList.size()>0){
					for (Object obj:extarlList) {
						nameValuePairs.add(new BasicNameValuePair(extarlName, obj.toString()));
					}
				}
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs,charset));
			}
			HttpResponse response = client.execute(post);
			return handleResponse(url,response, charset);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}  finally {
			post.releaseConnection();
			try {
				client.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private static RequestConfig getRequsetConfig() {
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(CONNECT_TIMEOUT).setConnectTimeout(READ_TIMEOUT).build();//设置请求和传输超时时间
		return requestConfig;
	}

	private static CloseableHttpClient getClient() {
		CloseableHttpClient client = HttpClients.createDefault();
		return client;
	}

	public static String get(String url, Map<String, String> params,  String charset) {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get =    new HttpGet(url);
		get.setConfig(getRequsetConfig());
		try {
			if (params != null && params.size() > 0) {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				for (Entry<String, String> param : params.entrySet()) {
					nameValuePairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
				}
				String str = EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs,charset));
				if(url.contains("?")){
					url = url + "&"+str;
				}else{
					url = url + "?"+str;
				}
				get.setURI(new URI(url));
			}
			HttpResponse response = client.execute(get);
			return handleResponse(url,response, charset);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		} finally {
			get.releaseConnection();
			try {
				client.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public static String postSOAP(String url , String soapContent ){
		return postSOAP(url, soapContent, DEFAULT_CHARSET);
	}
	public static String postSOAP(String url , String soapContent , String charset){
		CloseableHttpClient client = getClient() ;
		HttpPost post =    new HttpPost(url);
		post.setConfig(getRequsetConfig());
		try {
			post.setEntity(new StringEntity(soapContent, charset));
             HttpResponse response = client.execute(post);
             return handleResponse(url,response, charset);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} 
	}
	
	private static String handleResponse(String url, HttpResponse response , String charset){
		StatusLine statusLine = response.getStatusLine();
		if(statusLine.getStatusCode() >= HttpStatus.SC_INTERNAL_SERVER_ERROR && statusLine.getStatusCode() <= HttpStatus.SC_INSUFFICIENT_STORAGE){
			throw new RuntimeException("server error:"+statusLine.getStatusCode()+",url:"+url);
		}
		if(statusLine.getStatusCode() >= HttpStatus.SC_BAD_REQUEST&& statusLine.getStatusCode() <= HttpStatus.SC_FAILED_DEPENDENCY){
			throw new RuntimeException("request error:"+statusLine.getStatusCode()+",url:"+url);
		}
		try {
			return EntityUtils.toString(response.getEntity(), charset);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static String genQueryString(Map<String, String> params) {
		if (params != null && params.size() > 0) {
			StringBuilder queryString = new StringBuilder();
			int i = 0;
			for (Entry<String, String> param : params.entrySet()) {
				try {
					queryString.append(param.getKey()).append("=").append(param.getValue() != null ? URLEncoder.encode(param.getValue(), DEFAULT_CHARSET):"");
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
				i++;
				if (i < params.size()) {
					queryString.append("&");
				}
			}
			return queryString.toString();
		}
		return "";
	}
	
	public static void main(String[] args) {
		System.out.println(HttpUtil.get("http://www.transgen.com.cn/products/57.html" ));
	}
}
