package com.example.runningtracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runningtracker.R;
import com.example.runningtracker.entity.Track;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private ItemClickListener clickListener;
    private List<Track> data;
    private final Context context;

    public TrackAdapter(Context context) {
        this.data = new ArrayList<>();
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_single_track_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Log.d("psylha", "TrackRecycler: onBindViewHolder " + position);
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    public void setData(List<Track> newData) {
        if(data != null) {
            data.clear();
            data.addAll(newData);
            notifyDataSetChanged();
        } else {
            data = newData;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView date;
        TextView description;
        TextView distance;
        TextView time;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.dbTrackName);
            date = itemView.findViewById(R.id.dbTrackDate);
            description = itemView.findViewById(R.id.dbTrackDescription);
            distance = itemView.findViewById(R.id.dbTrackDistance);
            time = itemView.findViewById(R.id.dbTrackTime);
            itemView.setOnClickListener(this);
        }

        void bind(final Track track) {
            if(track != null) {
                name.setText(track.getName());
                date.setText(track.getDateFormat());
                description.setText(track.getDescription());
                distance.setText(String.format("%.2f", track.getTotalDistance()));
                time.setText(track.getDuration());
            }
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                try {
                    clickListener.onItemClick(view, getAdapterPosition());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position) throws ExecutionException, InterruptedException;
    }
}
