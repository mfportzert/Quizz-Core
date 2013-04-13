package com.quizz.core.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Progress values are from 0 - 100f
 * 
 * @author M-F.P
 *
 */
public class SectionProgressView extends ImageView {

	private int mProgressRes = 0;
	private NinePatchDrawable mProgressDrawable;
	private float mProgressValue = 0;

	private int mPaddingTop = 0;
	private int mPaddingLeft = 0;
	private int mPaddingRight = 0;
	private int mPaddingBottom = 0;

	/**
	 * even if progress if 0, display a tiny progress drawable
	 */
	private boolean mDisplayInitialProgressIfEmpty = false;
	
	private Rect mProgressBounds = new Rect();

	public SectionProgressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SectionProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SectionProgressView(Context context) {
		super(context);
	}

	public void setProgressRes(int res) {
		mProgressRes = res;
		if (res > 0) {
			mProgressDrawable = (NinePatchDrawable) getContext().getResources()
					.getDrawable(mProgressRes);
		}
	}

	public void setProgressDrawable(Drawable drawable) {
		if (drawable instanceof NinePatchDrawable) {
			mProgressDrawable = (NinePatchDrawable) drawable;
		} else {
			throw new IllegalArgumentException("Progress Drawable must be a Nine Patch.");
		}
	}
	
	public void setProgressValue(float value) {
		mProgressValue = value;
	}

	public void setPaddingProgress(int left, int top, int right, int bottom) {
		mPaddingLeft = left;
		mPaddingTop = top;
		mPaddingRight = right;
		mPaddingBottom = bottom;
	}

	public void setDisplayInitialProgressIfEmpty(boolean display) {
		mDisplayInitialProgressIfEmpty = display;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (mProgressDrawable != null) {
			mProgressBounds.left = mPaddingLeft;
			mProgressBounds.top = mPaddingTop;
			mProgressBounds.bottom = getHeight() - mPaddingBottom;
		
			if (mProgressValue == 0) {
				if (mDisplayInitialProgressIfEmpty) {
					mProgressBounds.right = mProgressBounds.left + mProgressDrawable.getIntrinsicWidth();
				} else {
					return;
				}
			} else {
				int progressWidth = (int) ((getWidth() - mPaddingLeft - mPaddingRight)
						* mProgressValue / 100f);
				mProgressBounds.right = mProgressBounds.left + progressWidth;
			}
			
			mProgressDrawable.setBounds(mProgressBounds);
			mProgressDrawable.draw(canvas);
		}
	}
}
