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
import com.iglobal.bookit.shared.BookRowObject;

public class BookRow extends Composite {

	private BookRowObject object;
	private static BookRowUiBinder uiBinder = GWT.create(BookRowUiBinder.class);

	interface BookRowUiBinder extends UiBinder<Widget, BookRow> {
	}

	@UiField Label idField, nameField, createdByField, groupField, dateField, statusField;
	@UiField AnchorElement actionField;
	
	public BookRow(BookRowObject object) {
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
		
		nameField.setText(Utils.getTruncatedText(object.getName().split("[:]")[1], 25));
		nameField.setTitle(object.getName().split("[:]")[1]);
		
		createdByField.setText(Utils.getTruncatedText(object.getCreatedBy(), 25));
		createdByField.setTitle(object.getCreatedBy());
		
		groupField.setText(Utils.getTruncatedText(Utils.getGroupName(object.getGroups(), ","), 25));
		groupField.setTitle(Utils.getGroupName(object.getGroups(), ","));
		
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
					GlobalResource.getInstance().getEventBus().fireEvent(new EditEvent(URLConstants.BOOKS, object));
				}
			});
		}
	}

	public BookRowObject getRowObject() {
		return object;
	}
}
