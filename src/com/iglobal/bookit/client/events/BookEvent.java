package com.iglobal.bookit.client.events;

import com.google.gwt.event.shared.GwtEvent;


public class BookEvent extends GwtEvent<BookEventHandler>{

	public static Type<BookEventHandler> TYPE = new Type<BookEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<BookEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(BookEventHandler handler) {
		handler.onBookInvoked(this);
	}
	
}
