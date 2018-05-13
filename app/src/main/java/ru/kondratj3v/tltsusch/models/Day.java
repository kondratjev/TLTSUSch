package ru.kondratj3v.tltsusch.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day implements Parcelable {
    public static final Parcelable.Creator<Day> CREATOR = new Parcelable.Creator<Day>() {
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

    private String date;
    private List<Lesson> lessons;
    private Map<String, String> hashMap;

    public Day(String date) {
        this.date = date;
        lessons = new ArrayList<>();
        hashMap = new HashMap<>();
    }

    public Day(String date, List<Lesson> lessons) {
        this.date = date;
        this.lessons = lessons;
    }

    private Day(Parcel in) {
        this.date = in.readString();
        this.lessons = in.createTypedArrayList(Lesson.CREATOR);
    }

    public String getDate() {
        return date;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void addLesson(Lesson lesson) {lessons.add(lesson);}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeTypedList(lessons);
    }
}