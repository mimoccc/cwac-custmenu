package com.commonsware.cwac.custmenu;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class SimpleMenuItemFactory
	implements CustomMenuBuilder.MenuItemFactory {
	int itemId;
	String title;
	int iconResourceId;
	Intent thingToRun;
												
	public SimpleMenuItemFactory(int itemId, String title,
												int iconResourceId,
												Intent thingToRun) {
		this.itemId=itemId;
		this.title=title;
		this.iconResourceId=iconResourceId;
		this.thingToRun=thingToRun;
	}
	
	public int getItemId() {
		return(itemId);
	}
	
	public String getTitle() {
		return(title);
	}
	
	public boolean isItemEnabled(int itemId) {
		return(true);
	}
	
	public void addToMenu(Menu menu) {
		MenuItem item=menu.add(Menu.NONE, getItemId(),
													 Menu.NONE, title);
		
		if (iconResourceId!=0) {
			item.setIcon(iconResourceId);
		}
		
		if (thingToRun!=null) {
			item.setIntent(thingToRun);
		}
		
		item.setEnabled(isItemEnabled(getItemId()));
	}
}
