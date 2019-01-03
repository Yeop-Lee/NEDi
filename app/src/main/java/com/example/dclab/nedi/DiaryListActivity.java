package com.example.dclab.nedi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DiaryListActivity extends AppCompatActivity {
    private ArrayList<DiaryData> mDataList = new ArrayList<>();
    private String[] eventType = {"Water cc", "Meal Time", "Sleep Time", "Urin cc"};
    private int flag = 0;

    public Button back;
    public Button save;
    public String filename;
    public static final String FILENAME = "";
    private final ListAdapter mListAdapter = new ListAdapter();
    FileManager mFileManager = new FileManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("일지 확인 및 수정");

        final Intent intent = getIntent();
        filename = intent.getStringExtra(FILENAME);
        mFileManager.loadByName(filename);
        String[] profile = mFileManager.getRead_profile();
        String[] thisTime = mFileManager.getStringFromFilename(filename);

        TextView day = findViewById(R.id.text_day_list_view);
        TextView name = findViewById(R.id.text_name_list_view);
        TextView sex = findViewById(R.id.text_sex_list_view);
        TextView age = findViewById(R.id.text_age_list_view);

        back = findViewById(R.id.back_list);
        save = findViewById(R.id.save_list);

        back.setOnClickListener(listClickListener);
        save.setOnClickListener(listClickListener);

//        day.setText(filename);
        day.setText(thisTime[1] + " " + thisTime[2]);
        name.setText(profile[0]);
        sex.setText(profile[1]);
        age.setText(profile[2]);

        mDataList = mFileManager.getAllData();
        mDataList = mFileManager.sortedByTime(mDataList);

//        final ListAdapter mListAdapter = new ListAdapter();
        final RecyclerView mRecycler = findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecycler.setAdapter(mListAdapter);
        mRecycler.setLayoutManager(mLayoutManager);
        mListAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(DiaryData data, int position) {
                flag = 0;
                show(position);
//                Toast.makeText(getApplicationContext(), data.EventContents, Toast.LENGTH_SHORT).show();
            }
        });

        for (int i = 0; i < mDataList.size(); i++) {
            mListAdapter.add(mDataList.get(i));
        }
    }

    Button.OnClickListener listClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.save_list:
                    mFileManager.reviseFile(filename, mDataList);
                    final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.back_list:
                    final Intent bintent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(bintent);
                    finish();
                    break;
            }
        }
    };

    private void show(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("기록 삭제");
        builder.setMessage("삭제하기 버튼을 누르시면 해당 기록이 삭제됩니다.");
        builder.setPositiveButton("삭제하기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        flag = 1;
                        if (mDataList.size() == 1) {
                            Toast.makeText(getApplicationContext(), "데이터가 한 개 뿐이기 때문에 삭제할 수 없습니다.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "해당 기록을 삭제합니다.", Toast.LENGTH_LONG).show();
                            mDataList.remove(position);
                            mListAdapter.clear();
                            for (int i = 0; i < mDataList.size(); i++) {
                                mListAdapter.add(mDataList.get(i));
                            }
                        }
                    }
                });
        builder.setNegativeButton("돌아가기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        flag = -1;
//                        Toast.makeText(getApplicationContext(), "아니오를 선택했습니다.", Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }
}