package com.iglobal.bookit.client.utils;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;


public class Animate {

	private static int counter;

	public static void fadeOut(final String id){
		Scheduler.get().scheduleDeferred(new Command() {
			@Override public void execute() {
				doFadeOut(id);
			}
		});
	}

	public static void fadeOut(final Element element){
		if(element.getId() == null || element.getId().trim().isEmpty()){
			element.setId("sels_animate_"+counter ++);
		}

		Scheduler.get().scheduleDeferred(new Command() {
			@Override public void execute() {
				doFadeOut(element.getId());
			}
		});
	}

	public static void fadeOut(Widget widget){
		final Element element = widget.getElement();
		
		if(element.getId() == null || element.getId().trim().isEmpty()){
			element.setId("sels_animate_"+counter ++);
		}

		Scheduler.get().scheduleDeferred(new Command() {
			@Override public void execute() {
				doFadeOut(element.getId());
			}
		});
	}

	public static void fadeIn(final String id){
		Scheduler.get().scheduleDeferred(new Command() {
			@Override public void execute() {
				doFadeIn(id);
			}
		});
	}

	public static void fadeIn(final Element element){
		if(element.getId() == null || element.getId().trim().isEmpty()){
			element.setId("sels_animate_"+counter ++);
		}

		Scheduler.get().scheduleDeferred(new Command() {
			@Override public void execute() {
				doFadeIn(element.getId());
			}
		});
	}

	public static void fadeIn(Widget widget){
		final Element element = widget.getElement();
		
		if(element.getId() == null || element.getId().trim().isEmpty()){
			element.setId("sels_animate_"+counter ++);
		}

		Scheduler.get().scheduleDeferred(new Command() {
			@Override public void execute() {
				doFadeIn(element.getId());
			}
		});
	}
	
	private static native void doFadeOut(String id)/*-{
		$wnd.$('#'+id).fadeOut(3000, 'linear');
	}-*/;

	private static native void doFadeIn(String id)/*-{
		$wnd.$('#'+id).fadeIn('slow');
	}-*/;

}
