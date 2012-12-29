package com.quizz.core.utils;

import android.view.View;

import com.actionbarsherlock.internal.nineoldandroids.animation.AnimatorSet;
import com.actionbarsherlock.internal.nineoldandroids.animation.ObjectAnimator;
import com.actionbarsherlock.internal.nineoldandroids.animation.PropertyValuesHolder;

public class AnimatorUtils {
	
	public static void bounceAnimator(ObjectAnimator baseMovement, float[] movementValues, 
			int bounceDistance, int bounceSpeed) {
		
		float startBouncePosition = movementValues[movementValues.length - 1];
		float endBouncePosition = startBouncePosition + bounceDistance;
		if (movementValues.length > 1) {
			float beforeLastMovementPos = movementValues[movementValues.length - 2];
			if (beforeLastMovementPos < startBouncePosition) {
				endBouncePosition = startBouncePosition - bounceDistance;
			}
		}
		
		ObjectAnimator bounceStart = ObjectAnimator.ofFloat(baseMovement.getTarget(),
				baseMovement.getPropertyName(), startBouncePosition, endBouncePosition);
		bounceStart.setDuration(100);

		ObjectAnimator bounceEnd = ObjectAnimator.ofFloat(baseMovement.getTarget(),
				baseMovement.getPropertyName(), endBouncePosition, startBouncePosition);
		bounceEnd.setDuration(100);

		AnimatorSet signAnimatorSet = new AnimatorSet();
		signAnimatorSet.playSequentially(baseMovement, bounceStart, bounceEnd);
		signAnimatorSet.start();
	}
}