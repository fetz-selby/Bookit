package com.iglobal.bookit.client.events;

import java.util.ArrayList;

public interface ModelLoadComplete<T> {
	public void onModuleLoadComple(ArrayList<T> moduleList);
}
