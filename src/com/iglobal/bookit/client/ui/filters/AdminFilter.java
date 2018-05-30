package com.iglobal.bookit.client.ui.filters;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.AdminServiceAsync;
import com.iglobal.bookit.client.parents.Filter;
import com.iglobal.bookit.client.ui.components.ComboDataListBox;
import com.iglobal.bookit.client.ui.components.CustomRangeDatePicker;
import com.iglobal.bookit.shared.PermsRowObject;
import com.iglobal.bookit.shared.QueryEnum;
import com.iglobal.bookit.shared.QueryObject;
import com.iglobal.bookit.shared.QueryOperatorEnum;

public class AdminFilter extends Composite implements Filter{

	private AdminServiceAsync rpc;
	private static AdminFilterUiBinder uiBinder = GWT
			.create(AdminFilterUiBinder.class);

	interface AdminFilterUiBinder extends UiBinder<Widget, AdminFilter> {
	}

	@UiField ComboDataListBox permList, statusList, superList;
	@UiField CustomRangeDatePicker dateRange;

	public AdminFilter(AdminServiceAsync rpc) {
		this.rpc = rpc;
		initWidget(uiBinder.createAndBindUi(this));
		initPermsComponent();
	}

	private void initStatusComponent(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("Active", "A");
		map.put("De-Active", "D");

		statusList.setLabelString("Status");
		statusList.setMap(map);
		statusList.setQueryEnum(QueryEnum.A_STATUS);
		statusList.setOperator(QueryOperatorEnum.EQUALS);
		statusList.load();
	}
	
	private void initSuperListComponent(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("Super Admins", "Y");
		map.put("Administrators", "N");

		superList.setLabelString("Admin Type");
		superList.setMap(map);
		superList.setQueryEnum(QueryEnum.A_IS_SA);
		superList.setOperator(QueryOperatorEnum.EQUALS);
		superList.load();
	}

	private void initDateComponent(){
		dateRange.setLabelString("Date");
		dateRange.setQueryEnumObject(QueryEnum.A_CREATED_TS);
		dateRange.setOperator(QueryOperatorEnum.BETWEEN);
		dateRange.load();
	}

	private void initPermsComponent(){
		ArrayList<QueryObject> permsQueryList = new ArrayList<QueryObject>();

		permsQueryList.add(new QueryObject(QueryEnum.P_STATUS, "A", true, QueryOperatorEnum.EQUALS));
		permsQueryList.add(new QueryObject(QueryEnum.P_ID, null, true, QueryOperatorEnum.EQUALS));
		permsQueryList.add(new QueryObject(QueryEnum.P_NAME, null, true, QueryOperatorEnum.EQUALS));
		permsQueryList.add(new QueryObject(QueryEnum.P_STRING, null, true, QueryOperatorEnum.EQUALS));

		rpc.getPermsList(permsQueryList, new AsyncCallback<ArrayList<PermsRowObject>>() {

			@Override
			public void onSuccess(ArrayList<PermsRowObject> result) {
				if(result != null){
					HashMap<String, String> adminMap = new HashMap<String, String>();
					for(PermsRowObject object : result){
						adminMap.put(object.getName(), object.getId());
					}
					permList.setLabelString("Permission");
					permList.setMap(adminMap);
					permList.setQueryEnum(QueryEnum.A_PERMS);
					permList.setOperator(QueryOperatorEnum.LIKE);
					permList.load();

					initStatusComponent();
					initSuperListComponent();
					initDateComponent();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public ArrayList<QueryObject> getFilterQuery() {
		ArrayList<QueryObject> queryList = new ArrayList<QueryObject>();

		if(permList.getQueryObject() != null){
			queryList.add(permList.getQueryObject());
		}else{
			queryList.add(new QueryObject(QueryEnum.A_PERMS, null, true, null));
		}

		if(statusList.getQueryObject() != null){
			queryList.add(statusList.getQueryObject());
		}else{
			queryList.add(new QueryObject(QueryEnum.A_STATUS, null, true, null));
		}
		
		if(superList.getQueryObject() != null){
			queryList.add(superList.getQueryObject());
		}else{
			queryList.add(new QueryObject(QueryEnum.A_IS_SA, null, true, null));
		}

		if(dateRange.getQueryObject() != null){
			queryList.add(dateRange.getQueryObject());
		}else{

		}

		queryList.add(new QueryObject(QueryEnum.A_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.A_ID, null, true, null));
		queryList.add(new QueryObject(QueryEnum.A_IS_SA, null, true, null));

		queryList.add(new QueryObject(QueryEnum.A_CREATED_TS, null, true, null));
		queryList.add(new QueryObject(QueryEnum.PER_EMAIL, null, true, null));
		queryList.add(new QueryObject(QueryEnum.PER_PASSWORD, null, true, null));
		queryList.add(new QueryObject(QueryEnum.PER_STATUS, null, true, null));

		queryList.add(new QueryObject(QueryEnum.PER_ID, QueryEnum.A_PERSON_ID.getField(), true, QueryOperatorEnum.EQUALS));

		return queryList;
	}

	@Override
	public ArrayList<QueryObject> getSearchFilterQuery(String query) {
		ArrayList<QueryObject> queryList = new ArrayList<QueryObject>();

		if(permList.getQueryObject() != null){
			queryList.add(permList.getQueryObject());
		}else{
			queryList.add(new QueryObject(QueryEnum.A_PERMS, null, true, null));
		}

		if(statusList.getQueryObject() != null){
			queryList.add(statusList.getQueryObject());
		}else{
			queryList.add(new QueryObject(QueryEnum.A_STATUS, null, true, null));
		}
		
		if(superList.getQueryObject() != null){
			queryList.add(superList.getQueryObject());
		}else{
			queryList.add(new QueryObject(QueryEnum.A_IS_SA, null, true, null));
		}

		if(dateRange.getQueryObject() != null){
			queryList.add(dateRange.getQueryObject());
		}else{

		}
		
		QueryObject nameQuery = new QueryObject(QueryEnum.A_NAME, null, true, null);
		nameQuery.setOrderField(QueryEnum.A_NAME.getField());
		
		queryList.add(nameQuery);

		//queryList.add(new QueryObject(QueryEnum.A_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.A_ID, null, true, null));
		queryList.add(new QueryObject(QueryEnum.A_IS_SA, null, true, null));

		queryList.add(new QueryObject(QueryEnum.A_CREATED_TS, null, true, null));
		if(query == null){
			queryList.add(new QueryObject(QueryEnum.PER_EMAIL, null, true, QueryOperatorEnum.EQUALS));
		}else{
			queryList.add(new QueryObject(QueryEnum.PER_EMAIL, query, true, QueryOperatorEnum.LIKE));
		}
		queryList.add(new QueryObject(QueryEnum.PER_PASSWORD, null, true, null));
		queryList.add(new QueryObject(QueryEnum.PER_STATUS, null, true, null));

		queryList.add(new QueryObject(QueryEnum.PER_ID, QueryEnum.A_PERSON_ID.getField(), true, QueryOperatorEnum.EQUALS));

		return queryList;
	}
}
