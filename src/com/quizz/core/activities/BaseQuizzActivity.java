package com.quizz.core.activities;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quizz.core.R;
import com.quizz.core.application.BaseQuizzApplication;
import com.quizz.core.db.BaseQuizzDAO;
import com.quizz.core.db.DbHelper;
import com.quizz.core.dialogs.ConfirmQuitDialog;
import com.quizz.core.dialogs.ConfirmQuitDialog.Closeable;
import com.quizz.core.interfaces.FragmentContainer;
import com.quizz.core.models.Level;
import com.quizz.core.models.Section;
import com.quizz.core.widgets.QuizzActionBar;

public class BaseQuizzActivity extends SherlockFragmentActivity implements FragmentContainer,
	Closeable {

    private static final String HIDE_AB_ON_ROTATION_CHANGE = "BaseQuizzActivity.HIDE_AB_ON_ROTATION_CHANGE";
    private static final String PREF_VERSION_KEY = "VERSION";
    private static final int PREF_VERSION_VALUE = 1;

    private View mQuizzLayout;
    private View mConfirmQuitDialogView;
    private ImageView mBackgroundAnimatedImage;

    private QuizzActionBar mQuizzActionBar;
    private ConfirmQuitDialog mConfirmQuitDialog;

    private boolean mHideAbOnRotation = false;

    ViewSwitcher viewSwitcher;

    private TextView mTvProgress;
    private ProgressBar mPbProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	super.onCreate(savedInstanceState);

	viewSwitcher = new ViewSwitcher(BaseQuizzActivity.this);
	viewSwitcher.addView(ViewSwitcher.inflate(BaseQuizzActivity.this, R.layout.loading_screen,
		null));
	viewSwitcher.addView(ViewSwitcher.inflate(BaseQuizzActivity.this, R.layout.activity_quizz,
		null));
	setContentView(viewSwitcher);
	buildLoadingLayout();
	buildGameLayout(savedInstanceState);

	SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
	if (!sharedPreferences.contains(PREF_VERSION_KEY)) {
	    DbHelper dbHelper = ((BaseQuizzApplication) getApplicationContext()).getDbHelper();
	    new FirstLaunchTask(dbHelper).execute();
	} else if (sharedPreferences.getInt(PREF_VERSION_KEY, 0) < PREF_VERSION_VALUE) {
	    // need to upgrade db
	}
    }

    private void buildLoadingLayout() {
	mTvProgress = (TextView) viewSwitcher.findViewById(R.id.tv_progress);
	mPbProgressBar = (ProgressBar) viewSwitcher.findViewById(R.id.pb_progressbar);
	mPbProgressBar.setMax(100);
    }

    private void buildGameLayout(Bundle savedInstanceState) {
	mQuizzLayout = findViewById(R.id.quizzLayout);
	mBackgroundAnimatedImage = (ImageView) findViewById(R.id.backgroundAnimatedImage);
	mQuizzActionBar = (QuizzActionBar) findViewById(R.id.quizzTopActionBar);

	View shadowView = viewSwitcher.findViewById(R.id.ab_separator_shadow);
	mQuizzActionBar.setShadowView(shadowView);

	if (savedInstanceState != null) {
	    mHideAbOnRotation = savedInstanceState.getBoolean(HIDE_AB_ON_ROTATION_CHANGE);
	    if (mHideAbOnRotation) {
		mQuizzActionBar.hide(QuizzActionBar.MOVE_DIRECT);
	    }
	}

	// FIXME: May not be displayed correctly on bigger screen when looping
	// (bad transition)
	// TODO: Make an image with beginning left similar to right end
	// TODO: Scroll the horizontalScrollView instead of translating the
	// imageView
	HorizontalScrollView bgAnimatedImageContainer = (HorizontalScrollView) viewSwitcher
		.findViewById(R.id.backgroundAnimatedImageContainer);
	bgAnimatedImageContainer.setOnTouchListener(new OnTouchListener() {

	    @Override
	    public boolean onTouch(View v, MotionEvent event) {
		return true;
	    }
	});

    }

    @Override
    public void close() {
	finish();
    }

    @Override
    public int getId() {
	return R.id.fragmentsContainer;
    }

    @Override
    public void onBackPressed() {
	if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
	    if (mConfirmQuitDialog == null) {
		mConfirmQuitDialog = (mConfirmQuitDialogView == null) ? new ConfirmQuitDialog(this)
			: new ConfirmQuitDialog(this, mConfirmQuitDialogView);
		mConfirmQuitDialog.setClosable(this);
	    }
	    mConfirmQuitDialog.show();
	} else {
	    super.onBackPressed();
	}
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
	outState.putBoolean(HIDE_AB_ON_ROTATION_CHANGE, mHideAbOnRotation);
	super.onSaveInstanceState(outState);
    }

    protected void setConfirmQuitDialogView(View view) {
	mConfirmQuitDialogView = view;
    }

    public View getQuizzLayout() {
	return mQuizzLayout;
    }

    public ImageView getBackgroundAnimatedImage() {
	return mBackgroundAnimatedImage;
    }

    public QuizzActionBar getQuizzActionBar() {
	return mQuizzActionBar;
    }

    /**
     * Default behaviour is 'false'
     * 
     * @param hide
     */
    public void setHideAbOnRotationChange(boolean hide) {
	mHideAbOnRotation = hide;
    }

    /**
     * First launch asyncTask<br />
     * Initiates database and fill it with json file content
     * 
     */
    public class FirstLaunchTask extends AsyncTask<Void, Integer, Void> {
	private int mProgress = 0;
	private BaseQuizzDAO mBaseQuizzDAO;

	public FirstLaunchTask(DbHelper dbHelper) {
	    mBaseQuizzDAO = new BaseQuizzDAO(dbHelper);
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
	    if (progress[0] <= 100) {
		mTvProgress.setText(Integer.toString(progress[0]) + "%");
		mPbProgressBar.setProgress(progress[0]);
	    }
	}

	@Override
	protected void onPostExecute(Void result) {
	    viewSwitcher.showNext();
	}

	@Override
	protected Void doInBackground(Void... arg0) {
	    // need to create db
	    SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
	    Editor editor = sharedPreferences.edit();
	    editor.putInt(PREF_VERSION_KEY, PREF_VERSION_VALUE);
	    editor.commit();

	    Gson gson = new Gson();
	    Type type = new TypeToken<Collection<Section>>() {
	    }.getType();
	    InputStream is;
	    try {
		is = getResources().getAssets().open("places.xml");
		Reader reader = new InputStreamReader(is);
		List<Section> sections = gson.fromJson(reader, type);
		int ratio = (sections.size() > 0) ? 100 / sections.size() : 100;
		for (Section section : sections) {
		    mBaseQuizzDAO.insertSection(section);
		    publishProgress(++mProgress * ratio);
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    return null;
	}
    }
}
