package com.example.dclab.nedi;

import android.media.Image;
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

public class ListAdapter extends RecyclerView.Adapter {
    private ArrayList<DiaryData> mDataList = new ArrayList<>();
    private String[] eventType = {"Water cc", "Meal Time", "Sleep Time", "Urin cc"};
    private OnItemClickListener mItemClickListener = null;

    public void add(DiaryData data) {
        Log.i("time","time: "+ data.EventTime);
        mDataList.add(data);
        notifyItemInserted(mDataList.size());
    }

    public void clear(){
        mDataList.clear();
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickLikstenr) {
        mItemClickListener = itemClickLikstenr;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((ViewHolder) viewHolder).setData(mDataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        Log.i("DiaryListActivity","data size: " + mDataList.size());
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout listContainer;
        TextView timeTextView;
        TextView contentsTextView;
        ImageView iconView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listContainer = itemView.findViewById(R.id.diary_list_layout);
            timeTextView = itemView.findViewById(R.id.diary_list_time);
            contentsTextView = itemView.findViewById(R.id.diary_list_contents);
            iconView = itemView.findViewById(R.id.diary_list_icon);
        }

        public void setData(final DiaryData data, final int position) {
            timeTextView.setText(data.EventTime);
            if (data.EventType.equals(eventType[0])) {
                listContainer.setBackgroundResource(R.drawable.bg_drink);
                iconView.setBackgroundResource(R.drawable.icon_drink);
                contentsTextView.setText(data.EventContents+"cc");
            } else if (data.EventType.equals(eventType[1])) {
                listContainer.setBackgroundResource(R.drawable.bg_meal);
                iconView.setBackgroundResource(R.drawable.icon_meal);
                contentsTextView.setText(data.EventContents);
            } else if (data.EventType.equals(eventType[2])) {
                listContainer.setBackgroundResource(R.drawable.bg_sleep);
                iconView.setBackgroundResource(R.drawable.icon_sleep);
                contentsTextView.setText(data.EventContents);
            } else if (data.EventType.equals(eventType[3])) {
                listContainer.setBackgroundResource(R.drawable.bg_urine);
                iconView.setBackgroundResource(R.drawable.icon_urine);
                contentsTextView.setText(data.EventContents+"cc");
            }

            listContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null){
                        mItemClickListener.onItemClicked(data, position);
                    }
                }
            });

        }
    }

    interface OnItemClickListener {
        void onItemClicked(DiaryData data, int position);
    }
}
