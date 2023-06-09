package com.banklannister.flowwithretrofit.repository

import com.banklannister.flowwithretrofit.api.ApiService
import com.banklannister.flowwithretrofit.utils.DataStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiService: ApiService) {


    suspend fun getCoinsLIst(vs_currency: String) = flow {
        emit(DataStatus.loading())
        val result = apiService.getCoins(vs_currency)
        when (result.code()) {
            200 -> {
                emit(DataStatus.success(result.body()))
            }

            400 -> {
                emit(DataStatus.error(result.message()))
            }

            500 -> {
                emit(DataStatus.error(result.message()))
            }

        }

    }.catch {
        emit(DataStatus.error(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    suspend fun getDetailCoin(id: String) = flow {
        emit(DataStatus.loading())
        val result = apiService.getDetailCoin(id)
        when (result.code()) {
            200 -> {
                emit(DataStatus.success(result.body()))
            }

            400 -> {
                emit(DataStatus.error(result.message()))
            }

            500 -> {
                emit(DataStatus.error(result.message()))
            }

        }

    }.catch {
        emit(DataStatus.error(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}