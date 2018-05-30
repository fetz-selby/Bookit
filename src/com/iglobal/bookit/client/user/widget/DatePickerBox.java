package com.iglobal.bookit.client.user.widget;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class DatePickerBox extends Composite implements HasDatePicker2 {
  private DatePickerEventHandler handler;

  private ArrayList<DateOption> dateOptionList = new ArrayList<DateOption>();
  private Element divElement;

  private boolean isClear = false;

  private static int counter = 0;
  private DateOption[] options;
  private Date startDate, endDate, currentDate;
  private String dateFormat = "yyyy-mm-dd", javaDateFormat = "yyyy-M-dd";
  private String startView = "";
  private String limitView = "";

  private String startDateString = null;
  private String endDateString = null;
  private String currentDateString = null;
  private static DatePickerBoxUiBinder uiBinder = GWT.create(DatePickerBoxUiBinder.class);

  interface DatePickerBoxUiBinder extends UiBinder<Widget, DatePickerBox> {
  }

  public enum DateOption {
    LIMIT_TO_MONTH, LIMIT_TO_YEAR, LIMIT_TO_DAY, READ_ONLY, START_VIEW_FROM_MONTH, START_VIEW_FROM_YEAR, START_VIEW_FROM_DECADE, DDMMYYYY("dd/M/yyyy"), MMDDYYYY("M/dd/yyyy"), YYYYDDMM("yyyy/dd/M"), YYYYMMDD("yyyy/M/dd");

    private final String value;

    private DateOption() {
      value = "";
    }

    private DateOption(String str) {
      value = str;
    }

    public String toString() {
      return value;
    }
  }

  @UiField HTMLPanel container;

  private DatePickerBox() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  public DatePickerBox(Date startDate, Date endDate, DateOption[] options) {
    this();
    this.startDate = startDate;
    this.endDate = endDate;
    this.options = options;

    doProcessing(null, startDate, endDate, options, false);
  }

  public DatePickerBox(Date currentDate, Date startDate, Date endDate, DateOption[] options) {
    this();
    this.currentDate = currentDate;
    this.startDate = startDate;
    this.endDate = endDate;
    this.options = options;

    doProcessing(currentDate, startDate, endDate, options, false);
  }

  private void doProcessing(Date currentDate, Date startDate, Date endDate, DateOption[] options, boolean isUpdate) {
    if (divElement == null) {
      divElement = DOM.createElement("div");
      divElement.addClassName("sels-inline-class");
      divElement.setId("sels_divDate_" + counter);

      container.getElement().appendChild(divElement);
    }

    if (options != null) {
      for (DateOption option : options) {
        switch (option) {
          case DDMMYYYY:
            dateFormat = "dd-M-yyyy";
            break;
          case MMDDYYYY:
            dateFormat = "M-dd-yyyy";
            break;
          case YYYYMMDD:
            dateFormat = "yyyy-M-dd";
            break;
          case YYYYDDMM:
            dateFormat = "yyyy-dd-M";
            break;
          case START_VIEW_FROM_DECADE:
            startView = "2";
            break;
          case START_VIEW_FROM_MONTH:
            startView = "0";
            break;
          case START_VIEW_FROM_YEAR:
            startView = "1";
            break;
          case LIMIT_TO_DAY:
            limitView = "0";
            break;
          case LIMIT_TO_MONTH:
            limitView = "1";
            break;
          case LIMIT_TO_YEAR:
            limitView = "2";
            break;
		default:
			break;
        }
      }
    }

    if (startDate != null) {
      startDateString = (dateFormat == null || dateFormat.isEmpty()) ? DateTimeFormat.getFormat("yyyy-M-dd").format(startDate) : DateTimeFormat.getFormat(dateFormat).format(startDate);
    }

    if (endDate != null) {
      endDateString = (dateFormat == null || dateFormat.isEmpty()) ? DateTimeFormat.getFormat("yyyy-M-dd").format(endDate) : DateTimeFormat.getFormat(dateFormat).format(endDate);
    }

    if (currentDate != null) {
      currentDateString = (dateFormat == null || dateFormat.isEmpty()) ? DateTimeFormat.getFormat("yyyy-M-dd").format(currentDate) : DateTimeFormat.getFormat(dateFormat).format(currentDate);
    }

    doJsDateFormatting();
    if (isUpdate) {
      Scheduler.get().scheduleDeferred(new Command() {
        @Override public void execute() {
          GWT.log("[update]current-date => " + currentDateString);
          doDateUpdate(divElement.getId(), currentDateString);
        }
      });
    } else {
      Scheduler.get().scheduleDeferred(new Command() {
        @Override public void execute() {
          GWT.log("current-date => " + currentDateString + ", start-date => " + startDateString + ", end-date => " + endDateString);
          if (startDateString != null && endDateString != null && currentDateString != null) {
            doDateInit(divElement.getId(), dateFormat, currentDateString, startDateString, endDateString, limitView, startView);
          } else if (startDateString != null && endDateString != null) {
            doStartDateAndEndDateInit(divElement.getId(), dateFormat, startDateString, endDateString);
          } else if (startDateString != null && currentDateString != null) {
            doStartDateAndCurrentDateInit(divElement.getId(), dateFormat, startDateString, currentDateString);
          } else if (endDateString != null && currentDateString != null) {
            doEndDateAndCurrentDateInit(divElement.getId(), dateFormat, endDateString, currentDateString);
          } else if (startDateString != null) {
            doOnlyStartDateInit(divElement.getId(), dateFormat, startDateString);
          } else if (endDateString != null) {
            doOnlyEndDateInit(divElement.getId(), dateFormat, endDateString);
          } else if (currentDateString != null) {
            doOnlyCurrentDateInit(divElement.getId(), dateFormat, currentDateString);
          }
        }
      });
    }
  }

  private void doJsDateFormatting() {
    if (dateFormat.trim().equals("yyyy-M-dd")) {
      dateFormat = "yyyy-mm-dd";
      javaDateFormat = "yyyy-M-dd";
    } else if (dateFormat.trim().equals("M-dd-yyyy")) {
      dateFormat = "mm-dd-yyyy";
      javaDateFormat = "M-dd-yyyy";
    } else if (dateFormat.trim().equals("yyyy-M-dd")) {
      dateFormat = "yyyy-mm-dd";
      javaDateFormat = "yyyy-M-dd";
    }
  }

//  private void loadScript() {
//    if (counter == 0) {
//      ScriptLoader loader = new ScriptLoader(new ScriptLoaderHandler() {
//        @Override public void onScriptLoadComplete() {
//        }
//      });
//      loader.loadScript(ScriptConstants.SELS_DATE_PICKER);
//    }
//    counter++;
//  }

  private void onDateSelected(int day, int month, int year) {

    if (isClear) {
      divElement.setAttribute("style", "color:#000000");
      isClear = !isClear;
    }

    year -= 1900;
    currentDate = new Date(year, month, day);
    currentDateString = (dateFormat == null || dateFormat.isEmpty()) ? DateTimeFormat.getFormat("yyyy-M-dd").format(currentDate) : DateTimeFormat.getFormat(javaDateFormat).format(currentDate);

    if (handler != null) {
      handler.onDateSelected(currentDateString);
    }

    GWT.log("Date => " + currentDateString);
  }

  private native void doDateInit(String id, String dateFormat, String currentDate, String startDate, String endDate, String minView, String startView)/*-{
                                                                                                                                                      var app = this;

                                                                                                                                                      $wnd.$('#' + id).datepicker({
                                                                                                                                                      format : dateFormat,
                                                                                                                                                      startDate : startDate,
                                                                                                                                                      endDate : endDate,
                                                                                                                                                      startView : startView,
                                                                                                                                                      minViewMode : minView,

                                                                                                                                                      })
                                                                                                                                                      .on(
                                                                                                                                                      'changeDate',
                                                                                                                                                      function(ev) {
                                                                                                                                                      console.log('ev-date => '+ev.date.getDate()+' '+ev.date.getMonth()+' '+ev.date.getYear());
                                                                                                                                                      app.@com.iglobal.bookit.client.user.widget.DatePickerBox::onDateSelected(III)(ev.date.getDate(), ev.date.getMonth(), ev.date.getFullYear())
                                                                                                                                                      });
                                                                                                                                                      
                                                                                                                                                      if(currentDate != ''){
                                                                                                                                                      $wnd.$('#'+id).datepicker('update', currentDate);
                                                                                                                                                      }
                                                                                                                                                      app.@com.iglobal.bookit.client.user.widget.DatePickerBox::reset()();
                                                                                                                                                      }-*/;

  private native void doOnlyCurrentDateInit(String id, String dateFormat, String currentDate)/*-{
                                                                                             var app = this;

                                                                                             $wnd.$('#' + id).datepicker({
                                                                                             format : dateFormat,
                                                                                             })
                                                                                             .on(
                                                                                             'changeDate',
                                                                                             function(ev) {
                                                                                             console.log('ev-date => '+ev.date.getDate()+' '+ev.date.getMonth()+' '+ev.date.getYear());
                                                                                             app.@com.iglobal.bookit.client.user.widget.DatePickerBox::onDateSelected(III)(ev.date.getDate(), ev.date.getMonth(), ev.date.getFullYear())
                                                                                             });
                                                                                             
                                                                                             $wnd.$('#'+id).datepicker('update', currentDate);
                                                                                             app.@com.iglobal.bookit.client.user.widget.DatePickerBox::reset()();
                                                                                             }-*/;

  private native void doStartDateAndCurrentDateInit(String id, String dateFormat, String startDate, String currentDate)/*-{
                                                                                                                       var app = this;

                                                                                                                       $wnd.$('#' + id).datepicker({
                                                                                                                       format : dateFormat,
                                                                                                                       startDate : startDate,
                                                                                                                       })
                                                                                                                       .on(
                                                                                                                       'changeDate',
                                                                                                                       function(ev) {
                                                                                                                       console.log('ev-date => '+ev.date.getDate()+' '+ev.date.getMonth()+' '+ev.date.getYear());
                                                                                                                       app.@com.iglobal.bookit.client.user.widget.DatePickerBox::onDateSelected(III)(ev.date.getDate(), ev.date.getMonth(), ev.date.getFullYear())
                                                                                                                       });
                                                                                                                       
                                                                                                                       $wnd.$('#'+id).datepicker('update', currentDate);
                                                                                                                       app.@com.iglobal.bookit.client.user.widget.DatePickerBox::reset()();
                                                                                                                       }-*/;

  private native void doEndDateAndCurrentDateInit(String id, String dateFormat, String endDate, String currentDate)/*-{
                                                                                                                   var app = this;

                                                                                                                   $wnd.$('#' + id).datepicker({
                                                                                                                   format : dateFormat,
                                                                                                                   endDate : endDate,
                                                                                                                   })
                                                                                                                   .on(
                                                                                                                   'changeDate',
                                                                                                                   function(ev) {
                                                                                                                   console.log('ev-date => '+ev.date.getDate()+' '+ev.date.getMonth()+' '+ev.date.getYear());
                                                                                                                   app.@com.iglobal.bookit.client.user.widget.DatePickerBox::onDateSelected(III)(ev.date.getDate(), ev.date.getMonth(), ev.date.getFullYear())
                                                                                                                   });
                                                                                                                   
                                                                                                                   $wnd.$('#'+id).datepicker('update', currentDate);
                                                                                                                   app.@com.iglobal.bookit.client.user.widget.DatePickerBox::reset()();
                                                                                                                   }-*/;

  private native void doOnlyStartDateInit(String id, String dateFormat, String startDate)/*-{
                                                                                         var app = this;

                                                                                         $wnd.$('#' + id).datepicker({
                                                                                         format : dateFormat,
                                                                                         startDate : startDate,
                                                                                         })
                                                                                         .on(
                                                                                         'changeDate',
                                                                                         function(ev) {
                                                                                         console.log('ev-date => '+ev.date.getDate()+' '+ev.date.getMonth()+' '+ev.date.getYear());
                                                                                         app.@com.iglobal.bookit.client.user.widget.DatePickerBox::onDateSelected(III)(ev.date.getDate(), ev.date.getMonth(), ev.date.getFullYear())
                                                                                         });
                                                                                         app.@com.iglobal.bookit.client.user.widget.DatePickerBox::reset()();
                                                                                         }-*/;

  private native void doOnlyEndDateInit(String id, String dateFormat, String endDate)/*-{
                                                                                     var app = this;

                                                                                     $wnd.$('#' + id).datepicker({
                                                                                     format : dateFormat,
                                                                                     endDate : endDate,
                                                                                     })
                                                                                     .on(
                                                                                     'changeDate',
                                                                                     function(ev) {
                                                                                     console.log('ev-date => '+ev.date.getDate()+' '+ev.date.getMonth()+' '+ev.date.getYear());
                                                                                     app.@com.iglobal.bookit.client.user.widget.DatePickerBox::onDateSelected(III)(ev.date.getDate(), ev.date.getMonth(), ev.date.getFullYear())
                                                                                     });
                                                                                     app.@com.iglobal.bookit.client.user.widget.DatePickerBox::reset()();
                                                                                     
                                                                                     }-*/;

  private native void doStartDateAndEndDateInit(String id, String dateFormat, String startDate, String endDate)/*-{
                                                                                                               var app = this;

                                                                                                               $wnd.$('#' + id).datepicker({
                                                                                                               format : dateFormat,
                                                                                                               startDate : startDate,
                                                                                                               endDate : endDate,
                                                                                                               })
                                                                                                               .on(
                                                                                                               'changeDate',
                                                                                                               function(ev) {
                                                                                                               console.log('ev-date => '+ev.date.getDate()+' '+ev.date.getMonth()+' '+ev.date.getYear());
                                                                                                               app.@com.iglobal.bookit.client.user.widget.DatePickerBox::onDateSelected(III)(ev.date.getDate(), ev.date.getMonth(), ev.date.getFullYear())
                                                                                                               });
                                                                                                               app.@com.iglobal.bookit.client.user.widget.DatePickerBox::reset()();
                                                                                                               
                                                                                                               }-*/;

  private native void doDateShow(String id, boolean isShow)/*-{
                                                           if (isShow) {
                                                           $wnd.$('#' + id).datepicker('show');
                                                           } else {
                                                           $wnd.$('#' + id).datepicker('hide');
                                                           }
                                                           }-*/;

  private native void doDateUpdate(String id, String currentDate)/*-{
                                                                 $wnd.$('#' + id).datepicker('update', currentDate);
                                                                 }-*/;

  public Element getDivElement() {
    return divElement;
  }

  @Override public String getAPIDate() {
    if (currentDate == null) {
      return null;
    }
    //return Utils.dateToAPIString(currentDate);
    return "";
  }

  @Override public Date getDate() {
    return currentDate;
  }

  @Override public void setDate(Date date) {
    currentDate = date;
  }

  @Override public void setStartDate(Date date) {
    this.startDate = date;
  }

  @Override public void setEndDate(Date date) {
    this.endDate = date;
  }

  @Override public void show() {
    // Scheduler.get().scheduleDeferred(new Command() {
    // @Override public void execute() {
    // doDateShow(divElement.getId(), true);
    // }
    // });
  }

  @Override public void hide() {
    // Scheduler.get().scheduleDeferred(new Command() {
    // @Override public void execute() {
    // doDateShow(divElement.getId(), false);
    // }
    // });
  }

  @Override public void addOption(DateOption option) {
    dateOptionList.add(option);
  }

  @Override public void reload() {
    DateOption[] options = new DateOption[dateOptionList.size()];
    int i = 0;
    for (DateOption option : dateOptionList) {
      options[i] = option;
      i++;
    }

    doProcessing(currentDate, startDate, endDate, options, true);
  }

  private void reset() {
    startDateString = null;
    endDateString = null;
    currentDateString = null;
    dateOptionList.clear();
  }

  @Override public void setDatePickerEventHandler(DatePickerEventHandler handler) {
    this.handler = handler;
  }

  @Override protected void initWidget(Widget widget) {
    //loadScript();
    super.initWidget(widget);
  }

  @Override public void clear() {
    if (divElement != null) {
      divElement.setAttribute("style", "color:#FFFFFF");
      currentDate = null;
    }
    isClear = true;
    currentDate = null;
  }

  @Override public String getDateFormat() {
    return javaDateFormat;
  }

  @Override public void load() {
    doProcessing(currentDate, startDate, endDate, options, false);
  }

}
