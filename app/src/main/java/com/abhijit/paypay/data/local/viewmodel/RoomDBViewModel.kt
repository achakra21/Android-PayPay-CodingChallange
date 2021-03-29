package com.abhijit.paypay.data.local.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abhijit.paypay.data.local.db.CurrencyDatabase
import com.abhijit.paypay.data.local.entity.CurrencyEntity
import com.abhijit.paypay.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
class RoomDBViewModel @ViewModelInject constructor(private val currencyDatabse: CurrencyDatabase) :
        ViewModel() {

    private val currencies = MutableLiveData<Resource<List<CurrencyEntity>>>()


    fun getCurrencies(): LiveData<Resource<List<CurrencyEntity>>> {
        return currencies
    }

    suspend fun insertCurrencies(doctor: CurrencyEntity) {

        withContext(Dispatchers.IO) {
            currencyDatabse.currencyDao().insert(doctor)
        }
    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            currencyDatabse.currencyDao().deleteAll()
        }

    }

    suspend fun getAll(): List<CurrencyEntity>? {
        lateinit var data: List<CurrencyEntity>
        data = listOf()
        withContext(Dispatchers.IO) {
            data = currencyDatabse.currencyDao().getAll()
            return@withContext data
        }
        return data

    }
}