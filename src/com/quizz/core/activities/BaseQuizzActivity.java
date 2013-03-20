package com.quizz.core.activities;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.quizz.core.R;
import com.quizz.core.application.BaseQuizzApplication;
import com.quizz.core.dialogs.ConfirmQuitDialog;
import com.quizz.core.dialogs.ConfirmQuitDialog.Closeable;
import com.quizz.core.interfaces.FragmentContainer;
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

//	private TextView mTvProgress;
//	private ProgressBar mPbProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_quizz);
		buildGameLayout(savedInstanceState);

//		SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
//		if (!sharedPreferences.contains(BaseQuizzApplication.PREF_VERSION_KEY)) {
//			Editor editor = sharedPreferences.edit();
//			editor.putInt(BaseQuizzApplication.PREF_UNLOCKED_HINTS_COUNT_KEY, 
//					BaseQuizzApplication.PREF_DEFAULT_UNLOCKED_HINTS_COUNT_VALUE);
//			editor.commit();
////			new FirstLaunchTask().execute();
//		} else if (sharedPreferences.getInt(BaseQuizzApplication.PREF_VERSION_KEY, 0) 
//				< BaseQuizzApplication.PREF_VERSION_VALUE) {
//			// need to upgrade db
//		} else {
////			viewSwitcher.showNext();
//		}
	}

//	private void buildLoadingLayout() {
//		mTvProgress = (TextView) viewSwitcher.findViewById(R.id.tv_progress);
//		mPbProgressBar = (ProgressBar) viewSwitcher
//				.findViewById(R.id.pb_progressbar);
//		mPbProgressBar.setMax(100);
//	}

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
		if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
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
