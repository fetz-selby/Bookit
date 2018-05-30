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
import com.iglobal.bookit.client.events.ForceSessionCloseEvent;
import com.iglobal.bookit.client.ui.components.CustomConfirmationPanel;
import com.iglobal.bookit.client.ui.components.CustomConfirmationPanel.CustomConfirmationPanelEventHandler;
import com.iglobal.bookit.client.utils.Utils;
import com.iglobal.bookit.shared.SessionsRowObject;

public class SessionRow extends Composite {

	private SessionsRowObject object;
	private static SessionRowUiBinder uiBinder = GWT
			.create(SessionRowUiBinder.class);

	interface SessionRowUiBinder extends UiBinder<Widget, SessionRow> {
	}
	
	@UiField Label emailField, dateField;
	@UiField AnchorElement actionField;

	public SessionRow(SessionsRowObject rowObject) {
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
		
		emailField.setText(Utils.getTruncatedText(object.getEmail(), 45));
		emailField.setTitle(object.getEmail());
		
		dateField.setText(object.getStartDateTime());
	}

	private void initEvent(){
		if(GlobalResource.getInstance().isCanAdminUpdate()){
			actionField.setInnerText("close");
			Element actionFieldElement = actionField.cast();
			DOM.sinkEvents(actionFieldElement, Event.ONCLICK);
			DOM.setEventListener(actionFieldElement, new EventListener() {
				
				@Override
				public void onBrowserEvent(Event event) {
					CustomConfirmationPanel popup = new CustomConfirmationPanel("Please click on continue to terminate session or cancel to abort.");
					popup.setCustomConfirmationPanelEventHandler(new CustomConfirmationPanelEventHandler() {
						
						@Override
						public void onContinueClicked() {
							GlobalResource.getInstance().getEventBus().fireEvent(new ForceSessionCloseEvent(object.getEmail()));
						}
						
						@Override
						public void onCancelClicked() {
							// TODO Auto-generated method stub
							
						}
					});
					
					popup.show();
				}
			});
		}
	}
	
	public SessionsRowObject getRowObject() {
		return object;
	}

}
