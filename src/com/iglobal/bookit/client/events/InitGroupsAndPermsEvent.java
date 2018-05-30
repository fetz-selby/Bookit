package com.iglobal.bookit.client.events;

import com.google.gwt.event.shared.GwtEvent;


public class InitGroupsAndPermsEvent extends GwtEvent<InitGroupsAndPermsEventHandler>{

	public static Type<InitGroupsAndPermsEventHandler> TYPE = new Type<InitGroupsAndPermsEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<InitGroupsAndPermsEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(InitGroupsAndPermsEventHandler handler) {
		handler.onGroupAndPermsEventInvoked(this);
	}

}
