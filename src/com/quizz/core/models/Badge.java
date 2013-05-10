package com.quizz.core.models;


public class Badge {
	
	public final static int STATUS_LOCKED = 0;
	public final static int STATUS_UNLOCKED = 1;
	
	public int requiredLevelProgression;
	public int label;
	public int icon;
	public int status = STATUS_UNLOCKED;
	
	public Badge(int label, int icon, int requiredLevel) {
		this.requiredLevelProgression = requiredLevel;
		this.icon = icon;
		this.label = label;
	}
	
	public Badge() {
		
	}
}
