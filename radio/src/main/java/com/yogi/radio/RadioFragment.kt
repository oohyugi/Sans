package com.yogi.radio

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.yogi.core.model.RadioItemsMdl
import com.yogi.core.model.ResponseRadioMdl
import com.yogi.core.utils.MarginItemDecoration
import kotlinx.android.synthetic.main.radio_fragment.*


class RadioFragment : Fragment() {

    companion object {
        fun newInstance() = RadioFragment()
    }

    private lateinit var viewModel: RadioViewModel
    lateinit var adapterRadio: RadioItemAdapter
    private var mListRadioItems: MutableList<ResponseRadioMdl> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.radio_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RadioViewModel::class.java)
        // TODO: Use the ViewModel

        initItemRadio()
        viewModel.getResponseApiListRadio().observe(this, Observer {
            mListRadioItems.addAll(it)
            adapterRadio.notifyDataSetChanged()
        })
    }

    private fun initItemRadio() {
        adapterRadio = RadioItemAdapter(activity,mListRadioItems)
        rv_radio?.apply {
            layoutManager = GridLayoutManager(activity,3)
            adapter  = adapterRadio
            addItemDecoration(MarginItemDecoration(14,MarginItemDecoration.TYPE_GRID_HORIZONTAL,3))
        }
        adapterRadio.notifyDataSetChanged()
    }

}
