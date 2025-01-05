package com.example.mah_music

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.text.contains
import kotlin.text.filter
import kotlin.text.lowercase

lateinit var myRecycler: RecyclerView
lateinit var myAdapter: MyAdapter

class MainActivity : AppCompatActivity() {

    private var originalDataList: List<Data> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myRecycler = findViewById(R.id.recycler_View)
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://deezerdevs-deezer.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
// converting retrofit data into a variable
        val retrofitData = retrofitBuilder.getData("eminem")

        retrofitData.enqueue(
            object : Callback<MyData?> {
                override fun onResponse(
                    call: retrofit2.Call<MyData?>, response: retrofit2.Response<MyData?>
                ) {
// If the API call is a success than this method will be executed
                    val dataList = response.body()?.data!!
                    originalDataList = dataList // Store the original data list
                   
                    myAdapter = MyAdapter(this@MainActivity, dataList, lifecycleScope)
                    myRecycler.adapter = myAdapter
                    myRecycler.layoutManager = LinearLayoutManager(this@MainActivity)

                    Log.d("Response", "onResponse: " + response.body())
                }

                override fun onFailure(call: retrofit2.Call<MyData?>, t: Throwable) {
// If the API call is a failure than this method will be executed
                    Log.d("Failure", "onFailure: " + t.message)
                }

            })

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission
                performSearch(query ?: "")
                Toast.makeText(this@MainActivity, "Searching for: $query", Toast.LENGTH_SHORT)
                    .show()
               
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle text changes in the search bar
               
                performSearch(newText ?: "")
                return true
            }
        })
    }
    fun performSearch(query: String) {
        val filteredData = if (query.isEmpty()) {
            originalDataList// Adjust filtering criteria as needed
        } else {
            originalDataList.filter { data ->
                data.title.lowercase().contains(query.lowercase())
            }
        }
        myAdapter.updateData(filteredData) 
    }
    }
