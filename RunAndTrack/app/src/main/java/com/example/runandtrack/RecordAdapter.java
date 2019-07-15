package com.example.runandtrack;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {
    private ArrayList<RecordHelper> recordList;
    private OnRecordListener onRecordListener;

    public static class RecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textDate, textCalories, textDistance, textTime;
        OnRecordListener onRecordListener;

        public RecordViewHolder(LinearLayout v, OnRecordListener onRecordListener) {
            super(v);
            textDate = v.findViewById(R.id.recDate);
            textCalories = v.findViewById(R.id.recCalories);
            textDistance = v.findViewById(R.id.recDistance);
            textTime = v.findViewById(R.id.recTime);
            this.onRecordListener = onRecordListener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRecordListener.onRecordClick(getAdapterPosition());
        }
    }

    public RecordAdapter(ArrayList<RecordHelper> list, OnRecordListener onRecordListener) {
        this.recordList = list;
        this.onRecordListener = onRecordListener;
    }

    @Override
    public RecordAdapter.RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.record, parent, false);
        RecordViewHolder vh = new RecordViewHolder(v, onRecordListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecordViewHolder holder, int position) {
        holder.textDate.setText("Date: " + recordList.get(position).getRecordDate());
        holder.textCalories.setText("Calories: " + recordList.get(position).getRecordCalories());
        holder.textDistance.setText("Distance(km): " + recordList.get(position).getRecordDistance());
        holder.textTime.setText("Time(sec): " + recordList.get(position).getRecordTime());
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public interface OnRecordListener {
        void onRecordClick (int position);
    }
}
