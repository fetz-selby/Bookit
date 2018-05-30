package com.iglobal.bookit.client.ui.renderer;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.parents.Renderer;
import com.iglobal.bookit.client.ui.components.renderer.GroupRow;
import com.iglobal.bookit.client.user.widget.ScrollableFlowPanel;
import com.iglobal.bookit.shared.GroupRowObject;

public class GroupRenderer extends Composite implements Renderer<GroupRowObject>{

	private static GroupRendererUiBinder uiBinder = GWT
			.create(GroupRendererUiBinder.class);

	interface GroupRendererUiBinder extends UiBinder<Widget, GroupRenderer> {
	}

	@UiField HTMLPanel panel;
	private ArrayList<GroupRowObject> objectList;
	
	public GroupRenderer(ArrayList<GroupRowObject> objectList) {
		this.objectList = objectList;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
	}

	private void initComponent(){
		if(objectList == null){
			return;
		}
		
		ScrollableFlowPanel scrollable = new ScrollableFlowPanel("admin-renderer-scrollable");
		for(GroupRowObject object : objectList){
			GroupRow row = new GroupRow(object);
			scrollable.add(row);
		}
		panel.add(scrollable);

	}
	
	@Override
	public void load() {
		initComponent();
	}

	@Override
	public void setRenderer(ArrayList<GroupRowObject> renderList) {
		this.objectList = renderList;
	}

}
