package com.iglobal.bookit.client.events;

import com.google.gwt.event.shared.EventHandler;


public interface ErrorEventHandler extends EventHandler{
	void onErrorEventInvoked(ErrorEvent event);
}
