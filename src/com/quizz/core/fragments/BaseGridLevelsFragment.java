package com.quizz.core.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;

import com.quizz.core.models.Level;
import com.quizz.core.models.Section;

public class BaseGridLevelsFragment extends Fragment {

    public static final String ARG_SECTION = "BaseGridLevelsFragment.ARG_SECTION";

    protected ArrayAdapter<Level> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	Section section = getArguments().getParcelable(ARG_SECTION);
	fillAdapter(section);
    }

    private void fillAdapter(Section section) {
	mAdapter.clear();
	for (Level level : section.levels) {
	    mAdapter.add(level);
	}
	mAdapter.notifyDataSetChanged();
    }
}
