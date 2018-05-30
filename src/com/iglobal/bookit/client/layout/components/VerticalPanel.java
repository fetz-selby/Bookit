package com.iglobal.bookit.client.layout.components;


import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class VerticalPanel extends FlowPanel{

	public VerticalPanel(){
		this.setStyleName("row");
	}
	
	@Override
	public void add(Widget w) {
		super.add(w);
		super.add(getClearfixPanel());
	}

	private SimplePanel getClearfixPanel(){
		SimplePanel panel = new SimplePanel();
		panel.setStyleName("clearfix");
		return panel;
	}
	
}
