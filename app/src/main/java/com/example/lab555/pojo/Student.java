package com.example.lab555.pojo;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Student implements Parcelable {
    private long id;
    private String name;
    private String phone;
    private Uri imageUri;

    public Student() {
    }

    public Student(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public Student(String name, String phone, Uri image) {
        this.name = name;
        this.phone = phone;
        this.imageUri = image;
    }

    public Student(long id, String name, String phone, Uri image) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.imageUri = image;
    }

    protected Student(Parcel in) {
        name = in.readString();
        phone = in.readString();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(phone);
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
