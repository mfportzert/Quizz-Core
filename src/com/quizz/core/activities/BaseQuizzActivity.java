package com.quizz.core.activities;

import java.util.Locale;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.quizz.core.R;
import com.quizz.core.application.BaseQuizzApplication;
import com.quizz.core.dialogs.ConfirmQuitDialog;
import com.quizz.core.dialogs.ConfirmQuitDialog.Closeable;
import com.quizz.core.interfaces.FragmentContainer;
import com.quizz.core.utils.PreferencesUtils;
import com.quizz.core.widgets.QuizzActionBar;

public abstract class BaseQuizzActivity extends SherlockFragmentActivity
		implements FragmentContainer, Closeable {
	private static final String TAG = BaseQuizzActivity.class.getSimpleName();

	private View mQuizzLayout;
	private View mConfirmQuitDialogView;
	private ImageView mBackgroundAnimatedImage;

	private QuizzActionBar mQuizzActionBar;
	private ConfirmQuitDialog mConfirmQuitDialog;

	private boolean mHideAbOnRotation = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_quizz);
		buildGameLayout(savedInstanceState);
		Log.d("TEST", Locale.getDefault().getLanguage());
	}

	private void buildGameLayout(Bundle savedInstanceState) {
		mQuizzLayout = findViewById(R.id.quizzLayout);
		mBackgroundAnimatedImage = (ImageView) findViewById(R.id.backgroundAnimatedImage);
		mQuizzActionBar = (QuizzActionBar) findViewById(R.id.quizzTopActionBar);

		View shadowView = this.findViewById(R.id.ab_separator_shadow);
		mQuizzActionBar.setShadowView(shadowView);

		if (savedInstanceState != null) {
			mHideAbOnRotation = savedInstanceState
					.getBoolean(BaseQuizzApplication.HIDE_AB_ON_ROTATION_CHANGE);
			if (mHideAbOnRotation) {
				mQuizzActionBar.hide(QuizzActionBar.MOVE_DIRECT);
			}
		}

		// FIXME: May not be displayed correctly on bigger screen when looping
		// (bad transition)
		// TODO: Make an image with beginning left similar to right end
		// TODO: Scroll the horizontalScrollView instead of translating the
		// imageView
		HorizontalScrollView bgAnimatedImageContainer = (HorizontalScrollView) 
				this.findViewById(R.id.backgroundAnimatedImageContainer);
		bgAnimatedImageContainer.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mConfirmQuitDialog != null) {
			mConfirmQuitDialog.dismiss();
		}
	}

	@Override
	public void close() {
		finish();
	}

	@Override
	public int getId() {
		return R.id.fragmentsContainer;
	}

	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().getBackStackEntryCount() == 0 
				&& PreferencesUtils.isExitPopupEnabled(this)) {
			if (mConfirmQuitDialog == null) {
				mConfirmQuitDialog = (mConfirmQuitDialogView == null) ? new ConfirmQuitDialog(
						this) : new ConfirmQuitDialog(this,
						mConfirmQuitDialogView);
				mConfirmQuitDialog.setClosable(this);
			}
			mConfirmQuitDialog.show();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(BaseQuizzApplication.HIDE_AB_ON_ROTATION_CHANGE, mHideAbOnRotation);
		super.onSaveInstanceState(outState);
	}

	protected void setConfirmQuitDialogView(View view) {
		mConfirmQuitDialogView = view;
	}

	public View getQuizzLayout() {
		return mQuizzLayout;
	}

	public ImageView getBackgroundAnimatedImage() {
		return mBackgroundAnimatedImage;
	}

	public QuizzActionBar getQuizzActionBar() {
		return mQuizzActionBar;
	}

	/**
	 * Default behaviour is 'false'
	 * 
	 * @param hide
	 */
	public void setHideAbOnRotationChange(boolean hide) {
		mHideAbOnRotation = hide;
	}
}
