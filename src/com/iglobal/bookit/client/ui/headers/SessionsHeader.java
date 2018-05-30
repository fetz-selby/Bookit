package com.iglobal.bookit.client.ui.headers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.parents.RendererHeader;

public class SessionsHeader extends Composite implements RendererHeader{

	private static SessionsHeaderUiBinder uiBinder = GWT
			.create(SessionsHeaderUiBinder.class);

	interface SessionsHeaderUiBinder extends UiBinder<Widget, SessionsHeader> {
	}

	public SessionsHeader() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
