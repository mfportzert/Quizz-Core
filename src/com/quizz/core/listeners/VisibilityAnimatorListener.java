package com.quizz.core.listeners;

import android.view.View;

import com.actionbarsherlock.internal.nineoldandroids.animation.Animator;
import com.actionbarsherlock.internal.nineoldandroids.animation.Animator.AnimatorListener;

public class VisibilityAnimatorListener implements AnimatorListener {
		
	View mView;
	
	public VisibilityAnimatorListener(View view) {
		mView = view;
	}
	
	@Override
	public void onAnimationStart(Animator animation) {
		mView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onAnimationEnd(Animator animation) {}

	@Override
	public void onAnimationCancel(Animator animation) {}

	@Override
	public void onAnimationRepeat(Animator animation) {}
}