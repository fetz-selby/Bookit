package com.iglobal.bookit.client.layout.components;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.ui.components.SidebarMenuWidget;
import com.iglobal.bookit.client.ui.components.SidebarMenuWidget.SidebarMenuWidgetEventHandler;

public class SideBar extends Composite {
	private ArrayList<String> moduleList;
	private static SideBarUiBinder uiBinder = GWT.create(SideBarUiBinder.class);

	interface SideBarUiBinder extends UiBinder<Widget, SideBar> {
	}

	@UiField VerticalPanel container;
	
	public SideBar(ArrayList<String> moduleList) {
		initWidget(uiBinder.createAndBindUi(this));
		this.moduleList = moduleList;
		initComponent();
	}
	
	private void initComponent(){
		if(moduleList != null){
			for(String moduleName : moduleList){
				SidebarMenuWidget widget = new SidebarMenuWidget("", moduleName);
				widget.setSidebarMenuWidgetEventHandler(new SidebarMenuWidgetEventHandler() {
					
					@Override
					public void onMenuClicked() {
						// TODO Auto-generated method stub
						
					}
				});
				container.add(widget);
			}
		}
	}

}
