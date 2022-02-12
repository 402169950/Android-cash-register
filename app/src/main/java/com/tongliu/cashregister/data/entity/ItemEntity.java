package com.tongliu.cashregister.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemEntity implements Parcelable {
    private String name;
    private int quantity;
    private float price;
    private long date;

    public ItemEntity(String name, int stock, float price, long date) {
        this.name = name;
        this.quantity = stock;
        this.price = price;
        this.date = date;
    }
    public ItemEntity(String name, int stock, float price) {
        this.name = name;
        this.quantity = stock;
        this.price = price;
    }
    public ItemEntity(ItemEntity src){
        this(src.name, src.quantity, src.price, src.date);
    }

    public ItemEntity() {
    }

    protected ItemEntity(Parcel in) {
        name = in.readString();
        quantity = in.readInt();
        price = in.readFloat();
        date = in.readLong();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public static final Creator<ItemEntity> CREATOR = new Creator<ItemEntity>() {
        @Override
        public ItemEntity createFromParcel(Parcel in) {
            return new ItemEntity(in);
        }

        @Override
        public ItemEntity[] newArray(int size) {
            return new ItemEntity[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeInt(quantity);
        dest.writeFloat(price);
        dest.writeLong(date);
    }
}
