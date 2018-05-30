package com.iglobal.bookit.client.events;

import com.google.gwt.event.shared.EventHandler;


public interface BookEventHandler extends EventHandler{
	void onBookInvoked(BookEvent event);
}
