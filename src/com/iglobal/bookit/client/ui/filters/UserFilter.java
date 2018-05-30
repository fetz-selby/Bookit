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
import com.iglobal.bookit.shared.GroupRowObject;
import com.iglobal.bookit.shared.QueryEnum;
import com.iglobal.bookit.shared.QueryObject;
import com.iglobal.bookit.shared.QueryOperatorEnum;

public class UserFilter extends Composite implements Filter{

	private AdminServiceAsync rpc;
	private static UserFilterUiBinder uiBinder = GWT
			.create(UserFilterUiBinder.class);

	interface UserFilterUiBinder extends UiBinder<Widget, UserFilter> {
	}

	@UiField ComboDataListBox groupList, createdByList, statusList;
	@UiField CustomRangeDatePicker dateRange;

	public UserFilter(AdminServiceAsync rpc) {
		this.rpc = rpc;
		initWidget(uiBinder.createAndBindUi(this));
		initGroupComponent();
	}

	private void initStatusComponent(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("Active", "A");
		map.put("De-Active", "D");

		statusList.setLabelString("Status");
		statusList.setMap(map);
		statusList.setQueryEnum(QueryEnum.U_STATUS);
		statusList.setOperator(QueryOperatorEnum.EQUALS);
		statusList.load();
	}

	private void initDateComponent(){
		dateRange.setLabelString("Date");
		dateRange.setQueryEnumObject(QueryEnum.U_CREATED_TS);
		dateRange.setOperator(QueryOperatorEnum.BETWEEN);
		dateRange.load();
	}

	private void initGroupComponent(){
		ArrayList<QueryObject> groupQueryList = new ArrayList<QueryObject>();

		groupQueryList.add(new QueryObject(QueryEnum.G_STATUS, "A", true, QueryOperatorEnum.EQUALS));
		groupQueryList.add(new QueryObject(QueryEnum.G_ID, null, true, QueryOperatorEnum.EQUALS));
		groupQueryList.add(new QueryObject(QueryEnum.G_NAME, null, true, QueryOperatorEnum.EQUALS));
		groupQueryList.add(new QueryObject(QueryEnum.G_PERMS, null, true, null));

		rpc.getGroupList(groupQueryList, new AsyncCallback<ArrayList<GroupRowObject>>() {

			@Override
			public void onSuccess(ArrayList<GroupRowObject> result) {
				if(result != null){
					HashMap<String, String> groupMap = new HashMap<String, String>();
					for(GroupRowObject object : result){
						groupMap.put(object.getName(), object.getId());
					}
					groupList.setLabelString("Group");
					groupList.setMap(groupMap);
					groupList.setQueryEnum(QueryEnum.U_GROUPS);
					groupList.setOperator(QueryOperatorEnum.LIKE);
					groupList.load();

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
					createdByList.setQueryEnum(QueryEnum.U_CREATED_BY);
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

		if(createdByList.getQueryObject() != null){
			queryList.add(createdByList.getQueryObject());
		}else{
			queryList.add(new QueryObject(QueryEnum.A_NAME, null, true, null));
		}

		if(groupList.getQueryObject() != null){
			queryList.add(groupList.getQueryObject());
		}else{
			queryList.add(new QueryObject(QueryEnum.U_GROUPS, null, true, null));
		}

		if(statusList.getQueryObject() != null){
			queryList.add(statusList.getQueryObject());
		}else{
			queryList.add(new QueryObject(QueryEnum.U_STATUS, null, true, null));
		}

		if(dateRange.getQueryObject() != null){
			queryList.add(dateRange.getQueryObject());
		}else{

		}

		queryList.add(new QueryObject(QueryEnum.U_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.U_ID, null, true, null));
		queryList.add(new QueryObject(QueryEnum.A_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.U_CREATED_TS, null, true, null));
		queryList.add(new QueryObject(QueryEnum.PER_EMAIL, null, true, null));
		queryList.add(new QueryObject(QueryEnum.PER_PASSWORD, null, true, null));
		queryList.add(new QueryObject(QueryEnum.PER_STATUS, null, true, null));

		queryList.add(new QueryObject(QueryEnum.PER_ID, QueryEnum.U_PERSON_ID.getField(), true, QueryOperatorEnum.EQUALS));
		queryList.add(new QueryObject(QueryEnum.U_CREATED_BY, QueryEnum.A_ID.getField(), true, QueryOperatorEnum.EQUALS));

		return queryList;
	}

	@Override
	public ArrayList<QueryObject> getSearchFilterQuery(String query) {
		ArrayList<QueryObject> queryList = new ArrayList<QueryObject>();

		if(createdByList.getQueryObject() != null){
			queryList.add(createdByList.getQueryObject());
		}else{
			queryList.add(new QueryObject(QueryEnum.A_NAME, null, true, null));
		}

		if(groupList.getQueryObject() != null){
			queryList.add(groupList.getQueryObject());
		}else{
			queryList.add(new QueryObject(QueryEnum.U_GROUPS, null, true, null));
		}

		if(statusList.getQueryObject() != null){
			queryList.add(statusList.getQueryObject());
		}else{
			queryList.add(new QueryObject(QueryEnum.U_STATUS, null, true, null));
		}

		if(dateRange.getQueryObject() != null){
			queryList.add(dateRange.getQueryObject());
		}else{

		}
		
		QueryObject userNameQuery = new QueryObject(QueryEnum.U_NAME, null, true, null);
		userNameQuery.setOrderField(QueryEnum.U_NAME.getField());
		
		queryList.add(userNameQuery);
		//queryList.add(new QueryObject(QueryEnum.U_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.U_ID, null, true, null));
		queryList.add(new QueryObject(QueryEnum.A_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.U_CREATED_TS, null, true, null));
		
		if(query != null){
			queryList.add(new QueryObject(QueryEnum.PER_EMAIL, query, true, QueryOperatorEnum.LIKE));
		}else{
			queryList.add(new QueryObject(QueryEnum.PER_EMAIL, null, true, QueryOperatorEnum.EQUALS));
		}
		
		queryList.add(new QueryObject(QueryEnum.PER_PASSWORD, null, true, null));
		queryList.add(new QueryObject(QueryEnum.PER_STATUS, null, true, null));

		queryList.add(new QueryObject(QueryEnum.PER_ID, QueryEnum.U_PERSON_ID.getField(), true, QueryOperatorEnum.EQUALS));
		queryList.add(new QueryObject(QueryEnum.U_CREATED_BY, QueryEnum.A_ID.getField(), true, QueryOperatorEnum.EQUALS));

		return queryList;
	}

}
