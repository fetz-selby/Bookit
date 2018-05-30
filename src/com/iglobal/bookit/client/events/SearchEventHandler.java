package com.iglobal.bookit.client.events;

import com.google.gwt.event.shared.EventHandler;


public interface SearchEventHandler extends EventHandler{
	void onSearchInvoked(SearchEvent event);
}
