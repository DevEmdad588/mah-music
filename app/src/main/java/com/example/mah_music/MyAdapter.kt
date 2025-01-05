package com.example.mah_music

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyAdapter
    (val context: Activity, val datalist: List<Data>,private val lifecycleScope: LifecycleCoroutineScope)
    :RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    private var adataList: List<Data> = datalist

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        //it will hold the view


        val picture :ImageView
        val title: TextView
     //   val play: ImageButton
     //   val pause: ImageButton

        init{
            picture= itemView.findViewById(R.id.musicImage)
            title= itemView.findViewById(R.id.textView)

        //    play= itemView.findViewById(R.id.playButton)
        //    pause= itemView.findViewById(R.id.pauseButton)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //it will create view if layout manager  fails to do so
      //  val itemView= context.layoutInflater.inflate(R.layout.each_item,parent,false)
        val itemView= LayoutInflater.from(context).inflate(R.layout.each_item,parent,false)
        return MyViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        //it will return the size of the list
       return datalist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // we will populate the data here


        val currentData= datalist[position]
        holder.title.text= currentData.title
        Picasso.get().load(currentData.album.cover).into(holder.picture)


            holder.itemView.setOnClickListener {
                val intent = Intent(context, MusicPlayerActivity::class.java)
                intent.putExtra("title", currentData.title)
                intent.putExtra("artist",currentData.artist.name)
                intent.putExtra("albumArt", currentData.album.cover)
                intent.putExtra("previewUrl", currentData.preview)

                // Pass the song list
                intent.putParcelableArrayListExtra("songList", ArrayList(datalist))
                context.startActivity(intent)
        }

      //  holder.play.visibility= View.VISIBLE
       // holder.pause.visibility= View.GONE

        //override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //     val currentData = datalist[position]
        //     holder.title.text= currentData.title
        //     Picasso.get().load(currentData.album.cover).into(holder.picture)
        //
        //     holder.itemView.setOnClickListener {
        //         val intent = Intent(context, MusicPlayerActivity::class.java)
        //         intent.putExtra("title", currentData.title)
        //         intent.putExtra("artist", currentData.artist.name)
        //         intent.putExtra("albumArt", currentData.album.cover)
        //         intent.putExtra("previewUrl", currentData.preview)
        //         context.startActivity(intent)
        //     }
        // }

    }
    fun updateData(newData: List<Data>) {
        adataList = datalist
        notifyDataSetChanged()
    }

    }

// var musicPlayer : MediaPlayer? = null
//        holder.play.setOnClickListener {
//            lifecycleScope.launch(Dispatchers.IO) {
//                if(musicPlayer== null) {
//                    //creating media player
//                    musicPlayer = MediaPlayer.create(context, currentData.preview.toUri())
//                }
//                musicPlayer?.start()
//            }
//        }
//        holder.pause.setOnClickListener {
//            lifecycleScope.launch(Dispatchers.IO) {
//                musicPlayer?.pause()
//            }
//        }