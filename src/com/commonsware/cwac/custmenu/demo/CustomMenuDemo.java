package com.commonsware.cwac.custmenu.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import java.util.ArrayList;
import com.commonsware.cwac.custmenu.CustomMenuBuilder;
import com.commonsware.cwac.custmenu.MenuCustomizer;

public class CustomMenuDemo extends Activity {
	private CustomMenuBuilder builder=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Button btn=new Button(this);
		
		btn.setText("Customize the menu!");
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					Intent i=new Intent(CustomMenuDemo.this,
																MenuCustomizer.class);
					
					i.putExtra(MenuCustomizer.CONFIGURATION,
											builder.getExtra());
					i.putExtra(MenuCustomizer.UP_RESOURCE,
											R.drawable.uparrow);
					i.putExtra(MenuCustomizer.DOWN_RESOURCE,
											R.drawable.downarrow);
					
					startActivityForResult(i, 1337);
				}
				catch (Throwable t) {
					Log.e("CustomMenuDemo", "Exception starting customizer", t);
					goBlooey(t);
				}
			}
		});
		
		setContentView(btn);
		
		try {
			builder=new CustomMenuBuilder(null);
			builder.add(1, getString(R.string.item_one),
									R.drawable.ic_menu_add);
			builder.add(2, getString(R.string.item_two),
									R.drawable.ic_menu_agenda);
			builder.add(3, getString(R.string.item_three),
									R.drawable.ic_menu_camera);
			builder.add(4, getString(R.string.item_four),
									R.drawable.ic_menu_compass);
		}
		catch (Throwable t) {
			Log.e("CustomMenuDemo", "Exception building menu", t);
			goBlooey(t);
		}
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		builder.build(menu, null);

		return(super.onPrepareOptionsMenu(menu));
	}
	
	@Override
	protected void onActivityResult(int requestCode,
																	int resultCode, Intent data) {
		if (resultCode==RESULT_OK) {
			String result=data.getStringExtra(MenuCustomizer.RESULT);
			
			try {
				builder.setOrder(result);
			}
			catch (Throwable t) {
				Log.e("CustomMenuDemo", "Exception updating menu", t);
				goBlooey(t);
			}
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