package com.iglobal.bookit.client.events;

import com.google.gwt.event.shared.GwtEvent;


public class ForceSessionCloseEvent extends GwtEvent<ForceSessionCloseEventHandler>{
	private String email;
	public static Type<ForceSessionCloseEventHandler> TYPE = new Type<ForceSessionCloseEventHandler>();
	
	public ForceSessionCloseEvent(String email){
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ForceSessionCloseEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ForceSessionCloseEventHandler handler) {
		handler.onSessionCloseInvoked(this);
	}

}
