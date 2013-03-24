package com.quizz.core.dialogs;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public abstract class BaseHintsDialog extends Activity {

	protected ViewGroup mHintsContainer;
	private SparseArray<View> mTabs = new SparseArray<View>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		// To remove the background of the 'Dialog'
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	}
	
	protected void addTab(final int tabId, int viewId) {
		if (mHintsContainer != null) {
			LayoutInflater inflater = LayoutInflater.from(this);
			View tabContentView = inflater.inflate(viewId, mHintsContainer, true);
			
			onInitTab(tabId, tabContentView);
			
			final View tabView = findViewById(tabId);
			tabView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					tabView.setPressed(true);
					selectTab(tabId);
				}
			});
			
			mTabs.append(tabId, tabContentView);
		}
	}

	public void selectTab(int tabId) {
		int key = 0;
		for(int i = 0; i < mTabs.size(); i++) {
		   key = mTabs.keyAt(i);
		   View view = mTabs.get(key);
		   view.setVisibility((key == tabId) ? View.VISIBLE : View.GONE);
		}
		
		View tabContentView = mTabs.get(tabId);
		onSelectTab(tabId, tabContentView);
	}
	
	abstract protected void onSelectTab(int tabId, View contentView);
	
	abstract protected void onInitTab(int tabId, View contentView);
	
	// ===========================================================
	// Listeners
	// ===========================================================

	// ===========================================================
	// Inner classes
	// ===========================================================
}
