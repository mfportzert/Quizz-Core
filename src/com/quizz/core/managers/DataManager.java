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
	public static boolean dataLoaded = false;

	public static synchronized void setSections(List<Section> sections) {
		mCacheSections = sections;
	}

	public static synchronized List<Section> getSections() {
		if (mCacheSections == null) {
			mCacheSections = QuizzDAO.INSTANCE.getSections();
		}
		return mCacheSections;
	}
	
	public static synchronized void resetGame() {
		if (mCacheSections != null) {
			List<Section> sections = getSections();
			for (Section section : sections) {
				if (section.id != 1) {
					section.status = Section.SECTION_LOCKED;
				}
				for (Level level : section.levels) {
					level.status = Level.STATUS_LEVEL_UNCLEAR;
				}
			}
			mCacheSections = sections;
		}
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
	
	/**
	 * @param level
	 * @return null if level is null, if section is not found or 
	 * if section's levels list is empty. Otherwise, true if last, false if not.
	 */
	public static Boolean isLastLevel(Level level) {
		if (level != null) {
			Section section = getSection(level.sectionId);
			if (section != null && section.levels != null && section.levels.size() > 0) {
				if (section.levels.indexOf(level) == (section.levels.size() - 1)) {
					return true;
				}
				return false;
			}
		}
		return null;
	}
	
	/**
	 * @param currentLevel
	 * @return null if not found or last level
	 */
	public static Level getNextLevel(Level currentLevel) {
		if (currentLevel != null) {
			Section section = getSection(currentLevel.sectionId);
			if (section != null && section.levels != null) {
				// Get index of current level
				int levelIndex = section.levels.indexOf(currentLevel);
				// If index was found and is < section's levels size
				if (levelIndex > -1 && levelIndex < (section.levels.size() - 1)) {
					return section.levels.get(levelIndex + 1);
				}
			}
		}
		return null;
	}

	/**
	 * @param level
	 * @return null if level is null, if section is not found or 
	 * if section's levels list is empty. Otherwise, true if last, false if not.
	 */
	public static Boolean isLastOpenedLevelInSection(Level level) {
		if (level != null) {
			Section section = getSection(level.sectionId);
			if (section != null && section.levels != null && section.levels.size() > 0) {
				// TODO
			}
		}
		return null;
	}
	
	/**
	 * @param currentLevel
	 * @return
	 */
	public static Level getNextOpenedLevelInSection(Level currentLevel) {
		if (currentLevel != null) {
			Section section = getSection(currentLevel.sectionId);
			if (section != null && section.levels != null) {
				// Get index of current level
				int levelIndex = section.levels.indexOf(currentLevel);
				// If index was found and is < section's levels size
				if (levelIndex > -1) {
					// TODO
				}
			}
		}
		return null;
	}
	
	/**
	 * @param currentSection
	 * @return null if not found or last section
	 */
	public static Section getNextSection(Section currentSection) {
		List<Section> sections = getSections();
		// Get index of current section
		int sectionIndex = sections.indexOf(currentSection);
		// If index was found and is < sections size
		if (sectionIndex > -1 && sectionIndex < (sections.size() - 1)) {
			return sections.get(sectionIndex + 1);
		}
		return null;
	}
	
	/**
	 * @param currentSection
	 * @return
	 */
	public static boolean isLastSection(Section currentSection) {
		List<Section> sections = getSections();
		return (sections.indexOf(currentSection) == sections.size() - 1);
	}
	
	/**
	 * @param sectionId
	 * @return true if nextSection has been unlocked, false if 
	 * 		   already unlocked or if currentSection is last
	 */
	public static boolean unlockNextSectionIfNecessary(int sectionId) {
		Section currentSection = getSection(sectionId);
		if (!currentSection.isLast() && 
				currentSection.getClearedPerc() >= Section.SECTION_DEFAULT_UNLOCK_PERC) {
			Section nextSection = currentSection.getNext();
			if (nextSection.status == Section.SECTION_LOCKED) {
				nextSection.status = Section.SECTION_UNLOCKED;
				nextSection.update();
				return true;
			}
		}
		return false;
	}
}
