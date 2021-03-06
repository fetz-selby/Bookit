package com.iglobal.bookit.client.events;

import com.google.gwt.event.shared.GwtEvent;


public class AdminEvent extends GwtEvent<AdminEventHandler>{

	public static Type<AdminEventHandler> TYPE = new Type<AdminEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AdminEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AdminEventHandler handler) {
		handler.onAdminInvoked(this);
	}

}
