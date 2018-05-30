package com.iglobal.bookit.client.user.widget.editpanel;

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
import com.iglobal.bookit.client.user.widget.EViewCalendar;
import com.iglobal.bookit.client.utils.Utils;

public class DateEditPanel extends Composite implements IsEditableUserWidget{

	private String title, value;
	private EditPanelEventHandler handler;
	private static DateEditPanelUiBinder uiBinder = GWT
			.create(DateEditPanelUiBinder.class);

	interface DateEditPanelUiBinder extends UiBinder<Widget, DateEditPanel> {
	}

	@UiField EViewCalendar dateCalendar;
	@UiField Button saveBtn;
	@UiField Anchor cancelLink;
	@UiField SpanElement closeBtn, formTitle;
	
	public DateEditPanel(String title, String value) {
		this.title = title;
		this.value = value;
		
		initWidget(uiBinder.createAndBindUi(this));
		initComponents();
		initEvent();
	}
	
	private void initComponents(){
		if(value != null && !value.trim().isEmpty()){
			//nameBox.getElement().setAttribute("placeholder", value);
		}

		formTitle.setInnerText("Edit "+title);
		cancelLink.setText("cancel");
	}
	
	private void initEvent(){
		saveBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					if(dateCalendar.getDate() != null){
						handler.onSaveClicked(Utils.getDateString(dateCalendar.getDate()));
					}else{
						handler.onSaveClicked("");
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
