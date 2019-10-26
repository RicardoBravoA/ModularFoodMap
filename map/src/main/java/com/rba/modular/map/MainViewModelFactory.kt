package com.rba.modular.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rba.modular.mapd.data.repository.RestaurantDataRepository
import com.rba.modular.mapd.domain.usecase.RestaurantUseCase

class MainViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val restaurantUseCase =
                RestaurantUseCase(RestaurantDataRepository())
            MainViewModel(restaurantUseCase) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}