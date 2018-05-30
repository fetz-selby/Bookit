package com.iglobal.bookit.client.user.report.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class AuxTemplate extends Composite {

	private static AuxTemplateUiBinder uiBinder = GWT
			.create(AuxTemplateUiBinder.class);

	interface AuxTemplateUiBinder extends UiBinder<Widget, AuxTemplate> {
	}

	public AuxTemplate() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
