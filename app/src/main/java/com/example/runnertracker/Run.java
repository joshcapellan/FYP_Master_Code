package com.example.runnertracker;

public class Run {

    public String distance, duration, avgSpeed, uid, fullName;

    public Run(){}

    public Run(String distance, String duration, String avgSpeed, String uid, String fullName){
        this.distance = distance;
        this.duration = duration;
        this.avgSpeed = avgSpeed;
        this.uid = uid;
        this.fullName = fullName;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(String avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
