package com.iglobal.bookit.client.parents;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface Presenter extends IsWidget{
	public void bind();
	public Widget getWidget();
	public void showScreen(HasOneWidget container);
}
