package com.example.dclab.nedi;

import java.util.ArrayList;

public class ResultData {
    public String fileName;
    ArrayList<DiaryData> mDataList = new ArrayList<>();
    ArrayList<DiaryData> mDataListTomorrow = new ArrayList<>();
    ArrayList<String> dayUrineData = new ArrayList<>();
    ArrayList<String> nightUrineData = new ArrayList<>();
    ArrayList<String> firstUrine = new ArrayList<>();
    private String[] eventType = {"Water cc", "Meal Time", "Sleep Time", "Urin cc"};
    private String[] sleepType = {"기상", "취침"};
    FileManager mFileManager = new FileManager();

    int sleepCounter = 0;
    int nightUrineCC = 0;
    int dayUrineCC = 0;
    int todayUrineCC = 0;
    int dayUrineMax = 0;
    int todayUrineCounter = 0;
    int port = 0;
    String problemMsg = "";
    String [] thisTime = new String[3];
    boolean isTomorrow = false;

    public int getNightUrineCC() {
        return nightUrineCC;
    }

    public int getTodayUrineCounter() {
        return todayUrineCounter;
    }

    public int getDayUrineMax() {
        return dayUrineMax;
    }

    public int getTodayUrineCC() {
        return todayUrineCC;
    }

    public int getPort() {
        return port;
    }

    public String getProblemMsg() {
        return problemMsg;
    }

    public String[] getThisTime() {
        return thisTime;
    }

    public String[] getProfile(){
        return mDataList.get(0).EventProfile;
    }

    public ResultData(String fileName){
        this.fileName = fileName;

        mFileManager.loadByName(fileName);
        mDataList = mFileManager.getAllData();
        mDataList = mFileManager.sortedByTime(mDataList);
        thisTime = mFileManager.getStringFromFilename(fileName);
        todayDataControl(mDataList);

        isTomorrow = mFileManager.loadByNameTomorrow(fileName);
        if (isTomorrow) {
            mDataListTomorrow = mFileManager.getAllData();
            mDataListTomorrow = mFileManager.sortedByTime(mDataListTomorrow);
            tomorrowDataControl(mDataListTomorrow);
        } else {
            problemMsg = "다음 날 파일 없음";
        }
        if (todayUrineCC != 0) {
            port = (int) (((float) nightUrineCC / (float) todayUrineCC)*100);
        }else{
            port = 0;
        }
    }

    public void todayDataControl(ArrayList<DiaryData> data) {
        nightUrineData.clear();
        dayUrineData.clear();
        firstUrine.clear();
        sleepCounter = -1;
        int firstUrineCounter = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).EventType.equals(eventType[3])) {
                if (sleepCounter == 0) {
                    nightUrineData.add(data.get(i).EventContents);
                } else if (sleepCounter == 1) {
                    if (firstUrineCounter == 1) {
                        firstUrine.add(data.get(i).EventContents);
                        firstUrineCounter = 0;
                    } else {
                        dayUrineData.add(data.get(i).EventContents);
                    }
                }
            } else if (data.get(i).EventType.equals(eventType[2])) {
                if (data.get(i).EventContents.equals(sleepType[0])) { //기상
                    sleepCounter = 1;
                    firstUrineCounter = 1;
                } else if (data.get(i).EventContents.equals(sleepType[1])) {//취침
                    sleepCounter = 0;
                }
            }
        }
        dayUrineCC = 0;
        dayUrineMax = 0;
        nightUrineCC = 0;
        todayUrineCC = 0;
        for (int i = 0; i < dayUrineData.size(); i++) {
            int thisUrine = Integer.valueOf(dayUrineData.get(i));
            if (thisUrine > dayUrineMax) {
                dayUrineMax = thisUrine;
            }
            dayUrineCC += thisUrine;
            todayUrineCC += thisUrine;
        }
        for (int i = 0; i < nightUrineData.size(); i++) {
            int thisUrine = Integer.valueOf(nightUrineData.get(i));
            if (thisUrine > dayUrineMax) {
                dayUrineMax = thisUrine;
            }
            nightUrineCC += thisUrine;
            todayUrineCC += thisUrine;
        }
        if (firstUrine.size() != 1) {
            problemMsg ="수면시간오류:"+String.valueOf(firstUrine.size());
        }
        todayUrineCounter = dayUrineData.size() + firstUrine.size();
    }

    public void tomorrowDataControl(ArrayList<DiaryData> data) {
        nightUrineData.clear();
        dayUrineData.clear();
        firstUrine.clear();
        sleepCounter = 0;
        int firstUrineCounter = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).EventType.equals(eventType[3])) {
                if (sleepCounter == 0) {
                    nightUrineData.add(data.get(i).EventContents);
                } else if (sleepCounter == 1) {
                    if (firstUrineCounter == 1) {
                        firstUrine.add(data.get(i).EventContents);
                        break;
                    }
                }
            } else if (data.get(i).EventType.equals(eventType[2])) {
                if (data.get(i).EventContents.equals(sleepType[0])) {//기상
                    sleepCounter = 1;
                    firstUrineCounter = 1;
                } else if (data.get(i).EventContents.equals(sleepType[1])) {//취침
                    sleepCounter = -1;
                }
            }
        }
        for (int i = 0; i < nightUrineData.size(); i++) {
            int thisUrine = Integer.valueOf(nightUrineData.get(i));
            if (thisUrine > dayUrineMax) {
                dayUrineMax = thisUrine;
            }
            nightUrineCC += thisUrine;
            todayUrineCC += thisUrine;
        }
        if (firstUrine.size() != 1) {
            problemMsg ="+수면시간오류:"+String.valueOf(firstUrine.size());
        }
        nightUrineCC += Integer.valueOf(firstUrine.get(0));
        todayUrineCC += Integer.valueOf(firstUrine.get(0));
    }
}
