package com.quizz.core.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

import com.quizz.core.application.BaseQuizzApplication;
import com.quizz.core.models.Level;
import com.quizz.core.models.Section;
import com.quizz.core.models.Stat;


public class BaseQuizzDAO {

	public BaseQuizzDAO() {}
	
	public void insertSection(Section section) {

		ContentValues sectionValues = new ContentValues();
	    sectionValues.put(DbHelper.COLUMN_NUMBER, section.number);
	    long sectionInsertId = BaseQuizzApplication.db().insert(DbHelper.TABLE_SECTIONS, null, sectionValues);

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
		    BaseQuizzApplication.db().insert(DbHelper.TABLE_LEVELS, null, levelValues);

//	    	String[] responses = level.response.split("|");
//	    	for (String response : responses) {
//	    	    ContentValues responseValues = new ContentValues();
//	    	    responseValues.put(DbHelper.COLUMN_RESPONSE, response);
//			    responseValues.put(DbHelper.COLUMN_FK_LEVEL, levelInsertId);
//	    	    BaseQuizzApplication.db().insert(DbHelper.TABLE_RESPONSES, null, responseValues);
//	    	}
	    }

	}

    public Cursor getSections() {

//		List<Section> sections = new ArrayList<Section>();

		String sqlQuery = "SELECT " + 
				DbHelper.TABLE_SECTIONS + "." + DbHelper.COLUMN_ID + ", " +
				DbHelper.TABLE_SECTIONS + "." + DbHelper.COLUMN_NUMBER + ", " +
				DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_LEVEL + ", " +
				DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_IMAGE + ", " +
				DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_DESCRIPTION + ", " +
				DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_DIFFICULTY + ", " +
				DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_RESPONSE + ", " +
				DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_LINK + ", " +
				DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_HINT1 + ", " +
				DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_HINT2 + ", " +
				DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_HINT3 + ", " +
				DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_HINT4 +
				" FROM " + DbHelper.TABLE_SECTIONS + 
				" LEFT JOIN " + DbHelper.TABLE_LEVELS +
				" ON " + DbHelper.TABLE_SECTIONS + "." + DbHelper.COLUMN_ID + " = " +
				DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_FK_SECTION;// +
//				" GROUP BY " + DbHelper.TABLE_SECTIONS + "." + DbHelper.COLUMN_ID;
		
		Cursor cursor = BaseQuizzApplication.db().rawQuery(sqlQuery, null);
//		cursor.moveToFirst();
//	    cursor.close();
//		int lastId = 0;
//		Section section;
//		while (!cursor.isAfterLast()) {
////		    Log.d("SectionDAO", "dump cursor" + DatabaseUtils.dumpCursorToString(cursor));
//			Level level = cursorToLevel(cursor);
//			if (lastId != cursor.getInt(cursor.getColumnIndex(DbHelper.COLUMN_NUMBER))) {
//				section = cursorToSection(cursor);
//				sections.add(section);
//			}
//			sections.get(sections.size() - 1).levels.add(level);
//			cursor.moveToNext();
//	    }

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
	
	public List<Stat> getStats() {
		
		String sqlQuery =
			"SELECT" + 
				" (SELECT" +
					" COUNT(" + DbHelper.TABLE_SECTIONS + "." + DbHelper.COLUMN_ID + ")" +
					" FROM " + DbHelper.TABLE_SECTIONS +
					" WHERE " + DbHelper.TABLE_SECTIONS + "." + DbHelper.COLUMN_UNLOCKED + " = " + Section.SECTION_UNLOCKED +
				") AS sections_unlocked," +
				" COUNT(" + DbHelper.TABLE_SECTIONS + "." + DbHelper.COLUMN_ID + ") AS sections_total," +
				" (SELECT" + 
					" COUNT(" + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_ID + ")" +
					" FROM " + DbHelper.TABLE_LEVELS + " " +
					" WHERE " + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_STATUS + " = " + Level.STATUS_LEVEL_CLEAR +
				") AS levels_clear," +
			" COUNT(" + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_ID + ") AS levels_total," +
			" (SELECT" + 
				" COUNT(" + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_ID + ")" +
				" FROM " + DbHelper.TABLE_LEVELS +
				" WHERE " + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_STATUS + " = " + Level.STATUS_LEVEL_CLEAR +
					" AND " + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_DIFFICULTY + " = \"" + Level.LEVEL_EASY + "\"" +
			") AS levels_easy_clear," +
			" (SELECT" +
				" COUNT(" + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_ID + ")" +
				" FROM " + DbHelper.TABLE_LEVELS + 
				" WHERE " + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_DIFFICULTY + " = \"" + Level.LEVEL_EASY + "\"" +
			") AS levels_easy_total," +
			" (SELECT" + 
				" COUNT(" + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_ID + ")" +
				" FROM " + DbHelper.TABLE_LEVELS +
				" WHERE " + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_STATUS + " = " + Level.STATUS_LEVEL_CLEAR +
					" AND " + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_DIFFICULTY + " = \"" + Level.LEVEL_MEDIUM + "\"" +
			") AS levels_medium_clear," +
			" (SELECT" +
				" COUNT(" + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_ID + ")" +
				" FROM " + DbHelper.TABLE_LEVELS + 
				" WHERE " + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_DIFFICULTY + " = \"" + Level.LEVEL_MEDIUM + "\"" +
			") AS levels_medium_total," +
			" (SELECT" + 
				" COUNT(" + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_ID + ")" +
				" FROM " + DbHelper.TABLE_LEVELS +
				" WHERE " + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_STATUS + " = " + Level.STATUS_LEVEL_CLEAR +
					" AND " + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_DIFFICULTY + " = \"" + Level.LEVEL_HARD + "\"" +
			") AS levels_hard_clear," +
			" (SELECT" +
				" COUNT(" + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_ID + ")" +
				" FROM " + DbHelper.TABLE_LEVELS + 
				" WHERE " + DbHelper.TABLE_LEVELS + "." + DbHelper.COLUMN_DIFFICULTY + " = \"" + Level.LEVEL_HARD + "\"" +
			") AS levels_hard_total" + 
			" FROM " + DbHelper.TABLE_SECTIONS +
			" LEFT JOIN " + DbHelper.TABLE_LEVELS;
		
		Log.d("BaseQuizzDAO", sqlQuery);
		
		Cursor cursor = BaseQuizzApplication.db().rawQuery(sqlQuery, null);
		Log.d("BaseQuizzDAO", DatabaseUtils.dumpCursorToString(cursor));
		return this.cursorToStat(cursor);
	}
	
	public List<Stat> cursorToStat(Cursor cursor) {
	    
		ArrayList<Stat> stats = new ArrayList<Stat>();
		
		cursor.moveToFirst();
		stats.add(new Stat(
						"Niveaux débloqués",
						cursor.getInt(cursor.getColumnIndex("sections_unlocked")),
						cursor.getInt(cursor.getColumnIndex("sections_total"))
						)
				 );
		stats.add(new Stat(
				"Images trouvées",
				cursor.getInt(cursor.getColumnIndex("levels_clear")),
				cursor.getInt(cursor.getColumnIndex("levels_total"))
				)
		 );
		stats.add(new Stat(
				"Trouvées en facile",
				cursor.getInt(cursor.getColumnIndex("levels_easy_clear")),
				cursor.getInt(cursor.getColumnIndex("levels_easy_total"))
				)
		 );
		stats.add(new Stat(
				"Trouvées en moyen",
				cursor.getInt(cursor.getColumnIndex("levels_medium_clear")),
				cursor.getInt(cursor.getColumnIndex("levels_medium_total"))
				)
		 );
		stats.add(new Stat(
				"Trouvées en difficile",
				cursor.getInt(cursor.getColumnIndex("levels_hard_clear")),
				cursor.getInt(cursor.getColumnIndex("levels_hard_total"))
				)
		 );
		cursor.close();
		
		Log.d("test", String.valueOf(stats.size()));
		
	    return stats;
	}

}
