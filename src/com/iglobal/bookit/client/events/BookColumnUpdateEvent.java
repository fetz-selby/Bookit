package com.iglobal.bookit.client.events;

import com.google.gwt.event.shared.GwtEvent;


public class BookColumnUpdateEvent extends GwtEvent<BookColumnUpdateEventHandler>{

	public static Type<BookColumnUpdateEventHandler> TYPE = new Type<BookColumnUpdateEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<BookColumnUpdateEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(BookColumnUpdateEventHandler handler) {
		handler.onBookColumnUpdate(this);
	}

}
