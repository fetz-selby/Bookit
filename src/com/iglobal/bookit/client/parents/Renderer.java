package com.iglobal.bookit.client.parents;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.IsWidget;

public interface Renderer<T> extends IsWidget{
	void load();
	void setRenderer(ArrayList<T> renderList);
}
