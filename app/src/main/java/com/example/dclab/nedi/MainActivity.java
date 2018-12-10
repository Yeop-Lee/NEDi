package com.example.dclab.nedi;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static String[] STORAGE_PERMISSION = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private String profile[] = new String[3]; //NAME, SEX, AGE
    public static final String NAME = "홍길동";
    public static final String SEX = "남자";
    public static final String AGE = "12세";

    public int client_id = 0;
    public int hour24 = 0;
    public int ten_minute = 0;
    FileManager mFileManager = new FileManager();

    private TextView text_profile_name;
    private TextView text_profile_sex;
    private TextView text_profile_age;
    private TextView text_last_sleepTime;
    private TextView text_last_mealTime;
    private TextView text_today_drink;
    private TextView text_today_urine;
    public int today_drink = 0;
    public int today_urine = 0;

    private String[] timer = {"오늘", "0", "0"}; // day, hour, minutes

    private final String[] ExtraMenuList = {"야뇨증에 대해서", "일지 파일 확인", "결과 파일 추출"};
    private final String[] mDays = {"오늘", "어제"};
    private final String[] mHours = {"오전 0시", "오전 1시", "오전 2시", "오전 3시", "오전 4시", "오전 5시", "오전 6시",
            "오전 7시", "오전 8시", "오전 9시", "오전 10시", "오전 11시", "오전 12시",
            "오후 1시", "오후 2시", "오후 3시", "오후 4시", "오후 5시", "오후 6시", "오후 7시", "오후 8시", "오후 9시", "오후 10시", "오후 11시"};
    private final String[] mMinutes = {"0분", "10분", "20분", "30분", "40분", "50분"};
    private final String[] mCCs = new String[41];
    private RelativeLayout layoutSleep;
    private RelativeLayout layoutMeal;
    private RelativeLayout layoutDrink;
    private RelativeLayout layoutUrine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(MainActivity.this, STORAGE_PERMISSION, 1);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button goProfile = findViewById(R.id.button_go_profile);
        Button extra = findViewById(R.id.button_extra);
        layoutSleep = findViewById(R.id.layout_sleep);
        layoutMeal = findViewById(R.id.layout_meal);
        layoutDrink = findViewById(R.id.layout_drink);
        layoutUrine = findViewById(R.id.layout_urine);

        goProfile.setOnClickListener(mainClickListener);
        extra.setOnClickListener(mainClickListener);
        layoutSleep.setOnClickListener(mainClickListener);
        layoutMeal.setOnClickListener(mainClickListener);
        layoutDrink.setOnClickListener(mainClickListener);
        layoutUrine.setOnClickListener(mainClickListener);

        text_profile_name = findViewById(R.id.text_name);
        text_profile_sex = findViewById(R.id.text_sex);
        text_profile_age = findViewById(R.id.text_age);
        text_last_sleepTime = findViewById(R.id.text_sleep);
        text_last_mealTime = findViewById(R.id.text_meal);
        text_today_drink = findViewById(R.id.text_drink);
        text_today_urine = findViewById(R.id.text_urine);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    Button.OnClickListener mainClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_go_profile:
                    profile_setter();
                    break;
                case R.id.button_extra:
                    showExtra();
                    break;
                case R.id.layout_sleep:
                    showEventDialog(2);
                    break;
                case R.id.layout_meal:
                    showEventDialog(1);
                    break;
                case R.id.layout_drink:
                    showEventDialog(0);
                    break;
                case R.id.layout_urine:
                    showEventDialog(3);
                    break;
            }
        }
    };

    public void profile_setter() {
        final Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        intent.putExtra(ProfileActivity.SET_NAME, String.valueOf(profile[0]));
        intent.putExtra(ProfileActivity.SET_SEX, String.valueOf(profile[1]));
        intent.putExtra(ProfileActivity.SET_AGE, String.valueOf(profile[2]));
        Log.e("Main", "Call ProfileSetting");
        startActivity(intent);
        finish();
    }

    private void time_setter(NumberPicker mHour, NumberPicker mMinute) {
        Calendar right_now = Calendar.getInstance();
        hour24 = right_now.get(Calendar.HOUR_OF_DAY);
        int minute_temp = (int) right_now.get(Calendar.MINUTE);

        ten_minute = (int) minute_temp / 10;
        int last_minute = minute_temp - (ten_minute * 10);

        for (int i = 0; i < mMinutes.length; i++) {
            mMinutes[i] = String.valueOf(i * 10 + last_minute) + "분";
        }
    }

    public void showEventDialog(int type) {// type 0: water, 1: meal, 2:sleep, 3: urine
        final int thisType = type;
        if (thisType == 1 || thisType == 2) {
            final Dialog d = new Dialog(MainActivity.this);
            if (thisType == 1) {
                d.setTitle("식사가 끝나는 시간");
            } else {
                d.setTitle("기상 및 취침 시간");
            }
            d.setContentView(R.layout.dialog_meal);
            final RadioButton Meal = d.findViewById(R.id.radioButton);
            final RadioButton Sweet = d.findViewById(R.id.radioButton2);
            if (thisType == 2) {
                Meal.setText("기상");
                Sweet.setText("취침");
            }
            final NumberPicker mDay = d.findViewById(R.id.numberPickerDay);
            final NumberPicker mHour = d.findViewById(R.id.numberPickerHour);
            final NumberPicker mMinute = d.findViewById(R.id.numberPickerMinute);
            numberPickSetter(mDay, mDays);
            numberPickSetter(mHour, mHours);
            numberPickSetter(mMinute, mMinutes);
            time_setter(mDay, mHour);
            mHour.setValue(hour24);
            mMinute.setValue(ten_minute);
            Button b1 = d.findViewById(R.id.button1);
            Button b2 = d.findViewById(R.id.button2);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.dismiss();
                }
            });
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timer[0] = String.valueOf(mDays[mDay.getValue()]);
                    timer[1] = String.valueOf(mHours[mHour.getValue()]);
                    timer[2] = String.valueOf(mMinutes[mMinute.getValue()]);
                    String tempEventText = "";
                    if (thisType == 1) {
                        tempEventText = "식사";
                        if (Meal.isChecked()) {
                            tempEventText = "정식";
                        } else if (Sweet.isChecked()) {
                            tempEventText = "간식";
                        }
                        mFileManager.saveUserFile(client_id, profile, tempEventText, timer, thisType);
                        text_last_mealTime.setText(timer[1] + " " + timer[2] + " (" + tempEventText + ")");
                    } else {
                        tempEventText = "수면";
                        if (Meal.isChecked()) {
                            tempEventText = "기상";
                        } else if (Sweet.isChecked()) {
                            tempEventText = "취침";
                        }
                        mFileManager.saveUserFile(client_id, profile, tempEventText, timer, thisType);
                        text_last_sleepTime.setText(timer[1] + " " + timer[2] + " (" + tempEventText + ")");
                    }
//                    Toast.makeText(MainActivity.this,timer[0]+timer[1]+timer[2], Toast.LENGTH_SHORT).show();
                    d.dismiss();
                }
            });
            d.show();
        } else {
            final Dialog d = new Dialog(MainActivity.this);
            if (thisType == 0) {
                d.setTitle("물을 마신 시간");
            } else if (thisType == 3) {
                d.setTitle("소변 배출 시간");
            }
            for (int i = 0; i < mCCs.length; i++) {
                mCCs[i] = String.valueOf(i * 10);
            }
            d.setContentView(R.layout.dialog_sleep);
            final NumberPicker mDay = d.findViewById(R.id.numberPickerDay);
            final NumberPicker mHour = d.findViewById(R.id.numberPickerHour);
            final NumberPicker mMinute = d.findViewById(R.id.numberPickerMinute);
            numberPickSetter(mDay, mDays);
            numberPickSetter(mHour, mHours);
            numberPickSetter(mMinute, mMinutes);
            time_setter(mDay, mHour);
            mHour.setValue(hour24);
            mMinute.setValue(ten_minute);
            Button b1 = d.findViewById(R.id.button1);
            Button b2 = d.findViewById(R.id.button2);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.dismiss();
                }
            });
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timer[0] = String.valueOf(mDays[mDay.getValue()]);
                    timer[1] = String.valueOf(mHours[mHour.getValue()]);
                    timer[2] = String.valueOf(mMinutes[mMinute.getValue()]);
                    if (thisType == 0) {
                        showCCCheker(thisType);
                    } else if (thisType == 3) {
                        showCCCheker(thisType);
                    }
//                    Toast.makeText(MainActivity.this,timer[0]+timer[1]+timer[2], Toast.LENGTH_SHORT).show();
                    d.dismiss();
                }
            });
            d.show();
        }
    }

    public void showCCCheker(int type) {
        final int thisType = type;
        final Dialog d = new Dialog(MainActivity.this);
        if (thisType == 0) {
            d.setTitle("수분섭취기록");
        } else {
            d.setTitle("소변측정기록");
        }
        d.setContentView(R.layout.dialog_cc_get);
        final NumberPicker getCc = d.findViewById(R.id.numberPickerCC);
        numberPickSetter(getCc, mCCs);
        getCc.setValue(19);
        Button b1 = d.findViewById(R.id.button1);
        Button b2 = d.findViewById(R.id.button2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempEventText = mCCs[getCc.getValue()];
                if (thisType == 0) {
                    today_drink += Integer.parseInt(mCCs[getCc.getValue()]);
                    mFileManager.saveUserFile(client_id, profile, tempEventText, timer, thisType);
                    text_today_drink.setText(String.valueOf(today_drink) + "cc");
                } else {
                    today_urine += Integer.parseInt(tempEventText);
                    mFileManager.saveUserFile(client_id, profile, tempEventText, timer, thisType);
                    text_today_urine.setText(String.valueOf(today_urine) + "cc");
                }
//                    Toast.makeText(MainActivity.this,timer[0]+timer[1]+timer[2], Toast.LENGTH_SHORT).show();
                d.dismiss();
            }
        });
        d.show();
    }


    public void showSavedList() {
        mFileManager.searchSavedFile();
        ArrayList<String> FileNames = mFileManager.getSavedFileName();
//                    ArrayList<String> FileNames = mFileManager.getViewListFofFile();
//                    final Map<String, String> Dictionary = mFileManager.getMappingFileNameAndList();

        final CharSequence[] items = FileNames.toArray(new String[FileNames.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("일지 기록 날짜");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                String selectedText = items[pos].toString();
//                            String filename = Dictionary.get(selectedText);
                Toast.makeText(MainActivity.this, selectedText, Toast.LENGTH_SHORT).show();
                final Intent intent = new Intent(getApplicationContext(), DiaryList.class);
                intent.putExtra(DiaryList.FILENAME, selectedText);
                startActivity(intent);
            }
        });
        builder.show();
    }

    public void showExtra() {
        final ArrayList<String> ExtraMenu = new ArrayList<String>();
        ExtraMenu.add(ExtraMenuList[0]);
        ExtraMenu.add(ExtraMenuList[1]);
        ExtraMenu.add(ExtraMenuList[2]);

        final CharSequence[] items = ExtraMenu.toArray(new String[ExtraMenu.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                String selectedText = items[pos].toString();
//                            String filename = Dictionary.get(selectedText);
                Toast.makeText(MainActivity.this, selectedText, Toast.LENGTH_SHORT).show();
                if (selectedText.equals(ExtraMenuList[0])) {

                } else if (selectedText.equals(ExtraMenuList[1])) {
                    showSavedList();
                } else if (selectedText.equals(ExtraMenuList[2])) {

                }
            }
        });
        builder.show();
    }

    private void numberPickSetter(final NumberPicker numPick, String[] msg) {
        numPick.setMinValue(0);
        numPick.setMaxValue(msg.length - 1);
        numPick.setValue(0);
        numPick.setDisplayedValues(msg);
        numPick.setWrapSelectorWheel(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        today_drink = 0;
        today_urine = 0;
        try {
            mFileManager.loadUserFile(client_id);
        } catch (Exception e) {

        }
        profile = mFileManager.getRead_profile();
        String[] preSetter = mFileManager.getPreSetter();
        today_drink = mFileManager.getTodayDrink();
        today_urine = mFileManager.getTodayUrine();

        final Intent intent = getIntent();
        if (intent.getStringExtra(NAME) != null) {
            profile[0] = intent.getStringExtra(NAME);
            profile[1] = intent.getStringExtra(SEX);
            profile[2] = intent.getStringExtra(AGE);
            today_urine = 0;
            today_drink = 0;
        }
        text_profile_name.setText(profile[0]);
        text_profile_sex.setText(profile[1]);
        text_profile_age.setText(profile[2]);
        if (preSetter[1] != null){
            text_last_mealTime.setText(preSetter[1]);
        }else{
            text_last_mealTime.setText("기록 없음");
        }
        if (preSetter[1] != null){
            text_last_sleepTime.setText(preSetter[2]);
        }else{
            text_last_sleepTime.setText("기록 없음");
        }
        text_today_drink.setText(String.valueOf(today_drink) + "cc");
        text_today_urine.setText(String.valueOf(today_urine) + "cc");
    }
}
