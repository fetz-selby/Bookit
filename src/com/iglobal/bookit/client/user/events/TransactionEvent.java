package com.iglobal.bookit.client.user.events;

import com.google.gwt.event.shared.GwtEvent;


public class TransactionEvent extends GwtEvent<TransactionEventHandler>{
	
	public static Type<TransactionEventHandler> TYPE = new Type<TransactionEventHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<TransactionEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(TransactionEventHandler handler) {
		handler.onTransactionEventInvoke(this);
	}
}
