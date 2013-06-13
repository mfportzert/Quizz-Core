package com.quizz.core.utils;

import java.io.IOException;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class ImageUtils {

	public static Drawable createFromAsset(Context context, String filePath) {
		try {
			return Drawable.createFromStream(
					context.getAssets().open(filePath), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
