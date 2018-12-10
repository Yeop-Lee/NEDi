package com.example.dclab.nedi;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FileManager {
    private static final String STRSAVEPATH = Environment.getExternalStorageDirectory() + "/NED/";
    private static final String TAG = "FileManager";

    private String filename;

    private String[] preSetter = {null, null, null, null};
    private int todayDrink = 0;
    private int todayUrine = 0;

    private String[] eventType = {"Water cc", "Meal Time", "Sleep Time", "Urin cc"};
    private String[] read_profile = {null, null, null};
    private ArrayList<String> savedFileName;
    private ArrayList<String> viewListFofFile;
    private Map<String, String> mappingFileNameAndList;

    private int fileLimit = 5;

    public FileManager() {

    }

    public void saveUserFile(int patientNum, String[] profile, String event, String[] time, int type) {
        Log.v(TAG, "save data started");
        if (profile[0] == null || profile[1] == null || profile[2] == null) {
            Log.e(TAG, "cannot save");
        } else {
            read_profile = profile;
            filename = setFileName(patientNum);
            FileOutputStream fos;
            createFile(filename);
            try {
                fos = new FileOutputStream((STRSAVEPATH + filename), true);
                String text = "";
                text += String.format("%s,%s,%s,%s,%s", profile[0], profile[1], profile[2], eventType[type], event);
                text += String.format(", %s, %s : %s", time[0], time[1], time[2]);
                text += "\n";
                fos.write(text.getBytes());
                fos.close();
            } catch (IOException e) {
                Log.w(TAG, "ERROR: saveUserData");
            }
        }

    }

    public void loadUserFile(int patientNum) {
        filename = setFileName(patientNum);
        File dir = makeDirectory(STRSAVEPATH);
        File file = new File(STRSAVEPATH + filename);

        if (isFileExist(file) == false) {
            searchSavedFile();
            filename = savedFileName.get(savedFileName.size() - 1);
            parseData();
            todayDrink = 0;
            todayUrine = 0;
        } else {
            Log.v(TAG, "LOAD DATA");
            parseData();
        }
    }

    private void parseData() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(STRSAVEPATH + filename));//Get month function
            String str = null;
            //profiling first
            str = br.readLine();
            String[] temp = str.split(",");
            read_profile[0] = temp[0];
            read_profile[1] = temp[1];
            read_profile[2] = temp[2];
            parseUserFile(str);
            while (((str = br.readLine()) != null)) {
                Log.d(TAG, str);
                parseUserFile(str);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String setFileName(int patientNum) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat_forPath = new SimpleDateFormat("yyMMdd");
        String filePathDate = String.format(dateFormat_forPath.format(c.getTime()));
        String temp_filename = String.valueOf(patientNum) + "_" + filePathDate + ".csv";
        return temp_filename;
    }

    private String setFileName_Yesterday(int patientNum) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        SimpleDateFormat dateFormat_forPath = new SimpleDateFormat("yyMMdd");
        String filePathDate = String.format(dateFormat_forPath.format(c.getTime()));
        String temp_filename = String.valueOf(patientNum) + "_" + filePathDate + ".csv";
        return temp_filename;
    }

    private void createFile(String file_name) {
        File dir = makeDirectory(STRSAVEPATH);
        Log.v(TAG, STRSAVEPATH + file_name);
        File file = new File(STRSAVEPATH + file_name);
        if (isFileExist(file) == false) {
            Log.v(TAG, "make file");
            makeFile(dir, (STRSAVEPATH + file_name));
        }
    }

    public void searchSavedFile() {
        savedFileName = new ArrayList<String>();
        viewListFofFile = new ArrayList<String>();
        Map<String, String> mappingFileNameAndList = new HashMap<>();
        File dir = makeDirectory(STRSAVEPATH);
        for (File f : dir.listFiles()) {
            if (f.isFile()) {
                savedFileName.add(String.valueOf(f.getName()));
            }
            while (savedFileName.size() > fileLimit) {
                savedFileName.remove(0);
            }
//            for (int i = 0; i < savedFileName.size(); i++){
//                String tempFileName = savedFileName.get(i);
//                int stringLength = tempFileName.length();
//                String str = "";
//                str += tempFileName.substring(stringLength -10,stringLength - 8);
//                str += "년 ";
//                str += tempFileName.substring(stringLength -8,stringLength - 6);
//                str += "월 ";
//                str += tempFileName.substring(stringLength -6,stringLength - 4);
//                str += "일 ";
//                mappingFileNameAndList.put(str,tempFileName);
//                viewListFofFile.add(str);
//            }

        }
    }

    private void parseUserFile(String line) {
        String[] temp = line.split(",");
        if (!(read_profile[0].equals(temp[0]) && read_profile[1].equals(temp[1]) && read_profile[2].equals(temp[2]))) {
            preSetter = new String[]{null, null, null, null};
            todayUrine = 0;
            todayDrink = 0;
        }
        read_profile[0] = temp[0];
        read_profile[1] = temp[1];
        read_profile[2] = temp[2];
        Log.d(TAG, "LOAD PARSING::PROFILE::" + temp[0] + ":" + temp[1] + ":" + temp[2]);
        Log.d(TAG, "LOAD PARSING::EVENT TYPE::" + temp[3]);
        temp[3] = String.valueOf(temp[3]);
        if (temp[3].equals(eventType[0])) {       //water
            todayDrink += Integer.valueOf(temp[4]);
            preSetter[0] = temp[4];
            Log.d(TAG, "LOAD PARSING::EVENT water::" + temp[4]);
        } else if (temp[3].equals(eventType[1])) {  //meal
            preSetter[1] = temp[4];
            Log.d(TAG, "LOAD PARSING::EVENT meal::" + temp[4]);
        } else if (temp[3].equals(eventType[2])) {  //sleep
            preSetter[2] = temp[4];
            Log.d(TAG, "LOAD PARSING::EVENT sleep::" + temp[4]);
        } else if (temp[3].equals(eventType[3])) {  //urine
            todayUrine += Integer.valueOf(temp[4]);
            preSetter[3] = temp[4];
            Log.d(TAG, "LOAD PARSING::EVENT urine::" + temp[4]);
        }
    }

    private boolean isFileExist(File file) {
        boolean result;
        if (file != null && file.exists()) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    private File makeDirectory(String dir_path) {
        File dir = new File(dir_path);
        if (!dir.exists()) {
            Log.d(TAG, "mkdir");
            boolean su = dir.mkdirs();
            if (su == true) {
                Log.d(TAG, "success");
            } else if (su == false) {
                Log.d(TAG, "fail");
            } else {
                Log.d(TAG, "I don't know anymore");
            }
        } else {
        }
        return dir;
    }

    private File makeFile(File dir, String file_path) {
        File file = null;
        boolean isSuccess = false;
        if (dir.isDirectory()) {
            file = new File(file_path);
            if (file != null && !file.exists()) {
                try {
                    isSuccess = file.createNewFile();
                } catch (IOException e) {
                    Log.w(TAG, "failed create file");
                }
            }
        }
        return file;
    }

    /**
     * Reader & Setter
     */
    public String[] getRead_profile() {
        return read_profile;
    }

    public ArrayList<String> getSavedFileName() {
        return savedFileName;
    }

    //    public Map<String,String> getMappingFileNameAndList() {
//        return mappingFileNameAndList;
//    }
//
//    public ArrayList<String> getViewListFofFile(){
//        return viewListFofFile;
//    }
    public int getTodayDrink() {
        return todayDrink;
    }

    public int getTodayUrine() {
        return todayUrine;
    }

    public String[] getPreSetter() {
        return preSetter;
    }
}
