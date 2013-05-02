package com.quizz.core.utils;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.quizz.core.application.BaseQuizzApplication;

public class PreferencesUtils {

//	public static SharedPreferences sharedPreferences(Activity pActivity) {
//		return ((Activity) pActivity).getPreferences(Activity.MODE_PRIVATE);
//	}

	public static SharedPreferences sharedPreferences(Activity pActivity) {
		String sharedPreferencesKey = pActivity.getApplication().getPackageName();
		return ((Activity) pActivity).getSharedPreferences(sharedPreferencesKey, Activity.MODE_PRIVATE);
	}
	
	public static boolean containsVibrationPreference(Activity pActivity) {
		return sharedPreferences(pActivity).contains(BaseQuizzApplication.PREF_VIBRATION_KEY);		
	}
	
	public static boolean containsAudioPreference(Activity pActivity) {
		return sharedPreferences(pActivity).contains(BaseQuizzApplication.PREF_AUDIO_KEY);
	}
	
	public static boolean containsExitPopupPreference(Activity pActivity) {
		return sharedPreferences(pActivity).contains(BaseQuizzApplication.PREF_EXIT_POPUP_KEY);
	}

	public static int getHintsAvailable(Activity pActivity) {
		return sharedPreferences(pActivity).getInt(BaseQuizzApplication.PREF_UNLOCKED_HINTS_COUNT_KEY, 0);
	}

	public static boolean isVibrationEnabled(Activity pActivity) {
		return sharedPreferences(pActivity).getBoolean(BaseQuizzApplication.PREF_VIBRATION_KEY, false);		
	}
	
	public static boolean isAudioEnabled(Activity pActivity) {
		return sharedPreferences(pActivity).getBoolean(BaseQuizzApplication.PREF_AUDIO_KEY, false);
	}
	
	public static boolean isExitPopupEnabled(Activity pActivity) {
		return sharedPreferences(pActivity).getBoolean(BaseQuizzApplication.PREF_EXIT_POPUP_KEY, true);
	}
	
	public static void setVibrationEnabled(Activity pActivity, boolean status) {		
		Editor editor = sharedPreferences(pActivity).edit();
		editor.putBoolean(BaseQuizzApplication.PREF_VIBRATION_KEY, status);		
		editor.commit();
	}
	
	public static void setAudioEnabled(Activity pActivity, boolean status) {
		Editor editor = sharedPreferences(pActivity).edit();
		editor.putBoolean(BaseQuizzApplication.PREF_AUDIO_KEY, status);
		editor.commit();
	}
	
	public static void setExitPopupEnabled(Activity pActivity, boolean status) {
		Editor editor = sharedPreferences(pActivity).edit();
		editor.putBoolean(BaseQuizzApplication.PREF_EXIT_POPUP_KEY, status);
		editor.commit();
	}
	
	public static void setHintsAvailable(Activity activity, int hintsNumber) {
		Editor editor = sharedPreferences(activity).edit();
		editor.putInt(BaseQuizzApplication.PREF_UNLOCKED_HINTS_COUNT_KEY, hintsNumber);
		editor.commit();
	}
}
