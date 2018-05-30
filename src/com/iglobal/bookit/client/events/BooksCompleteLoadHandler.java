package com.iglobal.bookit.client.events;

import java.util.ArrayList;

import com.iglobal.bookit.shared.BookRowObject;

public interface BooksCompleteLoadHandler {
	public void onBooksLoadComplete(ArrayList<BookRowObject> booksList);
}
