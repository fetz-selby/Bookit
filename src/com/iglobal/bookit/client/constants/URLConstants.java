package com.iglobal.bookit.client.constants;

public enum URLConstants {
	LOGIN ("login"), ADMINS ("admins"), USERS ("users"), BOOKS ("books"), GROUPS ("groups"), LOGOUT("logout"), SETTINGS("settings"), HELP("help"), SESSIONS("sessions");
	
	private String url;

	URLConstants(String url){
		this.url = url;
	}
	
	public String getUrl(){
		return url;
	}
}
