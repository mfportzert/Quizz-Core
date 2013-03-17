package com.quizz.core.managers;

import java.util.List;

import com.quizz.core.db.QuizzDAO;
import com.quizz.core.models.Section;

/**
 * @author M-F.P
 *
 * Manages data access and cache
 *
 */
public class DataManager {

	private static List<Section> mCacheSections = null;

	public static synchronized List<Section> getSections() {
		if (mCacheSections == null) {
			mCacheSections = QuizzDAO.INSTANCE.getSections();
		}
		return mCacheSections;
	}

	public static synchronized Section getSection(int id) {
		List<Section> sections = getSections();
		for (Section section : sections) {
			if (section.id == id) {
				return section;
			}
		}
		return null;
	}
}
