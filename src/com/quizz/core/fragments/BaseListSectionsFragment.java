package com.quizz.core.fragments;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ArrayAdapter;

import com.quizz.core.activities.BaseQuizzActivity;
import com.quizz.core.db.DbHelper;
import com.quizz.core.db.QuizzDAO;
import com.quizz.core.interfaces.SectionsLoaderListener;
import com.quizz.core.managers.DataManager;
import com.quizz.core.models.Level;
import com.quizz.core.models.Section;

public class BaseListSectionsFragment extends Fragment {

	protected View mLoadingView;
	protected ArrayAdapter<Section> mAdapter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (getActivity() instanceof BaseQuizzActivity) {
			((BaseQuizzActivity) getActivity())
					.setHideAbOnRotationChange(false);
		}
		
		super.onActivityCreated(savedInstanceState);
	}
}
