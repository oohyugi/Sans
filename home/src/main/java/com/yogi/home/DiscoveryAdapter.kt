package com.yogi.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yogi.core.utils.MarginItemDecoration
import com.yogi.core.utils.capitalizeWords
import com.yogi.core.model.YtItemsMdl

/**
 * Created by oohyugi on 2019-08-24.
 * github: https://github.com/oohyugi
 */
class DiscoveryAdapter(private val context: Context?, private val list: List<YtItemsMdl>,private val callBack:OnDiscoveryAdapterListener) :
    RecyclerView.Adapter<DiscoveryAdapter.ViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
        val tvMore = itemView.findViewById<TextView>(R.id.tv_more)
        val rvItemMusic = itemView.findViewById<RecyclerView>(R.id.rv_item_music)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val  view = inflater.inflate(R.layout.item_discovery, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        setDefaultContent(holder,item,position)


    }


    override fun getItemCount(): Int {
        return list.size
    }



    private fun setDefaultContent(
        holder: ViewHolder,
        item: YtItemsMdl,
        position: Int
    ) {
        val title = item.snippet.title.toLowerCase()
        holder.tvTitle.text = title.replace("tracks -", "charts").replace("videos -", "").capitalizeWords()
        var childLayoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        holder.rvItemMusic.apply {
            layoutManager = childLayoutManager
            adapter = if (position==0){
                holder.tvTitle.visibility = View.GONE
                holder.tvMore.visibility = View.GONE
                BannerItemAdapter(context,item.contentItems, title.replace("tracks -", "charts").replace("videos -", "").capitalizeWords())
            }else{
                DefaultItemAdapter(context,item.contentItems, title.replace("tracks -", "charts").replace("videos -", "").capitalizeWords())
            }

//            setRecycledViewPool(viewPool)
        }

    }


    interface OnDiscoveryAdapterListener {
        fun onItemSelected(item: YtItemsMdl, position: Int)
    }

}