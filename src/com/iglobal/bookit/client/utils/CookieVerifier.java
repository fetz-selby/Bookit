package com.iglobal.bookit.client.utils;

import java.util.Date;

import com.google.gwt.user.client.Cookies;

public class CookieVerifier {
	private static CookieVerifier cookie = new CookieVerifier();
	
	private CookieVerifier(){}
	
	public static CookieVerifier getInstance(){
		return cookie;
	}
	
	public static void addCookie(String id, String username, String type){
		Date date = new Date();
		
		//Expires in 8 hours from login
		date.setHours(date.getHours() + 8);
		//date.setMinutes(date.getMinutes()+1);
		//date.setMinutes(date.getMinutes() + 1);

		
		Cookies.setCookie("id", id, date);
		Cookies.setCookie("username", username);
		Cookies.setCookie("type", type, date);
		
	}
	
	public static void clearCookie(){
		Cookies.removeCookie("id");
		Cookies.removeCookie("username");
		Cookies.removeCookie("type");
	}
	
	public static boolean isCookieExist(){
		if(Cookies.getCookie("type") != null && !(Cookies.getCookie("type").isEmpty()) && Cookies.getCookie("id") != null && !(Cookies.getCookie("id").isEmpty())){
			return true;
		}
		
		return false;
	}
	
	public void setAppSessionId(String id){
		Cookies.setCookie("app_session", id);
	}

	public String getAppSessionId(){
		return Cookies.getCookie("app_session");
	}
	
	public String getPersonType(){
		return Cookies.getCookie("type");
	}
	
	public String getUsername(){
		return Cookies.getCookie("username");
	}
	
	public String getId(){
		return Cookies.getCookie("id");
	}
	
}
