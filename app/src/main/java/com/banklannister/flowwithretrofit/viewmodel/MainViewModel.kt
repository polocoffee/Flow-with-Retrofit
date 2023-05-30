package com.banklannister.flowwithretrofit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.banklannister.flowwithretrofit.repository.ApiRepository
import com.banklannister.flowwithretrofit.response.ResponseCoinsMarkets
import com.banklannister.flowwithretrofit.response.ResponseDetailsCoin
import com.banklannister.flowwithretrofit.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {

    //LiveData
    private val _coinsList =
        MutableLiveData<DataStatus<List<ResponseCoinsMarkets.ResponseCoinsMarketsItem>>>()
    val coinsList: LiveData<DataStatus<List<ResponseCoinsMarkets.ResponseCoinsMarketsItem>>>
        get() = _coinsList


    private val _detailCoin =
        MutableLiveData<DataStatus<ResponseDetailsCoin>>()
    val detailCoin: LiveData<DataStatus<ResponseDetailsCoin>>
        get() = _detailCoin


    //Function
    fun getCoinsList(vs_currency: String) = viewModelScope.launch {
        repository.getCoinsLIst(vs_currency).collect {
            _coinsList.value = it
        }
    }

    fun getDetailCoin(id: String) = viewModelScope.launch {
        repository.getDetailCoin(id).collect {
            _detailCoin.value = it
        }
    }
}