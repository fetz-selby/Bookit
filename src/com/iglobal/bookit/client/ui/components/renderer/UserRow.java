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
import com.iglobal.bookit.shared.UserRowObject;

public class UserRow extends Composite {
	private UserRowObject object;
	private static UserRowUiBinder uiBinder = GWT.create(UserRowUiBinder.class);

	interface UserRowUiBinder extends UiBinder<Widget, UserRow> {
	}

	@UiField Label idField, nameField, emailField, groupField, dateField, statusField;
	@UiField AnchorElement actionField;
	
	public UserRow(UserRowObject object) {
		this.object = object;
		initWidget(uiBinder.createAndBindUi(this));
		initDOMComponent();
		initEvent();
	}

	private void initDOMComponent(){
		if(object == null){
			return;
		}
		
		idField.setText(object.getId());
		idField.getElement().setAttribute("style", "display:none");
		
		nameField.setText(Utils.getTruncatedText(object.getName(), 25));
		nameField.setTitle(object.getName());
		
		emailField.setText(Utils.getTruncatedText(object.getEmail(), 25));
		emailField.setTitle(object.getEmail());
		
		groupField.setText(Utils.getTruncatedText(Utils.getGroupName(object.getGroups(),","), 25));
		groupField.setTitle(Utils.getGroupName(object.getGroups(),","));
		
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
					GlobalResource.getInstance().getEventBus().fireEvent(new EditEvent(URLConstants.USERS, object));
				}
			});
		}
	}

	public UserRowObject getRowObject() {
		return object;
	}
	
}
