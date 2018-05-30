package com.iglobal.bookit.client.user.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.ui.components.CustomDraggablePopupPanel;
import com.iglobal.bookit.client.user.widget.LoginWidget.LoginWidgetEventHandler;
import com.iglobal.bookit.client.user.widget.object.DynamicTextBoxQueryObject;
import com.iglobal.bookit.shared.DataTypeConstants;
import com.iglobal.bookit.shared.User;

public class DynamicLoginWidget extends Composite implements IsEntryWidget{
	private DataTypeConstants dataTypeId;
	private String labelName, fieldName, dataType, fieldValue = "Unknown";
	private static DynamicLoginWidgetUiBinder uiBinder = GWT
			.create(DynamicLoginWidgetUiBinder.class);

	interface DynamicLoginWidgetUiBinder extends
			UiBinder<Widget, DynamicLoginWidget> {
	}

	@UiField Button loginBtn;
	@UiField LabelElement label, labelLogin;
	
	public DynamicLoginWidget(String labelName, String fieldName, String dataType, DataTypeConstants dataTypeId) {
		this.labelName = labelName;
		this.fieldName = fieldName;
		this.dataTypeId = dataTypeId;
		this.dataType = dataType;
		
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
		initEvent();
	}
	
	private void initComponent(){
		label.setInnerText(labelName);
	}
	
	private void initEvent(){
		loginBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				showPopup();
			}
		});
	}
	
	private void showPopup(){
		final CustomDraggablePopupPanel panel = new CustomDraggablePopupPanel(true);
		panel.setGlassStyleName("glassPanel");
		panel.setAutoHideEnabled(true);
		panel.setGlassEnabled(true);
		
		LoginWidget loginWidget = new LoginWidget();
		loginWidget.setLoginWidgetEventHandler(new LoginWidgetEventHandler() {
			
			@Override
			public void onSuccessfulEntry(User user) {
				if(user != null){
					fieldValue = user.getName();
					labelLogin.setInnerText(user.getName());
					
					panel.hide();
				}
			}
			
			@Override
			public void onCloseInvoked() {
				panel.hide();
			}
		});
		panel.add(loginWidget);
		panel.center();
	}

	@Override
	public DynamicTextBoxQueryObject getQuery() {
		DynamicTextBoxQueryObject queryObject = new DynamicTextBoxQueryObject();
		queryObject.setFieldName(fieldName);
		queryObject.setFieldValue(fieldValue);
		queryObject.setDataType(dataType);
		
		return queryObject;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
