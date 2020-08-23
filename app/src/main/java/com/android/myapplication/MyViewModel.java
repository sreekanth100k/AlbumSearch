package com.android.myapplication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;


public class MyViewModel extends AndroidViewModel {
    private MyRepository mMyRepository;
    private MutableLiveData<ArrayList<Results>> resultsLiveData;


    public MyViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){
        mMyRepository   =   new MyRepository();
        resultsLiveData =   mMyRepository.getResponseLiveData();
    }

    public void fetchApiResponse() {
        mMyRepository.fetchApiResponse();
    }

    public MutableLiveData<ArrayList<Results>> getResultsLiveData(){
        return resultsLiveData;
    }
}