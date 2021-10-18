package com.example.runningtracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runningtracker.R;
import com.example.runningtracker.entity.TrackPoint;

import java.util.ArrayList;
import java.util.List;

public class PointAdapter extends RecyclerView.Adapter<PointAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private PointAdapter.ItemClickListener clickListener;
    private List<TrackPoint> data;
    private final Context context;

    public PointAdapter(Context context) {
        this.data = new ArrayList<>();
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<TrackPoint> newData) {
        if(data != null) {
            data.clear();
            data.addAll(newData);
            notifyDataSetChanged();
        } else {
            data = newData;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_single_point_view, parent, false);
        return new PointAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Log.d("psylha", "PointRecycler: onBindViewHolder " + position);
        holder.bind(data.get(position), position + 1);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView number;
        TextView latitude;
        TextView longitude;
        TextView time;

        ViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.pointNumber);
            latitude = itemView.findViewById(R.id.pointLatitudeData);
            longitude = itemView.findViewById(R.id.pointLongitudeData);
            time = itemView.findViewById(R.id.pointTime);
            itemView.setOnClickListener(this);
        }

        void bind(final TrackPoint trackPoint, int pos) {
            if(trackPoint != null) {
                number.setText(String.valueOf(pos));
                latitude.setText(String.format("%.7f", trackPoint.getLatitude()));
                longitude.setText(String.format("%.7f", trackPoint.getLongitude()));
                time.setText(trackPoint.getTimeFormatted());
            }
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null)
                clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(PointAdapter.ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
