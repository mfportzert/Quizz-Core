package com.quizz.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "quizz.db";

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

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sectionsTable = getCreateSectionsTableQuery();
		String levelsTable = getCreateLevelsTableQuery();
		String hintsTable = getCreateHintsTableQuery();
		db.execSQL(sectionsTable);
		db.execSQL(levelsTable);
		db.execSQL(hintsTable);
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
