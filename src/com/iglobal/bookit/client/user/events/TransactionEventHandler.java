package com.iglobal.bookit.client.user.events;

import com.google.gwt.event.shared.EventHandler;


public interface TransactionEventHandler extends EventHandler{
	void onTransactionEventInvoke(TransactionEvent event);
}
