package com.commonsware.cwac.custmenu;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONStringer;

public class CustomMenuBuilder {
	private List<Integer> customOrder=new ArrayList<Integer>();
	private List<Integer> originalOrder=new ArrayList<Integer>();
	private HashMap<Integer, MenuItemFactory> factories=
										new HashMap<Integer, MenuItemFactory>();
	
	public CustomMenuBuilder(String order) throws Exception {
		setOrder(order);
	}
	
	public void setOrder(String order) throws Exception {
		customOrder.clear();
		
		if (order!=null) {
			JSONArray json=new JSONArray(order);
			
			for (int i=0;i<json.length();i++) {
				this.customOrder.add(json.getInt(i));
			}
		}
	}
	
	public void add(MenuItemFactory f) {
		factories.put(f.getItemId(), f);
		originalOrder.add(f.getItemId());
	}
	
	public void add(int itemId, String title, int iconResourceId) {
		add(new SimpleMenuItemFactory(itemId, title,
																	iconResourceId, null));
	}
	
	public void add(int itemId, String title) {
		add(new SimpleMenuItemFactory(itemId, title, 0, null));
	}
	
	public void add(int itemId, String title, int iconResourceId,
									Intent thingToRun) {
		add(new SimpleMenuItemFactory(itemId, title, iconResourceId,
																	thingToRun));
	}
	
	public void add(int itemId, String title, Intent thingToRun) {
		add(new SimpleMenuItemFactory(itemId, title, 0, thingToRun));
	}
	
	public void build(Menu menu, int[] itemsToFilterOut) {
		List<Integer> orderToUse=customOrder;
		List<Integer> filter=new ArrayList<Integer>();
		
		if (itemsToFilterOut!=null) {
			for (int i : itemsToFilterOut) {
				filter.add(i);
			}
		}
		
		if (orderToUse.size()==0) {
			orderToUse=originalOrder;
		}
		
		for (Integer i : orderToUse) {
			MenuItemFactory f=factories.get(i);
			
			if (!filter.contains(f.getItemId())) {
				f.addToMenu(menu);
			}
		}
	}
	
	public String getExtra() throws Exception {
		JSONStringer json=new JSONStringer();
		
		List<Integer> orderToUse=customOrder;
		
		if (orderToUse.size()==0) {
			orderToUse=originalOrder;
		}
		
		json.array();
		
		for (Integer i : orderToUse) {
			MenuItemFactory f=factories.get(i);
			
			json
				.object()
				.key(MenuCustomizer.ITEM_ID).value(f.getItemId())
				.key(MenuCustomizer.TITLE).value(f.getTitle())
				.endObject();
		}
		
		return(json.endArray().toString());
	}
	
	public String getOrder() throws Exception {
		JSONStringer json=new JSONStringer();
		
		List<Integer> orderToUse=customOrder;
		
		if (orderToUse.size()==0) {
			orderToUse=originalOrder;
		}
		
		json.array();
		
		for (Integer i : orderToUse) {
			MenuItemFactory f=factories.get(i);
			
			json.value(f.getItemId());
		}
		
		return(json.endArray().toString());
	}
	
	public interface MenuItemFactory {
		int getItemId();
		void addToMenu(Menu menu);
		String getTitle();
	}
}