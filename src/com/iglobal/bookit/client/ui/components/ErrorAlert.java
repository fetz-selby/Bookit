package com.iglobal.bookit.client.ui.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.utils.Animate;

public class ErrorAlert extends Composite {

	private static ErrorAlertUiBinder uiBinder = GWT
			.create(ErrorAlertUiBinder.class);

	interface ErrorAlertUiBinder extends UiBinder<Widget, ErrorAlert> {
	}

	@UiField SpanElement message;

	public ErrorAlert(String message) {
		initWidget(uiBinder.createAndBindUi(this));
		if(message != null){
			this.message.setInnerText(message);
		}else{
			this.message.setInnerText("An Error occured while saving.");
		}
		doTiming(this);
	}

	private void doTiming(final Widget widget){
		Timer timer = new Timer() {

			@Override
			public void run() {
				runAnimation(widget);
			}
		};

		timer.schedule(3000);
	}

	private void runAnimation(Widget widget){
		Animate.fadeOut(widget);
	}
}
