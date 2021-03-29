package com.abhijit.paypay.data.local.entity

import androidx.room.*

import java.io.Serializable


@Entity(tableName = "currency")
data class CurrencyEntity(

        @PrimaryKey(autoGenerate = true) var id:Int,
        @ColumnInfo(name="privacy") var privacy: String,
        @ColumnInfo (name="quotes") var quotes: Map<String,Double>,
        @ColumnInfo(name="source") var source: String,
        @ColumnInfo(name="success") var success: String,
        @ColumnInfo(name="terms") var terms: String,
        @ColumnInfo(name="timestamp") var timestamp: Long

): Serializable




