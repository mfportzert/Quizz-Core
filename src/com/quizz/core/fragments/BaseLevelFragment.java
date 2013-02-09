package com.quizz.core.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class BaseLevelFragment extends Fragment {

    public static final String ARG_LEVEL = "BaseLevelFragment.ARG_LEVEL";

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
	super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
	super.onPause();
    }

    @Override
    public void onResume() {
	super.onResume();
    }
}
