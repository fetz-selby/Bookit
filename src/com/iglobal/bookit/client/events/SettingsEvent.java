package com.iglobal.bookit.client.events;

import com.google.gwt.event.shared.GwtEvent;


public class SettingsEvent extends GwtEvent<SettingsEventHandler> {

	public static Type<SettingsEventHandler> TYPE = new Type<SettingsEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SettingsEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SettingsEventHandler handler) {
		handler.onSettingsInvoked(this);
	}

}
