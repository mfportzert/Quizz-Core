package com.quizz.core.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;

import com.quizz.core.models.Level;
import com.quizz.core.models.Section;

public abstract class BaseGridLevelsFragment extends Fragment {

    public static final String ARG_SECTION = "BaseGridLevelsFragment.ARG_SECTION";
    
    protected ArrayAdapter<Level> mAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	Section section = getArguments().getParcelable(ARG_SECTION);
	fillAdapter(section);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
    }
    
    private void fillAdapter(Section section) {
	mAdapter.clear();
	for (Level level : section.levels) {
	    mAdapter.add(level);
	}
	mAdapter.notifyDataSetChanged();
    }

    protected abstract void initAdapter(ArrayAdapter<Level> adapter);
}
