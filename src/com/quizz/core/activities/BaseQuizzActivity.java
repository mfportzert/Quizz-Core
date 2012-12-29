package com.quizz.core.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.quizz.core.R;

public class BaseQuizzActivity extends SherlockFragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quizz);
	}
	
	protected void navigateTo(Class<?> cls) {
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		try {
			Fragment fragment = fragmentManager.findFragmentByTag(cls.getSimpleName());
            if (fragment == null) {
            	fragment = (Fragment) cls.newInstance();
            }
            
            if (!fragment.isAdded()) { // if fragment is already on the screen, do nothing
            	FragmentTransaction transaction = fragmentManager.beginTransaction();
            	transaction.replace(R.id.fragmentsContainer, (Fragment) cls.newInstance(), "test");
            	transaction.commit();
            }
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
