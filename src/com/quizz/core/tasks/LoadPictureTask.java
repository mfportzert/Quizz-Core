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
    private String mPath;

    public LoadPictureTask(Context context, String path, ImageView imageView,
	    LoadPictureListener listener) {
	mPath = path;
	mContext = context;
	mListener = listener;
	mImageView = imageView;
    }

    @Override
    protected Drawable doInBackground(Void... arg0) {
	return ImageUtils.createFromAsset(mContext, mPath);
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
	mListener.onPictureLoaded(drawable, mImageView);
    }
}
