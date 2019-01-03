package com.example.dclab.nedi;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {
    public static final String FILENAMES = "";
    private final ResultListAdapter mResultListAdapter = new ResultListAdapter();
    public String fileNameSet;
    public String[] fnames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("진료 정보");

        final Intent intent = getIntent();
        fileNameSet = intent.getStringExtra(FILENAMES);
        fnames = fileNameSet.split(",");
        ArrayList<String> filenames = new ArrayList<>();

        for (int i = 0; i<fnames.length; i++){
            filenames.add(fnames[i]);
        }

        Collections.sort(filenames, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                try {
                    String client_id = o1.split("_")[0];
                    Log.i("client",client_id);
                    SimpleDateFormat formatter = new SimpleDateFormat(client_id+"_yy년MM월dd일", Locale.getDefault());
                    Date date1 = formatter.parse(o1);
                    Date date2 = formatter.parse(o2);
                    return date1.compareTo(date2);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                return 0;
            }
        });

        final RecyclerView mRecycler = findViewById(R.id.recycler_result_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecycler.setAdapter(mResultListAdapter);
        mRecycler.setLayoutManager(mLayoutManager);

        for (int i = 0; i < filenames.size(); i++) {
            Log.i("Result load",filenames.get(i));
            mResultListAdapter.add(filenames.get(i));
        }

        mResultListAdapter.setOnItemClickListener(new ResultListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(String data, int position) {
                final Intent intent = new Intent(getApplicationContext(), DiaryListActivity.class);
                intent.putExtra(DiaryListActivity.FILENAME, data);
                startActivity(intent);
//                Toast.makeText(getApplicationContext(), data.EventContents, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
