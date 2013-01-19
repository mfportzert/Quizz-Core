package com.quizz.core.listeners;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public interface LoadPictureListener {
	public void onPictureLoaded(Drawable drawable, ImageView imageView);
}