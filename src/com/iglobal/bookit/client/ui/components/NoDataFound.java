package com.iglobal.bookit.client.ui.components;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.parents.Renderer;

public class NoDataFound extends Composite implements Renderer<Widget>{

	private static NoDataFoundUiBinder uiBinder = GWT
			.create(NoDataFoundUiBinder.class);

	interface NoDataFoundUiBinder extends UiBinder<Widget, NoDataFound> {
	}

	@UiField SpanElement lowerMsg;
	@UiField DivElement heading;
	
	public NoDataFound() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public NoDataFound(String heading){
		initWidget(uiBinder.createAndBindUi(this));
		this.heading.setInnerText(heading);
	}

	public void setHeading(String heading){
		this.heading.setInnerText(heading);
	}
	
	public void setLowerMessage(String lowerMsg){
		this.lowerMsg.setInnerText(lowerMsg);
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRenderer(ArrayList<Widget> renderList) {
		// TODO Auto-generated method stub
		
	}
	
}
