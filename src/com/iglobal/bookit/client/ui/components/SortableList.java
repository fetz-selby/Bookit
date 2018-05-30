package com.iglobal.bookit.client.ui.components;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.ui.components.CustomConfirmationPanel.CustomConfirmationPanelEventHandler;
import com.iglobal.bookit.client.ui.components.object.SortableObject;
import com.iglobal.bookit.client.utils.Utils;

public class SortableList extends Composite {

	private static SortableListUiBinder uiBinder = GWT
			.create(SortableListUiBinder.class);
	private SortableListEventHandler handler;
	private HashMap<String, SortableObject> objectMap;
	//private int counter;
	private static int globalCounter;

	interface SortableListUiBinder extends UiBinder<Widget, SortableList> {
	}

	public interface SortableListEventHandler{
		void onSortableRowCanceled(SortableObject object);
		void onSortableRowEdit(SortableObject object);
	}

	@UiField UListElement ulElement;
	
	public SortableList() {
		initWidget(uiBinder.createAndBindUi(this));
		initElement();
	}
	
	private void initElement(){
		ulElement.setId(""+globalCounter++);
		objectMap = new HashMap<String, SortableObject>();
	}
	
	public void add(SortableObject sortableObject){
		if(objectMap.containsKey(sortableObject.getValue())){
			return;
		}
		
		Element li = getLIElement();
		li.appendChild(getActionOptionElement(li, sortableObject));
		li.appendChild(getNumberTagElement());
		li.appendChild(getTextableTagElement(sortableObject));
		
		ulElement.appendChild(li);
		initNative();
		objectMap.put(sortableObject.getValue().trim(), sortableObject);
	}
	
	private Element getLIElement(){
		Element li = DOM.createElement("li");
		li.setClassName("list-group-item group-sortable-style sortable-hover");
		li.setAttribute("draggable", "true");
		li.setAttribute("style", "position: relative");
		
		return li;
	}
	
	private Element getActionOptionElement(final Element parentElement, final SortableObject obj){
		
		Element spanElement = DOM.createElement("span");
		spanElement.setClassName("pull-right");
		
		Element editAnchor = DOM.createElement("a");
		editAnchor.setAttribute("href", "#");
		
		Element edit = DOM.createElement("i");
		edit.setClassName("fa fa-pencil icon-muted fa-fw m-r-xs");
		
		Element closeAnchor = DOM.createElement("a");
		closeAnchor.setAttribute("href", "#");
		
		Element i = DOM.createElement("i");
		i.setClassName("fa fa-times icon-muted fa-fw");
		
		DOM.sinkEvents(edit, Event.ONCLICK);
		DOM.setEventListener(edit, new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				if(handler != null){
					handler.onSortableRowEdit(obj);
				}
			}
		});
		
		DOM.sinkEvents(i, Event.ONCLICK);
		DOM.setEventListener(i, new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				CustomConfirmationPanel panel = new CustomConfirmationPanel();
				panel.setCustomConfirmationPanelEventHandler(new CustomConfirmationPanelEventHandler() {
					
					@Override
					public void onContinueClicked() {
						if(objectMap.containsKey(obj.getValue().trim())){
							objectMap.remove(obj.getValue().trim());
						}
						
						parentElement.removeFromParent();
						if(handler != null){
							handler.onSortableRowCanceled(obj);
						}						
					}
					
					@Override
					public void onCancelClicked() {
						// TODO Auto-generated method stub
						
					}
				});
				panel.show();
			}
		});
		
		//editAnchor.appendChild(edit);
		closeAnchor.appendChild(i);
		
		spanElement.appendChild(editAnchor);
		spanElement.appendChild(closeAnchor);
		
		return spanElement;
	}
	
	public HashMap<String, SortableObject> getSelectedSortableHashMap(){
		return objectMap;
	}
	
	public String getSelectedKeyString(){
		final String DELIMITER = ",";
		String str = "";
		
		for(String key : objectMap.keySet()){
			str += key+DELIMITER;
		}
		
		if(str.isEmpty()){
			return "";
		}else{
			return str.substring(0, str.lastIndexOf(DELIMITER));
		}
	}
	
	private Element getNumberTagElement(){
		Element span = DOM.createElement("span");
		span.setClassName("pull-left media-xs");
		
		Element i = DOM.createElement("i");
		i.setClassName("fa fa-sort text-muted fa m-r-sm");
		
		Element counterSpan = DOM.createElement("span");
		//counterSpan.setInnerText(""+(counter ++));
		
		span.appendChild(i);
		span.appendChild(counterSpan);
		
		return span;
	}
	
	private Element getTextableTagElement(SortableObject obj){
		
		Element div = DOM.createElement("div");
		div.setClassName("clear");
		div.setInnerText(Utils.getTruncatedText(obj.getName(), 35));
		
		return div;
	}
	
	public void clear(){
		ulElement.setInnerText("");
	}
	
	public void setSortableListEventHandler(SortableListEventHandler handler){
		this.handler = handler;
	}

	private void initNative(){
		 Scheduler.get().scheduleDeferred(new Command() {
		        @Override public void execute() {
		        	doNativeSorting(ulElement.getId());
		        }
		      });
	}
	
	private native void doNativeSorting(String id)/*-{
		$wnd.$( "#"+id).sortable();
	}-*/;

	
}
