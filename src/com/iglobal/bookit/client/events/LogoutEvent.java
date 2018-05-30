package com.iglobal.bookit.client.events;

import com.google.gwt.event.shared.GwtEvent;


public class LogoutEvent extends GwtEvent<LogoutEventHandler>{

	public static Type<LogoutEventHandler> TYPE = new Type<LogoutEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<LogoutEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LogoutEventHandler handler) {
		handler.onLogoutInvoked(this);
	}

}
