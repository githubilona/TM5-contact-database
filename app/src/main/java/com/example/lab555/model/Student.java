package com.example.lab555.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {
    private Long id;
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

    public Student(Long id, String name, String phone, Uri image) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
