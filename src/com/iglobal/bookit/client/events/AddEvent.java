package com.iglobal.bookit.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.iglobal.bookit.client.constants.URLConstants;

public class AddEvent extends GwtEvent<AddEventHandler>{

	private URLConstants url;
	
	public static Type<AddEventHandler> TYPE = new Type<AddEventHandler>();
	
	public AddEvent(URLConstants url){
		this.url = url;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AddEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddEventHandler handler) {
		handler.onAddInvoked(this);
	}

	public URLConstants getUrl() {
		return url;
	}

}
