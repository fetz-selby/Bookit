package com.iglobal.bookit.client.events;

import com.google.gwt.event.shared.EventHandler;


public interface AddEventHandler extends EventHandler{
	void onAddInvoked(AddEvent event);
}
