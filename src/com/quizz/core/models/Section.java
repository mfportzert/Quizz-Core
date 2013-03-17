package com.quizz.core.models;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Section implements Parcelable {

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
	
	@SerializedName("section")
	public int id;

	@SerializedName("name")
	public String name;

	public int status = SECTION_LOCKED;
	
	@SerializedName("levels")
	public ArrayList<Level> levels = new ArrayList<Level>();

	
	/**
	 * @param parcel
	 */
	public Section(Parcel parcel) {
		this.id = parcel.readInt();
		this.name = parcel.readString();
		this.status = parcel.readInt();
		parcel.readList(this.levels, Section.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.name);
		dest.writeInt(this.status);
		dest.writeList(this.levels);
	}

	public Section() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int describeContents() {
		return 0;
	}

}
