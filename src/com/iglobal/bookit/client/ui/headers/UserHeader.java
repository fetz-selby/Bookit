package com.iglobal.bookit.client.ui.headers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.parents.RendererHeader;

public class UserHeader extends Composite implements RendererHeader{

	private static UserHeaderUiBinder uiBinder = GWT
			.create(UserHeaderUiBinder.class);

	interface UserHeaderUiBinder extends UiBinder<Widget, UserHeader> {
	}

	public UserHeader() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
