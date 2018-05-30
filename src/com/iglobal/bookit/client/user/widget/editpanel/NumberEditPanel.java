package com.iglobal.bookit.client.user.widget.editpanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class NumberEditPanel extends Composite implements IsEditableUserWidget{

	private String title, value;
	private EditPanelEventHandler handler;
	private static NumberEditPanelUiBinder uiBinder = GWT
			.create(NumberEditPanelUiBinder.class);

	interface NumberEditPanelUiBinder extends UiBinder<Widget, NumberEditPanel> {
	}

	@UiField TextBox numberBox;
	@UiField Button saveBtn;
	@UiField Anchor cancelLink;
	@UiField SpanElement closeBtn, formTitle;
	
	public NumberEditPanel(String title, String value) {
		this.title = title;
		this.value = value;
		
		initWidget(uiBinder.createAndBindUi(this));
		initComponents();
		doIntConstraints();
		initEvent();
	}
	
	private void initComponents(){
		if(value != null && !value.trim().isEmpty()){
			numberBox.getElement().setAttribute("placeholder", value);
		}
		
		formTitle.setInnerText("Edit "+title);
		cancelLink.setText("cancel");
	}
	
	private void initEvent(){
		saveBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					if(numberBox.getText().trim().isEmpty()){
						handler.onSaveClicked(value);
					}else{
						handler.onSaveClicked(numberBox.getText().trim());
					}
				}
			}
		});
		
		cancelLink.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					handler.onCancelClicked();
				}					
			}
		});
		
		Element closeBtnElement = closeBtn.cast();
		DOM.sinkEvents(closeBtnElement, Event.ONCLICK);
		DOM.setEventListener(closeBtnElement, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if(handler != null){
					handler.onCancelClicked();
				}			
			}
		});
	}
	
	private void doIntConstraints(){
		numberBox.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				Widget sender = (Widget) event.getSource();
			    char charCode = event.getCharCode();
			    int keyCode = event.getNativeEvent().getKeyCode();
			    if (!Character.isDigit(charCode) && charCode != '.' && charCode != '-' && keyCode != KeyCodes.KEY_TAB && keyCode != KeyCodes.KEY_BACKSPACE && keyCode != KeyCodes.KEY_DELETE && keyCode != KeyCodes.KEY_ENTER && keyCode != KeyCodes.KEY_HOME && keyCode != KeyCodes.KEY_END && keyCode != KeyCodes.KEY_LEFT && keyCode != KeyCodes.KEY_UP && keyCode != KeyCodes.KEY_RIGHT && keyCode != KeyCodes.KEY_DOWN) {
			      ((TextBox) sender).cancelKey();
			    }

			    String text = numberBox.getText();
			    if (text.contains(".") && charCode == '.') {
			      ((TextBox) sender).cancelKey();
			    }				
			}
		});
		
	}

	@Override
	public void setEditPanelEventHandler(EditPanelEventHandler handler) {
		this.handler = handler;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

}
