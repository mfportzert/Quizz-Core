package com.quizz.core.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class IntentUtils {

	public static Boolean isPlayStoreInstalled(Context context) {
		PackageManager pacman = context.getPackageManager();
		try {
			pacman.getApplicationInfo("com.android.vending", 0);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}
}
