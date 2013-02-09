package com.quizz.core.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.internal.nineoldandroids.animation.Animator;
import com.actionbarsherlock.internal.nineoldandroids.animation.Animator.AnimatorListener;
import com.actionbarsherlock.internal.nineoldandroids.animation.AnimatorSet;
import com.quizz.core.interfaces.FragmentContainer;

public class NavigationUtils {

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

    public static void directNavigationTo(Class<?> cls,
	    FragmentManager fragmentManager, FragmentContainer container,
	    boolean addToStack, FragmentTransaction transaction) {
	directNavigationTo(cls, fragmentManager, container, addToStack,
		transaction, null);
    }

    public static void directNavigationTo(Class<?> cls,
	    FragmentManager fragmentManager, FragmentContainer container,
	    boolean addToStack, FragmentTransaction transaction, Bundle args) {
	try {
	    Fragment fragment = fragmentManager.findFragmentByTag(cls
		    .getSimpleName());
	    if (fragment == null) {
		fragment = (Fragment) cls.newInstance();
	    }

	    // if fragment is already on the screen, do nothing
	    if (!fragment.isAdded()) {
		if (addToStack) {
		    transaction.addToBackStack(null);
		}
		if (args != null) {
		    fragment.setArguments(args);
		}
		transaction.replace(container.getId(), fragment,
			cls.getSimpleName());
		transaction.commit();
	    }

	} catch (InstantiationException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	}
    }
}
