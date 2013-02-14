package com.quizz.core.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Stat implements Parcelable {

	private String label;
	private int done;
	private int total;
	private int progressInPercent;
	private int icon;
	private boolean isAchievement = false;

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
	public Stat(Parcel parcel) {

		// this.number = parcel.readInt();
		// this.name = parcel.readString();
		// parcel.readList(this.levels, Stat.class.getClassLoader());
	}

	public Stat(int icon, String label, int done, int total,
			boolean isAchievement) {
		this.setIcon(icon);
		this.setLabel(label);
		this.setDone(done);
		this.setTotal(total);
		this.setProgressInPercent(done / (total != 0 ? total : 1) * 100);
		this.setAchievement(isAchievement);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		// dest.writeInt(this.number);
		// dest.writeString(this.name);
		// dest.writeList(this.levels);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getDone() {
		return done;
	}

	public void setDone(int done) {
		this.done = done;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getProgressInPercent() {
		return progressInPercent;
	}

	public void setProgressInPercent(int progressInPercent) {
		this.progressInPercent = progressInPercent;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public boolean isAchievement() {
		return isAchievement;
	}

	public void setAchievement(boolean isAchievement) {
		this.isAchievement = isAchievement;
	}

}
