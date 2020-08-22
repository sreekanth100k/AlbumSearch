package com.android.myapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

        var resultCount: String?         = response?.resultCount
        var results: ArrayList<Results>? = response?.results

    }
}