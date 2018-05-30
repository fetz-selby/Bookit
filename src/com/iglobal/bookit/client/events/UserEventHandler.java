package com.iglobal.bookit.client.events;

import com.google.gwt.event.shared.EventHandler;


public interface UserEventHandler extends EventHandler{
	void onUserInvoked(UserEvent event);
}
