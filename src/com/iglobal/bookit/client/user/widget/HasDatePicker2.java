package com.iglobal.bookit.client.user.widget;

import java.util.Date;

import com.iglobal.bookit.client.user.widget.DatePickerBox.DateOption;

public interface HasDatePicker2 {
  String getAPIDate();

  String getDateFormat();

  Date getDate();

  void setDate(Date date);

  void setStartDate(Date date);

  void setEndDate(Date date);

  void show();

  void hide();

  void addOption(DateOption option);

  void reload();

  void load();

  void clear();

  void setDatePickerEventHandler(DatePickerEventHandler handler);

}
