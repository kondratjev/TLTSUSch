package ru.kondratj3v.tltsusch.models;


import android.os.Parcel;
import android.os.Parcelable;

public class Lesson implements Parcelable {
    public static final Parcelable.Creator<Lesson> CREATOR = new Parcelable.Creator<Lesson>() {
        public Lesson createFromParcel(Parcel in) {
            return new Lesson(in);
        }

        public Lesson[] newArray(int size) {
            return new Lesson[size];
        }
    };

    private String number;
    private String time;
    private String type;
    private String room;
    private String name;
    private String teacher;

    public String getNumber() {
        return number;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public String getRoom() {
        return room;
    }

    public String getName() {
        return name;
    }

    public String getTeacher() {
        return teacher;
    }

    public Lesson(String number, String time, String type, String room, String name, String teacher) {
        this.number = number;
        this.time = time;
        this.type = type;
        this.room = room;
        this.name = name;
        this.teacher = teacher;
    }

    private Lesson(Parcel in) {
        number = in.readString();
        time = in.readString();
        type = in.readString();
        room = in.readString();
        name = in.readString();
        teacher = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(number);
        dest.writeString(time);
        dest.writeString(type);
        dest.writeString(room);
        dest.writeString(name);
        dest.writeString(teacher);
    }
}