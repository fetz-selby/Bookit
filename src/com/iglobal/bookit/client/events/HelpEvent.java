package com.iglobal.bookit.client.events;

import com.google.gwt.event.shared.GwtEvent;


public class HelpEvent extends GwtEvent<HelpEventHandler>{

	public static Type<HelpEventHandler> TYPE = new Type<HelpEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<HelpEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(HelpEventHandler handler) {
		handler.onHelpInvoked(this);
	}

}
