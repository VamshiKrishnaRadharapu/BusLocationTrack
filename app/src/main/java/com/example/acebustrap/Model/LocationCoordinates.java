package com.example.acebustrap.Model;

public class LocationCoordinates {
    String BusLatitude,BusLongitude,BusStatus,Name;
    public LocationCoordinates(){

    }

    public String getBusLatitude() {
        return BusLatitude;
    }

    public void setBusLatitude(String busLatitude) {
        BusLatitude = busLatitude;
    }

    public String getBusLongitude() {
        return BusLongitude;
    }

    public void setBusLongitude(String busLongitude) {
        BusLongitude = busLongitude;
    }

    public String getBusStatus() {
        return BusStatus;
    }

    public void setBusStatus(String busStatus) {
        BusStatus = busStatus;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
