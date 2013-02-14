package com.quizz.core.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ArrayAdapter;

import com.quizz.core.activities.BaseQuizzActivity;
import com.quizz.core.application.BaseQuizzApplication;
import com.quizz.core.db.QuizzDAO;
import com.quizz.core.db.DbHelper;
import com.quizz.core.interfaces.SectionsLoaderListener;
import com.quizz.core.models.Level;
import com.quizz.core.models.Section;

public class BaseListSectionsFragment extends Fragment implements
		SectionsLoaderListener {

	protected View mLoadingView;
	protected ArrayAdapter<Section> mAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (getActivity() instanceof BaseQuizzActivity) {
			((BaseQuizzActivity) getActivity())
					.setHideAbOnRotationChange(false);
		}
		super.onActivityCreated(savedInstanceState);
		new LoadSectionsTask().execute();
	}

	@Override
	public void onSectionsLoading() {
		if (mLoadingView != null) {
			mLoadingView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onSectionsLoaded(List<Section> listSections) {
		if (mAdapter != null) {
			mAdapter.clear();
			for (Section section : listSections) {
				section.name = "Level " + section.number;
				mAdapter.add(section);
			}
			mAdapter.notifyDataSetChanged();

			if (mLoadingView != null) {
				mLoadingView.setVisibility(View.GONE);
			}
		}
	}

	// ===========================================================
	// Inner classes
	// ===========================================================

	/**
	 * Load sections from database
	 * 
	 */
	public class LoadSectionsTask extends
			AsyncTask<Void, Integer, List<Section>> {

		@Override
		protected void onPreExecute() {
			onSectionsLoading();
			super.onPreExecute();
		}

		@Override
		protected List<Section> doInBackground(Void... arg0) {
			ArrayList<Section> sections = new ArrayList<Section>();
			Cursor sectionsCursor = QuizzDAO.INSTANCE.getSections();
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
				section = QuizzDAO.INSTANCE.cursorToSection(sectionsCursor);
				levels.add(QuizzDAO.INSTANCE.cursorToLevel(sectionsCursor));
				lastId = sectionsCursor.getInt(sectionsCursor
						.getColumnIndex(DbHelper.COLUMN_ID));
				if (sectionsCursor.isLast()) {
					for (Level level : levels) {
						section.levels.add(level);
					}
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
