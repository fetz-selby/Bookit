package com.iglobal.bookit.client.ui.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class SidebarMenuWidget extends Composite {

	private SidebarMenuWidgetEventHandler handler;
	private String imgURL, menuName;
	private static SidebarMenuWidgetUiBinder uiBinder = GWT
			.create(SidebarMenuWidgetUiBinder.class);

	interface SidebarMenuWidgetUiBinder extends
			UiBinder<Widget, SidebarMenuWidget> {
	}
	
	public interface SidebarMenuWidgetEventHandler{
		void onMenuClicked();
	}

	@UiField HTMLPanel mainBtn;
	@UiField Image imgIcon;
	@UiField LabelElement btnName;
	
	public SidebarMenuWidget(String imgURL, String menuName) {
		initWidget(uiBinder.createAndBindUi(this));
		this.imgURL = imgURL;
		this.menuName = menuName;
		initComponent();
		initEvent();
	}
	
	private void initComponent(){
		btnName.setInnerText(menuName);
		imgIcon.setUrl(imgURL);
	}
	
	private void initEvent(){
		mainBtn.addHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					handler.onMenuClicked();
				}
			}
		}, ClickEvent.getType());
	}

	public void setSidebarMenuWidgetEventHandler(SidebarMenuWidgetEventHandler handler){
		this.handler = handler;
	}
}
