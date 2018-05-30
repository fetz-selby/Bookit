package com.iglobal.bookit.client.user.events;

import com.google.gwt.event.shared.GwtEvent;


public class AddTabEvent extends GwtEvent<AddTabEventHandler>{

	private String tabName, id;
	public static Type<AddTabEventHandler> TYPE = new Type<AddTabEventHandler>();
	
	public AddTabEvent(String tabName, String id){
		this.tabName = tabName;
		this.id = id;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AddTabEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddTabEventHandler handler) {
		handler.onTabAddInvoked(this);
	}

	public String getTabName() {
		return tabName;
	}

	public String getId() {
		return id;
	}

}
