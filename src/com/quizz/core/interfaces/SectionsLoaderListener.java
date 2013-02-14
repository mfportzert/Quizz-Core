package com.quizz.core.interfaces;

import java.util.List;

import com.quizz.core.models.Section;

public interface SectionsLoaderListener {
	public void onSectionsLoading();

	public void onSectionsLoaded(List<Section> listSections);
}
