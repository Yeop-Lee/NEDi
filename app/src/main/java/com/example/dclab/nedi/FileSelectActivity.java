package com.example.dclab.nedi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

public class FileSelectActivity extends AppCompatActivity {
    FileManager mFileManager = new FileManager();
    ArrayList<String> dataList = new ArrayList<>();
    public final FileListAdapter mFileListAdapter = new FileListAdapter();
    public static final String TYPE = "";
    private String nowType;
    private String whatType[] = {"view", "result"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_select);
        setTitle("날짜를 선택해주세요.");

        final Intent intent = getIntent();
        nowType = intent.getStringExtra(TYPE);

        dataList = mFileManager.searchSavedFile();
        final RecyclerView mRecycler = findViewById(R.id.recycler_filelist_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecycler.setAdapter(mFileListAdapter);
        mRecycler.setLayoutManager(mLayoutManager);

        for (int i = 0; i < dataList.size(); i++) {
            Log.i("Result load", dataList.get(i));
            mFileListAdapter.add(dataList.get(i));
        }

        mFileListAdapter.setOnItemClickListener(new FileListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(String data, int position) {
                if (nowType.equals(whatType[0])) {
                    final Intent intent = new Intent(getApplicationContext(), DiaryListActivity.class);
                    intent.putExtra(DiaryListActivity.FILENAME, data);
                    startActivity(intent);
                } else if (nowType.equals(whatType[1])) {
                    final Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    intent.putExtra(ResultActivity.FILENAMES, data);
                    startActivity(intent);
                }
            }
        });
    }
}
