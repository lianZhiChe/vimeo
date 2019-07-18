/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lian.vimeo.controller;

import com.lian.vimeo.service.MicrosoftService;
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
public class MicrosoftController {
	
	@Autowired
	private MicrosoftService microsoftService;
	
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	/**
	 * 获取Microsft-Token
	 *
	 * @param code
	 * @param state
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/microsft")
	public String getMicrosftToken(String code, String state) throws IOException {
		if (!"1".equals(state)) {
			return null;
		}
		System.out.println("code+" + code);//refresh_token
		JSONObject jsonObject = new JSONObject(microsoftService.getMicrosftToken(code));
		//access_token
		String token = jsonObject.get("access_token").toString();
		System.out.println("Microsft-Token:" + token);
		HttpSession session = httpServletRequest.getSession();
		session.setAttribute("Microsft-Token", token);
		//refresh_token
		String refresh = jsonObject.get("refresh_token").toString();
		System.out.println("Microsft-refresh_token:" + refresh);
		session.setAttribute("Microsft-refresh_token", refresh);
		//id_token
		String idToken = jsonObject.get("id_token").toString();
		System.out.println("Microsft-id_token:" + idToken);
		session.setAttribute("Microsft-id_token", idToken);
		return token;
	}
	
	/**
	 * 使用Microsft令牌
	 *
	 * @return
	 */
	@RequestMapping("/useMicrosftToken")
	public String useMicrosftToken() throws IOException {
		//获取令牌
		HttpSession session = httpServletRequest.getSession();
		String token = session.getAttribute("Microsft-Token").toString();
		String useMicrosftToken = microsoftService.useMicrosftToken(token);
		return useMicrosftToken;
	}

	/**
	 * 刷新Microsft令牌
	 *
	 * @return
	 */
	@RequestMapping("/refreshMicrosftToken")
	public String refreshMicrosftToken() throws IOException {
		//获取令牌
		HttpSession session = httpServletRequest.getSession();
		String token = session.getAttribute("Microsft-refresh_token").toString();
		JSONObject jsonObject = new JSONObject(microsoftService.refreshMicrosftToken(token));
		//令牌
		String refreshToken = jsonObject.get("access_token").toString();
		System.out.println("Microsft-Token:" + refreshToken);
		session.setAttribute("Microsft-Token", refreshToken);
		//刷新令牌使用的token
		String refresh = jsonObject.get("refresh_token").toString();
		System.out.println("Microsft-refresh_token:" + refresh);
		session.setAttribute("Microsft-refresh_token", refresh);
		return jsonObject.toString();
	}
}
