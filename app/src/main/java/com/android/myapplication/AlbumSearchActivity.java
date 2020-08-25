package com.android.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class AlbumSearchActivity extends Activity {

    private Button mSearchBtn;
    private EditText mArtistNameEt;
    private EditText mTrackNameEt;
    private EditText mCollectionNameEt;
    private EditText mReleaseDateEt;
    private EditText mCollectionPriceEt;
    private Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.album_search_activity);

        mArtistNameEt = findViewById(R.id.id_artist_name_et);

        mTrackNameEt  = findViewById(R.id.id_track_name_et);

        mCollectionNameEt  = findViewById(R.id.id_collection_name_et);

        mCollectionPriceEt  = findViewById(R.id.id_collection_price_et);

        mReleaseDateEt = findViewById(R.id.id_release_date_et);

        mSearchBtn   = findViewById(R.id.id_search_btn);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              if(validateInput()){
                                                  //Proceed to next screen......
                                                  Intent mainActivityIntent = new Intent(AlbumSearchActivity.this, ResultListActivity.class);
                                                  mainActivityIntent.putExtra("ArtistName",mArtistNameEt.getText().toString().trim());
                                                  mainActivityIntent.putExtra("TrackName",mTrackNameEt.getText().toString().trim());
                                                  mainActivityIntent.putExtra("CollectionName",mCollectionNameEt.getText().toString().trim());
                                                  mainActivityIntent.putExtra("CollectionPrice",mCollectionPriceEt.getText().toString().trim());
                                                  mainActivityIntent.putExtra("ReleaseDate",mReleaseDateEt.getText().toString().trim());
                                                  startActivityForResult(mainActivityIntent,0);
                                              }else{
                                                  Toast.makeText(mContext,"Please fill mandatory fields",Toast.LENGTH_LONG).show();
                                              }
                                          }
                                      }
        );

    }


    boolean validateInput(){
        if(mArtistNameEt.getText().toString().trim().equals("")){
            return false;
        }
        if(mTrackNameEt.getText().toString().trim().equals("")){
            return false;
        }

        return true;
    }
}
