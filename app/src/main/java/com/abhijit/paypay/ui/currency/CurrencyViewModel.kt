package com.abhijit.paypay.ui.currency

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhijit.paypay.BuildConfig
import com.abhijit.paypay.R
import com.abhijit.paypay.data.remote.model.Currencies
import com.abhijit.paypay.data.remote.repository.FreeNowRepository
import com.abhijit.paypay.utils.NetworkHelper
import com.abhijit.paypay.utils.Resource
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.launch

//method for binding image in xml using glide
@BindingAdapter("profileImage")
fun loadImage(view: ImageView, imageUrl: String?) {
    Glide.with(view.getContext())
        .load(imageUrl).apply(RequestOptions().error(R.drawable.ic_baseline_person_24).circleCrop())
        .into(view)
}

//method for binding image in xml using glide
@BindingAdapter("passportImage")
fun loadPassportImage(view: ImageView, imageUrl: String?) {
    Glide.with(view.getContext())
        .load(imageUrl).apply(RequestOptions().error(R.drawable.ic_baseline_person_24))
        .into(view)
}

class DotcorsViewModel @ViewModelInject constructor(
    private val freeNowRepository: FreeNowRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {


    private val _currency = MutableLiveData<Resource<Currencies>>()
    val currency: LiveData<Resource<Currencies>>
        get() = _currency

    public fun fetchCurrency() {
        viewModelScope.launch {
            _currency.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                freeNowRepository.getCurrencyies(
                    BuildConfig.APIKEY

                ).let {
                    if (it.isSuccessful) {

                        _currency.postValue(Resource.success(it.body()))
                    } else {
                        _currency.postValue(Resource.error(it.errorBody().toString(), null))
                    }
                }
            } else {
                _currency.postValue(Resource.error("No internet connection", null))
            }

        }
    }


}