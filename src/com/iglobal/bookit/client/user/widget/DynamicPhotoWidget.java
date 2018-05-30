package com.iglobal.bookit.client.user.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.user.widget.object.DynamicTextBoxQueryObject;
import com.iglobal.bookit.shared.DataTypeConstants;

public class DynamicPhotoWidget extends Composite implements IsEntryWidget{

	private String base64String;
	private static DynamicPhotoWidgetUiBinder uiBinder = GWT
			.create(DynamicPhotoWidgetUiBinder.class);

	interface DynamicPhotoWidgetUiBinder extends
			UiBinder<Widget, DynamicPhotoWidget> {
	}

	@UiField FileUpload fileUpload;
	@UiField ImageElement imgElement;
	@UiField FormPanel form;
	@UiField LabelElement label;
	
	private String columnName;
	private String fieldName;
	private String dataTypeCons;
	//private DataTypeConstants dtc;
	
	public DynamicPhotoWidget(String columnName, String fieldName, String dataTypeCons, DataTypeConstants dtc, String base64String) {
		this.columnName = columnName;
		this.fieldName = fieldName;
		this.dataTypeCons = dataTypeCons;
		//this.dtc = dtc;
		this.base64String = base64String;
		initWidget(uiBinder.createAndBindUi(this));
		initEvent();
		initForm();
		initComponent();
	}

	private void initEvent(){
		fileUpload.setName(fieldName);
		fileUpload.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				//Show wait image
				GWT.log("Form submitted");
				form.submit();
			}
		});
	}
	
	private void initComponent(){
		if(base64String == null){
			imgElement.setAttribute("src", "images/blue_avatar.png");
		}else{
			imgElement.setAttribute("src", base64String);
		}
		
		label.setInnerText(columnName);
	}
	
	private void initForm(){
		form.setAction(GWT.getModuleBaseURL()+"preview");
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				String imageUrl = event.getResults();
				
				if(imageUrl.length() > 0){
					GWT.log("image url is "+imageUrl);
					base64String = getProcessedImage(imageUrl);
					imgElement.setAttribute("src", base64String);
					//isImageAttached = true;
				}
				
			}
		});
	}
	
	private String getProcessedImage(String unProcessedImage){
		HTML html = new HTML(unProcessedImage);
		GWT.log("Object text is "+html.getText());
			
		return html.getText();
	}

//	private void reInitFormUpload(){
//		FileUpload newUpload = new FileUpload();
//		newUpload.setName(fieldName);
//		newUpload.addChangeHandler(new ChangeHandler() {
//			
//			@Override
//			public void onChange(ChangeEvent event) {
//				form.submit();
//			}
//		});
//		form.clear();
//		form.setWidget(newUpload);
//	}
	
	@Override
	public DynamicTextBoxQueryObject getQuery() {
		DynamicTextBoxQueryObject queryObject = new DynamicTextBoxQueryObject();
		queryObject.setFieldName(fieldName);
		queryObject.setFieldValue(base64String);
		queryObject.setDataType(dataTypeCons);
		queryObject.setFileUpload(fileUpload);
		
		return queryObject;
	}

	@Override
	public void reset() {
		//reInitFormUpload();
		//imgElement.setAttribute("src", "images/blue_avatar.png");
	}
}
