package com.example.ipet.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class CasoOng implements Parcelable {

    private Caso caso;
    private Ong ong;

    public CasoOng(Caso caso, Ong ong) {
        this.caso = caso;
        this.ong = ong;
    }

    public Caso getCaso() {
        return caso;
    }

    public void setCaso(Caso caso) {
        this.caso = caso;
    }

    public Ong getOng() {
        return ong;
    }

    public void setOng(Ong ong) {
        this.ong = ong;
    }

    @Override
    public String toString() {
        return "CasoOng{" +
                "caso=" + caso +
                ", ong=" + ong +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(caso, flags);
        dest.writeParcelable(ong, flags);
    }

    public CasoOng(Parcel in){
        caso = in.readParcelable(Caso.class.getClassLoader());
        ong = in.readParcelable(Ong.class.getClassLoader());
    }

    public static final Parcelable.Creator<CasoOng> CREATOR = new Parcelable.Creator<CasoOng>() {
        public CasoOng createFromParcel(Parcel in) {
            return new CasoOng(in);
        }

        public CasoOng[] newArray(int size) {
            return new CasoOng[size];
        }
    };
}
