package com.iglobal.bookit.client.events;

public interface RPCCompleteHandler<T> {
	void onProccessComplete(T t);
}
