package com.quizz.core.activities;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.internal.nineoldandroids.animation.ObjectAnimator;
import com.quizz.core.R;
import com.quizz.core.interfaces.FragmentContainer;

public class BaseQuizzActivity extends SherlockFragmentActivity implements FragmentContainer {
	
	private boolean mBackgroundImageAnimated = true;
	private ImageView mBackgroundImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quizz);
		
		mBackgroundImage = (ImageView) findViewById(R.id.backgroundAnimatedImage);
		
		if (mBackgroundImageAnimated) {
			
			// FIXME: May not be displayed correctly on bigger screen when looping (bad transition)
			// TODO: Make an image with beginning left similar to right end
			// TODO: Scroll the horizontalScrollView instead of translating the
			// imageView
			HorizontalScrollView bgAnimatedImageContainer = (HorizontalScrollView) 
					findViewById(R.id.backgroundAnimatedImageContainer);
			bgAnimatedImageContainer.setOnTouchListener(new OnTouchListener() {
	
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return true;
				}
			});
			
			initActivityAnimations();
		}
	}
	
	/**
	 * Call this method before super.onCreate()<br />
	 * Prevents a useless background animation if you don't want to set an image
	 */
	protected void requestNoAnimatedBackgroundImage() {
		mBackgroundImageAnimated = false;
	}
	
	private void initActivityAnimations() {
		ObjectAnimator bgAnimation = ObjectAnimator.ofFloat(mBackgroundImage, "translationX", 500, -1700);
		bgAnimation.setDuration(30000);
		bgAnimation.setInterpolator(new LinearInterpolator());
		bgAnimation.setRepeatCount(ObjectAnimator.INFINITE);
		bgAnimation.start();
	}

	@Override
	public int getId() {
		return R.id.fragmentsContainer;
	}
}
