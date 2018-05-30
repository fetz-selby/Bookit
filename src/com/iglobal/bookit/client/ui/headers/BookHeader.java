package com.iglobal.bookit.client.ui.headers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.parents.RendererHeader;

public class BookHeader extends Composite implements RendererHeader{

	private static BookHeaderUiBinder uiBinder = GWT
			.create(BookHeaderUiBinder.class);

	interface BookHeaderUiBinder extends UiBinder<Widget, BookHeader> {
	}

	public BookHeader() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
