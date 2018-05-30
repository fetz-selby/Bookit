package com.iglobal.bookit.client.ui.renderer;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.parents.Renderer;
import com.iglobal.bookit.client.ui.components.renderer.UserRow;
import com.iglobal.bookit.client.user.widget.ScrollableFlowPanel;
import com.iglobal.bookit.shared.UserRowObject;

public class UserRenderer extends Composite implements Renderer<UserRowObject>{

	private static UserRendererUiBinder uiBinder = GWT
			.create(UserRendererUiBinder.class);

	interface UserRendererUiBinder extends UiBinder<Widget, UserRenderer> {
	}

	private ArrayList<UserRowObject> objectList;

	@UiField HTMLPanel panel;
	
	public UserRenderer(ArrayList<UserRowObject> objectList) {
		this.objectList = objectList;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
	}

	private void initComponent(){
		if(objectList == null){
			return;
		}
		
		ScrollableFlowPanel scrollable = new ScrollableFlowPanel("admin-renderer-scrollable");
		for(UserRowObject object : objectList){
			UserRow row = new UserRow(object);
			scrollable.add(row);
		}
		panel.add(scrollable);
	}
	
	@Override
	public void load() {
		initComponent();
	}

	@Override
	public void setRenderer(ArrayList<UserRowObject> renderList) {
		this.objectList = renderList;
	}

}
