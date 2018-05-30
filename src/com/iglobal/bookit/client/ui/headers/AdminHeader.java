package com.iglobal.bookit.client.ui.headers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.parents.RendererHeader;

public class AdminHeader extends Composite implements RendererHeader{

	private static AdminHeaderUiBinder uiBinder = GWT
			.create(AdminHeaderUiBinder.class);

	interface AdminHeaderUiBinder extends UiBinder<Widget, AdminHeader> {
	}

	public AdminHeader() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
