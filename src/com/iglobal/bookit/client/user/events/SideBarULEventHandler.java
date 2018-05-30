package com.iglobal.bookit.client.user.events;

import java.util.ArrayList;

import com.google.gwt.user.client.Element;
import com.iglobal.bookit.shared.BookRowObject;

public interface SideBarULEventHandler {
	void onItemClicked(String itemId, String itemName, String columnString);
	void onItemRenderComplete(Element ulElement, ArrayList<BookRowObject> bookList);
}
