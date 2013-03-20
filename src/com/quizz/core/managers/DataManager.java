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

	public static synchronized void setSections(List<Section> sections) {
		mCacheSections = sections;
	}

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
	
	public static boolean isLastLevel(Level level) {
		return (getNextLevel(level) == null) ? true : false;
	}
	
	public static Level getNextLevel(Level currentLevel) {
		Section section = getSection(currentLevel.sectionId);
		if (section != null && section.levels != null) {
			for (Level level : section.levels) {
				// Compare reference, mCacheSections must be the only data access source
				if (level == currentLevel) {
					// If not the last level of the section, get next
					int levelIndex = section.levels.indexOf(level);
					if (levelIndex < (section.levels.size() - 1)) {
						return section.levels.get(levelIndex + 1);
					} else {
						return null;
					}
				}
			}
		}
		return null;
	}
}
