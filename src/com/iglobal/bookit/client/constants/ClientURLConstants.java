package com.iglobal.bookit.client.constants;

public enum ClientURLConstants {
	RECORDS("records"), TRANSACTION("transactions"), LOGOUT("logout"), SETTINGS("settings"), HELP("help"), REPORTS("reports");
	
	private String url;

	ClientURLConstants(String url){
		this.url = url;
	}
	
	public String getUrl(){
		return url;
	}
}
