/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lian.vimeo.service;

import java.io.IOException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

/**
 *
 * @author lian
 */
@Component
public class VimeoService {

	String clentid = "5f90a0518d7ab04ec4d9b1e414c6c3bdc084b6d6";
//	String clentid = "192fba2af80ee1a44f0c4d0771435814b082a4d3";

	String clientSecret = "8S094hcMBCmeaidEB2LzddqJ+Cin6TlvnLjrCimGaIGumay3X0mApH4K1WuQOaGfASBclRBnW8Ddx9fXd9lsyDmcAU3XuABu6SicPkcLop9Trgo7QXoTgBEoIL4arWZe";
//	String clientSecret = "7PtnyyayNSd7rh8leGw53ygVHHvxEwukJx4ULj/nLJewaNeG41eYfO0PysIWmL5OvzJ1NmztnWLB83AEPmqifEeWsVTsT3dJfYTSxuwQDrzoa1BIY9m4/ZKScupR9vfR";

	private static BASE64Encoder encoder = new BASE64Encoder();// 加密

	/**
	 * 获取Token-使用授权码
	 *
	 * @param code
	 * @return
	 * @throws IOException
	 */
	public String getToken(String code) throws IOException {

		//创建post请求https://api.vimeo.com/oauth/access_token
		HttpPost httpPost = new HttpPost("https://api.vimeo.com/oauth/access_token");

		String encode = encoder.encode((clentid + ":" + clientSecret).getBytes());

		//添加请求头
		httpPost.setHeader("Authorization", "basic " + encode);
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Accept", "application/vnd.vimeo.*+json;version=3.4");

		//封装请求参数
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("grant_type", "authorization_code");
		jsonObject.put("code", code);
		jsonObject.put("redirect_uri", "http://localhost:8080/index");

		//封装请求体
		StringEntity stringEntity = new StringEntity(jsonObject.toString(), "UTF-8");
		httpPost.setEntity(stringEntity);

		//发送请求
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);

		//获取返回参数
		return new JSONObject(EntityUtils.toString(closeableHttpResponse.getEntity())).toString();
	}

	/**
	 * 获取Token-不用授权码
	 *
	 * @return
	 * @throws IOException
	 */
	public String getToken2() throws IOException {
		//创建post请求https://api.vimeo.com/oauth/access_token
		HttpPost httpPost = new HttpPost("https://api.vimeo.com/oauth/authorize/client");

		String encode = encoder.encode((clentid + ":" + clientSecret).getBytes());

		//添加请求头
		httpPost.setHeader("Authorization", "basic " + encode);
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Accept", "application/vnd.vimeo.*+json;version=3.4");

		//封装请求参数
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("grant_type", "client_credentials");
		jsonObject.put("scope", "private purchased create edit delete interact upload promo_codes video_files public");

		//封装请求体
		StringEntity stringEntity = new StringEntity(jsonObject.toString(), "UTF-8");
		httpPost.setEntity(stringEntity);

		//发送请求
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);

		//获取返回参数
		return new JSONObject(EntityUtils.toString(closeableHttpResponse.getEntity())).toString();
	}

	/**
	 * 制作视频-基于表单
	 *
	 * @param token
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public String getVideos(String token) throws IOException {
		//创建post请求
		HttpPost httpPost = new HttpPost("https://api.vimeo.com/me/videos");

		//添加请求头
		httpPost.setHeader("Authorization", "bearer " + token);
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Accept", "application/vnd.vimeo.*+json;version=3.4");

		//添加请求体
		JSONObject jsonObject = new JSONObject();
		JSONObject uploadJson = new JSONObject();

		uploadJson.put("approach", "post");
		uploadJson.put("size", "1024");//视频字节码
		uploadJson.put("redirect_url", "http://localhost:8080/videospost.html");//返回路径wbe
		jsonObject.put("upload", uploadJson);

		//封装请求体
		StringEntity stringEntity = new StringEntity(jsonObject.toString(), "UTF-8");
		httpPost.setEntity(stringEntity);

		//发送请求
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);

		//获取返回参数
		return new JSONObject(EntityUtils.toString(closeableHttpResponse.getEntity())).toString();
	}

	/**
	 * 删除视频
	 *
	 * @param token
	 * @param id
	 * @return
	 * @throws IOException
	 */
	public String deleteVideos(String token, String id) throws IOException {
		//创建post请求
		HttpDelete httpDelete = new HttpDelete("https://api.vimeo.com/videos/" + id);

		//添加请求头
		httpDelete.setHeader("Authorization", "bearer " + token);
		httpDelete.setHeader("Content-Type", "application/json");
		httpDelete.setHeader("Accept", "application/vnd.vimeo.*+json;version=3.4");

		//发送请求
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpDelete);

		//获取返回参数
		try {
			String toString = new JSONObject(EntityUtils.toString(closeableHttpResponse.getEntity())).toString();
			return toString;
		} catch (Exception e) {
			e.getMessage();
			return "删除成功";
		}
	}

	/**
	 * 制作视频-基于pull
	 *
	 * @param token
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public String getVideosPull(String token, String url) throws IOException {
		//创建post请求
		HttpPost httpPost = new HttpPost("https://api.vimeo.com/me/videos");

		//添加请求头
		httpPost.setHeader("Authorization", "bearer " + token);
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Accept", "application/vnd.vimeo.*+json;version=3.4");

		//添加请求体
		JSONObject jsonObject = new JSONObject();
		JSONObject uploadJson = new JSONObject();
		uploadJson.put("approach", "pull");
		uploadJson.put("size", "1024");
		uploadJson.put("link", url);
		jsonObject.put("upload", uploadJson);

		//封装请求体
		StringEntity stringEntity = new StringEntity(jsonObject.toString(), "UTF-8");
		httpPost.setEntity(stringEntity);

		//发送请求
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);

		//获取返回参数
		return new JSONObject(EntityUtils.toString(closeableHttpResponse.getEntity())).toString();
	}

	/**
	 * 修改视频-基于表单
	 *
	 * @param token
	 * @param id
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public String updateVideos(String token, String id, String name) throws IOException {
		//创建post请求
		HttpPost httpPost = new HttpPost("https://api.vimeo.com/videos/" + id + "/versions");

		//添加请求头
		httpPost.setHeader("Authorization", "bearer " + token);
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Accept", "application/vnd.vimeo.*+json;version=3.4");

		//添加请求体
		JSONObject jsonObject = new JSONObject();
		JSONObject uploadJson = new JSONObject();

		uploadJson.put("approach", "post");
		uploadJson.put("size", "1024");
		uploadJson.put("status", "in_progress");
		jsonObject.put("upload", uploadJson);
		jsonObject.put("file_name", name);

		//封装请求体
		StringEntity stringEntity = new StringEntity(jsonObject.toString(), "UTF-8");
		httpPost.setEntity(stringEntity);

		//发送请求
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);

		//获取返回参数
		return new JSONObject(EntityUtils.toString(closeableHttpResponse.getEntity())).toString();
	}

//	/**
//	 * 获取Microsft-Token
//	 *
//	 * @param code
//	 * @return
//	 */
//	public String getMicrosftToken(String code) throws IOException {
//
//		String id = "e541cb7b-c24a-4211-991b-7c9ab25ad964";
//
//		//创建post请求grant_type authorization_code
//		HttpPost httpPost = new HttpPost("https://login.microsoftonline.com/common/oauth2/v2.0/token?"
//			+ "client_id=" + id
//			+ "&grant_type=authorization_code"
//			+ "&scope=openid%20offline_access%20https%3A%2F%2Fgraph.microsoft.com%2Fuser.read"
//			+ "&code=" + code
//			+ "&redirect_uri=http://localhost:8080/microsft"
//			+ "&client_secret=9c111fbb-82a1-405a-8f7a-d827857141d9");
//
//		//添加请求头
//		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
//
//		//发送请求
//		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
//		CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
//
//		System.out.println("1111111111111----" + EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8"));
//
//		//获取返回参数
//		return EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
//	}
}
