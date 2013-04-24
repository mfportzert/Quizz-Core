package com.quizz.core.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// TABLES
	public static final String TABLE_SECTIONS = "sections";
	public static final String TABLE_LEVELS = "levels";
	public static final String TABLE_HINTS = "hints";

	// COLUMNS
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_UNLOCKED = "unlocked";
	public static final String COLUMN_NUMBER = "number";
	public static final String COLUMN_LEVEL = "level";
	public static final String COLUMN_IMAGE = "image";
	public static final String COLUMN_INDICATION = "indication";
	public static final String COLUMN_DIFFICULTY = "difficulty";
	public static final String COLUMN_LINK = "link";
	public static final String COLUMN_HINT = "hint";
	public static final String COLUMN_HINT_TYPE = "hint_type";
	public static final String COLUMN_RESPONSE = "response";
	public static final String COLUMN_STATUS = "status";
	public static final String COLUMN_PARTIAL_RESPONSE = "partial_response";
	public static final String COLUMN_FK_SECTION = "fk_section_id";
	public static final String COLUMN_FK_LEVEL = "fk_level_id";

	private String mDatabasesPath;
	private String mDatabaseName;
	private String mUserDataDatabaseName;
	private Context mContext;
	
	public DbHelper(Context context) {
		super(context, 
				context.getPackageName().substring(
						context.getPackageName().lastIndexOf('.') + 1) + ".db",
				null,
				DATABASE_VERSION);
		mContext = context;
		mDatabasesPath = "/data/data/" + context.getPackageName() + "/databases";
		mDatabaseName = context.getPackageName().substring(
				context.getPackageName().lastIndexOf('.') + 1) + ".db";
		
	}
	
	private String getAppNameFormPackageName() {
		return mContext.getPackageName().substring(mContext.getPackageName().lastIndexOf('.') + 1);
	}
	
	public void createDataBases() throws IOException {

		if (this.dataBaseExists(null))
			return;
		// By calling this method with empty db, it will create one in the default system path
		// of the app so I'm able to overwrite that db with our db.
		this.getReadableDatabase();
		try {
			boolean copyGameDataOnly;
			if (this.dataBaseExists(getAppNameFormPackageName() + "_userdata.db"))
				copyGameDataOnly = true;
			else
				copyGameDataOnly = false;
			this.copyDataBases(copyGameDataOnly);
		} catch (IOException e) {
			throw new Error("Error copying database");
		}

	}

	private boolean dataBaseExists(String dbName) {

		if (dbName == null || dbName.length() == 0)
			dbName = mDatabaseName;
		
		boolean databaseExists = false;		
		try {
			String myPath = mDatabasesPath + dbName;
			SQLiteDatabase checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
			if (checkDB != null) {
				databaseExists = true;
				checkDB.close();
			}
		} catch (SQLiteException e) {
			databaseExists = false;
		}
		return databaseExists;
	}

	private void copyDataBases(boolean copyDataAndProgressDb) throws IOException {

		byte[] buffer = new byte[1024];
		int length;
		String assetDbFileName;
		String packageName = mContext.getPackageName();
		
		if (Locale.getDefault().getLanguage().equalsIgnoreCase("fr"))
			assetDbFileName = packageName.substring(packageName.lastIndexOf('.') + 1) + "_fr.db";
		else
			assetDbFileName = packageName.substring(packageName.lastIndexOf('.') + 1) + "_en.db";
		
		InputStream myInput = mContext.getAssets().open("databases/" + assetDbFileName);
		String outFileName = mDatabasesPath + mDatabaseName;
		OutputStream myOutput = new FileOutputStream(outFileName);

		while ((length = myInput.read(buffer)) > 0)
			myOutput.write(buffer, 0, length);

		myOutput.flush();
		myOutput.close();
		myInput.close();
		
		if (copyDataAndProgressDb)
		{
			myInput = mContext.getAssets().open("databases/" + packageName + "_userdata.db");
			outFileName = mDatabasesPath + mUserDataDatabaseName;
			myOutput = new FileOutputStream(outFileName);

			while ((length = myInput.read(buffer)) > 0)
				myOutput.write(buffer, 0, length);			
		}

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		 String sectionsTable = getCreateSectionsTableQuery();
		 String levelsTable = getCreateLevelsTableQuery();
		 String hintsTable = getCreateHintsTableQuery();
		
		 db.beginTransaction();
		 try {
			 db.execSQL(sectionsTable);
			 db.execSQL(levelsTable);
			 db.execSQL(hintsTable);
			 db.setTransactionSuccessful();
		 } finally {
			 db.endTransaction();
		 }
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECTIONS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEVELS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HINTS);
		onCreate(db);
	}

	private String getCreateSectionsTableQuery() {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder
				.append("CREATE TABLE IF NOT EXISTS " + TABLE_SECTIONS + " (");
		sqlBuilder.append(COLUMN_ID + " INTEGER PRIMARY KEY, ");
		sqlBuilder.append(COLUMN_NUMBER + " INTEGER, ");
		sqlBuilder.append(COLUMN_UNLOCKED + " INTEGER");
		sqlBuilder.append(");");
		String sql = sqlBuilder.toString();

		return sql;
	}

	private String getCreateLevelsTableQuery() {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("CREATE TABLE IF NOT EXISTS " + TABLE_LEVELS + " (");
		sqlBuilder.append(COLUMN_ID + " INTEGER PRIMARY KEY, ");
		sqlBuilder.append(COLUMN_LEVEL + " TEXT, ");
		sqlBuilder.append(COLUMN_IMAGE + " TEXT, ");
		sqlBuilder.append(COLUMN_INDICATION + " TEXT, ");
		sqlBuilder.append(COLUMN_DIFFICULTY + " TEXT, ");
		sqlBuilder.append(COLUMN_LINK + " TEXT, ");
		sqlBuilder.append(COLUMN_RESPONSE + " TEXT, ");
		sqlBuilder.append(COLUMN_PARTIAL_RESPONSE + " TEXT, ");
		sqlBuilder.append(COLUMN_STATUS + " INTEGER, ");
		sqlBuilder.append(COLUMN_FK_SECTION + " INTEGER");
		sqlBuilder.append(");");
		String sql = sqlBuilder.toString();

		return sql;
	}

	private String getCreateHintsTableQuery() {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("CREATE TABLE IF NOT EXISTS " + TABLE_HINTS + " (");
		sqlBuilder.append(COLUMN_ID + " INTEGER PRIMARY KEY, ");
		sqlBuilder.append(COLUMN_HINT + " TEXT, ");
		sqlBuilder.append(COLUMN_HINT_TYPE + " INTEGER, ");
		sqlBuilder.append(COLUMN_UNLOCKED + " INTEGER, ");
		sqlBuilder.append(COLUMN_FK_LEVEL + " INTEGER");
		sqlBuilder.append(");");
		String sql = sqlBuilder.toString();

		return sql;
	}

}
