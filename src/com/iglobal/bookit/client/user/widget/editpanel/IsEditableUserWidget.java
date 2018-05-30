package com.iglobal.bookit.client.user.widget.editpanel;

import com.google.gwt.user.client.ui.IsWidget;


public interface IsEditableUserWidget extends IsWidget{
	public void setEditPanelEventHandler(EditPanelEventHandler handler);
	public void show();
	public void hide();
}
