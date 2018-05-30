package com.iglobal.bookit.client.user.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.constants.TabWidgetTypesEnum;
import com.iglobal.bookit.client.utils.Utils;

public class TabWidget extends Composite {

	private String bookId, title, columnString;
	private TabWidgetTypesEnum type;
	private TabWidgetEventHandler handler;
	private static TabWidgetUiBinder uiBinder = GWT
			.create(TabWidgetUiBinder.class);

	interface TabWidgetUiBinder extends UiBinder<Widget, TabWidget> {
	}

	public interface TabWidgetEventHandler{
		void onTabWigetClicked(String bookId, String columnString, TabWidget widget);
		void onTabWigetCloseClicked(String bookId);
	}
	
	@UiField HTMLPanel mainPanel, closeBtn;
	@UiField SpanElement titleSpan;
	@UiField DivElement activeIndicator, tabTypeIndicator, activeDiv;
	@UiField Image moduleImg;
	
	public TabWidget(String bookId, String title, String columnString, TabWidgetTypesEnum type) {
		this.bookId = bookId;
		this.title = title;
		this.columnString = columnString;
		this.type = type;
		
		initWidget(uiBinder.createAndBindUi(this));
		initComponents();
		initEvents();
	}

	private void initComponents(){
		titleSpan.setInnerText(Utils.getTruncatedText(title, 13));
		titleSpan.setAttribute("title", title);
		
		switch(type){
		case RECORDS:
			tabTypeIndicator.addClassName("tab-transaction");
			moduleImg.setUrl("images/record.png");
			break;
		case TRANSACTIONS:
			tabTypeIndicator.addClassName("tab-record");
			moduleImg.setUrl("images/transaction.png");
			break;
		case REPORTS:
			tabTypeIndicator.addClassName("tab-report");
			moduleImg.removeFromParent();
			break;
		default:
			break;
		}
	}
	
	private void initEvents(){
		
		Element titleSpanElement = titleSpan.cast();
		DOM.sinkEvents(titleSpanElement, Event.ONCLICK);
		DOM.setEventListener(titleSpanElement, new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				setActive(true);
				if(handler != null){
					handler.onTabWigetClicked(bookId, columnString, TabWidget.this);
				}				
			}
		});
		
		
		Element divElement = activeIndicator.cast();
		DOM.sinkEvents(divElement, Event.ONCLICK);
		DOM.setEventListener(divElement, new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				setActive(true);
				if(handler != null){
					handler.onTabWigetClicked(bookId, columnString, TabWidget.this);
				}				
			}
		});
		
		closeBtn.sinkEvents(Event.ONCLICK);
		closeBtn.addHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					handler.onTabWigetCloseClicked(bookId);
				}
			}
		}, ClickEvent.getType());
	}
	
	public void fireOnTabWidgetClick(){
		if(handler != null){
			handler.onTabWigetClicked(bookId, columnString, TabWidget.this);
			setActive(true);
		}
	}

	public void setTabWidgetEventHandler(TabWidgetEventHandler handler){
		this.handler = handler;
	}
	
	public void setActive(boolean isActive){
		if(isActive){
			activeIndicator.addClassName("tab-active-indicator");
			activeDiv.addClassName("tab-active-indicator");
		}else{
			activeIndicator.removeClassName("tab-active-indicator");
			activeDiv.removeClassName("tab-active-indicator");
		}
	}
	
	public String getTabTitle(){
		return title;
	}
}
