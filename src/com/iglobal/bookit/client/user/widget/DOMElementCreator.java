package com.iglobal.bookit.client.user.widget;

import java.util.ArrayList;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.iglobal.bookit.client.GlobalResource;
import com.iglobal.bookit.client.user.events.SideBarULEventHandler;
import com.iglobal.bookit.shared.BookRowObject;
import com.iglobal.bookit.shared.QueryEnum;
import com.iglobal.bookit.shared.QueryObject;
import com.iglobal.bookit.shared.QueryOperatorEnum;

public class DOMElementCreator {
	public void initSideBarUL(ArrayList<String> groupList, final SideBarULEventHandler handler){
		
//		<ul class="nav nav-main" data-ride="collapse" id="itemList">
//
//		<li><a href="javascript:void(0)" class="auto"
//			id="adminMenu"> <i class="i i-statistics icon"> </i> <span
//				class="font-bold">Admins</span>
//		</a></li>
//	</ul>
		
		
		final Element ul = DOM.createElement("ul");
		ul.setClassName("nav nav-main");
		ul.setAttribute("data-ride", "collapse");
		
		ArrayList<QueryObject> queryList = new ArrayList<QueryObject>();

		for(int i = 0; i < groupList.size(); i++){
			String groupId = groupList.get(i);
			QueryObject query = new QueryObject(QueryEnum.B_GROUPS, groupId, false, QueryOperatorEnum.LIKE);
			
			//if it's the last, donot use OR.
			if(i != groupList.size() - 1){
				query.setJoinOperator(QueryOperatorEnum.OR);
			}
			
			queryList.add(query);
		}
		
		queryList.add(new QueryObject(QueryEnum.B_STATUS, "A", true, QueryOperatorEnum.EQUALS));
		queryList.add(new QueryObject(QueryEnum.B_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_ID, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_GROUPS, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_TABLE_COL, null, true, null));

		
		GlobalResource.getInstance().getAdminRPC().getBookList(queryList, new AsyncCallback<ArrayList<BookRowObject>>() {
			
			@Override
			public void onSuccess(ArrayList<BookRowObject> result) {
				if(result != null){
					for(BookRowObject row : result){
						Element li = getSideBarULItems(row.getName().split("[:]")[1], row.getId(), row.getColumnString(), handler);
						ul.appendChild(li);
					}
					
					if(handler != null){
						handler.onItemRenderComplete(ul, result);
					}
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private Element getSideBarULItems(final String name, final String id, final String columnString, final SideBarULEventHandler handler){
		Element li = DOM.createElement("li");
		
		Element a = DOM.createElement("a");
		a.setClassName("auto");
		a.setAttribute("href", "javascript:void(0)");
		
		Element span = DOM.createElement("span");
		span.setClassName("font-bold");
		span.setInnerText(name);
		
		a.appendChild(span);
		
		DOM.sinkEvents(a, Event.ONCLICK);
		DOM.setEventListener(a, new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				if(handler != null){
					handler.onItemClicked(id, name, columnString);
				}
			}
		});
		
		
		li.appendChild(a);
		
		return li;
	}
	
	public Element getThumbPrint(){
		Element img = DOM.createElement("img");
		img.setAttribute("src", "images/thumb.jpeg");
		img.addClassName("col-md-12");
		
		return img;
	}
	
	public Element getNoPreview(){
		Element img = DOM.createElement("img");
		img.setAttribute("src", "images/no_preview.png");
		img.addClassName("col-md-12 m-t-lg");
		
		return img;
	}
	
}
