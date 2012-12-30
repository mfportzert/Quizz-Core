package com.quizz.core.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.quizz.core.models.Section;

public abstract class BaseListSectionsFragment extends Fragment {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // TODO: Load Sections here!
        onSectionsLoaded(null);
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
	
	protected abstract void onSectionsLoaded(ArrayList<Section> listSections);
}
