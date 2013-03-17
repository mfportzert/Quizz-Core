package com.quizz.core.application;

import android.app.Application;

import com.quizz.core.db.DbHelper;
import com.quizz.core.db.QuizzDAO;

public class BaseQuizzApplication extends Application {

	public static final String HIDE_AB_ON_ROTATION_CHANGE = 
			"BaseQuizzApplication.HIDE_AB_ON_ROTATION_CHANGE";
	public static final String PREF_VERSION_KEY = 
			"BaseQuizzApplication.VERSION";
	public static final String PREF_UNLOCKED_HINTS_COUNT_KEY = 
			"BaseQuizzApplication.UNLOCKED_HINTS_COUNT";
	public static final String PREF_DEFAULT_NB_HINTS_ONSUCCESS_KEY = 
			"BaseQuizzApplication.DEFAULT_NB_HINTS_ONSUCCESS";
	
	public static final int PREF_VERSION_VALUE = 1;
	public static final int PREF_DEFAULT_UNLOCKED_HINTS_COUNT_VALUE = 3;
	public static final int PREF_DEFAULT_NB_HINTS_ONSUCCESS_VALUE = 2;
	
	@Override
	public void onCreate() {
		super.onCreate();
		QuizzDAO.INSTANCE.setDbHelper(new DbHelper(this));
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		if (QuizzDAO.INSTANCE.getDbHelper() != null) {
			QuizzDAO.INSTANCE.getDbHelper().close();
		}
	}
}
