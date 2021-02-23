package com.project.dotamarketplace.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

public class Item implements Parcelable {
    int itemId;
    String name;
    int price, stock;
    double latitude, logitude;
    int isVisible;

    public Item(){

    }

    public Item(int itemId, String name, int price, int stock, double latitude, double longitude) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.latitude = latitude;
        this.logitude = longitude;
        this.isVisible = View.INVISIBLE;
    }

    protected Item(Parcel in) {
        itemId = in.readInt();
        name = in.readString();
        price = in.readInt();
        stock = in.readInt();
        latitude = in.readDouble();
        logitude = in.readDouble();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public int getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLogitude() {
        return logitude;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }

    public int getIsVisible() {
        return isVisible;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(itemId);
        parcel.writeString(name);
        parcel.writeInt(price);
        parcel.writeInt(stock);
        parcel.writeDouble(latitude);
        parcel.writeDouble(logitude);
    }
}
