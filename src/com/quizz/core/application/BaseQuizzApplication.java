package com.quizz.core.application;

import android.app.Application;

import com.quizz.core.db.DbHelper;

public class BaseQuizzApplication extends Application {
    private DbHelper mDbHelper;

    public synchronized DbHelper getDbHelper() {
	    return mDbHelper;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
    	mDbHelper = new DbHelper(this);
    }
    
    @Override
    public void onTerminate() {
    	super.onTerminate();
    	mDbHelper.close();
    }
}
