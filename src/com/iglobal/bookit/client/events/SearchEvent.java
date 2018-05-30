package com.iglobal.bookit.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.iglobal.bookit.client.constants.URLConstants;

public class SearchEvent extends GwtEvent<SearchEventHandler>{

	public static Type<SearchEventHandler> TYPE = new Type<SearchEventHandler>();
	private URLConstants url;
	
	public SearchEvent(URLConstants url){
		this.url = url;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SearchEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SearchEventHandler handler) {
		handler.onSearchInvoked(this);
	}

	public URLConstants getUrl() {
		return url;
	}
}
