package com.iglobal.bookit.client.ui.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class CustomConfirmationPanel extends Composite {

	private CustomConfirmationPanelEventHandler handler;
	private CustomDraggablePopupPanel popup = new CustomDraggablePopupPanel(true);
	private static CustomConfirmationPanelUiBinder uiBinder = GWT
			.create(CustomConfirmationPanelUiBinder.class);

	interface CustomConfirmationPanelUiBinder extends
			UiBinder<Widget, CustomConfirmationPanel> {
	}
	
	public interface CustomConfirmationPanelEventHandler{
		void onCancelClicked();
		void onContinueClicked();
	}

	@UiField Button continueBtn;
	@UiField Anchor cancelLink;
	@UiField SpanElement closeBtn, message;
	
	public CustomConfirmationPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
		initEvent();
		popup.add(this);
	}
	
	public CustomConfirmationPanel(String message) {
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
		initEvent();
		popup.add(this);
		doAddMessage(message);
	}
	
	private void doAddMessage(String message){
		this.message.setInnerText(message);
	}
	
	private void initComponent(){
		cancelLink.setText("cancel");
		popup.setAutoHideEnabled(true);
		popup.setGlassEnabled(true);
		popup.setModal(true);
	}
	
	private void initEvent(){
		continueBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					handler.onContinueClicked();
				}
				popup.hide();
			}
		});
		
		cancelLink.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					handler.onCancelClicked();
				}
				popup.hide();
			}
		});
		
		Element closeBtnElement = closeBtn.cast();
		DOM.sinkEvents(closeBtnElement, Event.ONCLICK);
		DOM.setEventListener(closeBtnElement, new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				popup.hide();
			}
		});
	}
	
	public void show(){
		popup.center();
	}
	
	public void hide(){
		popup.hide();
	}

	public void setCustomConfirmationPanelEventHandler(CustomConfirmationPanelEventHandler handler){
		this.handler = handler;
	}
}
