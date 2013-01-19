package com.quizz.core.listeners;

import android.graphics.drawable.Drawable;

public interface LoadAdapterPictureListener {
	public void onPictureLoaded(Drawable drawable, int position, Object tag);
}