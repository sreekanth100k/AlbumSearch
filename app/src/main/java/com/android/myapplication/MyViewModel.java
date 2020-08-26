package com.android.myapplication;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;


public class MyViewModel extends AndroidViewModel {
    private MyRepository mMyRepository;
    private MutableLiveData<ArrayList<Results>> resultsLiveData;
    private Application mApplicationContext;


    public MyViewModel(@NonNull Application application) {
        super(application);

        mApplicationContext = application;
    }

    public void init(Context iApplicationContext, Context iActivityContext){
        mMyRepository   =   new MyRepository(iApplicationContext,iActivityContext,true);
        resultsLiveData =   mMyRepository.getResponseLiveData();
    }

    public void fetchApiResponse() {
        mMyRepository.fetchApiResponse();
    }

    public MutableLiveData<ArrayList<Results>> getResultsLiveData(){
        return resultsLiveData;
    }
}