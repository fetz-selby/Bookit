package com.iglobal.bookit.client.user.widget.editpanel;

public interface EditPanelEventHandler {
	void onCancelClicked();
	void onSaveClicked(String value);
	void onImageSaveClicked(String imgUrl);
}
