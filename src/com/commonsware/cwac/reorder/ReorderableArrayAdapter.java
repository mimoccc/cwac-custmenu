package com.commonsware.cwac.reorder;

import android.content.Context;
import android.widget.ArrayAdapter;
import java.util.List;

public class ReorderableArrayAdapter<T> extends ArrayAdapter<T>
	implements Reorderable {
	public ReorderableArrayAdapter(Context context,
																 int textViewResourceId,
																 List<T> objects)	{
		super(context, textViewResourceId, objects);
	}
	
	@Override
	public void moveTo(int fromPosition, int toPosition) {
		T item=getItem(fromPosition);
		int endPosition=toPosition;
		
		remove(item);
		insert(item, endPosition);
	}
}