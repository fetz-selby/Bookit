package com.iglobal.bookit.client.ui.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.GlobalResource;

public class SideBarMenuWidget2 extends Composite {

	private String[] menuIds = {"adminMenu", "groupMenu", "bookMenu", "userMenu", "sessionMenu"};
	private String[] menuText = {"Admins", "Groups", "Books", "Users", "Sessions"};
	private static SideBarMenuWidget2UiBinder uiBinder = GWT
			.create(SideBarMenuWidget2UiBinder.class);

	interface SideBarMenuWidget2UiBinder extends
			UiBinder<Widget, SideBarMenuWidget2> {
	}

	@UiField UListElement ulElement;
	
	public SideBarMenuWidget2() {
		initWidget(uiBinder.createAndBindUi(this));
		doInit();
	}
	
	private void doInit(){
		for(int i = 0; i < menuIds.length; i++){
			if(menuIds[i].trim().equals("adminMenu") && menuText[i].trim().equals("Admins") && GlobalResource.getInstance().getUser().isSuperUser()){
				ulElement.appendChild(getLIElement(menuIds[i], menuText[i]));
				continue;
			}else if(menuIds[i].trim().equals("adminMenu") && menuText[i].trim().equals("Admins")){
				continue;
			}
			
			ulElement.appendChild(getLIElement(menuIds[i], menuText[i]));
		}
	}
	
	private Element getLIElement(String id, String name){
		Element li = DOM.createElement("li");
		
		Element anchor = DOM.createElement("a");
		anchor.setClassName("auto");
		anchor.setAttribute("href", "javascript:void(0)");
		anchor.setId(id);
		
		Element i = DOM.createElement("i");
		i.setClassName("i i-statistics icon");
		
		Element span = DOM.createElement("span");
		span.setClassName("font-bold theme2-menuarea-fontcolor2");
		span.setInnerText(name);
		
		
		anchor.appendChild(i);
		anchor.appendChild(span);
		
		li.appendChild(anchor);
		
		return li;
	}
	
}
