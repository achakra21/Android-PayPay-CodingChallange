package com.abhijit.paypay.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.abhijit.paypay.data.local.entity.CurrencyEntity
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CommonUtils {
    companion object {
        lateinit var doc: List<CurrencyEntity>

        @JvmStatic
        fun getAddress(context: Context, latitude: Double, longitude: Double): String {
            var geocoder = Geocoder(context, Locale.getDefault())
            var address = ""
            address = try {
                var addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
                addresses[0].getAddressLine(0)

            } catch (e: IOException) {
                e.message.toString()
            }
            return address
        }

        @JvmStatic
        fun setRecentDoctor(doc: List<CurrencyEntity>) {
            this.doc = doc

        }

        @JvmStatic
        fun getRecentDoctor(): List<CurrencyEntity> {
            return doc
        }

        @JvmStatic
        fun convertFromDuration(timeInSeconds: Long): TimeInHours {
            var time = timeInSeconds
            val hours = time / 3600
            time %= 3600
            val minutes = time / 60
            time %= 60
            val seconds = time
            return TimeInHours(hours.toInt(), minutes.toInt(), seconds.toInt())
        }
       @JvmStatic
       fun getDateTime(timestamp:  Long): String? {
            try {
                val sdf = SimpleDateFormat("MM/dd/yyyy")
                val netDate = Date(timestamp * 1000)
                return sdf.format(netDate)
            } catch (e: Exception) {
                return e.toString()
            }
        }

    }


}