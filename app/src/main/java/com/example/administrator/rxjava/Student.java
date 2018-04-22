package com.example.administrator.rxjava;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */

public class Student implements Parcelable {

    private String name;
    private int age;

    private List<Curse> curses;       //课程对象，一个学生可能会修多门课程

    public List<Curse> getCurses() {
        return curses;
    }
    public void setCurses(List<Curse> curses) {
        this.curses = curses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    protected Student(Parcel in) {
        name = in.readString();
        age = in.readInt();

    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
    }


    public Student(){

    }
}
