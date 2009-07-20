package com.commonsware.cwac.custmenu;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.util.Log;
import com.commonsware.cwac.reorder.ReorderableArrayAdapter;
import com.commonsware.cwac.reorder.ReorderingAdapter;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

public class MenuCustomizer extends ListActivity {
	public static String CONFIGURATION="com.commonsware.cwac.custmenu.CONFIGURATION";
	public static String UP_RESOURCE="com.commonsware.cwac.custmenu.UP_RESOURCE";
	public static String DOWN_RESOURCE="com.commonsware.cwac.custmenu.DOWN_RESOURCE";
	public static String RESULT="com.commonsware.cwac.custmenu.RESULT";
	public static String ITEM_ID="itemId";
	public static String TITLE="title";
	private ArrayAdapter<MenuEntry> aa=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String cfg=getIntent().getStringExtra(CONFIGURATION);
		int upArrow=getIntent().getIntExtra(UP_RESOURCE, -1);
		int downArrow=getIntent().getIntExtra(DOWN_RESOURCE, -1);
		
		if (cfg==null) {
			finish();
		}
		else {
			try {
				ArrayList<MenuEntry> list=new ArrayList<MenuEntry>();
				JSONArray json=new JSONArray(cfg);
				
				for (int i=0;i<json.length();i++) {
					list.add(new MenuEntry(json.getJSONObject(i)));
				}
				
				aa=new ReorderableArrayAdapter<MenuEntry>(this,
														android.R.layout.simple_list_item_1,
														list);
				
				setListAdapter(new ReorderingAdapter(this, aa,
																						 upArrow,
																						 downArrow));
			}
			catch (Throwable t) {
				Log.e("MenuCustomizer", "Exception in configuration", t);
				goBlooey(t);
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			try {
				JSONStringer json=new JSONStringer();
				
				json.array();
				
				for (int i=0;i<aa.getCount();i++) {
					MenuEntry entry=aa.getItem(i);
					
					json.value(entry.itemId);
				}
				
				Intent i=new Intent();
				
				i.putExtra(RESULT, json.endArray().toString());
				
				setResult(RESULT_OK, i);
			}
			catch (Throwable t) {
				Log.e("MenuCustomizer", "Exception in setting result", t);
				goBlooey(t);
			}
		}
		
		return(super.onKeyDown(keyCode, event));
	}
	
	class MenuEntry {
		int itemId;
		String title;
		
		MenuEntry(JSONObject json) throws org.json.JSONException {
			itemId=json.getInt(ITEM_ID);
			title=json.getString(TITLE);
		}
		
		public String toString() {
			return(title);
		}
	}
	
	private void goBlooey(Throwable t) {
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		
		builder
			.setTitle("Exception!")
			.setMessage(t.toString())
			.setPositiveButton("OK", null)
			.show();
	}
}
