package com.iglobal.bookit.client.events;

import com.google.gwt.event.shared.GwtEvent;


public class SuccessEvent extends GwtEvent<SuccessEventHandler>{

	public static Type<SuccessEventHandler> TYPE = new Type<SuccessEventHandler>();
	private String message;
	
	public SuccessEvent(String message){
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SuccessEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SuccessEventHandler handler) {
		handler.onSuccessEventInvoked(this);
	}

}
