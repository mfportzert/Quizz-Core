package com.quizz.core.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.quizz.core.interfaces.FragmentContainer;

public class NavigationUtils {
	
	public static void navigateTo(Class<?> cls, FragmentManager fragmentManager, FragmentContainer container) {
		try {
			Fragment fragment = fragmentManager.findFragmentByTag(cls.getSimpleName());
            if (fragment == null) {
            	fragment = (Fragment) cls.newInstance();
            }
            
            if (!fragment.isAdded()) { // if fragment is already on the screen, do nothing
            	FragmentTransaction transaction = fragmentManager.beginTransaction();
            	transaction.replace(container.getId(), (Fragment) cls.newInstance(), cls.getSimpleName());
            	transaction.commit();
            }
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
