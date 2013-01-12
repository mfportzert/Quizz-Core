package com.quizz.core.db;

import java.util.ArrayList;
import java.util.List;

import com.quizz.core.application.BaseQuizzApplication;
import com.quizz.core.models.Level;
import com.quizz.core.models.Section;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
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
//    public static final String TABLE_HINTS = "hints";
//    public static final String TABLE_RESPONSES = "responses";
    
    // COLUMNS
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NUMBER = "number";    
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DIFFICULTY = "difficulty";
    public static final String COLUMN_LINK = "link";
//    public static final String COLUMN_HINT = "hint";
    public static final String COLUMN_HINT1 = "hint1";
    public static final String COLUMN_HINT2 = "hint2";
    public static final String COLUMN_HINT3 = "hint3";
    public static final String COLUMN_HINT4 = "hint4";
    public static final String COLUMN_RESPONSE = "response";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_FK_SECTION = "fk_section_id";
    public static final String COLUMN_FK_LEVEL = "fk_level_id";
    

    private SQLiteDatabase database;
    
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sectionsTable = getCreateSectionsTableQuery();
        String levelsTable = getCreateLevelsTableQuery();
//        String hintsTable = getCreateHintsTableQuery();
//        String responsesTable = getCreateResponsesTableQuery();
        db.execSQL(sectionsTable);
        db.execSQL(levelsTable);
//        db.execSQL(hintsTable);
//        db.execSQL(responsesTable);
    }

	// Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECTIONS); 
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEVELS); 
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HINTS); 
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPONSES);
        onCreate(db);
    }

	public SQLiteDatabase open() throws SQLException {
		if (database == null || !database.isOpen())
			database = this.getWritableDatabase();
		return database;
	}

	public boolean isOpen() {
		return database.isOpen();
	}
    
	public void close() {
		database.close();
	}
	
    private String getCreateSectionsTableQuery() {
    	StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("CREATE TABLE IF NOT EXISTS " + TABLE_SECTIONS + " (");
		sqlBuilder.append(COLUMN_ID + " INTEGER PRIMARY KEY, ");
		sqlBuilder.append(COLUMN_NUMBER + " INTEGER");
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
		sqlBuilder.append(COLUMN_DESCRIPTION + " TEXT, ");
		sqlBuilder.append(COLUMN_HINT1 + " TEXT, ");
		sqlBuilder.append(COLUMN_HINT2 + " TEXT, ");
		sqlBuilder.append(COLUMN_HINT3 + " TEXT, ");
		sqlBuilder.append(COLUMN_HINT4 + " TEXT, ");
		sqlBuilder.append(COLUMN_DIFFICULTY + " TEXT, ");
		sqlBuilder.append(COLUMN_LINK + " TEXT, ");
		sqlBuilder.append(COLUMN_RESPONSE + " TEXT, ");
		sqlBuilder.append(COLUMN_STATUS + " INTEGER, ");
		sqlBuilder.append(COLUMN_FK_SECTION + " INTEGER");
		sqlBuilder.append(");");
		String sql = sqlBuilder.toString();
    	
    	return sql;
    }


    
//    private String getCreateHintsTableQuery() {
//    	StringBuilder sqlBuilder = new StringBuilder();
//		sqlBuilder.append("CREATE TABLE IF NOT EXISTS " + TABLE_HINTS + " (");
//		sqlBuilder.append(COLUMN_ID + " INTEGER PRIMARY KEY, ");
//		sqlBuilder.append(COLUMN_HINT + " TEXT, ");
//		sqlBuilder.append(COLUMN_FK_LEVEL + " INTEGER");
//		sqlBuilder.append(");");
//		String sql = sqlBuilder.toString();
//    	
//    	return sql;
//    }
    
//    private String getCreateResponsesTableQuery() {
//    	StringBuilder sqlBuilder = new StringBuilder();
//		sqlBuilder.append("CREATE TABLE IF NOT EXISTS " + TABLE_RESPONSES + " (");
//		sqlBuilder.append(COLUMN_ID + " INTEGER PRIMARY KEY, ");
//		sqlBuilder.append(COLUMN_RESPONSE + " TEXT, ");
//		sqlBuilder.append(COLUMN_FK_LEVEL + " INTEGER");
//		sqlBuilder.append(");");
//		String sql = sqlBuilder.toString();
//    	
//    	return sql;
//	}
    
}
