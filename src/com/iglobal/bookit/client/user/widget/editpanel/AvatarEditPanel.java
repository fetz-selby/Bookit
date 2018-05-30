package com.iglobal.bookit.client.user.widget.editpanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.user.widget.object.UserSummaryDisplayObject;
import com.iglobal.bookit.client.utils.FormSaveEventHandler;
import com.iglobal.bookit.client.utils.Utils;

public class AvatarEditPanel extends Composite implements IsEditableUserWidget{

	private EditPanelEventHandler handler;
	private UserSummaryDisplayObject obj;
	private String imageUrl, base64String, title;
	private static AvatarEditPanelUiBinder uiBinder = GWT
			.create(AvatarEditPanelUiBinder.class);

	interface AvatarEditPanelUiBinder extends UiBinder<Widget, AvatarEditPanel> {
	}

	@UiField Button saveBtn;
	@UiField Anchor cancelLink;
	@UiField SpanElement closeBtn, formTitle;
	@UiField Image avatarPanel;
	@UiField FileUpload fileUploader;
	@UiField FormPanel form, updateForm;
	@UiField HTMLPanel mainPanel;
	@UiField FlowPanel queryStringContainer;

	public AvatarEditPanel(String title, String imageUrl, UserSummaryDisplayObject obj) {
		this.obj = obj;
		this.imageUrl = imageUrl;
		this.title = title;

		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
		initForm();
		initEvent();
	}

	private void initComponent(){
		avatarPanel.getElement().setAttribute("style", "width:128px;height:128px");

		if(imageUrl != null && !imageUrl.trim().isEmpty()){
			avatarPanel.setUrl(imageUrl);
		}else{
			avatarPanel.setUrl("images/blue_avatar.png");
		}

		cancelLink.setText("cancel");
		formTitle.setInnerText("Edit "+title);
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
					//imgElement.setAttribute("src", base64String);
					avatarPanel.setUrl(base64String);
					//isImageAttached = true;
				}

			}
		});
	}

	private void doSave(){
		//updateForm.setStyleName("gwt-hide");
		//		updateForm.setAction(GWT.getModuleBaseURL()+"updatepicture");
		//		updateForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		//		updateForm.setMethod(FormPanel.METHOD_POST);
		//		
		//		updateForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {
		//			
		//			@Override
		//			public void onSubmitComplete(SubmitCompleteEvent event) {
		//				//Window.alert(event.getResults());
		//				if(handler != null){
		//					handler.onImageSaveClicked(base64String);
		//				}
		//			}
		//		});

		Hidden tableNameHidden = new Hidden("table_name", obj.getTableName());
		Hidden columnNameHidden = new Hidden("column_name", obj.getColumnName());
		Hidden idValue = new Hidden("id", obj.getId());

		queryStringContainer.add(tableNameHidden);
		queryStringContainer.add(columnNameHidden);
		queryStringContainer.add(idValue);
		queryStringContainer.add(fileUploader);

		//updateForm.setWidget(queryStringContainer);

		//updateForm.submit();

		Utils.setFeed(queryStringContainer, "updatepicture", true, new FormSaveEventHandler() {

			@Override
			public void onSuccessfulSave(SubmitCompleteEvent event) {
				if(handler != null){
					handler.onImageSaveClicked(base64String);
				}		
			}

			@Override
			public void onFailSave() {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initEvent(){
		saveBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					handler.onImageSaveClicked("");
				}
				//form.submit();

				doSave();
			}
		});

		cancelLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					handler.onCancelClicked();
				}
			}
		});

		Element closeBtnElement = closeBtn.cast();
		DOM.sinkEvents(closeBtnElement, Event.ONCLICK);
		DOM.setEventListener(closeBtnElement, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if(handler != null){
					handler.onCancelClicked();
				}			
			}
		});

		fileUploader.setName("preview");
		fileUploader.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				//Show wait image
				GWT.log("Form submitted");
				form.submit();
			}
		});
	}

	private String getProcessedImage(String unProcessedImage){
		HTML html = new HTML(unProcessedImage);
		GWT.log("Object text is "+html.getText());

		return html.getText();
	}

	@Override
	public void show(){
		//popup.center();
	}

	@Override
	public void hide(){
		//popup.hide();
	}

	@Override
	public void setEditPanelEventHandler(EditPanelEventHandler handler){
		this.handler = handler;
	}

}
