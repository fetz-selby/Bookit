package com.iglobal.bookit.client.user.widget;

import java.util.Date;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.user.widget.DatePickerBox.DateOption;

public class EViewCalendar extends Composite implements HasValue<Date> {
  private Date startDate, endDate;
  private Date date = new Date();
  private EViewCalendarEventHandler handler;
  private DatePickerBox datePicker;
  private SimplePanel container = new SimplePanel();
  private HandlerManager handleManager;
  private DateOption dateFormat = DateOption.YYYYMMDD;

  public interface EViewCalendarEventHandler {
    void onloadComplete(DatePickerBox datePicker);
  }

  public EViewCalendar() {
    init();
    initWidget(container);
    handleManager = new HandlerManager(this);
  }

  private void init() {

    datePicker = new DatePickerBox(date, startDate, endDate, new DateOption[] { dateFormat });
    datePicker.getDivElement().setClassName("sels-range-class");
    datePicker.setDatePickerEventHandler(new DatePickerEventHandler() {

      @Override public void onDateSelected(String tmpDate) {
        fireChange();
      }
    });
    container.setWidget(datePicker);
    if (handler != null) {
      handler.onloadComplete(datePicker);
    }
  }

  public void reload() {
    init();
  }

  public void setDateFormat(DateOption dateFormat) {
    this.dateFormat = dateFormat;
  }

  public Widget asWidget() {
    return container;
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
  }

  public void fireChange() {
    ValueChangeEvent.fireIfNotEqual(this, date, getValue());
    date = getValue();
  }

  public void show() {

  }

  public void hide() {

  }

  public String getAPIDate() {
    if (date == null) {
      return "";
    }
    return "";
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
    if (fireEvents) {
      fireChange();
    }
  }

  public void setEViewCalendarEventHandler(EViewCalendarEventHandler handler) {
    this.handler = handler;
  }

}
