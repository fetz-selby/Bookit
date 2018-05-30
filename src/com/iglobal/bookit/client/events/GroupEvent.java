package com.iglobal.bookit.client.events;

import com.google.gwt.event.shared.GwtEvent;


public class GroupEvent extends GwtEvent<GroupEventHandler>{

	public static Type<GroupEventHandler> TYPE = new Type<GroupEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<GroupEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(GroupEventHandler handler) {
		handler.onGroupInvoked(this);
	}

}
