package com.quizz.core.activities;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.quizz.core.R;
import com.quizz.core.interfaces.FragmentContainer;

public class BaseQuizzActivity extends SherlockFragmentActivity implements FragmentContainer {
	
	private View mQuizzLayout;
	private ImageView mBackgroundAnimatedImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quizz);
		
		mQuizzLayout = findViewById(R.id.quizzLayout);
		mBackgroundAnimatedImage = (ImageView) findViewById(R.id.backgroundAnimatedImage);
		
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
	}
	
	@Override
	public int getId() {
		return R.id.fragmentsContainer;
	}

	public View getQuizzLayout() {
		return mQuizzLayout;
	}
	
	public ImageView getBackgroundAnimatedImage() {
		return mBackgroundAnimatedImage;
	}
}
