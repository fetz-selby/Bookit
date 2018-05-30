package com.iglobal.bookit.client.events;

import com.google.gwt.event.shared.GwtEvent;


public class SessionsEvent extends GwtEvent<SessionsEventHandler>{

	public static Type<SessionsEventHandler> TYPE = new Type<SessionsEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SessionsEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SessionsEventHandler handler) {
		handler.onSessionsInvoked(this);
	}

}
