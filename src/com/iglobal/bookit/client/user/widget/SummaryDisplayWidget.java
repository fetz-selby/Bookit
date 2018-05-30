package com.iglobal.bookit.client.user.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.user.widget.object.UserSummaryDisplayObject;

public class SummaryDisplayWidget extends Composite {

	private Element imageElement;
	private SummaryDisplayWidgetEventHandler handler;
	private static SummaryDisplayWidgetUiBinder uiBinder = GWT
			.create(SummaryDisplayWidgetUiBinder.class);

	interface SummaryDisplayWidgetUiBinder extends
	UiBinder<Widget, SummaryDisplayWidget> {
	}

	public interface SummaryDisplayWidgetEventHandler{
		void onEditClicked(UserSummaryDisplayObject obj);
	}
	
	private UserSummaryDisplayObject obj;

	@UiField SpanElement label;
	@UiField SimplePanel dynamicWidgetContainer;
	@UiField Anchor editAnchor;
	@UiField DivElement valueDisplay;

	public SummaryDisplayWidget(UserSummaryDisplayObject obj) {
		this.obj = obj;

		initWidget(uiBinder.createAndBindUi(this));
		initComponent();

		if(obj.isCanEdit()){
			initEvent();
		}
	}

	private void initComponent(){
		label.setInnerText(obj.getAlias());

		if(obj != null){
			switch(obj.getDataType()){
			case BLOB:
				if(obj.getValue().trim().isEmpty()){
					//Show default picture
					imageElement = getImageElement("images/blue_avatar.png");
					valueDisplay.appendChild(imageElement);
				}else{
					imageElement = getImageElement(obj.getValue());
					valueDisplay.appendChild(imageElement);
				}

				break;
			case DATE:
				valueDisplay.setInnerText(obj.getValue());

				break;
			case DATETIME:
				valueDisplay.setInnerText(obj.getValue());

				break;
			case INT:
				valueDisplay.setInnerText(obj.getValue());

				break;
			case STRING:
				valueDisplay.setInnerText(obj.getValue());

				break;
			case TIME:
				valueDisplay.setInnerText(obj.getValue());

				break;
			case LOGIN:
				valueDisplay.setInnerText(obj.getValue());

				break;
			default:
				break;
				}
		}

	}
	
	public void setValue(String value){
		valueDisplay.setInnerText(value);
	}

	private Element getImageElement(String imageSrc){
		Element img = DOM.createElement("img");
		img.setAttribute("src", imageSrc);
		img.setAttribute("style", "width:128px;height:128px");

		return img;
	}

	private void initEvent(){
		editAnchor.setText("edit");
		editAnchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				//Show popup for edit
				if(handler != null){
					handler.onEditClicked(obj);
				}
			}
		});
	}
	
	public void setImage(String url){
		imageElement.setAttribute("src", url);
	}
	
	public void setSummaryDisplayWidgetEventHandler(SummaryDisplayWidgetEventHandler handler){
		this.handler = handler;
	}
}
