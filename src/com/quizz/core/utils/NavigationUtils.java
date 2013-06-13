package com.quizz.core.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.quizz.core.interfaces.FragmentContainer;

public class NavigationUtils {
	private static final String TAG = NavigationUtils.class.getSimpleName();
	
	public static void animatedNavigationTo(final Class<?> cls,
			final FragmentManager fragmentManager,
			final FragmentContainer container, final boolean addToStack,
			final FragmentTransaction transaction, AnimatorSet animatorSet) {

		if (animatorSet != null) {
			animatorSet.addListener(new AnimatorListener() {

				@Override
				public void onAnimationStart(Animator animation) {
				}

				@Override
				public void onAnimationRepeat(Animator animation) {
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					NavigationUtils.directNavigationTo(cls, fragmentManager,
							container, addToStack, transaction);
				}

				@Override
				public void onAnimationCancel(Animator animation) {
				}
			});
			animatorSet.start();
		}
	}

	public static void directNavigationTo(Class<?> cls, FragmentManager fragmentManager, 
			FragmentContainer container, boolean addToStack, FragmentTransaction transaction) {
		directNavigationTo(cls, fragmentManager, container, addToStack, transaction, null, false);
	}

	public static void directNavigationTo(Class<?> cls, FragmentManager fragmentManager, 
			FragmentContainer container, boolean addToStack, FragmentTransaction transaction, 
			Bundle args) {
		directNavigationTo(cls, fragmentManager, container, addToStack, transaction, args, false);
	}
	
	/**
	 * @param cls
	 * @param fragmentManager
	 * @param container
	 * @param addToStack
	 * @param forceReplace - if the fragment is already added but must be refreshed (new instance of same fragment)
	 * @param transaction
	 * @param args
	 */
	public static void directNavigationTo(Class<?> cls, FragmentManager fragmentManager, 
			FragmentContainer container, boolean addToStack, FragmentTransaction transaction, 
			Bundle args, boolean forceReplace) {
		try {
			Fragment fragment = fragmentManager.findFragmentByTag(cls.getSimpleName());
			if (fragment == null || forceReplace) {
				fragment = (Fragment) cls.newInstance();
			}

			// if fragment is already on the screen, do nothing
			if (!fragment.isAdded() || forceReplace) {
				if (addToStack) {
					transaction.addToBackStack(null);
				}
				if (args != null && !fragment.isAdded()) {
					fragment.setArguments(args);
				}
				transaction.replace(container.getId(), fragment, cls.getSimpleName());
			} else {
				transaction.show(fragment);
			}
			
			transaction.commit();
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
