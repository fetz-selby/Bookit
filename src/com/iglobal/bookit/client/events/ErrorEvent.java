package com.iglobal.bookit.client.events;

import com.google.gwt.event.shared.GwtEvent;


public class ErrorEvent extends GwtEvent<ErrorEventHandler>{

	public static Type<ErrorEventHandler> TYPE = new Type<ErrorEventHandler>();
	private String message;
	
	public ErrorEvent(String message){
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ErrorEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ErrorEventHandler handler) {
		handler.onErrorEventInvoked(this);
	}

}
