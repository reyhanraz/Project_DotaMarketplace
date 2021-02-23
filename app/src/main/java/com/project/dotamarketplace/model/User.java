package com.project.dotamarketplace.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.project.dotamarketplace.DataHelper;

public class User implements Parcelable {
    int userID;
    String name, userName, password, phoneNumber, gender;
    int balance;

    public User(String name, String userName, String password, String phoneNumber, String gender, int balance) {
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.balance = balance;
    }

    public User(int userID, String name, String userName, String password, String phoneNumber, String gender, int balance) {
        this.userID = userID;
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.balance = balance;
    }

    public User(){

    }

    protected User(Parcel in) {
        userID = in.readInt();
        name = in.readString();
        userName = in.readString();
        password = in.readString();
        phoneNumber = in.readString();
        gender = in.readString();
        balance = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public int getBalance() {
        return balance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userID);
        dest.writeString(name);
        dest.writeString(userName);
        dest.writeString(password);
        dest.writeString(phoneNumber);
        dest.writeString(gender);
        dest.writeInt(balance);
    }
}
