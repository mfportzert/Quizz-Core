package com.quizz.core.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

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

	// public void insertSection(Section section) {
	//
	// ContentValues sectionValues = new ContentValues();
	// sectionValues.put(DbHelper.COLUMN_NUMBER, section.number);
	// long sectionInsertId =
	// mDbHelper.getWritableDatabase().insert(DbHelper.TABLE_SECTIONS,
	// null, sectionValues);
	//
	// for (Level level : section.levels) {
	// ContentValues levelValues = new ContentValues();
	// levelValues.put(DbHelper.COLUMN_IMAGE, level.imageName);
	// levelValues.put(DbHelper.COLUMN_DESCRIPTION, level.description);
	// // levelValues.put(DbHelper.COLUMN_HINT1, level.firstHint);
	// // levelValues.put(DbHelper.COLUMN_HINT2, level.secondHint);
	// // levelValues.put(DbHelper.COLUMN_HINT3, level.thirdHint);
	// // levelValues.put(DbHelper.COLUMN_HINT4, level.fourthHint);
	// levelValues.put(DbHelper.COLUMN_LINK, level.moreInfosLink);
	// levelValues.put(DbHelper.COLUMN_DIFFICULTY, level.difficulty);
	// levelValues.put(DbHelper.COLUMN_RESPONSE, level.response);
	// levelValues.put(DbHelper.COLUMN_STATUS, Level.STATUS_LEVEL_UNCLEAR);
	// levelValues.put(DbHelper.COLUMN_FK_SECTION, sectionInsertId);
	// mDbHelper.getWritableDatabase().insert(DbHelper.TABLE_LEVELS, null,
	// levelValues);
	//
	//
	// }
	//
	// }
	//
	// public Cursor getSections() {
	//
	// // List<Section> sections = new ArrayList<Section>();
	//
	// String sqlQuery = "SELECT " + DbHelper.TABLE_SECTIONS + "." +
	// DbHelper.COLUMN_ID + ", "
	// + DbHelper.TABLE_SECTIONS + "." + DbHelper.COLUMN_NUMBER + ", "
	// + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_LEVEL + ", "
	// + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_IMAGE + ", "
	// + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_DESCRIPTION + ", "
	// + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_DIFFICULTY + ", "
	// + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_RESPONSE + ", "
	// + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_LINK //+ ", "
	// // + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_HINT1 + ", "
	// // + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_HINT2 + ", "
	// // + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_HINT3 + ", "
	// // + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_HINT4
	// + " FROM " + DbHelper.TABLE_SECTIONS
	// + " LEFT JOIN "
	// + DbHelper.TABLE_LEVELS
	// + " ON " + DbHelper.TABLE_SECTIONS + "." + DbHelper.COLUMN_ID + " = " +
	// DbHelper.TABLE_LEVELS + "."
	// + DbHelper.COLUMN_FK_SECTION;// +
	// // " GROUP BY " + DbHelper.TABLE_SECTIONS + "." + DbHelper.COLUMN_ID;
	//
	// Cursor cursor = mDbHelper.getWritableDatabase().rawQuery(sqlQuery, null);
	// // cursor.moveToFirst();
	// // cursor.close();
	// // int lastId = 0;
	// // Section section;
	// // while (!cursor.isAfterLast()) {
	// // // Log.d("SectionDAO", "dump cursor" +
	// // DatabaseUtils.dumpCursorToString(cursor));
	// // Level level = cursorToLevel(cursor);
	// // if (lastId !=
	// // cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMN_NUMBER))) {
	// // section = cursorToSection(cursor);
	// // sections.add(section);
	// // }
	// // sections.get(sections.size() - 1).levels.add(level);
	// // cursor.moveToNext();
	// // }
	//
	// return cursor;
	// }
	//
	// public Section cursorToSection(Cursor cursor) {
	// Section section = new Section();
	// section.number =
	// cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMN_NUMBER));
	// section.levels = new ArrayList<Level>();
	// return section;
	// }
	//
	// public Level cursorToLevel(Cursor cursor) {
	// Level level = new Level();
	// level.imageName =
	// cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_IMAGE));
	// level.description =
	// cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_DESCRIPTION));
	// level.difficulty =
	// cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_DIFFICULTY));
	// level.response =
	// cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_RESPONSE));
	// level.moreInfosLink =
	// cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_LINK));
	// // level.firstHint =
	// cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_HINT1));
	// // level.secondHint =
	// cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_HINT2));
	// // level.thirdHint =
	// cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_HINT3));
	// // level.fourthHint =
	// cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_HINT4));
	// return level;
	// }

	public void insertSection(Section section) {

		ContentValues sectionValues = new ContentValues();
		sectionValues.put(DbHelper.COLUMN_NUMBER, section.number);
		long sectionInsertId = mDbHelper.getWritableDatabase().insert(
				DbHelper.TABLE_SECTIONS, null, sectionValues);

		for (Level level : section.levels) {
			ContentValues levelValues = new ContentValues();
			levelValues.put(DbHelper.COLUMN_IMAGE, level.imageName);
			levelValues.put(DbHelper.COLUMN_INDICATION, level.indication);
			levelValues.put(DbHelper.COLUMN_LINK, level.moreInfosLink);
			levelValues.put(DbHelper.COLUMN_DIFFICULTY, level.difficulty);
			levelValues.put(DbHelper.COLUMN_RESPONSE, level.response);
			levelValues.put(DbHelper.COLUMN_PARTIAL_RESPONSE,
					level.partialResponse);
			levelValues.put(DbHelper.COLUMN_STATUS, Level.STATUS_LEVEL_UNCLEAR);
			levelValues.put(DbHelper.COLUMN_FK_SECTION, sectionInsertId);
			long levelInsertId = mDbHelper.getWritableDatabase().insert(
					DbHelper.TABLE_LEVELS, null, levelValues);
			Log.d("Quizz-Core - BaseQuizzDAO", "INFO [" + level.imageName
					+ " : " + String.valueOf(level.hints.size()) + "]");
			for (Hint hint : level.hints) {
				ContentValues hintValues = new ContentValues();
				hintValues.put(DbHelper.COLUMN_HINT, hint.hint);
				hintValues.put(DbHelper.COLUMN_HINT_TYPE, hint.type);
				hintValues.put(DbHelper.COLUMN_UNLOCKED,
						Hint.STATUS_HINT_UNREVEALED);
				hintValues.put(DbHelper.COLUMN_FK_LEVEL, levelInsertId);
				mDbHelper.getWritableDatabase().insert(DbHelper.TABLE_HINTS,
						null, hintValues);
			}
		}

	}

	public static Cursor getHintsCursor(Level level) {
		String sqlQuery = "SELECT " + DbHelper.TABLE_HINTS + "."
				+ DbHelper.COLUMN_HINT + ", " + DbHelper.TABLE_HINTS + "."
				+ DbHelper.COLUMN_HINT_TYPE + ", " + DbHelper.TABLE_HINTS + "."
				+ DbHelper.COLUMN_UNLOCKED + " FROM " + DbHelper.TABLE_HINTS
				+ " WHERE " + DbHelper.COLUMN_FK_LEVEL + " = "
				+ String.valueOf(level.id);

		Cursor cursor = QuizzDAO.INSTANCE.mDbHelper.getWritableDatabase()
				.rawQuery(sqlQuery, null);
		return cursor;
	}

	public static List<Hint> getHints(Level level) {
		List<Hint> hints = new ArrayList<Hint>();
		Cursor cursor = getHintsCursor(level);

		cursor.moveToFirst();
		while (!cursor.isLast())
			hints.add(cursorToHint(cursor));
		cursor.close();

		return hints;
	}

	public Cursor getSections() {

		String sqlQuery = "SELECT " + DbHelper.TABLE_SECTIONS + "."
				+ DbHelper.COLUMN_ID + ", " + DbHelper.TABLE_SECTIONS + "."
				+ DbHelper.COLUMN_NUMBER + ", " + DbHelper.TABLE_LEVELS + "."
				+ DbHelper.COLUMN_LEVEL + ", " + DbHelper.TABLE_LEVELS + "."
				+ DbHelper.COLUMN_IMAGE + ", " + DbHelper.TABLE_LEVELS + "."
				+ DbHelper.COLUMN_PARTIAL_RESPONSE + ", "
				+ DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_INDICATION
				+ ", " + DbHelper.TABLE_LEVELS + "."
				+ DbHelper.COLUMN_DIFFICULTY + ", " + DbHelper.TABLE_LEVELS
				+ "." + DbHelper.COLUMN_RESPONSE + ", " + DbHelper.TABLE_LEVELS
				+ "." + DbHelper.COLUMN_LINK + DbHelper.TABLE_LEVELS + "."
				+ DbHelper.COLUMN_STATUS + " FROM " + DbHelper.TABLE_SECTIONS
				+ " LEFT JOIN " + DbHelper.TABLE_LEVELS + " ON "
				+ DbHelper.TABLE_SECTIONS + "." + DbHelper.COLUMN_ID + " = "
				+ DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_FK_SECTION;// +
		// " GROUP BY " + DbHelper.TABLE_SECTIONS + "." + DbHelper.COLUMN_ID;

		Cursor cursor = mDbHelper.getReadableDatabase()
				.rawQuery(sqlQuery, null);
		return cursor;
	}

	public static Hint cursorToHint(Cursor cursor) {
		Hint hint = new Hint();
		hint.hint = cursor.getString(cursor
				.getColumnIndex(DbHelper.COLUMN_HINT));
		hint.type = cursor.getInt(cursor
				.getColumnIndex(DbHelper.COLUMN_HINT_TYPE));
		hint.isUnlocked = (0 == cursor.getInt(cursor
				.getColumnIndex(DbHelper.COLUMN_UNLOCKED)));
		return hint;
	}

	public Level cursorToLevel(Cursor cursor) {
		Level level = new Level();
		level.imageName = cursor.getString(cursor
				.getColumnIndex(DbHelper.COLUMN_IMAGE));
		level.indication = cursor.getString(cursor
				.getColumnIndex(DbHelper.COLUMN_INDICATION));
		level.partialResponse = cursor.getString(cursor
				.getColumnIndex(DbHelper.COLUMN_PARTIAL_RESPONSE));
		level.difficulty = cursor.getString(cursor
				.getColumnIndex(DbHelper.COLUMN_DIFFICULTY));
		level.response = cursor.getString(cursor
				.getColumnIndex(DbHelper.COLUMN_RESPONSE));
		level.moreInfosLink = cursor.getString(cursor
				.getColumnIndex(DbHelper.COLUMN_LINK));
		level.hints = new ArrayList<Hint>();
		level.status = cursor.getInt(cursor
				.getColumnIndex(DbHelper.COLUMN_STATUS));
		return level;
	}

	public Section cursorToSection(Cursor cursor) {
		Section section = new Section();
		section.number = cursor.getInt(cursor
				.getColumnIndex(DbHelper.COLUMN_NUMBER));
		section.levels = new ArrayList<Level>();
		return section;
	}
}
