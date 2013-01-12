package com.quizz.core.models;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public class Level implements Parcelable {
	
	public static int STATUS_LEVEL_CLEAR = 1;
	public static int STATUS_LEVEL_UNCLEAR = 0;
	
	public static final Parcelable.Creator<Level> CREATOR = new Parcelable.Creator<Level>() {
		
		public Level createFromParcel(Parcel parcel) {
			return new Level(parcel);
        }
		
        public Level[] newArray(int size) {
        	return new Level[size];
        }
	};
	
	@SerializedName("hint1")
	public String firstHint;
	
	@SerializedName("hint2")
	public String secondHint;
	
	@SerializedName("hint3")
	public String thirdHint;
	
	@SerializedName("hint4")
	public String fourthHint;
	
	@SerializedName("responses")
	public String response;
	
	@SerializedName("description")
	public String description;
	
	@SerializedName("link")
	public String moreInfosLink;
	
	@SerializedName("image")
	public String imageName;
	
	@SerializedName("difficulty")
	public String difficulty;
	
	public Level() {
		
	}
	
	/**
	 * @param parcel
	 */
	public Level(Parcel parcel) {
		this.firstHint = parcel.readString();
		this.secondHint = parcel.readString();
		this.thirdHint = parcel.readString();
		this.fourthHint = parcel.readString();
		this.response = parcel.readString();
		this.description = parcel.readString();
		this.imageName = parcel.readString();
		this.difficulty = parcel.readString();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.firstHint);
		dest.writeString(this.secondHint);
		dest.writeString(this.thirdHint);
		dest.writeString(this.fourthHint);
		dest.writeString(this.response);
		dest.writeString(this.description);
		dest.writeString(this.imageName);
		dest.writeString(this.difficulty);
	}
}
