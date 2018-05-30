package com.iglobal.bookit.client.user.report.widget;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

public class ReportSuggestBox extends Composite {

	private String item, label;
	private ReportSuggestBoxEventHandler handler;
	private ArrayList<String> items;
	private MultiWordSuggestOracle oracle;
	private SuggestBox suggest;
	private static ReportSuggestBoxUiBinder uiBinder = GWT
			.create(ReportSuggestBoxUiBinder.class);

	interface ReportSuggestBoxUiBinder extends
			UiBinder<Widget, ReportSuggestBox> {
	}
	
	public interface ReportSuggestBoxEventHandler{
		void onItemSuggest(String label, String item);
	}

	@UiField SpanElement fieldNameSpan;
	@UiField SimplePanel suggestContainer;
	private String field;
	private String value;
	
	public ReportSuggestBox(String label, String field, ArrayList<String> items) {
		this.label = label;
		this.items = items;
		this.field = field;
		initWidget(uiBinder.createAndBindUi(this));
		initComponents();
	}
	
	private void initComponents(){
		
		fieldNameSpan.setInnerText(label);
		
		if(items != null){
			oracle = new MultiWordSuggestOracle();
			for(String item : items){
				oracle.add(item);
			}
			
			suggest = new SuggestBox(oracle);
			suggest.getTextBox().setStyleName("form-control");
			
			suggest.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
				
				@Override
				public void onSelection(SelectionEvent<Suggestion> event) {
					item = event.getSelectedItem().getReplacementString();
					value = item;
					if(handler != null){
						handler.onItemSuggest(label, item);
					}
				}
			});
			
			suggestContainer.setWidget(suggest);
		}
	}
	
	public TextBoxBase getSuggestBaseBox(){
		return suggest.getTextBox();
	}

	public String getItems() {
		return item;
	}
	
	public String getField(){
		return field;
	}
	
	public String getValue(){
		return value;
	}
	
	public void setReportSuggestBoxEventHandler(ReportSuggestBoxEventHandler handler){
		this.handler = handler;
	}
	
}
