/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lian.vimeo.controller;

import com.lian.vimeo.service.VimeoService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lian
 */
@RestController
@RequestMapping("/")
public class VimeoController {

	@Autowired
	private VimeoService vimeoService;

	@Autowired
	private HttpServletRequest httpServletRequest;

	/**
	 * 获取令牌-使用授权码
	 *
	 * @param state
	 * @param code
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/index")
	public String getIndex(String state, String code) throws IOException {
		if (!"1".equals(state)) {
			return null;
		}
		//获取token--该方法会获取用户数据
		String token = vimeoService.getToken(code);
		String toString = new JSONObject(token).get("access_token").toString();
		System.out.println("1---" + toString);
		HttpSession session = httpServletRequest.getSession();
		session.setAttribute("token", toString);
		return toString;
	}

	/**
	 * 制作视频-基于pull
	 *
	 * @param url
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/getVideosPull")
	public String getVideosPull(String url) throws IOException {
		HttpSession session = httpServletRequest.getSession();
		String token = session.getAttribute("token").toString();
		System.out.println("token:" + token);
		System.out.println("url:" + url);
		String videosPull = vimeoService.getVideosPull(token, url);
		System.out.println("getVideosPull--" + videosPull);
		return videosPull;
	}

	/**
	 * 制作视频-基于表单的方法
	 *
	 * @param url
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/getVideos")
	public String getVideos() throws IOException {
		HttpSession session = httpServletRequest.getSession();
		String token = session.getAttribute("token").toString();
		System.out.println("token:" + token);
		String videos = vimeoService.getVideos(token);
		System.out.println("getVideos--" + videos);
		return videos;
	}

	/**
	 * 修改视频-基于表单
	 *
	 * @param id
	 * @param name
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/updateVideos")
	public String updateVideos(String id, String name) throws IOException {
		HttpSession session = httpServletRequest.getSession();
		String token = session.getAttribute("token").toString();
		System.out.println("token:" + token);
		String updateVideos = vimeoService.updateVideos(token, id, name);
		System.out.println("updateVideos" + updateVideos);
		return updateVideos;
	}

	/**
	 * 获取Microsft-Token
	 *
	 * @param code
	 * @param state
	 * @return
	 * @throws IOException
	 */
//	@RequestMapping("/microsft")
//	public String getMicrosftToken(String code, String state) throws IOException {
//		if (!"1".equals(state)) {
//			return null;
//		}
////		String microsftTokenObject = vimeoService.getMicrosftToken(code);
//		return code;
//	}
	/**
	 * 获取令牌
	 *
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/getToken")
	public String getToken() throws IOException {
		//获取token
		String token = vimeoService.getToken2();
		return token;
	}
}
