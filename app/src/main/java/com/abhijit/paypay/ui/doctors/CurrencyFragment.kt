package com.abhijit.paypay.ui.doctors


import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.abhijit.paypay.R
import com.abhijit.paypay.data.local.entity.CurrencyEntity
import com.abhijit.paypay.data.local.viewmodel.RoomDBViewModel
import com.abhijit.paypay.databinding.FragmentCurrencyBinding
import com.abhijit.paypay.ui.adapter.CurrencyListRecyclerViewAdapter
import com.abhijit.paypay.utils.CommonUtils
import com.abhijit.paypay.utils.Status
import com.abhijit.paypay.utils.TimeInHours
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_currency.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CurrencyFragment : Fragment() {

    private val doctorViewModel: DotcorsViewModel by viewModels()
    private val roomDBViewModel: RoomDBViewModel by viewModels()
    private lateinit var binding: FragmentCurrencyBinding
    var currencyList: MutableList<String> = ArrayList()
    var currencyValue: MutableList<Double> = ArrayList()
    var masterCurValue: MutableList<Double> = ArrayList()
    var currencyMap: HashMap<String, Double> = HashMap<String, Double>()
    var currencyData: List<CurrencyEntity> = ArrayList()
    var isMoreThan2Hours: Boolean = false
    var currencyName: String = ""

    //var timeInHours: TimeInHours = null
    lateinit var dateMonthYear: String
    var date: Int = 0
    var month: Int = 0
    var year: Int = 0


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
        setupObserver()
    }

    override fun onResume() {
        super.onResume()

        //when back button press the below method called
        requireActivity().supportFragmentManager.addOnBackStackChangedListener {
            val fm = requireActivity().supportFragmentManager
            fm?.let {
                if (it.backStackEntryCount == 0) {

                }
            }
        }
        initSpinner(currencyList)
        renderList()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupObserver() {
        roomDBViewModel.getCurrencies().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { localCurrencyData ->
                        if (isMoreThan2Hours) {
                            var map = localCurrencyData[0].quotes


                        }

                    }

                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    //Handle Error
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()

                }
            }
        })

        doctorViewModel.currency.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    //progressBar.visibility = View.GONE
                    it.data?.let { currency ->

                        //iterate over the JSON Object
                        var obj = Gson().toJsonTree(currency.quotes).getAsJsonObject()
                        for ((key, value) in obj.entrySet()) {
                            currencyList.add(key)
                            currencyValue.add(value.asDouble)
                            currencyMap.put(key, value.asDouble)
                            println("Key = $key Value = $value")
                        }
                        masterCurValue.addAll(currencyValue.filterNotNull())
                        initSpinner(currencyList)
                        GlobalScope.launch {

                            roomDBViewModel.deleteAll()
                            var doc = CurrencyEntity(0, currency.privacy, currencyMap, currency.source, currency.privacy, currency.terms, System.currentTimeMillis() / 1000)
                            roomDBViewModel.insertCurrencies(doc)
                        }

                        // }
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        startFetching()

    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_currency, container, false)
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                currencyValue.clear()
                if (p0 != null && !p0.equals("")) {


                    for ((key, value) in currencyMap) {

                        if (!key.equals(currencyName)) {

                            currencyValue.add(value * p0.toDouble())

                        } else {
                            currencyValue.add(value)
                        }

                    }


                }
                if (p0 != null) {
                    if (p0.isEmpty()) {
                        currencyValue.addAll(masterCurValue.filterNotNull())
                    }
                }

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                currencyValue.clear()
                if (p0 != null && !p0.equals("")) {

                    for ((key, value) in currencyMap) {
                        if (!key.equals(currencyName)) {

                            currencyValue.add(value * p0.toDouble())

                        } else {
                            currencyValue.add(value)
                        }

                    }


                }
                if (p0.equals("")) {
                    currencyValue.addAll(masterCurValue.filterNotNull())
                }
                renderList()
                return false
            }
        })


        return binding.root
    }

    private fun renderList() {


        if (binding.gridView is GridView) {
            with(binding.gridView) {

                val mainAdapter = CurrencyListRecyclerViewAdapter(currencyList, currencyValue, context)
                gridView.adapter = mainAdapter

            }
        }
    }

    fun <Doctor> concatenate(vararg lists: List<Doctor>): List<Doctor> {
        return listOf(*lists).flatten()
    }

    private fun initSpinner(currencyList: List<String>) {

        spinner?.adapter = activity?.applicationContext?.let { it1 ->
            ArrayAdapter(it1, R.layout.support_simple_spinner_dropdown_item, currencyList)
        } as SpinnerAdapter
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("erreur")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val type = parent?.getItemAtPosition(position).toString()
                currencyName = type
            }
        }
    }

    private fun startFetching() {
        var curTimeHourMin: TimeInHours? = null
        var curDateMonthYear: String
        var curDateObj: List<String>
        var currentMonth = 0
        var currentDate = 0
        var currentYear = 0
        var timeInHours: TimeInHours? = null;
        GlobalScope.launch {

            //roomDBViewModel.deleteAll()
            currencyData = roomDBViewModel.getAll()!!

            if (currencyData.size != 0) {
                //stored timestamps
                timeInHours = CommonUtils.convertFromDuration(currencyData[0].timestamp)
                dateMonthYear = CommonUtils.getDateTime(currencyData[0].timestamp).toString()
                val dateObj = dateMonthYear.split('/')
                month = dateObj[0].toInt()
                date = dateObj[1].toInt()
                year = dateObj[2].toInt()
            }
            //current timestamps
            curTimeHourMin = CommonUtils.convertFromDuration(System.currentTimeMillis() / 1000)
            curDateMonthYear = CommonUtils.getDateTime(System.currentTimeMillis() / 1000).toString()
            curDateObj = curDateMonthYear.split('/')
            currentMonth = curDateObj[0].toInt()
            currentDate = curDateObj[1].toInt()
            currentYear = curDateObj[2].toInt()

            if (currentYear >= year && currentMonth >= month && currentDate >= date) {
                if (timeInHours != null) {
                    if (((curTimeHourMin!!.hours - timeInHours!!.hours) * 60 +
                                    (curTimeHourMin!!.minutes - timeInHours!!.minutes) +
                                    (curTimeHourMin!!.seconds - timeInHours!!.seconds) / 60) > 2 * 60) {
                        isMoreThan2Hours = true


                    }
                }
            }

            if (isMoreThan2Hours || currencyData.size == 0) {
                doctorViewModel.fetchCurrency()
            } else {
                var localData = currencyData[0].quotes
                for ((key, value) in localData) {
                    currencyList.add(key)
                    currencyValue.add(value)
                    currencyMap.put(key, value)
                }
                // masterCurValue = currencyValue
                masterCurValue.addAll(currencyValue.filterNotNull())
                initSpinner(currencyList)

            }


        }


    }


}