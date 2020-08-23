package com.android.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultHolder> {
    private List<Results> results = new ArrayList<>();

    @NonNull
    @Override
    public ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_layout, parent, false);

        return new ResultHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultHolder holder, int position) {
        Results result = results.get(position);

        holder.artistNameTv.setText(result.artistName);
        holder.trackNameTv.setText(result.trackName);
        holder.collectionNameTv.setText(result.collectionName);

//            Glide.with(holder.itemView)
//                    .load(imageUrl)
//                    .into(holder.smallThumbnailImageView);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void setResults(List<Results> results) {
        this.results = results;
        notifyDataSetChanged();

    }

    class ResultHolder extends RecyclerView.ViewHolder {
        private TextView artistNameTv;
        private TextView collectionNameTv;
        private TextView trackNameTv;

        public ResultHolder(@NonNull View itemView) {
            super(itemView);

            artistNameTv            =   itemView.findViewById(R.id.id_artist_name_tv);
            collectionNameTv        =   itemView.findViewById(R.id.id_collection_name_tv);
            trackNameTv             =   itemView.findViewById(R.id.id_track_name_tv);
        }
    }
}
