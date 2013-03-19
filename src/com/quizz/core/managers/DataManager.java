package com.quizz.core.managers;

import java.util.List;

import com.quizz.core.db.QuizzDAO;
import com.quizz.core.models.Level;
import com.quizz.core.models.Section;

/**
 * @author M-F.P
 *
 * Manages data access and cache
 *
 */
public class DataManager {

	private static List<Section> mCacheSections = null;

	public static List<Section> getSections() {
		if (mCacheSections == null) {
			mCacheSections = QuizzDAO.INSTANCE.getSections();
		}
		return mCacheSections;
	}

	public static Section getSection(int id) {
		List<Section> sections = getSections();
		for (Section section : sections) {
			if (section.id == id) {
				return section;
			}
		}
		return null;
	}
	
	public static boolean isLastLevel(Section section, Level level) {
		return (getNextLevel(level) == null) ? true : false;
	}
	
	public static Level getNextLevel(Level level) {
		return null;
	}
}
