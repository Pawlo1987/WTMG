package com.example.wtmg;

import android.os.Parcel;
import android.os.Parcelable;

//Object for packing data about works
public class Works implements Parcelable {

    String idPRJ;
    String name;
    String timeLimit;
    String wasteTime;
    String company;
    String supportMan;

    //default constructor for creating object class
    public Works(){}

    //constructor for creating object class
    public Works( String idPRJ, String name, String timeLimit,
             String wasteTime, String company, String supportMan){
        //reassignment input data to object data
        this.idPRJ =  idPRJ;
        this.name =  name;
        this.timeLimit =  timeLimit;
        this.wasteTime = wasteTime;
        this.company =  company;
        this.supportMan =  supportMan;
    }//public Works

    protected Works(Parcel in) {
        idPRJ = in.readString();
        name = in.readString();
        timeLimit = in.readString();
        wasteTime = in.readString();
        company = in.readString();
        supportMan = in.readString();
    }//protected Works(Parcel in)

    public static final Creator<Works> CREATOR = new Creator<Works>() {
        @Override
        public Works createFromParcel(Parcel in) {
            return new Works(in);
        }

        @Override
        public Works[] newArray(int size) {
            return new Works[size];
        }
    };

    //getters and setters of object
    public String getIdPRJ() { return idPRJ; }
    public void setIdPRJidPRJ(String idPRJ) { this.idPRJ = idPRJ; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTimeLimit() { return timeLimit; }
    public void setTimeLimit(String timeLimit) { this.timeLimit = timeLimit; }

    public String getWasteTime() { return wasteTime; }
    public void setWasteTime(String wasteTime) { this.wasteTime = wasteTime; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getSupportMan() { return supportMan; }
    public void setSupportMan(String supportMan) { this.supportMan = supportMan; }

    @Override
    public int describeContents() {
        return 0;
    }//describeContents

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idPRJ);
        dest.writeString(name);
        dest.writeString(timeLimit);
        dest.writeString(wasteTime);
        dest.writeString(company);
        dest.writeString(supportMan);
    }//writeToParcel

}//class Works
