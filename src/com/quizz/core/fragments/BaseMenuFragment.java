package com.quizz.core.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.internal.nineoldandroids.animation.AnimatorSet;
import com.quizz.core.interfaces.FragmentContainer;
import com.quizz.core.utils.NavigationUtils;

public class BaseMenuFragment extends Fragment {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
    public void onPause() {
        super.onPause();
    }
	
	@Override
    public void onResume() {
        super.onResume();
    }
	
	protected void initMenuButton(Button button, final Class<?> cls) {
		initMenuButton(button, cls, null);
	}
	
	protected void initMenuButton(Button button, final Class<?> cls, final AnimatorSet animatorSet) {
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (getActivity() instanceof FragmentContainer) {
					FragmentContainer container = (FragmentContainer) getActivity();
					NavigationUtils.animatedNavigationTo(cls, getActivity().getSupportFragmentManager(), 
							container, true, animatorSet);
				}
			}
		});
	}
}
