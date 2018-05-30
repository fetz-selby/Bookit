package com.iglobal.bookit.client.ui.renderer;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.parents.Renderer;
import com.iglobal.bookit.client.ui.components.renderer.SessionRow;
import com.iglobal.bookit.client.user.widget.ScrollableFlowPanel;
import com.iglobal.bookit.shared.SessionsRowObject;

public class SessionsRenderer extends Composite implements Renderer<SessionsRowObject>{
	private ArrayList<SessionsRowObject> objectList;

	private static SessionsRendererUiBinder uiBinder = GWT
			.create(SessionsRendererUiBinder.class);

	interface SessionsRendererUiBinder extends
			UiBinder<Widget, SessionsRenderer> {
	}

	@UiField HTMLPanel panel;

	public SessionsRenderer(ArrayList<SessionsRowObject> objectList) {
		this.objectList = objectList;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
	}
	
	private void initComponent(){
		if(objectList == null){
			return;
		}
		
		ScrollableFlowPanel scrollable = new ScrollableFlowPanel("admin-renderer-scrollable");
		for(SessionsRowObject object : objectList){
			SessionRow row = new SessionRow(object);
			scrollable.add(row);
		}
		panel.add(scrollable);
	}

	@Override
	public void load() {
		initComponent();
	}

	@Override
	public void setRenderer(ArrayList<SessionsRowObject> renderList) {
		this.objectList = renderList;
	}

}
