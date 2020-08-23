package com.android.myapplication;

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


public class MainActivity:AppCompatActivity() {

    private var mViewModel: MyViewModel? = null
    private val adapter: ResultsAdapter? = null
    private var resultsList:ArrayList<Results>? = null
    private var mRecyclerView:RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        mRecyclerView = findViewById(R.id.id_rv)
        mRecyclerView?.layoutManager = LinearLayoutManager(this.applicationContext);


        resultsList = ArrayList<Results>();

        mViewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        mViewModel!!.init()
        mViewModel!!.resultsLiveData.observe(this, Observer<ArrayList<Results>>() {
            if (it != null) {
                var resultsAdapter:ResultsAdapter =    ResultsAdapter();
                resultsAdapter.setResults(it);

                mRecyclerView?.adapter = resultsAdapter
                mRecyclerView?.invalidate()
                mRecyclerView?.adapter?.notifyDataSetChanged()


            }
        })
    }

    fun performSearch() {
        mViewModel?.fetchApiResponse()
    }
}