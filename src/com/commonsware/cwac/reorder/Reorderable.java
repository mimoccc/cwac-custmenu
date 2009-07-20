package com.commonsware.cwac.reorder;

public interface Reorderable {
	void moveTo(int fromPosition, int toPosition);
}