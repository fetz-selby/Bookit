package com.iglobal.bookit.client.ui.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class SearchBoxWidget extends Composite {

	private SearchBoxWidgetEventHandler handler;
	private static SearchBoxWidgetUiBinder uiBinder = GWT
			.create(SearchBoxWidgetUiBinder.class);

	interface SearchBoxWidgetUiBinder extends UiBinder<Widget, SearchBoxWidget> {
	}

	public interface SearchBoxWidgetEventHandler{
		void onSearchInvoked(String query);
	}
	
	@UiField TextBox textBox;
	@UiField Button searchBtn;
	
	public SearchBoxWidget(SearchBoxWidgetEventHandler handler) {
		this.handler = handler;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
		initEvent();
	}
	
	private void initComponent(){
		searchBtn.getElement().appendChild(getSearchIconElement());
	}
	
	private Element getSearchIconElement(){
		Element iElement = DOM.createElement("i");
		iElement.setClassName("fa fa-search");
		
		return iElement;
	}
	
	private void initEvent(){
		textBox.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if((event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) && !(textBox.getText().trim().isEmpty()) && handler != null){
					handler.onSearchInvoked(textBox.getText().trim());
				}
			}
		});
		
		searchBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(!(textBox.getText().trim().isEmpty()) && handler != null){
					handler.onSearchInvoked(textBox.getText().trim());
				}
			}
		});
	}
	
	public String getSearchText(){
		return textBox.getText().trim();
	}
	
	public void setPlaceHolder(String text){
		textBox.getElement().setAttribute("placeholder", text);
	}

}
