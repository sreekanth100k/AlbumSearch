package com.android.myapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.ArrayList


public class MainActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        supportActionBar?.hide()


        NetworkService.getInstance().jsonApi.fetchApiResponse().enqueue(object:Callback<TestResponse>{
            override fun onResponse(call: Call<TestResponse>, response: Response<TestResponse>) {

                Log.d("onResponse",response.body().toString());

                decodeResponseAndDoNecessaryActions(response)
            }

            override fun onFailure(call: Call<TestResponse>, t: Throwable) {
                Log.d("onFailure","");
            }
        });
    }

    fun decodeResponseAndDoNecessaryActions(response:Response<TestResponse>) {

        val response: TestResponse? = response.body()
        Log.d("Response", response.toString())

        var resultCount: String?            =  response?.resultCount
        var results: ArrayList<Results>?    =  response?.results
        var resultsAfterTrackNameDupRemoval:ArrayList<Results> = ArrayList()
        //Trackname#
        //Remove duplicate data by Trackname#
        if (results != null) {
            for(i in 0..results.size-1){
                var resultObj:Results   = results.get(i);
                var isDuplicate:Boolean = false
                for(j in 0..resultsAfterTrackNameDupRemoval.size-1){
                    try {
                        if (resultsAfterTrackNameDupRemoval.get(j).trackName == resultObj.trackName) {
                            //Dont add, duplicate entry...
                            isDuplicate = true;
                        }
                    }catch (e:Exception){
                        Log.e("Exception",e.message.toString());
                    }


                }
                if(!isDuplicate){
                    resultsAfterTrackNameDupRemoval.add(resultObj);
                }
            }

            Log.d("Response trkNm Removal", resultsAfterTrackNameDupRemoval.toString())

            //Sort the data by release date in ascending order.


            val resultsAfterSortingByReleaseDate = resultsAfterTrackNameDupRemoval.sortedWith(compareBy(Results::releaseDate))
            Log.d("Response Sorting RlDate", resultsAfterSortingByReleaseDate.toString())





        }
    }
}