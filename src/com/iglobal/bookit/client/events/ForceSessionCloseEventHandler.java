package com.iglobal.bookit.client.events;

import com.google.gwt.event.shared.EventHandler;


public interface ForceSessionCloseEventHandler extends EventHandler{
	void onSessionCloseInvoked(ForceSessionCloseEvent event);
}
