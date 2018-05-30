package com.iglobal.bookit.client.user.widget;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.user.widget.DatePickerBox.DateOption;

public class ECalender extends Composite implements HasValue<Date> {

  private Date startDate, endDate;
  private Date date = new Date();

  private DatePickerBox datePicker;
  private boolean isHide = true;
  private PopupPanel datePopup = new PopupPanel();
  private TextBox textBox = new TextBox();
  private HandlerManager handleManager;
  private DateOption dateFormat = DateOption.DDMMYYYY;

  public ECalender() {
    initWidget(textBox);
    initTextBox();
    // initPopup(false);
    initEvent();
  }

  public ECalender(String style) {
	    initWidget(textBox);
	    initTextBox(style);
	    // initPopup(false);
	    initEvent();
	  }
  
  private void initTextBox(){
	  textBox.setStyleName("form-control no-border theme2-widget-style");
  }
  
  private void initTextBox(String style){
	  textBox.setStyleName(style);
  }
  
  private void initEvent() {
    textBox.addKeyPressHandler(new KeyPressHandler() {

      @Override public void onKeyPress(KeyPressEvent event) {
        ((TextBox) event.getSource()).cancelKey();
      }
    });
    textBox.addClickHandler(new ClickHandler() {

      @Override public void onClick(ClickEvent event) {
        initPopup(true);
      }
    });

    handleManager = new HandlerManager(this);
  }

  private void initPopup(boolean isShow) {
    datePicker = new DatePickerBox(date, startDate, endDate, new DateOption[] { dateFormat });
    datePopup.clear();
    datePopup.add(datePicker);
    datePopup.setPopupPosition(textBox.getAbsoluteLeft(), textBox.getAbsoluteTop() + textBox.getOffsetHeight());
    datePopup.setAutoHideEnabled(true);
    datePicker.setDatePickerEventHandler(new DatePickerEventHandler() {

      @Override public void onDateSelected(String strdate) {
        textBox.setText(strdate);
        fireChange();
      }
    });

    if (isShow) {
      datePopup.show();
    }
  }

  public void setDateFormat(DateOption dateFormat) {
    this.dateFormat = dateFormat;
  }

  public Widget asWidget() {
    return textBox;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Date getDate() {
    return date;
  }

  public void reset() {
    date = null;
    startDate = null;
    endDate = null;
  }

  public void clear() {
    date = null;
    textBox.setText("");
  }

  public void fireChange() {
    ValueChangeEvent.fireIfNotEqual(this, date, getValue());
    date = getValue();

    if (isHide) {
      datePopup.hide();
    }
  }

  public void show() {
    initPopup(true);
  }

  public void hide() {
    if (datePopup != null) {
      datePopup.hide();
    }
  }

  public String getAPIDate() {
    if (date == null) {
      return "";
    }
    
    return "";
    
    //return Utils.dateToAPIString(date);
  }
  
  public String getDateString(){
	  return textBox.getText();
  }

  public void hideOnSelect(boolean isHide) {
    this.isHide = isHide;
  }
  
  public void setStyle(String style){
	  textBox.setStyleName(style);
  }

  @Override public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Date> handler) {
    return handleManager.addHandler(ValueChangeEvent.getType(), handler);
  }

  @Override public void fireEvent(GwtEvent<?> event) {
    handleManager.fireEvent(event);
  }

  @Override public Date getValue() {
    if (datePicker != null) {
      return datePicker.getDate();
    }
    return null;
  }

  @Override public void setValue(Date value) {
    setValue(value, false);
  }

  @Override public void setValue(Date value, boolean fireEvents) {
    this.date = value;
    textBox.setText(DateTimeFormat.getFormat(dateFormat.toString()).format(value));
    if (fireEvents) {
      fireChange();
    }

  }

}
