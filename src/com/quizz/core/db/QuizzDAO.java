package com.quizz.core.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.quizz.core.models.Hint;
import com.quizz.core.models.Level;
import com.quizz.core.models.Section;

public enum QuizzDAO {
	INSTANCE;

	private DbHelper mDbHelper;

	public void setDbHelper(DbHelper dbHelper) {
		mDbHelper = dbHelper;
	}

	public synchronized DbHelper getDbHelper() {
		return mDbHelper;
	}

	public void insertSection(Section section) {

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			ContentValues sectionValues = new ContentValues();
			sectionValues.put(DbHelper.COLUMN_NUMBER, section.number);
			sectionValues.put(DbHelper.COLUMN_UNLOCKED, section.status);
			long sectionInsertId = db.insert(DbHelper.TABLE_SECTIONS, null, sectionValues);

			for (Level level : section.levels) {
				ContentValues levelValues = new ContentValues();
				levelValues.put(DbHelper.COLUMN_IMAGE, level.imageName);
				levelValues.put(DbHelper.COLUMN_INDICATION, level.indication);
				levelValues.put(DbHelper.COLUMN_LINK, level.moreInfosLink);
				levelValues.put(DbHelper.COLUMN_DIFFICULTY, level.difficulty);
				levelValues.put(DbHelper.COLUMN_RESPONSE, level.response);
				levelValues.put(DbHelper.COLUMN_PARTIAL_RESPONSE, level.partialResponse);
				levelValues.put(DbHelper.COLUMN_STATUS, Level.STATUS_LEVEL_UNCLEAR);
				levelValues.put(DbHelper.COLUMN_FK_SECTION, sectionInsertId);
				long levelInsertId = db.insert(DbHelper.TABLE_LEVELS, null, levelValues);
				
				for (Hint hint : level.getHints()) {
					ContentValues hintValues = new ContentValues();
					hintValues.put(DbHelper.COLUMN_HINT, hint.hint);
					hintValues.put(DbHelper.COLUMN_HINT_TYPE, hint.type);
					hintValues.put(DbHelper.COLUMN_UNLOCKED, Hint.STATUS_HINT_UNREVEALED);
					hintValues.put(DbHelper.COLUMN_FK_LEVEL, levelInsertId);
					db.insert(DbHelper.TABLE_HINTS, null, hintValues);
				}
			}
			db.setTransactionSuccessful();// marks a commit
		} finally {
			db.endTransaction();
		}
	}

	public Cursor getHintsCursor(Level level) {
		String sqlQuery = "SELECT "
				+ DbHelper.TABLE_HINTS + "." + DbHelper.COLUMN_ID + ", "
				+ DbHelper.TABLE_HINTS + "." + DbHelper.COLUMN_HINT + ", "
				+ DbHelper.TABLE_HINTS + "." + DbHelper.COLUMN_HINT_TYPE + ", "
				+ DbHelper.TABLE_HINTS + "." + DbHelper.COLUMN_UNLOCKED
				+ " FROM " + DbHelper.TABLE_HINTS
				+ " WHERE " + DbHelper.COLUMN_FK_LEVEL + " = " + level.id;

		Cursor cursor = mDbHelper.getReadableDatabase().rawQuery(sqlQuery, null);
		return cursor;
	}

	public List<Hint> getHints(Level level) {
		List<Hint> hints = new ArrayList<Hint>();
		Cursor cursor = getHintsCursor(level);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			hints.add(cursorToHint(cursor));
			cursor.moveToNext();
		}
		cursor.close();

		return hints;
	}

	public List<Section> getSections() {
		String sqlQuery = "SELECT "
					+ DbHelper.TABLE_SECTIONS + "." + DbHelper.COLUMN_ID + " AS section_id, "
					+ DbHelper.TABLE_SECTIONS + "." + DbHelper.COLUMN_NUMBER + ", "
					+ DbHelper.TABLE_SECTIONS + "." + DbHelper.COLUMN_UNLOCKED + " AS section_lock_status, "
					+ DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_ID + " AS level_id, "
					+ DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_LEVEL + ", "
					+ DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_IMAGE + ", "
					+ DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_PARTIAL_RESPONSE + ", "
					+ DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_INDICATION + ", "
					+ DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_DIFFICULTY + ", "
					+ DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_RESPONSE + ", "
					+ DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_LINK + ", "
					+ DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_FK_SECTION + ", "
					+ DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_STATUS
				+ " FROM " + DbHelper.TABLE_SECTIONS
				+ " LEFT JOIN " + DbHelper.TABLE_LEVELS
					+ " ON "
						+ DbHelper.TABLE_SECTIONS + "." + DbHelper.COLUMN_ID
					+ " = "
						+ DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_FK_SECTION;

		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sqlQuery, null);
		return cursorToSections(cursor);
	}

	private static Hint cursorToHint(Cursor cursor) {
		Hint hint = new Hint();
		hint.id = cursor.getInt(cursor
				.getColumnIndex(DbHelper.COLUMN_ID));
		hint.hint = cursor.getString(cursor
				.getColumnIndex(DbHelper.COLUMN_HINT));
		hint.type = cursor.getInt(cursor
				.getColumnIndex(DbHelper.COLUMN_HINT_TYPE));
		hint.isUnlocked = (0 == cursor.getInt(cursor
				.getColumnIndex(DbHelper.COLUMN_UNLOCKED)));
		return hint;
	}

	private Level cursorToLevel(Cursor cursor) {
		Level level = new Level();
		level.id = cursor.getInt(cursor.getColumnIndex("level_id"));
		level.imageName = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_IMAGE));
		level.indication = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_INDICATION));
		level.partialResponse = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_PARTIAL_RESPONSE));
		level.difficulty = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_DIFFICULTY));
		level.response = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_RESPONSE));
		level.moreInfosLink = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_LINK));
		level.status = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMN_STATUS));
		return level;
	}

	private Section cursorToSection(Cursor cursor) {
		Section section = new Section();
		section.id = cursor.getInt(cursor.getColumnIndex("section_id"));
		section.number = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMN_NUMBER));
		section.status = cursor.getInt(cursor.getColumnIndex("section_lock_status"));
		return section;
	}
	
	private List<Section> cursorToSections(Cursor cursor) {
		ArrayList<Section> sections = new ArrayList<Section>();
		int lastId = 0;
		Section section = null;
		List<Level> levels = new ArrayList<Level>();

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int column = cursor.getColumnIndex("section_id");
			if (cursor.getInt(column) != lastId && lastId != 0) {
				if (section != null) {
					section.levels.addAll(levels);
					sections.add(section);
				}
				levels.clear();
			}
			section = QuizzDAO.INSTANCE.cursorToSection(cursor);
			Level tmpLevel = QuizzDAO.INSTANCE.cursorToLevel(cursor);
			tmpLevel.sectionId = section.id;
			levels.add(tmpLevel);
			lastId = cursor.getInt(cursor
					.getColumnIndex("section_id"));
			if (cursor.isLast()) {
				for (Level level : levels) {
					section.levels.add(level);
				}
				sections.add(section);
			}
			cursor.moveToNext();
		}
		cursor.close();
		return sections;
	}

	public void updateSection(Section section) {
		ContentValues sectionValues = new ContentValues();
		sectionValues.put(DbHelper.COLUMN_UNLOCKED, section.status);
		mDbHelper.getWritableDatabase().update(
				DbHelper.TABLE_SECTIONS, sectionValues, 
				DbHelper.COLUMN_ID+"="+String.valueOf(section.id), null);
	}
	
	public void updateLevel(Level level) {
		ContentValues cv = new ContentValues();
		cv.put(DbHelper.COLUMN_STATUS, level.status);
		mDbHelper.getWritableDatabase().update(
				DbHelper.TABLE_LEVELS, cv, 
				DbHelper.COLUMN_ID+"="+String.valueOf(level.id), null);
	}
	
	public void updateHint(Hint hint) {
		ContentValues cv = new ContentValues();
		cv.put(DbHelper.COLUMN_UNLOCKED, 
				hint.isUnlocked ? Hint.STATUS_HINT_REVEALED : Hint.STATUS_HINT_UNREVEALED);
		mDbHelper.getWritableDatabase().update(
				DbHelper.TABLE_HINTS, cv, 
				DbHelper.COLUMN_ID+"="+String.valueOf(hint.id), null);
	}
}
