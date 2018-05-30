package com.iglobal.bookit.client.ui.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class CustomRadio extends Composite {

	private RadioButton radioButton;
	private String radioText;
	private CustomRadioEventHandler handler;
	private static CustomRadioUiBinder uiBinder = GWT
			.create(CustomRadioUiBinder.class);

	interface CustomRadioUiBinder extends UiBinder<Widget, CustomRadio> {
	}
	
	public interface CustomRadioEventHandler{
		void onRadioClicked(String radioValue);
	}

	@UiField SpanElement radioLabel;
	@UiField SimplePanel radioDiv;
	
	private String radioValue;
	private String groupName;
	
	public CustomRadio(String radioLabel, String radioValue, String groupName) {
		this.radioText = radioLabel;
		this.radioValue = radioValue;
		this.groupName = groupName;
		
		
		initWidget(uiBinder.createAndBindUi(this));
		initComponents();
		initEvent();
	}
	
	private void initComponents(){
		radioButton = new RadioButton(groupName);
		radioLabel.setInnerText(radioText);
		
		radioDiv.add(radioButton);
	}
	
	private void initEvent(){
		radioButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					handler.onRadioClicked(radioValue);
				}
			}
		});
	}
	
	public void setActive(boolean isEnabled){
		if(isEnabled){
			radioButton.setValue(true, true);
		}else{
			radioButton.setValue(false);
		}
	}
	
	public void setRadioGroupName(String groupName){
		radioButton.setName(groupName);
	}

	public String getRadioValue(){
		return radioValue;
	}
	
	public void setCustomRadioEventHandler(CustomRadioEventHandler handler){
		this.handler = handler;
	}
	
}
