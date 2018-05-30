package com.iglobal.bookit.client.user.widget;

import com.google.gwt.user.client.ui.IsWidget;
import com.iglobal.bookit.client.user.widget.object.DynamicTextBoxQueryObject;

public interface IsEntryWidget extends IsWidget{
	public DynamicTextBoxQueryObject getQuery();
	public void reset();
}
