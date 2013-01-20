package com.quizz.core.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.quizz.core.activities.BaseQuizzActivity;
import com.quizz.core.application.BaseQuizzApplication;
import com.quizz.core.db.BaseQuizzDAO;
import com.quizz.core.db.DbHelper;
import com.quizz.core.models.Level;
import com.quizz.core.models.Section;

public abstract class BaseListSectionsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	if (getActivity() instanceof BaseQuizzActivity) {
	    ((BaseQuizzActivity) getActivity()).setHideAbOnRotationChange(false);
	}
	super.onActivityCreated(savedInstanceState);
	Context appContext = getActivity().getApplicationContext();
	DbHelper dbHelper = ((BaseQuizzApplication) appContext).getDbHelper();
	new LoadSectionsTask(dbHelper).execute();
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

    protected abstract void onSectionsLoading();

    protected abstract void onSectionsLoaded(List<Section> listSections);

    /**
     * Load sections from database
     * 
     */
    public class LoadSectionsTask extends AsyncTask<Void, Integer, List<Section>> {

	private BaseQuizzDAO mBaseQuizzDAO;

	public LoadSectionsTask(DbHelper dbHelper) {
	    mBaseQuizzDAO = new BaseQuizzDAO(dbHelper);
	}

	@Override
	protected void onPreExecute() {
	    onSectionsLoading();
	    super.onPreExecute();
	}

	@Override
	protected List<Section> doInBackground(Void... arg0) {
	    ArrayList<Section> sections = new ArrayList<Section>();
	    Cursor sectionsCursor = mBaseQuizzDAO.getSections();
	    int lastId = 0;
	    Section section = null;
	    List<Level> levels = new ArrayList<Level>();

	    sectionsCursor.moveToFirst();
	    while (!sectionsCursor.isAfterLast()) {
		int column = sectionsCursor.getColumnIndex(DbHelper.COLUMN_ID);
		if (sectionsCursor.getInt(column) != lastId && lastId != 0) {
		    if (section != null) {
			section.levels.addAll(levels);
			sections.add(section);
		    }
		    levels.clear();
		}
		section = mBaseQuizzDAO.cursorToSection(sectionsCursor);
		levels.add(mBaseQuizzDAO.cursorToLevel(sectionsCursor));
		lastId = sectionsCursor.getInt(sectionsCursor.getColumnIndex(DbHelper.COLUMN_ID));
		if (sectionsCursor.isLast()) {
		    section.levels.addAll(levels);
		    sections.add(section);
		}
		sectionsCursor.moveToNext();
	    }
	    sectionsCursor.close();
	    return sections;
	}

	@Override
	protected void onPostExecute(List<Section> result) {
	    onSectionsLoaded(result);
	    super.onPostExecute(result);
	}
    }
}
