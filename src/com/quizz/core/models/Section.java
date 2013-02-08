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

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.number);
		dest.writeString(this.name);
		dest.writeList(this.levels);
	}

	@SerializedName("section")
	public int number;

	@SerializedName("name")
	public String name;

	@SerializedName("levels")
	public ArrayList<Level> levels = new ArrayList<Level>();

	/**
	 * @param parcel
	 */
	public Section(Parcel parcel) {
		this.number = parcel.readInt();
		this.name = parcel.readString();
		parcel.readList(this.levels, Section.class.getClassLoader());
	}

	public Section() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int describeContents() {
		return 0;
	}

}
