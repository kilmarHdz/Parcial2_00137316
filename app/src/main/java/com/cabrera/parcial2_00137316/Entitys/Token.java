package com.cabrera.parcial2_00137316.Entitys;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Token implements Parcelable {
    @SerializedName("token")
    private String tokenSecurity;

    protected Token(Parcel in) {
        tokenSecurity = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tokenSecurity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Token> CREATOR = new Creator<Token>() {
        @Override
        public Token createFromParcel(Parcel in) {
            return new Token(in);
        }

        @Override
        public Token[] newArray(int size) {
            return new Token[size];
        }
    };

    public Token(String tokenSecurity) {
        this.tokenSecurity = tokenSecurity;
    }

    public String getTokenSecurity() {
        return tokenSecurity;
    }

    public void setTokenSecurity(String tokenSecurity) {
        this.tokenSecurity = tokenSecurity;
    }
}
