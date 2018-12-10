package com.example.dclab.nedi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiaryList extends AppCompatActivity {
    // The data to show
    List<String> logList = new ArrayList<String>();
    private Map<String, String> logSleep = new HashMap<>();
    private Map<String, String> logMeal = new HashMap<>();
    private Map<String, String> logDrink = new HashMap<>();
    private Map<String, String> logUrine = new HashMap<>();

    public static final String FILENAME = "";
    FileManager mFileManager = new FileManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Intent intent = getIntent();
        String filename = intent.getStringExtra(FILENAME);
        ListView listview = findViewById(R.id.listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,logList);
        //리스트뷰의 어댑터를 지정해준다.
        listview.setAdapter(adapter);


        //리스트뷰의 아이템을 클릭시 해당 아이템의 문자열을 가져오기 위한 처리
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

                //클릭한 아이템의 문자열을 가져옴
                String selected_item = (String)adapterView.getItemAtPosition(position);
                Toast.makeText(DiaryList.this,selected_item,Toast.LENGTH_SHORT).show();
            }
        });

        initList(filename);


    }

    private void initList(String filename) {
        mFileManager.loadByName(filename);
        logSleep = mFileManager.getLogSleepTime();
        logMeal = mFileManager.getLogMealTime();
        logDrink = mFileManager.getLogDrink();
        logUrine = mFileManager.getLogUrine();

        String getKey = "";
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j++) {
                int hour = i;
                if (i < 12) {
                    getKey += "오전 ";
                } else {
                    getKey += "오후 ";
                    hour -= 12;
                }
                getKey += String.format("%d시 %d분",i,j);
            }

        }
    }
}
