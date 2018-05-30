package com.iglobal.bookit.client.user.events;

import com.google.gwt.event.shared.GwtEvent;


public class ReportEvent extends GwtEvent<ReportEventHandler>{

	public static Type<ReportEventHandler> TYPE = new Type<ReportEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ReportEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ReportEventHandler handler) {
		handler.onReportClicked(this);
	}

}
