package com.example.paymentmanagement;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Money implements Parcelable {
    private String unit;           //currency
    private float value;           //value

    public Money(String unit, float value){
        this.unit = unit;
        this.value = value;
    }

    public Money(){}

    public Money(Parcel in){
        this.unit = in.readString();
        this.value = in.readFloat();
    }

    public static final Parcelable.Creator<Money> CREATOR = new Parcelable.Creator<Money>() {
        @Override
        public Money createFromParcel(Parcel in) {
            return new Money(in);
        }

        @Override
        public Money[] newArray(int size) {
            return new Money[size];
        }
    };

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.unit);
        dest.writeFloat(this.value);
    }

    @NonNull
    @Override
    public String toString() {
        return unit + " " + value;
    }
}
