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
			// Clean content to keep only the new view
			mHintsContainer.removeAllViews();
			
			final View tabView = findViewById(tabId);
			LayoutInflater inflater = LayoutInflater.from(this);
			final View tabContentView = inflater.inflate(viewId, null);
			tabView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					tabView.setPressed(true);
					mHintsContainer.removeAllViews();
					
					// To avoid a 'child already has a parent'
					if (tabContentView.getParent() != null) {
						((ViewGroup) tabContentView.getParent()).removeView(tabContentView);
					}
					mHintsContainer.addView(tabContentView);
					onTabSelected(tabId, tabContentView);
				}
			});
			
			mTabs.append(tabId, tabContentView);
		}
	}

	abstract protected void onTabSelected(int tabId, View contentView);
	
	// ===========================================================
	// Listeners
	// ===========================================================

	// ===========================================================
	// Inner classes
	// ===========================================================
}
