package com.quizz.core.fragments;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quizz.core.models.Level;
import com.quizz.core.models.Section;

public abstract class BaseGridLevelsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	loadLevels();
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

    private void loadLevels() {
	Gson gson = new Gson();
	Type type = new TypeToken<Collection<Section>>() {
	}.getType();
	InputStream is;
	/*
	 * try { is = getResources().getAssets().open("quizz.json"); Reader
	 * reader = new InputStreamReader(is); ArrayList<Level> levels =
	 * gson.fromJson(reader, type); } catch (IOException e) { // TODO: Make
	 * a reload button for errors e.printStackTrace(); }
	 */
	ArrayList<Level> levels = new ArrayList<Level>();
	for (int i = 0; i < 30; i++) {
	    Level level = new Level();
	    level.imageName = "big_ben.jpg";
	    levels.add(level);
	}
	onLevelsLoaded(levels);
    }

    protected abstract void onLevelsLoaded(ArrayList<Level> listLevels);
}
