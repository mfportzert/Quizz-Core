package com.quizz.core.utils;

import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.quizz.core.models.Level;

public class PreferencesUtils {

	public static final String PREF_VERSION_KEY = "PreferencesUtils.VERSION";
	public static final String PREF_UNLOCKED_HINTS_COUNT_KEY = "PreferencesUtils.UNLOCKED_HINTS_COUNT";
	public static final String PREF_DEFAULT_NB_HINTS_ONSUCCESS_KEY = "PreferencesUtils.DEFAULT_NB_HINTS_ONSUCCESS";
	public static final String PREF_AUDIO_KEY = "PreferencesUtils.AUDIO";
	public static final String PREF_VIBRATION_KEY = "PreferencesUtils.VIBRATION";
	public static final String PREF_EXIT_POPUP_KEY = "PreferencesUtils.EXIT_POPUP";
	public static final String PREF_LAST_PLAYED_LEVEL_KEY = "PreferencesUtils.PREF_LAST_LEVEL_PLAYED_KEY";
	public static final String PREF_UNLOCKED_LETTERS_KEY = "PreferencesUtils.PREF_UNLOCKED_LETTERS_KEY";
	public static final String PREF_USED_HINTS_COUNT_KEY = "PreferencesUtils.PREF_USED_HINTS_COUNT_KEY";

	public static final int PREF_VERSION_VALUE = 1;
	public static final int PREF_DEFAULT_UNLOCKED_HINTS_COUNT_VALUE = 3;
	public static final int PREF_DEFAULT_NB_HINTS_ONSUCCESS_VALUE = 2;
	

	public static SharedPreferences getSharedPreferences(Activity activity) {
		String getSharedPreferencesKey = activity.getApplication().getPackageName();
		return ((Activity) activity).getSharedPreferences(getSharedPreferencesKey, Activity.MODE_PRIVATE);
	}
	
	public static boolean containsVibrationPreference(Activity activity) {
		return getSharedPreferences(activity).contains(PREF_VIBRATION_KEY);		
	}
	
	public static boolean containsAudioPreference(Activity activity) {
		return getSharedPreferences(activity).contains(PREF_AUDIO_KEY);
	}
	
	public static boolean containsExitPopupPreference(Activity activity) {
		return getSharedPreferences(activity).contains(PREF_EXIT_POPUP_KEY);
	}

	public static int getHintsAvailable(Activity activity) {
		return getSharedPreferences(activity).getInt(PREF_UNLOCKED_HINTS_COUNT_KEY, 0);
	}

	public static boolean isVibrationEnabled(Activity activity) {
		return getSharedPreferences(activity).getBoolean(PREF_VIBRATION_KEY, false);		
	}
	
	public static boolean isAudioEnabled(Activity activity) {
		return getSharedPreferences(activity).getBoolean(PREF_AUDIO_KEY, false);
	}
	
	public static boolean isExitPopupEnabled(Activity activity) {
		return getSharedPreferences(activity).getBoolean(PREF_EXIT_POPUP_KEY, true);
	}

	public static int getLastPlayedLevel(Activity activity, int sectionId) {
		return getSharedPreferences(activity).getInt(PREF_LAST_PLAYED_LEVEL_KEY+"_s"+sectionId, -1);
	}

	public static String getUnlockedLetters(Activity activity, Level level) {
		String lang = Locale.getDefault().getLanguage();
		String sectionId = "_s"+level.sectionId;
		String levelId = "_l"+level.id;
		return getSharedPreferences(activity).getString(
				PREF_UNLOCKED_LETTERS_KEY+sectionId+levelId+lang, "");
	}

	public static void setVibrationEnabled(Activity activity, boolean status) {		
		Editor editor = getSharedPreferences(activity).edit();
		editor.putBoolean(PREF_VIBRATION_KEY, status);		
		editor.commit();
	}
	
	public static void setAudioEnabled(Activity activity, boolean status) {
		Editor editor = getSharedPreferences(activity).edit();
		editor.putBoolean(PREF_AUDIO_KEY, status);
		editor.commit();
	}
	
	public static void setExitPopupEnabled(Activity activity, boolean status) {
		Editor editor = getSharedPreferences(activity).edit();
		editor.putBoolean(PREF_EXIT_POPUP_KEY, status);
		editor.commit();
	}
	
	public static void setHintsAvailable(Activity activity, int hintsNumber) {
		Editor editor = getSharedPreferences(activity).edit();
		editor.putInt(PREF_UNLOCKED_HINTS_COUNT_KEY, hintsNumber);
		editor.commit();
	}
	
	public static void removeUnlockedLetters(Activity activity, Level level) {
		removeUnlockedLetters(activity, level, true);
	}

	public static void resetGame(Activity activity) {
		SharedPreferences sharedPrefs = getSharedPreferences(activity);
		Map<String, ?> allPrefs = sharedPrefs.getAll();

		Editor editor = sharedPrefs.edit();
		editor.putInt(PREF_UNLOCKED_HINTS_COUNT_KEY, PREF_DEFAULT_UNLOCKED_HINTS_COUNT_VALUE);
		for (String pref : allPrefs.keySet()) {
			if (pref.startsWith(PREF_UNLOCKED_LETTERS_KEY) 
					|| pref.startsWith(PREF_LAST_PLAYED_LEVEL_KEY)) {
				editor.remove(pref);
			}
		}
		editor.commit();
	}

	public static void removeUnlockedLetters(Activity activity, Level level, boolean allLangs) {
		String lang = Locale.getDefault().getLanguage();
		String sectionId = "_s"+level.sectionId;
		String levelId = "_l"+level.id;
		
		SharedPreferences sharedPrefs = getSharedPreferences(activity);
		Map<String, ?> allPrefs = sharedPrefs.getAll();
		
		Editor editor = sharedPrefs.edit();
		for (String pref : allPrefs.keySet()) {
			if (allLangs && pref.startsWith(PREF_UNLOCKED_LETTERS_KEY+sectionId+levelId)) {
				editor.remove(pref);
			} else if (pref.equals(PREF_UNLOCKED_LETTERS_KEY+sectionId+levelId+lang)) {
				editor.remove(pref);
			}
		}
		editor.commit();
	}
	
	public static void setUnlockedLetters(Activity activity, Level level, String letters) {
		String lang = Locale.getDefault().getLanguage();
		String sectionId = "_s"+level.sectionId;
		String levelId = "_l"+level.id;
		
		Editor editor = getSharedPreferences(activity).edit();
		editor.putString(PREF_UNLOCKED_LETTERS_KEY+sectionId+levelId+lang, letters);
		editor.commit();
	}
	
	public static void removeLastPlayedLevel(Activity activity, int sectionId) {
		Editor editor = getSharedPreferences(activity).edit();
		editor.remove(PREF_LAST_PLAYED_LEVEL_KEY+"_s"+sectionId);
		editor.commit();
	}
	
	public static void setLastPlayedLevel(Activity activity, int sectionId, int levelId) {
		Editor editor = getSharedPreferences(activity).edit();
		editor.putInt(PREF_LAST_PLAYED_LEVEL_KEY+"_s"+sectionId, levelId);
		editor.commit();
	}
	
	public static void setUsedHintsCount(Activity activity, int usedHintCount) {
		Editor editor = getSharedPreferences(activity).edit();
		editor.putInt(PREF_USED_HINTS_COUNT_KEY, usedHintCount);
		editor.commit();
	}

	public static void incrementUsedHintsCount(Activity activity) {
		setUsedHintsCount(activity, getUsedHintsCount(activity) + 1);
	}
	
	public static int getUsedHintsCount(Activity activity) {
		return getSharedPreferences(activity).getInt(PREF_USED_HINTS_COUNT_KEY, 0);
	}
	
}