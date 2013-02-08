package com.quizz.core.models;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Level implements Parcelable {

	public static int STATUS_LEVEL_CLEAR = 1;
	public static int STATUS_LEVEL_UNCLEAR = 0;

	public static String LEVEL_EASY = "easy";
	public static String LEVEL_MEDIUM = "medium";
	public static String LEVEL_HARD = "hard";

	public static int HINT_USED = 1;
	public static int HINT_UNUSED = 0;

	public static final Parcelable.Creator<Level> CREATOR = new Parcelable.Creator<Level>() {

		public Level createFromParcel(Parcel parcel) {
			return new Level(parcel);
		}

		public Level[] newArray(int size) {
			return new Level[size];
		}
	};

	public int id;

	@SerializedName("response")
	public String response;

	@SerializedName("partial_response")
	public String partial_response;

	@SerializedName("description")
	public String description;

	@SerializedName("link")
	public String moreInfosLink;

	@SerializedName("image")
	public String imageName;

	@SerializedName("difficulty")
	public String difficulty;

	@SerializedName("hints")
	public ArrayList<Hint> hints = new ArrayList<Hint>();

	public Level() {

	}

	/**
	 * @param parcel
	 */
	public Level(Parcel parcel) {
		this.response = parcel.readString();
		this.description = parcel.readString();
		this.imageName = parcel.readString();
		this.difficulty = parcel.readString();
		parcel.readList(this.hints, Level.class.getClassLoader());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.response);
		dest.writeString(this.description);
		dest.writeString(this.imageName);
		dest.writeString(this.difficulty);
		dest.writeList(this.hints);
	}

	// public Level getHintsFromDb() {
	// this.hints = (ArrayList<Hint>) QuizzDAO.getHints(this);
	// return this;
	// }

}
