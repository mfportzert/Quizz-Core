package com.quizz.core.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Hint implements Parcelable {

    public static int STATUS_HINT_UNREVEALED = 0;
    public static int STATUS_HINT_REVEALED = 1;

    public static int HINT_TYPE_LECTURE = 0;
    public static int HINT_TYPE_MAP = 1;
    public static int HINT_TYPE_LETTER = 2;

    private int id;

    @SerializedName("hint")
    public String hint;

    @SerializedName("type")
    public int type;

    public boolean isUnlocked = false;

    public static final Parcelable.Creator<Stat> CREATOR = new Parcelable.Creator<Stat>() {

	public Stat createFromParcel(Parcel parcel) {
	    return new Stat(parcel);
	}

	public Stat[] newArray(int size) {
	    return new Stat[size];
	}
    };

    /**
     * @param parcel
     */
    public Hint(Parcel parcel) {
	this.hint = parcel.readString();
	this.type = parcel.readInt();
	this.isUnlocked = parcel.readInt() != 0;

    }

    public Hint(String hint, int type) {
	this.hint = hint;
	this.type = type;
    }

    public Hint() {
	// TODO Auto-generated constructor stub
    }

    @Override
    public int describeContents() {
	return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
	dest.writeString(this.hint);
	dest.writeInt(this.type);
	dest.writeInt(this.isUnlocked ? 1 : 0);
    }
}
