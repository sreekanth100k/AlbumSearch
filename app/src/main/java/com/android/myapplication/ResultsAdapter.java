package com.android.myapplication;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultHolder> {
    private List<Results> results = new ArrayList<>();
    private Context mApplicationContext,mActivityContext;


    @NonNull
    @Override
    public ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_layout, parent, false);

        return new ResultHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setContext(Context iApplicationContext, Context iActivityContext){
        mApplicationContext = iApplicationContext;
        mActivityContext    =   iActivityContext;
    }

    @Override
    public void onBindViewHolder(@NonNull ResultHolder holder, int position) {
        Results result = results.get(position);

        holder.artistNameTv.setText("Artist Name:"+ result.artistName);
        holder.trackNameTv.setText("Track Name:"+ result.trackName);
        holder.collectionNameTv.setText("Collection Name:"+result.collectionName);
        holder.collectionPriceTv.setText("Collection Price:"+result.collectionPrice);
        try {
            holder.releaseDateTv.setText("Release Date:"+convertToNewFormat(result.releaseDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.isInCartCb.setOnCheckedChangeListener(null);

        holder.isInCartCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CartItem cartItem = new CartItem();
                cartItem.setArtistName(result.getArtistName());
                cartItem.setCollectionName(result.getCollectionName());
                cartItem.setTrackName(result.getTrackName());
                cartItem.setCollectionPrice(result.getCollectionPrice());
                cartItem.setReleaseDate(result.getReleaseDate());
                MyRepository repository = new MyRepository(mApplicationContext,mActivityContext,false);


                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {

                        repository.getDbInstance().cartDAOAccess().insertTask(cartItem);
                        return null;
                    }
                }.execute();



            }
        });

        Glide.with(holder.thumbNailIv)
                    .load(result.artworkUrl100)
                    .into(holder.thumbNailIv);

        try{

                holder.isInCartCb.setChecked(result.isInCart);

        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void setResults(List<Results> results) {
        this.results = results;
        notifyDataSetChanged();

    }

    public static String convertToNewFormat(String dateStr) throws ParseException {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat destFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sourceFormat.setTimeZone(utc);
        Date convertedDate = sourceFormat.parse(dateStr);
        return destFormat.format(convertedDate);
    }

    class ResultHolder extends RecyclerView.ViewHolder {
        private TextView artistNameTv;
        private TextView collectionNameTv;
        private TextView trackNameTv;
        private TextView collectionPriceTv;
        private TextView releaseDateTv;
        private ImageView thumbNailIv;
        private CheckBox isInCartCb;

        public ResultHolder(@NonNull View itemView) {
            super(itemView);

            artistNameTv            =   itemView.findViewById(R.id.id_artist_name_tv);
            collectionNameTv        =   itemView.findViewById(R.id.id_collection_name_tv);
            trackNameTv             =   itemView.findViewById(R.id.id_track_name_tv);
            collectionPriceTv       =   itemView.findViewById(R.id.id_collection_price_tv);
            releaseDateTv           =   itemView.findViewById(R.id.id_releaseDate_tv);
            thumbNailIv             =   itemView.findViewById(R.id.id_thumb_nail);
            isInCartCb              =   itemView.findViewById(R.id.id_cb);
        }
    }
}
