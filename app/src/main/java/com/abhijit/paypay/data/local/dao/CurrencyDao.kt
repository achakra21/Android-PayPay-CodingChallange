package com.abhijit.paypay.data.local.dao

import androidx.room.*
import com.abhijit.paypay.data.local.entity.CurrencyEntity


@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currency")
    fun getAll(): List<CurrencyEntity>

    @Insert
    fun insertAll(doctor: List<CurrencyEntity>)

    @Delete
    fun deleteAll(doctor: CurrencyEntity)

    @Query("DELETE FROM currency")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(doc: CurrencyEntity)


}