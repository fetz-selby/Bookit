package com.iglobal.bookit.client.ui.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class NotificationSideWidget extends Composite {

	private static NotificationSideWidgetUiBinder uiBinder = GWT
			.create(NotificationSideWidgetUiBinder.class);

	interface NotificationSideWidgetUiBinder extends
			UiBinder<Widget, NotificationSideWidget> {
	}

	@UiField HTMLPanel mainPanel;
	@UiField DivElement statementDiv, notiCloseBtn, counterDiv;
	
	public NotificationSideWidget(String bookName, int counter) {
		initWidget(uiBinder.createAndBindUi(this));
		initComponents(counter, bookName);
		initEvent();
	}

	private void initComponents(int counter, String bookName){
		String alertString = "alert";
		
		if(counter > 1){
			alertString = "alerts";
		}
		
		statementDiv.setInnerText(" "+alertString+" from "+bookName);
		counterDiv.setInnerText(""+counter);
	}
	
	private void initEvent(){
		Element btn = (Element)notiCloseBtn.cast();
		
		DOM.sinkEvents(btn, Event.ONCLICK);
		DOM.setEventListener(btn, new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				//Do kekye
				removeFromParent();
			}
		});
	}
	
}
