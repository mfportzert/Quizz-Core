package com.quizz.core.tasks;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.quizz.core.listeners.LoadAdapterPictureListener;
import com.quizz.core.utils.ImageUtils;

public class LoadAdapterPictureTask extends AsyncTask<Void, Void, Drawable> {
    private LoadAdapterPictureListener mListener;
    private Context mContext;
    private int mPosition;
    private Object mTag;
    private String mPath;

    public LoadAdapterPictureTask(Context context, String path, int position, Object tag,
	    LoadAdapterPictureListener listener) {
	mContext = context;
	mListener = listener;
	mPosition = position;
	mPath = path;
	mTag = tag;
    }

    @Override
    protected Drawable doInBackground(Void... arg0) {
	return ImageUtils.createFromAsset(mContext, mPath);
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
	mListener.onPictureLoaded(drawable, mPosition, mTag);
    }
}
