package com.quizz.core.models;

import android.app.Activity;

import com.quizz.core.managers.DataManager;
import com.quizz.core.utils.PreferencesUtils;


public class Badge {
	
	public int requiredLevelProgression;
	public int label;
	public int icon;
	
	public Badge(int label, int icon, int requiredLevel) {
		this.requiredLevelProgression = requiredLevel;
		this.icon = icon;
		this.label = label;
	}
	
	public Badge() {
		
	}
}
