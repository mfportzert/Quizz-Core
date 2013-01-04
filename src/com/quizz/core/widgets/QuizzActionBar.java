package com.quizz.core.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.internal.nineoldandroids.animation.Animator;
import com.actionbarsherlock.internal.nineoldandroids.animation.Animator.AnimatorListener;
import com.actionbarsherlock.internal.nineoldandroids.animation.ObjectAnimator;
import com.quizz.core.R;
import com.quizz.core.utils.AnimatorUtils;

public class QuizzActionBar extends RelativeLayout {
	
	public static final int MOVE_DIRECT = 1;
	public static final int MOVE_NORMAL = 300;
	
	private ImageView mBackButton;
	private TextView mMiddleText;
	private TextView mRightText;
	private ObjectAnimator mAbAnimation = new ObjectAnimator();
	
	public QuizzActionBar(Context context) {
		super(context);
		init(context, null);
	}

	public QuizzActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TypedArray style = context.obtainStyledAttributes(attrs,
		// R.styleable.PPListViewContainer);
		init(context, null);
	}

	public QuizzActionBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, null);
	}

	//@SuppressWarnings("deprecation")
	private void init(Context context, TypedArray style) {
		LayoutInflater.from(context).inflate(R.layout.layout_quizz_action_bar, this, true);
		this.mBackButton = (ImageView) findViewById(R.id.ab_back_button);
		this.mMiddleText = (TextView) findViewById(R.id.ab_middle_text);
		this.mRightText = (TextView) findViewById(R.id.ab_right_text);
		
		/*
		if (style != null) {
			if (style.hasValue(R.styleable.PPListViewContainer_refreshButtonDrawable)) {
				Drawable refreshDrawable = style.getDrawable(R.styleable.PPListViewContainer_refreshButtonDrawable);
				if (null != refreshDrawable) {
					this.mRefreshButton.setBackgroundDrawable(refreshDrawable);
				}
			}

			if (style.hasValue(R.styleable.PPListViewContainer_emptyListMessage)) {
				this.mEmptyMsg = style.getString(R.styleable.PPListViewContainer_emptyListMessage);
				if (null == this.mEmptyMsg) {
					this.mEmptyMsg = context
							.getResources()
							.getString(
									style.getResourceId(
											R.styleable.PPListViewContainer_emptyListMessage,
											0));
				}
			}

			style.getResourceId(
					R.styleable.PPListViewContainer_emptyListMessage,
					R.drawable.ic_refresh);
			style.recycle();
		}
		*/
	}
	
	public ImageView getBackButton() {
		return mBackButton;
	}
	
	public TextView getMiddleText() {
		return mMiddleText;
	}
	
	public TextView getRightText() {
		return mRightText;
	}
	
	private void animate(float[] movements, int duration, boolean bounce, AnimatorListener listener) {
		if (mAbAnimation.isRunning()) {
			mAbAnimation.cancel();
		}
		
		mAbAnimation = ObjectAnimator.ofFloat(this, "translationY", movements);
		mAbAnimation.setDuration(duration);
		if (listener != null) {
			mAbAnimation.addListener(listener);
		}
		
		if (bounce) {
			AnimatorUtils.bounceAnimator(mAbAnimation, movements, 5, 100);
		} else {
			mAbAnimation.start();
		}
	}
	
	private int getBarHeight() {
		int height = getHeight();
		if (height == 0) {
			measure(0, 0);
			height = getMeasuredHeight();
		}
		return height;
	}
	
	/**
	 * Calls the show() method only if the visibility is set to GONE
	 */
	public void showIfNecessary(int duration) {
		if (getVisibility() == View.GONE) {
			show(duration);
		}
	}
	
	public void show(int duration) {
		animate(new float[] { -getBarHeight(), 0 }, duration, (duration > MOVE_DIRECT) ? true : false, 
				mAbShowAnimatorListener);
	}
	
	public void hide(int duration) {
		animate(new float[] { 0, -getBarHeight() }, duration, false, mAbHideAnimatorListener);
	}
	
	// ===========================================================
    // Listeners
    // ===========================================================
	
	AnimatorListener mAbShowAnimatorListener = new AnimatorListener() {
		
		@Override
		public void onAnimationStart(Animator animation) {
			setVisibility(View.VISIBLE);
		}
		
		@Override
		public void onAnimationRepeat(Animator animation) {}
		
		@Override
		public void onAnimationEnd(Animator animation) {}
		
		@Override
		public void onAnimationCancel(Animator animation) {}
	};
	
	AnimatorListener mAbHideAnimatorListener = new AnimatorListener() {
		
		@Override
		public void onAnimationStart(Animator animation) {}
		
		@Override
		public void onAnimationRepeat(Animator animation) {}
		
		@Override
		public void onAnimationEnd(Animator animation) {
			setVisibility(View.GONE);
		}
		
		@Override
		public void onAnimationCancel(Animator animation) {}
	};
}
