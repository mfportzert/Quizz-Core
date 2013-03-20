package com.quizz.core.models;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.quizz.core.db.QuizzDAO;

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

	@SerializedName("id")
	public int id;

	@SerializedName("response")
	public String response;

	@SerializedName("partial_response")
	public String partialResponse;

	@SerializedName("indication")
	public String indication;

	@SerializedName("link")
	public String moreInfosLink;

	@SerializedName("image")
	public String imageName;

	@SerializedName("difficulty")
	public String difficulty;

	@SerializedName("hints")
	public ArrayList<Hint> hints;

	public int status;

	public int sectionOwner;

	public Level() {

	}

	/**
	 * @param parcel
	 */
	public Level(Parcel parcel) {
		this.id = parcel.readInt();
		this.response = parcel.readString();
		this.partialResponse = parcel.readString();
		this.indication = parcel.readString();
		this.moreInfosLink = parcel.readString();
		this.imageName = parcel.readString();
		this.difficulty = parcel.readString();
		parcel.readList(this.hints, Level.class.getClassLoader());
		this.status = parcel.readInt();
		this.sectionOwner = parcel.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.response);
		dest.writeString(this.partialResponse);
		dest.writeString(this.indication);
		dest.writeString(this.moreInfosLink);
		dest.writeString(this.imageName);
		dest.writeString(this.difficulty);
		dest.writeList(this.hints);
		dest.writeInt(this.status);
		dest.writeInt(this.sectionOwner);
	}

	public ArrayList<Hint> getHintsFromDb() {
		if (this.hints == null) {
			this.hints = (ArrayList<Hint>) QuizzDAO.INSTANCE.getHints(this);
		}
		return this.hints;
	}
	
	public void update() {
		QuizzDAO.INSTANCE.updateLevel(this);
	}

}
