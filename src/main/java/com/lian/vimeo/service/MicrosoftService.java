package com.lian.vimeo.service;

import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.stereotype.Component;

/**
 *
 * @author lian
 */
@Component
public class MicrosoftService {

	/**
	 * 获取Microsft-Token
	 *
	 * @param code
	 * @return
	 */
	public String getMicrosftToken(String code) throws IOException {

		String id = "e541cb7b-c24a-4211-991b-7c9ab25ad964";

		//创建post请求grant_type authorization_code
		PostMethod postMethod = new PostMethod("https://login.microsoftonline.com/common/oauth2/v2.0/token");
		//添加请求头
		postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

		//添加请求体
		NameValuePair[] data = {
			new NameValuePair("client_id", id),
			new NameValuePair("scope", "Calendars.Read offline_access user.read openid profile"),
			new NameValuePair("code", code),
			new NameValuePair("redirect_uri", "http://localhost:8080/microsft"),
			new NameValuePair("grant_type", "authorization_code"),
			new NameValuePair("client_secret", "X/GTuV/[[KQIIe39GMwf2c1*W2js[jey")
		};

		//封装请求体
		postMethod.setRequestBody(data);

		//发送请求
		HttpClient httpClient = new HttpClient();
		int response = httpClient.executeMethod(postMethod); //执行POST方法
		String result = postMethod.getResponseBodyAsString();
		//返回状态码
		System.out.println("getMicrosftToken----" + response);
		//返回数据
		System.out.println("getMicrosftToken----" + result);

		//获取返回参数
		return result;
	}

	/**
	 * 使用Microsft-Token
	 *
	 * @param token
	 * @return
	 */
	public String useMicrosftToken(String token) throws IOException {
		//创建get请求
		GetMethod getMethod = new GetMethod("https://graph.microsoft.com/v1.0/me/messages");
		//添加请求头
		getMethod.setRequestHeader("Authorization", "Bearer " + token);
		//发送请求
		HttpClient httpClient = new HttpClient();
		int response = httpClient.executeMethod(getMethod);
		String result = getMethod.getResponseBodyAsString();
		//返回状态码
		System.out.println("getMicrosftToken----" + response);
		//返回数据
		System.out.println("getMicrosftToken----" + result);
		//获取返回参数
		return result;
	}

	/**
	 * 刷新Microsft-Token
	 *
	 * @param token
	 * @return
	 */
	public String refreshMicrosftToken(String token) throws IOException {

		String id = "e541cb7b-c24a-4211-991b-7c9ab25ad964";

		//创建post请求grant_type authorization_code
		PostMethod postMethod = new PostMethod("https://login.microsoftonline.com/common/oauth2/v2.0/token");
		//添加请求头
		postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

		//添加请求体
		NameValuePair[] data = {
			new NameValuePair("client_id", id),
			new NameValuePair("scope", "Calendars.Read offline_access"),
			new NameValuePair("refresh_token", token),
			new NameValuePair("grant_type", "refresh_token"),
			new NameValuePair("client_secret", "X/GTuV/[[KQIIe39GMwf2c1*W2js[jey")
		};

		//封装请求体
		postMethod.setRequestBody(data);

		//发送请求
		HttpClient httpClient = new HttpClient();
		int response = httpClient.executeMethod(postMethod); //执行POST方法
		String result = postMethod.getResponseBodyAsString();
		//返回状态码
		System.out.println("refreshMicrosftToken----" + response);
		//返回数据
		System.out.println("refreshMicrosftToken----" + result);

		//获取返回参数
		return result;
	}
}
