package com.iglobal.bookit.client.events;

import com.google.gwt.event.shared.GwtEvent;


public class PageSwitchEvent extends GwtEvent<PageSwitchEventHandler>{

	public enum AppType{
		ADMIN, USER, LOGIN;
	}
	
	private AppType appType;
	public static Type<PageSwitchEventHandler> TYPE = new Type<PageSwitchEventHandler>();
	
	public PageSwitchEvent(AppType appType){
		this.appType = appType;
	}
	
	public AppType getAppType(){
		return appType;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PageSwitchEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PageSwitchEventHandler handler) {
		handler.onPageSwitch(this);
	}

}
