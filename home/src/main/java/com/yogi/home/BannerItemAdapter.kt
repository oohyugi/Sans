package com.yogi.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yogi.core.model.YtItemsMdl
import android.app.Activity
import android.util.DisplayMetrics
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.yogi.core.SharedViewModel
import com.yogi.core.model.YtIsPlayingNowMdl


/**
 * Created by oohyugi on 2019-08-24.
 * github: https://github.com/oohyugi
 */
class BannerItemAdapter(private val context: Context?, private val list: List<YtItemsMdl>, private val playlistName:String?) :
    RecyclerView.Adapter<BannerItemAdapter.ViewHolder>(){

    lateinit var sharedViewModel: SharedViewModel

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivPoster = itemView.findViewById<ImageView>(R.id.iv_poster)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_banner_discovery, parent, false)
        sharedViewModel = ViewModelProviders.of(context as FragmentActivity).get(SharedViewModel::class.java)




        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val title = item.snippet.title.toLowerCase()
        val displaymetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displaymetrics)
        //if you need three fix imageview in width
        val devicewidth = displaymetrics.widthPixels / 1.4

        holder.itemView.layoutParams.width= devicewidth.toInt()
        context?.let { Glide.with(it).load(item.snippet.thumbnails.high.url).apply(RequestOptions().override(devicewidth.toInt(),170).centerCrop()).into(holder.ivPoster) }
//        holder.tvTitle.text = title.capitalize()
        holder.itemView.setOnClickListener {
            playMusic(position,list)
        }


    }


    override fun getItemCount(): Int {
        return list.size
    }

    private fun playMusic(position: Int, list: List<YtItemsMdl>) {

        sharedViewModel.startPlaying.postValue(YtIsPlayingNowMdl(list,position,playlistName))
    }




}