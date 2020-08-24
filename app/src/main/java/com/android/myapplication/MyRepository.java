package com.android.myapplication;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.lang.Exception;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


class MyRepository {
    private MutableLiveData<ArrayList<Results>> responseLiveData          =  null;
    private ApiService apiService                       =  null;
    private String BASE_URL                             =  "https://itunes.apple.com/";

    MutableLiveData<ArrayList<Results>> getResponseLiveData() {
        return responseLiveData;
    }

    MyRepository() {
        responseLiveData                                    =   new MutableLiveData();
        HttpLoggingInterceptor interceptor                  =   new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client                                 =   new OkHttpClient.Builder().addInterceptor(interceptor).build();
        apiService                                          =   new Retrofit.Builder()
                                                                .baseUrl(BASE_URL)
                                                                .client(client)
                                                                .addConverterFactory(GsonConverterFactory.create())
                                                                .build()
                                                                .create(ApiService.class);

        fetchApiResponse();


    }

    void fetchApiResponse(){

        apiService.fetchApiResponse().enqueue(new Callback<TestResponse>() {
            @Override
            public void onResponse(Call<TestResponse> call, Response<TestResponse> response) {
                Log.d("onResponse",response.body().toString());

                decodeResponseAndDoNecessaryActions(response);
            }

            @Override
            public void onFailure(Call<TestResponse> call, Throwable t) {
                Log.d("onFailure","");

            }
        });
    }

    void decodeResponseAndDoNecessaryActions(Response<TestResponse> response) {


        String resultCount           =  response.body().resultCount;
        ArrayList<Results> results   =  response.body().results;
        ArrayList<Results> resultsAfterTrackNameDupRemoval = new ArrayList<Results>();
        //Trackname#
        //Remove duplicate data by Trackname#
        if (results != null) {
            for(int i=0;i<results.size();i++){
                Results resultObj   = results.get(i);
                boolean isDuplicate = false;
                for(int j= 0;j<resultsAfterTrackNameDupRemoval.size();j++){
                    try {
                        Log.d("Before trackname1",resultsAfterTrackNameDupRemoval.get(j).trackName);
                        Log.d("Before trackname2",resultObj.trackName);
                        if (resultsAfterTrackNameDupRemoval.get(j).trackName.equals(resultObj.trackName)) {
                            //Dont add, duplicate entry...
                            Log.d("After trackname1",resultsAfterTrackNameDupRemoval.get(j).trackName);
                            Log.d("After trackname2",resultObj.trackName);

                            isDuplicate = true;
                        }
                    }catch (Exception e){
                        Log.e("Exception",e.getMessage().toString());
                    }
                }
                if(!isDuplicate){
                    resultsAfterTrackNameDupRemoval.add(resultObj);
                }
            }

            Log.d("Response trkNm Removal", resultsAfterTrackNameDupRemoval.toString());

            //Sort the data by release date in ascending order.

            Collections.sort(resultsAfterTrackNameDupRemoval,new CustomerSortingComparator());
            responseLiveData.postValue(resultsAfterTrackNameDupRemoval);

        }
    }

    static class CustomerSortingComparator implements Comparator<Results> {

        @Override
        public int compare(Results results, Results t1) {

            // for comparison
            String releaseDate1 = results.getReleaseDate();
            String releaseDate2 = t1.getReleaseDate();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
            boolean isDate1BeforeDate2 = false;
            try {
                isDate1BeforeDate2 = sdf.parse(releaseDate1).before(sdf.parse(releaseDate2));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(isDate1BeforeDate2){
                return -1;
            }

            return 1;
        }
    }

}