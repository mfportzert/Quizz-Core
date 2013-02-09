package com.quizz.core.utils;

import java.io.IOException;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class ImageUtils {

    public static Drawable createFromAsset(Context context, String filePath) {
	try {
	    return Drawable.createFromStream(
		    context.getAssets().open(filePath), null);
	} catch (IOException e) {
	    Log.e("ImageUtils", e.getMessage(), e);
	}
	return null;
    }
}
