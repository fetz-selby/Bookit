package com.iglobal.bookit.client.parents;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.IsWidget;
import com.iglobal.bookit.shared.QueryObject;

public interface Filter extends IsWidget{
	ArrayList<QueryObject> getFilterQuery();
	ArrayList<QueryObject> getSearchFilterQuery(String query);
}
