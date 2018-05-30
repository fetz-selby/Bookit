package com.iglobal.bookit.client.user.content;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.events.ModelLoadComplete;
import com.iglobal.bookit.client.events.SingleEntityLoadCompleteHandler;
import com.iglobal.bookit.client.user.UserReportGlobalUI;
import com.iglobal.bookit.client.user.misc.ReportStepsEnum;
import com.iglobal.bookit.client.user.misc.ReportWizardObject;
import com.iglobal.bookit.client.user.widget.ReportBookDetail;
import com.iglobal.bookit.client.user.widget.object.ReportDetailSummaryViewObject;
import com.iglobal.bookit.client.utils.UserPermissionHandler;
import com.iglobal.bookit.client.utils.Utils;
import com.iglobal.bookit.shared.BookRowObject;

public class ReportContent extends ReportWizard {

	private Widget wizard;
	private ReportContentEventHandler handler;
	private MultiWordSuggestOracle oracle;
	private SuggestBox suggest;
	private HashMap<String, String> oracleMap;
	
	private static ReportContentUiBinder uiBinder = GWT
			.create(ReportContentUiBinder.class);

	interface ReportContentUiBinder extends UiBinder<Widget, ReportContent> {
	}
	
	public interface ReportContentEventHandler{
		public void onBookLoadComplete(ReportWizardObject wizardObject);
	}

	@UiField SimplePanel booklistPanel, searchPanel;
	@UiField Anchor nextBtn;
	
	//private String bookId;
	private String bookName;
	
	public ReportContent(String bookId, String bookName, RootPanel auxPanel, UserPermissionHandler permsHandler) {
		//this.bookId = bookId;
		this.bookName = bookName;
		
		setWidget(uiBinder.createAndBindUi(this));
		initNextBtn();
		initSearchBox();
		doBookLoad(bookId);
	}
	
	private void initSearchBox(){
		Utils.doBooksLoading(new ModelLoadComplete<BookRowObject>() {
			
			@Override
			public void onModuleLoadComple(ArrayList<BookRowObject> moduleList) {
				oracleMap = new HashMap<String, String>();
				oracle = new MultiWordSuggestOracle();
				
				for(BookRowObject row : moduleList){
					oracle.add(Utils.getBookAliasName(row.getName()));
					oracleMap.put(row.getId(), Utils.getBookAliasName(row.getName()));
				}
				
				suggest = new SuggestBox(oracle);
				suggest.getTextBox().setText(bookName);
				suggest.getTextBox().setStyleName("form-control user-report-search-control");
				suggest.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
					
					@Override
					public void onSelection(SelectionEvent<Suggestion> event) {
						String item = event.getSelectedItem().getReplacementString();
						if(oracleMap.containsValue(item)){
							for(String key : oracleMap.keySet()){
								if(oracleMap.get(key).equals(item)){
									UserReportGlobalUI.getInstance().setBookName(item);
									UserReportGlobalUI.getInstance().setBookId(key);

									Utils.doBookLoad(key, new SingleEntityLoadCompleteHandler<BookRowObject>() {
										
										@Override
										public void onEntityLoadComplete(BookRowObject t) {
											if(t != null){
												doBookListSetting(t);
											}
										}
									});
								}
							}
						}
					}
				});
				
				searchPanel.add(suggest);
			}
		});
	}
	
	private void initNextBtn(){
		nextBtn.setText("Next");
		nextBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				GWT.log("Next Invoked");
				
				next();
			}
		});
	}
	
	private void doBookLoad(String bookId){
		Utils.doBookLoad(bookId, new SingleEntityLoadCompleteHandler<BookRowObject>() {
			
			@Override
			public void onEntityLoadComplete(BookRowObject t) {
				if(t != null){
					GWT.log("bookname "+t.getName()+", column "+t.getColumnString()+", groups "+t.getGroups());
					doBookListSetting(t);
				}
			}
		});
	}
	
	private void doBookListSetting(BookRowObject book){
		ReportBookDetail report = new ReportBookDetail(book.getId(), Utils.getBookAliasName(book.getName()), new ReportDetailSummaryViewObject(book.getCreatedBy(), Utils.getTruncatedText(Utils.getGroupName(book.getGroups(), ","), 25), book.getDate()));
		
		booklistPanel.setWidget(report);
		
		if(handler != null){
			handler.onBookLoadComplete(new ReportWizardObject(book.getId(), Utils.getBookTableName(book.getName()), Utils.getBookAliasName(book.getName()), book.getCreatedBy(), book.getDate(), Utils.getTruncatedText(Utils.getGroupName(book.getGroups(), ","), 25), book.getColumnString()));
		}
		
	}
	
	public void setReportContentEventHandler(ReportContentEventHandler handler){
		this.handler = handler;
	}
	
	public void load(){}

	@Override
	public ReportStepsEnum getStage() {
		return ReportStepsEnum.BOOK_STAGE;
	}

	@Override
	public ReportWizardObject getWizardObject() {
		return null;
	}

	@Override
	public Widget getWidget() {
		return wizard;
	}

	@Override
	public ReportStepsEnum getNextWizard() {
		return ReportStepsEnum.FIELDS_STAGE;
	}

	@Override
	public ReportStepsEnum getPreviousWizard() {
		return null;
	}

}
