package com.example.dclab.nedi;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class FileListAdapter extends RecyclerView.Adapter {
    ArrayList<String> mFileList = new ArrayList<>();
    private OnItemClickListener fileClickedListener = null;

    public void add(String data) {
        mFileList.add(data);
        notifyItemInserted(mFileList.size());
    }

    public void setOnItemClickListener(FileListAdapter.OnItemClickListener itemClickLikstener) {
        fileClickedListener = itemClickLikstener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FileListAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_file_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((FileListAdapter.ViewHolder) viewHolder).setData(mFileList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout fileListLayout;
        TextView fileDate;
        TextView fileID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileListLayout = itemView.findViewById(R.id.file_list_layout);
            fileDate = itemView.findViewById(R.id.file_list_day);
            fileID = itemView.findViewById(R.id.file_list_id);
        }

        public void setData(final String data, final int position) {
            FileManager tempFileManager = new FileManager();
            String getDate[] = tempFileManager.getStringFromFilename(data);

            String splitedFileName[] = data.split("_");
            String proFile[] = splitedFileName[0].split("-");

            fileDate.setText(getDate[0] + " " + getDate[1] + " " + getDate[2]);
            fileID.setText(proFile[0] + " " + proFile[1] + " " + proFile[2]);

            fileListLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (fileClickedListener != null) {
                        fileClickedListener.onItemClicked(data, position);
                    }
                }
            });
        }
    }

    interface OnItemClickListener {
        void onItemClicked(String data, int position);
    }
}
