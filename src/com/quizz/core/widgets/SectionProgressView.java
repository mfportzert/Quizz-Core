package com.quizz.core.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SectionProgressView extends ImageView {

	private int mProgressRes = 0;
	private float mProgressValue = 0;

	private int mPaddingTop = 0;
	private int mPaddingLeft = 0;
	private int mPaddingRight = 0;
	private int mPaddingBottom = 0;

	public SectionProgressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public SectionProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SectionProgressView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void setProgressRes(int res) {
		mProgressRes = res;
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

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (mProgressRes > 0) {
			NinePatchDrawable npd = (NinePatchDrawable) getContext()
					.getResources().getDrawable(mProgressRes);

			// TODO: Optimize memory
			Rect npdBounds = new Rect(mPaddingLeft, mPaddingTop, 
					(int) ((getWidth() - mPaddingRight) * mProgressValue / 100f), 
					getHeight() - mPaddingBottom);
			npd.setBounds(npdBounds);
			npd.draw(canvas);
		}
	}
}
