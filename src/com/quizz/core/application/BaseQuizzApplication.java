package com.quizz.core.application;

import com.quizz.core.db.DbHelper;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BaseQuizzApplication extends Application {

    private DbHelper mDbHelper;
    private static BaseQuizzApplication mSelf;

    public synchronized static SQLiteDatabase db() {
		if (self() == null)
			Log.d("BaseQuizzApplication", "self() returned null");
        if (self().mDbHelper == null)
            self().mDbHelper = new DbHelper(self());
        return self().mDbHelper.open();
    }

    public static Context context() {
        return self();
    }

    private static BaseQuizzApplication self() {
        return mSelf;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        mSelf = this;
        Log.d("BaseQuizzApplication", "onCreate");
    }
    
    @Override
    public void onTerminate() {
    	super.onTerminate();
      	if (self().mDbHelper != null && self().mDbHelper.isOpen())
      		self().mDbHelper.close();
      	self().mDbHelper = null;
	}
    
}
