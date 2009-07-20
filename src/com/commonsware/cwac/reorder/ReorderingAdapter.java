package com.commonsware.cwac.reorder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.LinearLayout;

public class ReorderingAdapter extends AdapterWrapper {
	private static int UP_BUTTON_ID=1337;
	private static int DOWN_BUTTON_ID=UP_BUTTON_ID+1;
	private int upResourceId;
	private int downResourceId;
	private Context ctxt=null;
	
	public ReorderingAdapter(Context ctxt,
													 ListAdapter delegate,
													 int upResourceId,
													 int downResourceId) {
		super(delegate);
		
		this.ctxt=ctxt;
		this.upResourceId=upResourceId;
		this.downResourceId=downResourceId;
	}
	
	public View getView(int position, View convertView,
											ViewGroup parent) {
		View result=null;
		
		if (isReorderable()) {
			if (convertView==null) {
				result=wrapRow(super.getView(position, null, parent),
											 position);
			}
			else {
				View guts=((ViewGroup)convertView).getChildAt(0);
				View newGuts=super.getView(position, guts, parent);
				
				if (newGuts!=guts) {
					result=wrapRow(newGuts, position);
				}
				else {
					result=updateRow((ViewGroup)convertView, position);
				}
			} 
		}
		else {
			result=super.getView(position, convertView, parent);
		}
		
		return(result);
	}
		
	private View wrapRow(View guts, final int position) {
		LinearLayout layout=new LinearLayout(ctxt);
		final ImageButton up=new ImageButton(ctxt);
		final ImageButton down=new ImageButton(ctxt);
		
		layout.setOrientation(LinearLayout.HORIZONTAL); 
		
		layout.setLayoutParams(guts.getLayoutParams());
		guts.setLayoutParams(new LinearLayout.LayoutParams(
											0,
											LinearLayout.LayoutParams.WRAP_CONTENT,
											1.0f));
			
		LinearLayout.LayoutParams params=
			new LinearLayout.LayoutParams(48,	48);
			
		params.gravity=0x10;		// center vertical
		params.setMargins(0, 0, 4, 0);

		up.setId(UP_BUTTON_ID);
		up.setImageResource(upResourceId);
		up.setLayoutParams(params);
		up.setTag(position);
		up.setEnabled(position>0);
		up.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int current=((Integer)up.getTag()).intValue();
				
				if (current>0) {
					((Reorderable)delegate).moveTo(current, current-1);
					up.setTag(current-1);
					up.setEnabled(current>1);
				}
			}
		});
		
		down.setId(DOWN_BUTTON_ID);
		down.setImageResource(downResourceId);
		down.setLayoutParams(params);
		down.setTag(position);
		down.setEnabled(position<getCount()-1);
		down.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int current=((Integer)down.getTag()).intValue();
				
				if (current<getCount()-1) {
					((Reorderable)delegate).moveTo(current, current+1);
					down.setTag(current+1);
					down.setEnabled(current<getCount()-2);
				}
			}
		});
		
		layout.addView(guts);
		layout.addView(up);
		layout.addView(down);
		
		return(layout);
	}		
		
	private View updateRow(ViewGroup row, int position) {
		View up=row.findViewById(UP_BUTTON_ID);
		
		up.setTag(position);
		up.setEnabled(position>0);

		View down=row.findViewById(DOWN_BUTTON_ID);
		
		down.setTag(position);
		down.setEnabled(position<getCount()-1);
		
		return(row);
	}		
	
	private boolean isReorderable() {
		return(delegate instanceof Reorderable);
	}
}