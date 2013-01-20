package com.quizz.core.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;

import com.quizz.core.models.Level;
import com.quizz.core.models.Section;

public class BaseQuizzDAO {

    private DbHelper mDbHelper;

    public BaseQuizzDAO(DbHelper dbHelper) {
	mDbHelper = dbHelper;
    }

    public void insertSection(Section section) {

	ContentValues sectionValues = new ContentValues();
	sectionValues.put(DbHelper.COLUMN_NUMBER, section.number);
	long sectionInsertId = mDbHelper.getWritableDatabase().insert(DbHelper.TABLE_SECTIONS,
		null, sectionValues);

	for (Level level : section.levels) {
	    ContentValues levelValues = new ContentValues();
	    levelValues.put(DbHelper.COLUMN_IMAGE, level.imageName);
	    levelValues.put(DbHelper.COLUMN_DESCRIPTION, level.description);
	    levelValues.put(DbHelper.COLUMN_HINT1, level.firstHint);
	    levelValues.put(DbHelper.COLUMN_HINT2, level.secondHint);
	    levelValues.put(DbHelper.COLUMN_HINT3, level.thirdHint);
	    levelValues.put(DbHelper.COLUMN_HINT4, level.fourthHint);
	    levelValues.put(DbHelper.COLUMN_LINK, level.moreInfosLink);
	    levelValues.put(DbHelper.COLUMN_DIFFICULTY, level.difficulty);
	    levelValues.put(DbHelper.COLUMN_RESPONSE, level.response);
	    levelValues.put(DbHelper.COLUMN_STATUS, Level.STATUS_LEVEL_UNCLEAR);
	    levelValues.put(DbHelper.COLUMN_FK_SECTION, sectionInsertId);
	    mDbHelper.getWritableDatabase().insert(DbHelper.TABLE_LEVELS, null, levelValues);

	    // String[] responses = level.response.split("|");
	    // for (String response : responses) {
	    // ContentValues responseValues = new ContentValues();
	    // responseValues.put(DbHelper.COLUMN_RESPONSE, response);
	    // responseValues.put(DbHelper.COLUMN_FK_LEVEL, levelInsertId);
	    // BaseQuizzApplication.db().insert(DbHelper.TABLE_RESPONSES, null,
	    // responseValues);
	    // }
	}

    }

    public Cursor getSections() {

	// List<Section> sections = new ArrayList<Section>();

	String sqlQuery = "SELECT " + DbHelper.TABLE_SECTIONS + "." + DbHelper.COLUMN_ID + ", "
		+ DbHelper.TABLE_SECTIONS + "." + DbHelper.COLUMN_NUMBER + ", "
		+ DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_LEVEL + ", "
		+ DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_IMAGE + ", "
		+ DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_DESCRIPTION + ", "
		+ DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_DIFFICULTY + ", "
		+ DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_RESPONSE + ", "
		+ DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_LINK + ", " + DbHelper.TABLE_LEVELS
		+ "." + DbHelper.COLUMN_HINT1 + ", " + DbHelper.TABLE_LEVELS + "."
		+ DbHelper.COLUMN_HINT2 + ", " + DbHelper.TABLE_LEVELS + "."
		+ DbHelper.COLUMN_HINT3 + ", " + DbHelper.TABLE_LEVELS + "."
		+ DbHelper.COLUMN_HINT4 + " FROM " + DbHelper.TABLE_SECTIONS + " LEFT JOIN "
		+ DbHelper.TABLE_LEVELS + " ON " + DbHelper.TABLE_SECTIONS + "."
		+ DbHelper.COLUMN_ID + " = " + DbHelper.TABLE_LEVELS + "."
		+ DbHelper.COLUMN_FK_SECTION;// +
	// " GROUP BY " + DbHelper.TABLE_SECTIONS + "." + DbHelper.COLUMN_ID;

	Cursor cursor = mDbHelper.getWritableDatabase().rawQuery(sqlQuery, null);
	// cursor.moveToFirst();
	// cursor.close();
	// int lastId = 0;
	// Section section;
	// while (!cursor.isAfterLast()) {
	// // Log.d("SectionDAO", "dump cursor" +
	// DatabaseUtils.dumpCursorToString(cursor));
	// Level level = cursorToLevel(cursor);
	// if (lastId !=
	// cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMN_NUMBER))) {
	// section = cursorToSection(cursor);
	// sections.add(section);
	// }
	// sections.get(sections.size() - 1).levels.add(level);
	// cursor.moveToNext();
	// }

	return cursor;
    }

    public Level cursorToLevel(Cursor cursor) {
	Level level = new Level();
	level.imageName = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_IMAGE));
	level.description = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_DESCRIPTION));
	level.difficulty = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_DIFFICULTY));
	level.response = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_RESPONSE));
	level.moreInfosLink = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_LINK));
	level.firstHint = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_HINT1));
	level.secondHint = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_HINT2));
	level.thirdHint = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_HINT3));
	level.fourthHint = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_HINT4));
	return level;
    }

    public Section cursorToSection(Cursor cursor) {
	Section section = new Section();
	section.number = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMN_NUMBER));
	section.levels = new ArrayList<Level>();
	return section;
    }

}
