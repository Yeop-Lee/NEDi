package com.example.dclab.nedi;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultListAdapter extends RecyclerView.Adapter {
    private ArrayList<String> mDataNameList = new ArrayList<>();
    private String[] eventType = {"Water cc", "Meal Time", "Sleep Time", "Urin cc"};
    FileManager mFileManager = new FileManager();
    private ArrayList<DiaryData> mDataList = new ArrayList<>();
    private boolean isYesterday;
    private OnItemClickListener mResultItemClickListener = null;

    public void add(String data) {
        mDataNameList.add(data);
        notifyItemInserted(mDataNameList.size());
    }

    public void setOnItemClickListener(ResultListAdapter.OnItemClickListener itemClickLikstenr) {
        mResultItemClickListener = itemClickLikstenr;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ResultListAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_result_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((ResultListAdapter.ViewHolder) viewHolder).setData(mDataNameList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDataNameList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout profileLayout;
        TextView profileDate;
        TextView profileName;
        TextView profileSex;
        TextView profileAge;
        TextView dayMax;
        TextView nightUrine;
        TextView todayUrineSum;
        TextView portion;
        TextView todayUrine;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileLayout = itemView.findViewById(R.id.day_setting);
            profileDate = itemView.findViewById(R.id.text_day_list_view);
            profileName = itemView.findViewById(R.id.text_name_list_view);
            profileSex = itemView.findViewById(R.id.text_sex_list_view);
            profileAge = itemView.findViewById(R.id.text_age_list_view);
            dayMax = itemView.findViewById(R.id.day_max_cc);
            nightUrine = itemView.findViewById(R.id.night_sum_cc);
            todayUrineSum = itemView.findViewById(R.id.today_sum_cc);
            portion = itemView.findViewById(R.id.portion_day_percent);
            todayUrine = itemView.findViewById(R.id.day_urine_time);
        }

        public void setData(final String data, final int position) {
            isYesterday = mFileManager.loadByNameYesterday(data);
            if (isYesterday){
                mDataList = mFileManager.getAllData();
            }

            mFileManager.loadByName(data);
            mDataList = mFileManager.getAllData();
            String[] thisTime = mFileManager.getStringFromFilename(data);

            profileDate.setText(thisTime[1] + " " + thisTime[2]);
            profileName.setText(mDataList.get(0).EventProfile[0]);
            profileSex.setText(mDataList.get(0).EventProfile[1]);
            profileAge.setText(mDataList.get(0).EventProfile[2]);

            profileLayout.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if (mResultItemClickListener != null){
                        mResultItemClickListener.onItemClicked(data, position);
                    }
                }
            });
        }
    }

    interface OnItemClickListener {
        void onItemClicked(String data, int position);
    }
}
