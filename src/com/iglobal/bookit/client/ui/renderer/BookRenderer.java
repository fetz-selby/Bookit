package com.iglobal.bookit.client.ui.renderer;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.parents.Renderer;
import com.iglobal.bookit.client.ui.components.renderer.BookRow;
import com.iglobal.bookit.client.user.widget.ScrollableFlowPanel;
import com.iglobal.bookit.shared.BookRowObject;

public class BookRenderer extends Composite implements Renderer<BookRowObject>{

	private ArrayList<BookRowObject> objectList;
	private static BookRendererUiBinder uiBinder = GWT
			.create(BookRendererUiBinder.class);

	interface BookRendererUiBinder extends UiBinder<Widget, BookRenderer> {
	}

	@UiField HTMLPanel panel;
	
	public BookRenderer(ArrayList<BookRowObject> objectList) {
		this.objectList = objectList;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
	}

	private void initComponent(){
		if(objectList == null){
			return;
		}
		
		ScrollableFlowPanel scrollable = new ScrollableFlowPanel("admin-renderer-scrollable");
		for(BookRowObject object : objectList){
			BookRow row = new BookRow(object);
			scrollable.add(row);
		}
		panel.add(scrollable);

	}
	
	@Override
	public void load() {
		initComponent();
	}

	@Override
	public void setRenderer(ArrayList<BookRowObject> renderList) {
		this.objectList = renderList;
	}

}
