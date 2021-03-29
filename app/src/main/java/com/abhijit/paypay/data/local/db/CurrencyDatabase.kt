package com.abhijit.paypay.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.abhijit.paypay.data.local.dao.CurrencyDao
import com.abhijit.paypay.data.local.entity.CurrencyEntity

@Database(entities = [CurrencyEntity::class],version = 2, exportSchema = false)
@TypeConverters(mapConverter::class)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun currencyDao():CurrencyDao
}