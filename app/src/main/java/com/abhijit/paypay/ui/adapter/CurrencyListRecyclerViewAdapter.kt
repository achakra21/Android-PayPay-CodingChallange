package com.abhijit.paypay.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.abhijit.paypay.R
import com.abhijit.paypay.databinding.FragmentCurrencyListBinding


class CurrencyListRecyclerViewAdapter(
        private val currencyName: List<String>,
        private val cuurencyValue: List<Double>,
        private val context: Context
) : BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var binding: FragmentCurrencyListBinding
    override fun getCount(): Int {
        return currencyName.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
    ): View? {


        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.fragment_currency_list,
                parent,
                false)
        binding.currencyNameObj = currencyName[position]
        binding.currencyValueObj = cuurencyValue[position].toString()
        return binding.root
    }


}


