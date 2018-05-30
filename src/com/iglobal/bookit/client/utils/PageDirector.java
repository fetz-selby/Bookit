package com.iglobal.bookit.client.utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;


public class PageDirector {
	private static PageDirector director = new PageDirector();

	private PageDirector(){}

	public static PageDirector getInstance(){
		return director;
	}

	public void directTo(String htmlPage, String token){

		if(GWT.isProdMode()){
			String baseUrl = GWT.getHostPageBaseURL()+htmlPage+"#"+token;
			Window.Location.assign(baseUrl);
		}else{
			String fullURL = Window.Location.createUrlBuilder().setPath(htmlPage).buildString();
			if(fullURL.contains("#")){
				String rootURL = Window.Location.createUrlBuilder().setPath(htmlPage).buildString().split("#")[0];
				Window.Location.assign(rootURL+"#"+token);
			}
		}
	}
}
