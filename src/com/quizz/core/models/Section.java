package com.quizz.core.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.quizz.core.db.QuizzDAO;
import com.quizz.core.managers.DataManager;

public class Section implements Parcelable {
	/**
	 * Required cleared percentage to unlock next section
	 */
//	public static float SECTION_DEFAULT_UNLOCK_PERC = 0.5f;
	
	public static int SECTION_UNLOCKED = 1;
	public static int SECTION_LOCKED = 0;

	// TODO: Mettre les valeurs dans arrays.xml
	private static List<Integer> sectionsSteps = 
			new ArrayList<Integer>(Arrays.asList(0, 7, 16, 26, 36, 48, 60, 76, 96));
	
	public static final Parcelable.Creator<Section> CREATOR = new Parcelable.Creator<Section>() {

		public Section createFromParcel(Parcel parcel) {
			return new Section(parcel);
		}

		public Section[] newArray(int size) {
			return new Section[size];
		}
	};
	
	public int id;
	
	@SerializedName("section")
	public int number;

	public String ref;
	
	@SerializedName("name")
	public String name;

	public int status = SECTION_LOCKED;
	
	@SerializedName("levels")
	public ArrayList<Level> levels = new ArrayList<Level>();

	public Section() {
		
	}

	/**
	 * @param parcel
	 */
	public Section(Parcel parcel) {
		this.id = parcel.readInt();
		this.ref = parcel.readString();
		this.number = parcel.readInt();
		this.name = parcel.readString();
		this.status = parcel.readInt();
		parcel.readList(this.levels, Section.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.ref);
		dest.writeInt(this.number);
		dest.writeString(this.name);
		dest.writeInt(this.status);
		dest.writeList(this.levels);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public void update() {
		QuizzDAO.INSTANCE.updateSection(this);
	}
	
	public boolean isComplete() {
		for (Level level : levels) {
			if (level.status == Level.STATUS_LEVEL_UNCLEAR) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * return number of cleared level left required to unlock the section
	 * 
	 * @return number of levels left to clear or null
	 */
	public int remainingClearedLevelCount() {
		int remaining = this.requiredClearedLevelCount() - DataManager.getClearedLevelTotalCount();
		if (remaining < 0)
			remaining = -1;
		return remaining;
	}
	
	/*
	 * return number of cleared level required to unlock the section
	 * 
	 * @return number of levels to clear or null
	 */
	public int requiredClearedLevelCount() {
		if (this.number < sectionsSteps.size())
			return sectionsSteps.get(this.number - 1);
		return -1;
	}
	
	public boolean isUnlockRequirementsReached() {
		Log.d("NUMBER", "this.number : " + String.valueOf(this.number) + " < " + String.valueOf(sectionsSteps.size()));
		if (this.number < sectionsSteps.size() &&
				DataManager.getClearedLevelTotalCount() >= sectionsSteps.get(this.number - 1))
			return true;
		return false;
	}
	
	public boolean isLast() {
		return DataManager.isLastSection(this);
	}
	
	public boolean isFirst() {
		return DataManager.isFirstSection(this);
	}
	
	public Section getNext() {
		return DataManager.getNextSection(this);
	}
}
