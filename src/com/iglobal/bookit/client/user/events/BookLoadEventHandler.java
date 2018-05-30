package com.iglobal.bookit.client.user.events;

import java.util.ArrayList;

import com.iglobal.bookit.shared.BookRowObject;

public interface BookLoadEventHandler {
	void onLoadComplete(ArrayList<BookRowObject> bookList);
}
