package com.quizz.core.tasks;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.quizz.core.listeners.LoadPictureListener;
import com.quizz.core.utils.ImageUtils;

public class LoadPictureTask extends AsyncTask<Void, Void, Drawable> {
	private LoadPictureListener mListener;
	private Context mContext;
	private ImageView mImageView;
	
	public LoadPictureTask(Context context, ImageView imageView, LoadPictureListener listener) {
		mContext = context;
		mListener = listener;
		mImageView = imageView;
	}
	
	@Override
	protected Drawable doInBackground(Void... arg0) {
		return ImageUtils.createFromAsset(mContext, "pictures/big_ben.jpg");
	}
	
	@Override
	protected void onPostExecute(Drawable drawable) {
		mListener.onPictureLoaded(drawable, mImageView);
	}
}
