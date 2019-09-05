package com.yogi.radio

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yogi.core.SharedViewModel
import com.yogi.core.model.ResponseRadioMdl


/**
 * Created by oohyugi on 2019-08-24.
 * github: https://github.com/oohyugi
 */
class RadioItemAdapter(private val context: Context?, private val list: List<ResponseRadioMdl>) :
    RecyclerView.Adapter<RadioItemAdapter.ViewHolder>() {


    private val onItemClickListener: AdapterView.OnItemClickListener? = null
    var playerDialogFrag: BottomSheetDialogFragment? = null
    lateinit var sharedViewModel: SharedViewModel

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
        val ivPoster = itemView.findViewById<ImageView>(R.id.iv_poster)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)



        val view = inflater.inflate(R.layout.item_radio, parent, false)
        sharedViewModel = ViewModelProviders.of(context as FragmentActivity).get(SharedViewModel::class.java)




        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val title = item.title.toLowerCase()
        context?.let {
            Glide.with(it).load(item.img).apply(RequestOptions().centerCrop())
                .into(holder.ivPoster)
        }
        holder.tvTitle.text = title.capitalize()
        holder.itemView.setOnClickListener {
//            showPlayer(position,list)
            playMusic(position, item)


        }


    }

    private fun playMusic(position: Int, list: ResponseRadioMdl) {
        var index =0
//        for (itemMdl in list) {
//            index++
//            if (index==1)
//
//
//        }

        sharedViewModel.startPlayingRadio.postValue(list)
    }


    override fun getItemCount(): Int {
        return list.size
    }



}