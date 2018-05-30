package com.iglobal.bookit.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.iglobal.bookit.shared.IsUser;
import com.iglobal.bookit.shared.QueryObject;

public interface LoginServiceAsync {

	void getHasQueryUser(ArrayList<QueryObject> userQueryObjects,
			QueryObject userQuery, QueryObject passwordQuery,
			AsyncCallback<IsUser> callback);

	void isLogout(String email, AsyncCallback<Boolean> callback);


}
