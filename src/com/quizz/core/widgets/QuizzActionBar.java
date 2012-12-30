package com.quizz.core.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quizz.core.R;

public class QuizzActionBar extends RelativeLayout {

	private ImageButton mBackButton;
	private TextView mMiddleText;
	private TextView mRightText;

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
		this.mBackButton = (ImageButton) findViewById(R.id.ab_back_button);
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
	
	public ImageButton getBackButton() {
		return mBackButton;
	}
	
	public TextView getMiddleText() {
		return mMiddleText;
	}
	
	public TextView getRightText() {
		return mRightText;
	}
}
