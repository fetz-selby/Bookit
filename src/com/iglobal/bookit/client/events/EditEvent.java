package com.iglobal.bookit.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.iglobal.bookit.client.constants.URLConstants;

public class EditEvent extends GwtEvent<EditEventHandler>{

	private IsSerializable object;
	private URLConstants modelType;
	
	public static Type<EditEventHandler> TYPE = new Type<EditEventHandler>();
	
	public EditEvent(URLConstants modelType, IsSerializable object){
		this.modelType = modelType;
		this.object = object;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<EditEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EditEventHandler handler) {
		handler.onEditEventInvoked(this);
	}

	public IsSerializable getObject() {
		return object;
	}

	public URLConstants getModelType() {
		return modelType;
	}

}
