package com.quizz.core.fragments;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quizz.core.models.Section;

public abstract class BaseListSectionsFragment extends Fragment {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadSections();
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
	
	private void loadSections() {
		Gson gson = new Gson();
		Type type = new TypeToken<Collection<Section>>(){}.getType();
		InputStream is;
		try {
			is = getResources().getAssets().open("quizz.json");
			Reader reader = new InputStreamReader(is);
			ArrayList<Section> sections = gson.fromJson(reader, type);
	        onSectionsLoaded(sections);
		} catch (IOException e) {
			// TODO: Make a reload button for errors
			e.printStackTrace();
		}
	}
	
	protected abstract void onSectionsLoaded(ArrayList<Section> listSections);
}
