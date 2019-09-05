package com.yogi.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.yogi.core.model.YtItemsMdl
import com.yogi.core.utils.DateHelper
import com.yogi.core.utils.MarginItemDecoration
import com.yogi.core.utils.PrefHelper
import com.yogi.core.utils.getDateNow
import kotlinx.android.synthetic.main.home_fragment.*


class HomeFragment : Fragment(), DiscoveryAdapter.OnDiscoveryAdapterListener {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var mAdapterDiscovery: DiscoveryAdapter
    private var mListDiscovery: MutableList<YtItemsMdl> = mutableListOf()
    private var mListDiscoveryItems: MutableList<YtItemsMdl> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        initDiscoveryItem()
        val lastUpdate = PrefHelper.getDiscovery(context)?.lastUpdate
        if (lastUpdate != context.getDateNow(DateHelper.DATE_FORMAT_V1)) {
            viewModel.getYtResponseDiscovery().observe(this, Observer {
                Log.wtf("dataHome", Gson().toJson(it.items!![0]))
                it.lastUpdate = context.getDateNow(DateHelper.DATE_FORMAT_V1)
                PrefHelper.saveDiscovery(context,it)
                mListDiscovery.addAll(it.items!!)

                mAdapterDiscovery.notifyDataSetChanged()
            })
        } else {
            PrefHelper.getDiscovery(context)?.items?.let {
                mListDiscovery.addAll(it)
                mAdapterDiscovery.notifyDataSetChanged()
            }
        }

    }


    override fun onItemSelected(item: YtItemsMdl, position: Int) {
    }


    private fun initDiscoveryItem() {
        mAdapterDiscovery = DiscoveryAdapter(context, mListDiscovery, this)
        rv_discovery.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            addItemDecoration(MarginItemDecoration(32,MarginItemDecoration.TYPE_VERTICAL))
//            setRecycledViewPool(RecyclerView.RecycledViewPool())
            adapter = mAdapterDiscovery
        }



    }

}
