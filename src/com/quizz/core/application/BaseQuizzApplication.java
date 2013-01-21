package com.quizz.core.application;

import android.app.Application;

import com.quizz.core.db.DbHelper;
import com.quizz.core.db.QuizzDAO;

public class BaseQuizzApplication extends Application {

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
