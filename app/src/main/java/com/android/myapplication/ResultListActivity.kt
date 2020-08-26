package com.android.myapplication;

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


public class ResultListActivity:AppCompatActivity() {

    private var mViewModel: MyViewModel? = null
    private var resultsList:ArrayList<Results>? = null
    private var mRecyclerView:RecyclerView? = null
    private var mSpinner:Spinner? = null
    // Spinner Drop down elements
    val mCategories: MutableList<String> = ArrayList()
    private var mSearchArtistName:String? = ""
    private var mTrackName:String? = ""
    private var mCollectionName:String? = ""
    private var mCollectionPrice:String? =""
    private var mReleaseDate:String? = ""
    private lateinit  var mIdEmptyTv:TextView
    private lateinit var mSearchIv:ImageView;
    private lateinit var  mSpinnerItems: ArrayList<String>;
    private var mSearchedResultsList:ArrayList<Results>?=null
    private lateinit var mCartButton:Button;


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK){
            if(requestCode == 1000) {


                var artistName: String? =   data?.getStringExtra("ArtistName")
                artistName              =   artistName.toString().trim()

                var trackName: String? =   data?.getStringExtra("TrackName")
                trackName              =   trackName.toString().trim()

                var collectionName: String? =   data?.getStringExtra("CollectionName")
                collectionName              =   collectionName.toString().trim()

                var collectionPrice: String? =   data?.getStringExtra("CollectionPrice")
                collectionPrice              =   collectionPrice.toString().trim()

                var releaseDate: String? =   data?.getStringExtra("ReleaseDate")
                releaseDate              =   releaseDate.toString().trim()

                mSearchedResultsList?.clear();

                mSearchedResultsList = ArrayList<Results>()


                for(result:Results in resultsList!!){

                    try {
                        if (result.artistName.contains(artistName)) {
                            if (result.trackName.contains(trackName)) {
                                if (result.collectionName.contains(collectionName)) {
                                    if (result.collectionPrice.contains(collectionPrice)) {
                                        if (result.releaseDate.contains(releaseDate)) {

                                            mSearchedResultsList?.add(result);


                                        }
                                    }
                                }
                            }
                        }
                    }catch (e: Exception){

                    }
                }

                if(mSearchedResultsList?.size!! >0) {
                    var resultsAdapter: ResultsAdapter = ResultsAdapter();
                    resultsAdapter.setContext(applicationContext, this@ResultListActivity);
                    resultsAdapter.setResults(mSearchedResultsList);


                    mRecyclerView?.adapter = resultsAdapter
                    mRecyclerView?.invalidate()
                    mRecyclerView?.adapter?.notifyDataSetChanged()
                }else{
                    Toast.makeText(this, "Searched item not found", Toast.LENGTH_LONG).show()
                }
            }
        }
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSearchArtistName       =   intent.getStringExtra("ArtistName")
        mTrackName              =   intent.getStringExtra("TrackName")
        mCollectionName         =   intent.getStringExtra("CollectionName");

        mCollectionPrice        =   intent.getStringExtra("CollectionPrice");
        mReleaseDate            =   intent.getStringExtra("ReleaseDate");


        mSpinnerItems           =   ArrayList<String>()
        mSpinnerItems.add("Collection Name");
        mSpinnerItems.add("track name");
        mSpinnerItems.add("artist name");
        mSpinnerItems.add("collection price Descending");

        setContentView(com.android.myapplication.R.layout.result_list_activity)

        mRecyclerView           =   findViewById(com.android.myapplication.R.id.id_rv)
        mIdEmptyTv              =   findViewById(com.android.myapplication.R.id.id_empty_tv);

        mSearchIv               =   findViewById(com.android.myapplication.R.id.id_search_icon_iv);

        mSpinner                =   findViewById(com.android.myapplication.R.id.id_spinner);

        mCartButton             =   findViewById(com.android.myapplication.R.id.id_cart_btn);

        val repository          =   MyRepository(applicationContext, this)


        mCartButton.setOnClickListener(View.OnClickListener {

            if (mCartButton.text.toString().equals("CART")) {

                mCartButton.setText("LIST");
                var mResultArrayList: java.util.ArrayList<Results?> = java.util.ArrayList<Results?>()

                var resultsAdapter: ResultsAdapter = ResultsAdapter();
                resultsAdapter.setResults(mResultArrayList);
                mRecyclerView?.adapter = resultsAdapter
                mRecyclerView?.invalidate()
                mRecyclerView?.adapter?.notifyDataSetChanged()

                repository.getDbInstance().cartDAOAccess().fetchAllCartItems().observe(it.context as LifecycleOwner, Observer<List<CartItem?>> { cartItems ->

                    Log.d("Length", cartItems.size.toString());

                    val resultArrayList: java.util.ArrayList<Results?> = java.util.ArrayList<Results?>(cartItems.size)
                    for (item: CartItem? in cartItems) {
                        var artistName: String? = item?.getArtistName()
                        var collectionName: String? = item?.getCollectionName()
                        var trackName: String? = item?.getTrackName()
                        var collectionPrice: String? = item?.getCollectionPrice()
                        var releaseDate: String? = item?.getReleaseDate()

                        var resultObj: Results = Results()
                        resultObj.setArtistName(artistName);
                        resultObj.setCollectionName(collectionName);
                        resultObj.setTrackName(trackName)
                        resultObj.setCollectionPrice(collectionPrice)
                        resultObj.setReleaseDate(releaseDate)

                        resultArrayList.add(resultObj)

                        var resultsAdapter: ResultsAdapter = ResultsAdapter();
                        resultsAdapter.setContext(applicationContext, this@ResultListActivity);
                        resultsAdapter.setResults(resultArrayList);
                        mRecyclerView?.adapter = resultsAdapter
                        mRecyclerView?.invalidate()
                        mRecyclerView?.adapter?.notifyDataSetChanged()
                    }


                })
            } else {
                //show normal...

                restartActivity()

            }


        })

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, com.android.myapplication.R.layout.my_spinner_layout, mSpinnerItems)
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        mSpinner?.adapter = arrayAdapter

        mSpinner?.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                if (position == 0) {
                    //Collection Name..
                    var myRepository: MyRepository = MyRepository(applicationContext, view.context);
                    var lifeCycleOwner: LifecycleOwner = view.context as LifecycleOwner;
                    myRepository.getDbInstance().resultsDAOAccess().fetchAllResultsSortedByCollectionName().observe(lifeCycleOwner, Observer<List<Results?>> { results ->
                        val resultArrayList: java.util.ArrayList<Results?> = java.util.ArrayList<Results?>(results.size)
                        resultArrayList.addAll(results)

                        var resultsAdapter: ResultsAdapter = ResultsAdapter();
                        resultsAdapter.setContext(applicationContext, this@ResultListActivity);
                        resultsAdapter.setResults(resultArrayList);
                        mRecyclerView?.adapter = resultsAdapter
                        mRecyclerView?.invalidate()
                        mRecyclerView?.adapter?.notifyDataSetChanged()
                    })
                } else if (position == 1) {
                    // track name...
                    var myRepository: MyRepository = MyRepository(applicationContext, view.context);
                    var lifeCycleOwner: LifecycleOwner = view.context as LifecycleOwner;
                    myRepository.getDbInstance().resultsDAOAccess().fetchAllResultsSortedByTrackName().observe(lifeCycleOwner, Observer<List<Results?>> { results ->
                        val resultArrayList: java.util.ArrayList<Results?> = java.util.ArrayList<Results?>(results.size)
                        resultArrayList.addAll(results)

                        var resultsAdapter: ResultsAdapter = ResultsAdapter();
                        resultsAdapter.setContext(applicationContext, this@ResultListActivity);
                        resultsAdapter.setResults(resultArrayList);
                        mRecyclerView?.adapter = resultsAdapter
                        mRecyclerView?.invalidate()
                        mRecyclerView?.adapter?.notifyDataSetChanged()
                    })
                } else if (position == 2) {
                    //artist name..
                    var myRepository: MyRepository = MyRepository(applicationContext, view.context);
                    var lifeCycleOwner: LifecycleOwner = view.context as LifecycleOwner;
                    myRepository.getDbInstance().resultsDAOAccess().fetchAllResultsSortedByArtistName().observe(lifeCycleOwner, Observer<List<Results?>> { results ->
                        val resultArrayList: java.util.ArrayList<Results?> = java.util.ArrayList<Results?>(results.size)
                        resultArrayList.addAll(results)

                        var resultsAdapter: ResultsAdapter = ResultsAdapter();
                        resultsAdapter.setResults(resultArrayList);
                        mRecyclerView?.adapter = resultsAdapter
                        mRecyclerView?.invalidate()
                        mRecyclerView?.adapter?.notifyDataSetChanged()
                    })

                } else if (position == 3) {
                    //collection price descending..
                    var myRepository: MyRepository = MyRepository(applicationContext, view.context);
                    var lifeCycleOwner: LifecycleOwner = view.context as LifecycleOwner;
                    myRepository.getDbInstance().resultsDAOAccess().fetchAllResultsSortedByCollectionPriceDescending().observe(lifeCycleOwner, Observer<List<Results?>> { results ->
                        val resultArrayList: java.util.ArrayList<Results?> = java.util.ArrayList<Results?>(results.size)
                        resultArrayList.addAll(results)

                        var resultsAdapter: ResultsAdapter = ResultsAdapter();
                        resultsAdapter.setContext(applicationContext, this@ResultListActivity);
                        resultsAdapter.setResults(resultArrayList);
                        mRecyclerView?.adapter = resultsAdapter
                        mRecyclerView?.invalidate()
                        mRecyclerView?.adapter?.notifyDataSetChanged()
                    })
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        })

        mSearchIv.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@ResultListActivity, AlbumSearchActivity::class.java)
            startActivityForResult(intent, 1000)
        })

        mRecyclerView?.layoutManager = LinearLayoutManager(this.applicationContext);

        resultsList = ArrayList<Results>();

        mViewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        mViewModel!!.init(applicationContext, this)
        mViewModel!!.resultsLiveData.observe(this, Observer<ArrayList<Results>>() {


            var matchingResults: ArrayList<Results> = ArrayList()
            if (it != null) {

                for (i in 0..it.size - 1) {
                    var results: Results = it.get(i)

                    try {
                        var collectionName: String? = results.collectionName;
                    } catch (e: Exception) {
                        Log.e("", "");
                    }

                    try {
                        var collectionPrice: String? = results.collectionPrice;
                    } catch (e: Exception) {
                        Log.e("", "");
                    }
                    try {

                        var trackName: String? = results.trackName;
                    } catch (e: Exception) {
                        Log.e("", "");
                    }

                    try {
                        var releaseDate: String? = results.releaseDate;
                    } catch (e: Exception) {
                        Log.e("", "");
                    }


//                    if (doesCollectionNameMatch(collectionName)) {
//                            if (doesCollectionPriceMatch(collectionPrice)) {
//                                if (doesTrackNameMatch(trackName)) {
//                                    if (doesReleaseDateMatch(releaseDate)) {
//                                        matchingResults.add(results)
//                                    }
//                                }
//                            }
//                        }

                }


                resultsList?.addAll(it)
                var resultsAdapter: ResultsAdapter = ResultsAdapter();
                resultsAdapter.setContext(applicationContext, this@ResultListActivity);
                resultsAdapter.setResults(it);
//                if(matchingResults.size == 0){
//                    //No items found..
//                    //Show empty message...
//                    mIdEmptyTv.setText("Search Result Is Empty")
//
//                }

                mRecyclerView?.adapter = resultsAdapter
                mRecyclerView?.invalidate()
                mRecyclerView?.adapter?.notifyDataSetChanged()
            }
        })
    }

    fun performSearch() {
        mViewModel?.fetchApiResponse()
    }

    fun restartActivity() {
        val mIntent = intent
        finish()
        startActivity(mIntent)
    }

    fun doesCollectionNameMatch(collectionName: String):Boolean{
        if(collectionName==null){
            return false;
        }
        if(mCollectionName.equals("")){
            return false;
        }

        if(mCollectionName.equals(collectionName)){
            return true;
        }

        return false;
    }

    fun doesCollectionPriceMatch(collectionPrice: String):Boolean{
        if(collectionPrice == null){
            return false;
        }
        if(mCollectionPrice.equals("")){
            return false;
        }

        if(mCollectionPrice.equals(collectionPrice)){
            return true;
        }

        return false;
    }

    fun doesTrackNameMatch(trackName: String):Boolean{
        if(trackName==null){
            return false;
        }
        if(mTrackName.equals("")){
            return false;
        }

        if(mTrackName.equals(trackName)){
            return true;
        }

        return false;
    }

    fun doesReleaseDateMatch(releaseDate: String):Boolean{
        if(releaseDate==null){
            return false;
        }
        if(mReleaseDate.equals("")){
            return false;
        }

        if(mReleaseDate.equals(releaseDate)){
            return true;
        }

        return false;
    }
}