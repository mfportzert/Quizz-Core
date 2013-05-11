package com.quizz.core.managers;

import java.util.List;

import android.util.Log;

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
	
	public static int getClearedLevelTotalCount() {
		int levelClearedCount = 0;
		List<Section> sections = getSections();
		for (Section section : sections) {
			for (Level level : section.levels) {
				if (level.status == Level.STATUS_LEVEL_CLEAR)
					levelClearedCount++;
			}
		}
		return levelClearedCount;
	}

	public static Level getNextLevel(Level currentLevel) {
		return getNextLevel(currentLevel, true);
	}

	public static Level getPreviousLevel(Level currentLevel) {
		return getPreviousLevel(currentLevel, true);
	}

	/**
	 * @param currentLevel
	 * @param circular. If circular is true, returns first level when currentLevel is last
	 * @return null if not found or last level
	 */
	public static Level getNextLevel(Level currentLevel, boolean circular) {
		if (currentLevel != null) {
			Section section = getSection(currentLevel.sectionId);
			if (section != null && section.levels != null) {
				// Get index of current level
				int levelIndex = section.levels.indexOf(currentLevel);
				if (levelIndex == -1) {
					return null;
				}
				
				// If index was found and is < section's levels size
				if (levelIndex >= 0 && levelIndex < (section.levels.size() - 1)) {
					return section.levels.get(levelIndex + 1);
				} else if (circular && levelIndex == (section.levels.size() - 1)) {
					return section.levels.get(0);
				}
			}
		}
		return null;
	}

	/**
	 * @param currentLevel
	 * @param circular. If circular is true, returns last level when currentLevel is first
	 * @return null if not found or last level
	 */
	public static Level getPreviousLevel(Level currentLevel, boolean circular) {
		if (currentLevel != null) {
			Section section = getSection(currentLevel.sectionId);
			if (section != null && section.levels != null) {
				// Get index of current level
				int levelIndex = section.levels.indexOf(currentLevel);
				if (levelIndex == -1) {
					return null;
				}
				
				// If index was found and is < section's levels size
				if (levelIndex > 0) {
					return section.levels.get(levelIndex - 1);
				} else if (circular && levelIndex == 0) {
					return section.levels.get(section.levels.size() - 1);
				}
			}
		}
		return null;
	}
	
	/**
	 * @param currentLevel
	 * @return next opened level or null if not found
	 */
	public static Level getNextOpenedLevelInSection(Level currentLevel) {
		if (currentLevel != null) {
			Section section = getSection(currentLevel.sectionId);
			if (section != null && section.levels != null && section.levels.size() > 1) {
				// Get index of current level
				int levelIndex = section.levels.indexOf(currentLevel);
				// If index was found and is < section's levels size
				if (levelIndex > -1) {
					int savedIndex = levelIndex;
					if (levelIndex == (section.levels.size() - 1)) {
						levelIndex = 0;
					} else {
						levelIndex++;
					}
					
					// we'll make a loop and search for the first open level until we
					// get back to the currentLevel
					while (levelIndex != savedIndex) {
						Level tmpLevel = section.levels.get(levelIndex);
						if (tmpLevel.status == Level.STATUS_LEVEL_UNCLEAR) {
							return tmpLevel;
						}
						if (levelIndex == (section.levels.size() - 1)) {
							levelIndex = 0;
						} else {
							levelIndex++;
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * @param currentLevel
	 * @return
	 */
	public static Level getFirstOpenedLevelInSection(Section section) {
		if (section.levels != null) {
			// Get index of current level
			for (Level level : section.levels) {
				if (level.status == Level.STATUS_LEVEL_UNCLEAR) {
					return level;
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
	public static boolean isFirstSection(Section currentSection) {
		List<Section> sections = getSections();
		return (sections.indexOf(currentSection) == 0);
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
	 * @return id of section if one was unlocked or -1 otherwise
	 */
	public static int unlockNextSectionIfNecessary() {
		for (Section section : getSections()) {
			if (section.status == Section.SECTION_LOCKED
					&& section.isUnlockRequirementsReached()) {			
				section.status = Section.SECTION_UNLOCKED;
				section.update();
				return section.number;
			}
		}
		
		return -1;
	}
}
