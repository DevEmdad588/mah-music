package com.example.mah_music

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

lateinit var myRecycler: RecyclerView
lateinit var myAdapter: MyAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
// creating retrofit builder object

        myRecycler=findViewById(R.id.recycler_View)
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
                    call: retrofit2.Call<MyData?>,response: retrofit2.Response<MyData?>) {
// If the API call is a success than this method will be executed
                    val dataList = response.body()?.data !!
             //       val textView = findViewById<TextView>(R.id.hlwView)
             //       textView.text = dataList.toString()
                   // Log.d("Response", "onResponse: $dataList")
                    myAdapter = MyAdapter(this@MainActivity,dataList, lifecycleScope)
                    myRecycler.adapter = myAdapter
                    myRecycler.layoutManager= LinearLayoutManager(this@MainActivity)

                    Log.d("Response", "onResponse: "+ response.body())
                }
                override fun onFailure(call: retrofit2.Call<MyData?>, t: Throwable) {
// If the API call is a failure than this method will be executed
                    Log.d("Failure", "onFailure: "+ t.message)
                }

                })
    }
}