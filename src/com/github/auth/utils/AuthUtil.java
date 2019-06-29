package com.github.auth.utils;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


public class AuthUtil {
	public static  String GITHUB_CLIENT_ID = null;
	public static  String GITHUB_CLIENT_SECRET = null;
	public static  String GITHUB_REDIRECT_URL = null;
	
	 
	static {
		Properties pro = new Properties();
		InputStreamReader in = null; 
		try {
			in=new InputStreamReader(AuthUtil.class.getResourceAsStream("/com/github/auth/github.properties"),"utf-8");
			pro.load(in);
			GITHUB_CLIENT_ID = pro.getProperty("GITHUB_CLIENT_ID").trim();
			GITHUB_CLIENT_SECRET = pro.getProperty("GITHUB_CLIENT_SECRET").trim();
			GITHUB_REDIRECT_URL = pro.getProperty("GITHUB_REDIRECT_URL").trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		
	}
	public static JSONObject doPostJson(String url){
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
		JSONObject jsonObject = null;
		try {
			//创建连接
			HttpResponse response = client.execute(httpPost);
			if(response != null && response.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = response.getEntity();
				if(entity != null){
					String result = EntityUtils.toString(entity,"UTF-8");
					result = toJSON(result);
					 jsonObject = JSON.parseObject(result);
					return jsonObject;
				}
            }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			httpPost.releaseConnection();
		}
		return null;
	}
	//Get方式提交
	public static JSONObject doGetJson(String url){
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		try {
			//创建连接
			HttpResponse response = client.execute(httpGet);
			if(response != null && response.getStatusLine().getStatusCode() == 200){
					HttpEntity entity = response.getEntity();
					if(entity != null){
						String result = EntityUtils.toString(entity,"UTF-8");
						//如果返回用户信息，json格式。不必转换为json对象
						if(result.indexOf("{")<0){
							result = toJSON(result);
						}
						 jsonObject = JSON.parseObject(result);
						return jsonObject;
					}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			httpGet.releaseConnection();
		}
		return null;
	}
	public static String toJSON(String str){
		    String[] params = str.split("&");
		    StringBuffer sb = new StringBuffer();
		    sb.append("{");
		    for(int i =0;i<params.length;i++){
		    	sb.append("\""+params[i].split("=")[0]+"\":");
		    	if(i==params.length-1){
		    		sb.append("\""+params[i].split("=")[1]+"\"}");
		    	}else{
		    		sb.append("\""+params[i].split("=")[1]+"\",");
		    	}
		    }
		return sb.toString();
	}
}
