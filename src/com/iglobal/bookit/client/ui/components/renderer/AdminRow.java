package com.iglobal.bookit.client.ui.components.renderer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.GlobalResource;
import com.iglobal.bookit.client.constants.URLConstants;
import com.iglobal.bookit.client.events.EditEvent;
import com.iglobal.bookit.client.utils.Utils;
import com.iglobal.bookit.shared.AdminRowObject;

public class AdminRow extends Composite {

	private AdminRowObject object;
	private static AdminRowUiBinder uiBinder = GWT
			.create(AdminRowUiBinder.class);

	interface AdminRowUiBinder extends UiBinder<Widget, AdminRow> {
	}

	@UiField Label idField, nameField, emailField, permsField, dateField, statusField;
	@UiField AnchorElement actionField;
	
	public AdminRow(AdminRowObject rowObject) {
		this.object = rowObject;
		initWidget(uiBinder.createAndBindUi(this));
		initDOMComponent();
		initEvent();
	}

	private void initDOMComponent(){
		if(object == null){
			GWT.log("was null");
			return;
		}
		
		GWT.log("id = "+object.getId()+", name = "+object.getName()+", email = "+object.getEmail()+", perms = "+object.getPerms()+", date = "+object.getDate()+", status = "+object.getStatus());
		
		idField.setText(object.getId());
		idField.getElement().setAttribute("style", "display:none");
		
		nameField.setText(Utils.getTruncatedText(object.getName(), 25));
		nameField.setTitle(object.getName());
		
		emailField.setText(Utils.getTruncatedText(object.getEmail(), 25));
		emailField.setTitle(object.getEmail());
		
		permsField.setText(Utils.getPermsName(object.getPerms(), ","));
		dateField.setText(object.getDate());
		statusField.setText(object.getStatus());
	}

	private void initEvent(){
		if(GlobalResource.getInstance().isCanAdminUpdate()){
			actionField.setInnerText("edit");
			Element actionFieldElement = actionField.cast();
			DOM.sinkEvents(actionFieldElement, Event.ONCLICK);
			DOM.setEventListener(actionFieldElement, new EventListener() {
				
				@Override
				public void onBrowserEvent(Event event) {
					GlobalResource.getInstance().getEventBus().fireEvent(new EditEvent(URLConstants.ADMINS, object));
				}
			});
		}
	}
	
	public AdminRowObject getRowObject() {
		return object;
	}
}
