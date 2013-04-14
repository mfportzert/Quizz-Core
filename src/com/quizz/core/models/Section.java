package com.quizz.core.models;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.quizz.core.db.QuizzDAO;
import com.quizz.core.managers.DataManager;

public class Section implements Parcelable {
	/**
	 * Required cleared percentage to unlock next section
	 */
	public static float SECTION_DEFAULT_UNLOCK_PERC = 0.5f;
	
	public static int SECTION_UNLOCKED = 1;
	public static int SECTION_LOCKED = 0;

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
		this.number = parcel.readInt();
		this.name = parcel.readString();
		this.status = parcel.readInt();
		parcel.readList(this.levels, Section.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
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
	
	/**
	 * @return 0.0f - 1.0f
	 */
	public float getClearedPerc() {
		float clearedCount = 0;
		for (Level level : levels) {
			if (level.status == Level.STATUS_LEVEL_CLEAR) {
				clearedCount++;
			}
		}
		return clearedCount / (float) levels.size();
	}
	
	public boolean isLast() {
		return DataManager.isLastSection(this);
	}
	
	public Section getNext() {
		return DataManager.getNextSection(this);
	}
}
