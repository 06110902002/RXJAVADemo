package com.example.administrator.rxjava;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/1/5.
 */

public class Curse  implements Parcelable{

    private String curse;

    protected Curse(Parcel in) {
        curse = in.readString();
    }

    public static final Creator<Curse> CREATOR = new Creator<Curse>() {
        @Override
        public Curse createFromParcel(Parcel in) {
            return new Curse(in);
        }

        @Override
        public Curse[] newArray(int size) {
            return new Curse[size];
        }
    };

    public String getCurse() {
        return curse;
    }

    public void setCurse(String curse) {
        this.curse = curse;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(curse);
    }

    public Curse(){};
}
