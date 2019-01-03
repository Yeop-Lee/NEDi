package com.example.dclab.nedi;

public class DiaryData {
    public String[] EventProfile;
    public String EventTime;
    public String EventType;
    public String EventContents;

    public DiaryData(String[] EventProfile, String EventTime, String EventType, String EventContents){
        this.EventProfile = EventProfile;
        this.EventTime = EventTime;
        this.EventType = EventType;
        this.EventContents = EventContents;
    }
}