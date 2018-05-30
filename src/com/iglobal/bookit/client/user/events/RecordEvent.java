package com.iglobal.bookit.client.user.events;

import com.google.gwt.event.shared.GwtEvent;


public class RecordEvent extends GwtEvent<RecordEventHandler>{

	public static Type<RecordEventHandler> TYPE = new Type<RecordEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<RecordEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RecordEventHandler handler) {
		handler.onRecordEventInvoked(this);
	}

}
