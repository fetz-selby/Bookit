package com.iglobal.bookit.client.ui.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ModuleTitleWidget extends Composite {
	private String title;
	private static ModuleTitleWidgetUiBinder uiBinder = GWT
			.create(ModuleTitleWidgetUiBinder.class);

	interface ModuleTitleWidgetUiBinder extends
			UiBinder<Widget, ModuleTitleWidget> {
	}

	@UiField HeadingElement titleSpan;
	
	public ModuleTitleWidget(String title) {
		this.title = title;
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private void initComponent(){
		titleSpan.setInnerText(title);
	}

	public void load(){
		initComponent();
	}
	
}
