package com.iglobal.bookit.client.ui.renderer;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.parents.Renderer;
import com.iglobal.bookit.client.ui.components.renderer.AdminRow;
import com.iglobal.bookit.client.user.widget.ScrollableFlowPanel;
import com.iglobal.bookit.shared.AdminRowObject;

public class AdminRenderer extends Composite implements Renderer<AdminRowObject>{
	private ArrayList<AdminRowObject> objectList;
	private static AdminRendererUiBinder uiBinder = GWT
			.create(AdminRendererUiBinder.class);

	interface AdminRendererUiBinder extends UiBinder<Widget, AdminRenderer> {
	}

	@UiField HTMLPanel panel;
	
	public AdminRenderer(ArrayList<AdminRowObject> objectList) {
		this.objectList = objectList;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
	}
	
	private void initComponent(){
		if(objectList == null){
			return;
		}
		
		ScrollableFlowPanel scrollable = new ScrollableFlowPanel("admin-renderer-scrollable");
		for(AdminRowObject object : objectList){
			AdminRow row = new AdminRow(object);
			scrollable.add(row);
		}
		panel.add(scrollable);
	}

	@Override
	public void load() {
		initComponent();
	}

	@Override
	public void setRenderer(ArrayList<AdminRowObject> renderList) {
		this.objectList = renderList;
	}

}
