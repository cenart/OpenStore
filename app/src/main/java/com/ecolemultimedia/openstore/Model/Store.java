package com.ecolemultimedia.openstore.Model;

/**
 * Created by Cen on 13/07/2017.
 */

public class Store {
    public String Name;
    public String DayClosed;
    public int StartHour;
    public int EndHour;
    public String Image;
    public String Lat;
    public String Lng;
    public String Category;
    public String Description;


    @Override
    public String toString() {
        return Name;
    }
    public String getName() {
        return Name;
    }

    public String getLat() {
        return Lat;
    }

    public String getLng() {
        return Lng;
    }

    public String getDayClosed() {
        return DayClosed;
    }

    public int getStartHour() {
        return StartHour;
    }

    public int getEndHour() {
        return EndHour;
    }

    public String getImage() {
        return Image;
    }

    public String getCategory() {
        return Category;
    }

    public String getDescription() {
        return Description;
    }

}
