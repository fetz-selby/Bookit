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
import com.iglobal.bookit.shared.AdminRowObject;
import com.iglobal.bookit.shared.PermsRowObject;
import com.iglobal.bookit.shared.QueryEnum;
import com.iglobal.bookit.shared.QueryObject;
import com.iglobal.bookit.shared.QueryOperatorEnum;

public class GroupFilter extends Composite implements Filter{
	private AdminServiceAsync rpc;
	private static GroupFilterUiBinder uiBinder = GWT
			.create(GroupFilterUiBinder.class);

	interface GroupFilterUiBinder extends UiBinder<Widget, GroupFilter> {
	}

	@UiField ComboDataListBox permList, createdByList, statusList;
	@UiField CustomRangeDatePicker dateRange;

	public GroupFilter(AdminServiceAsync rpc) {
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
		statusList.setQueryEnum(QueryEnum.G_STATUS);
		statusList.setOperator(QueryOperatorEnum.EQUALS);
		statusList.load();
	}

	private void initDateComponent(){
		dateRange.setLabelString("Date");
		dateRange.setQueryEnumObject(QueryEnum.G_CREATED_TS);
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
					permList.setQueryEnum(QueryEnum.G_PERMS);
					permList.setOperator(QueryOperatorEnum.LIKE);
					permList.load();

					initCreatedByComponent();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initCreatedByComponent(){
		ArrayList<QueryObject> adminQueryList = new ArrayList<QueryObject>();

		adminQueryList.add(new QueryObject(QueryEnum.A_STATUS, "A", true, QueryOperatorEnum.EQUALS));
		adminQueryList.add(new QueryObject(QueryEnum.A_ID, null, true, QueryOperatorEnum.EQUALS));
		adminQueryList.add(new QueryObject(QueryEnum.A_NAME, null, true, QueryOperatorEnum.EQUALS));
		//adminQueryList.add(new QueryObject(QueryEnum.A_PERMS, DBConstants.WRITE, true, QueryOperatorEnum.LIKE));
		adminQueryList.add(new QueryObject(QueryEnum.A_PERMS, null, true, null));


		rpc.getAdminList(adminQueryList, new AsyncCallback<ArrayList<AdminRowObject>>() {

			@Override
			public void onSuccess(ArrayList<AdminRowObject> result) {
				if(result != null){
					HashMap<String, String> adminMap = new HashMap<String, String>();
					for(AdminRowObject object : result){
						adminMap.put(object.getName(), object.getId());
					}
					createdByList.setLabelString("Created By");
					createdByList.setMap(adminMap);
					createdByList.setQueryEnum(QueryEnum.G_CREATED_BY);
					createdByList.setOperator(QueryOperatorEnum.EQUALS);
					createdByList.load();

					initStatusComponent();
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
			queryList.add(new QueryObject(QueryEnum.G_PERMS, null, true, null));
		}

		if(statusList.getQueryObject() != null){
			queryList.add(statusList.getQueryObject());
		}else{
			queryList.add(new QueryObject(QueryEnum.G_STATUS, null, true, null));
		}

		if(createdByList.getQueryObject() != null){
			queryList.add(createdByList.getQueryObject());
		}else{
			queryList.add(new QueryObject(QueryEnum.G_CREATED_BY, null, true, null));
		}

		if(dateRange.getQueryObject() != null){
			queryList.add(dateRange.getQueryObject());
		}else{

		}

		queryList.add(new QueryObject(QueryEnum.G_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.G_ID, null, true, null));
		queryList.add(new QueryObject(QueryEnum.G_CREATED_TS, null, true, null));
		queryList.add(new QueryObject(QueryEnum.G_PERMS, null, true, null));
		queryList.add(new QueryObject(QueryEnum.G_STATUS, null, true, null));
		queryList.add(new QueryObject(QueryEnum.G_IS_NOTIFICATION, null, true, null));


		queryList.add(new QueryObject(QueryEnum.A_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.A_ID, QueryEnum.G_CREATED_BY.getField(), true, QueryOperatorEnum.EQUALS));

		return queryList;
	}

	@Override
	public ArrayList<QueryObject> getSearchFilterQuery(String query) {
		ArrayList<QueryObject> queryList = new ArrayList<QueryObject>();

		if(permList.getQueryObject() != null){
			queryList.add(permList.getQueryObject());
		}else{
			queryList.add(new QueryObject(QueryEnum.G_PERMS, null, true, null));
		}

		if(statusList.getQueryObject() != null){
			queryList.add(statusList.getQueryObject());
		}else{
			queryList.add(new QueryObject(QueryEnum.G_STATUS, null, true, null));
		}

		if(createdByList.getQueryObject() != null){
			queryList.add(createdByList.getQueryObject());
		}else{
			queryList.add(new QueryObject(QueryEnum.G_CREATED_BY, null, true, null));
		}

		if(dateRange.getQueryObject() != null){
			queryList.add(dateRange.getQueryObject());
		}else{

		}
		
		if(query != null){
			QueryObject groupNameQuery = new QueryObject(QueryEnum.G_NAME, query, true, QueryOperatorEnum.LIKE);
			groupNameQuery.setOrderField(QueryEnum.G_NAME.getField());
			
			queryList.add(groupNameQuery);
		}else{
			
			QueryObject groupNameQuery = new QueryObject(QueryEnum.G_NAME, null, true, QueryOperatorEnum.EQUALS);
			groupNameQuery.setOrderField(QueryEnum.G_NAME.getField());
			
			queryList.add(groupNameQuery);
		}

		queryList.add(new QueryObject(QueryEnum.G_ID, null, true, null));
		queryList.add(new QueryObject(QueryEnum.G_CREATED_TS, null, true, null));
		queryList.add(new QueryObject(QueryEnum.G_PERMS, null, true, null));
		queryList.add(new QueryObject(QueryEnum.G_STATUS, null, true, null));
		queryList.add(new QueryObject(QueryEnum.G_IS_NOTIFICATION, null, true, null));

		queryList.add(new QueryObject(QueryEnum.A_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.A_ID, QueryEnum.G_CREATED_BY.getField(), true, QueryOperatorEnum.EQUALS));



		return queryList;
	}

}
