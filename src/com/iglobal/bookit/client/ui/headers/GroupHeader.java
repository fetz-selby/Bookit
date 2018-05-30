package com.iglobal.bookit.client.ui.headers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.parents.RendererHeader;

public class GroupHeader extends Composite implements RendererHeader{

	private static GroupHeaderUiBinder uiBinder = GWT
			.create(GroupHeaderUiBinder.class);

	interface GroupHeaderUiBinder extends UiBinder<Widget, GroupHeader> {
	}

	public GroupHeader() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
