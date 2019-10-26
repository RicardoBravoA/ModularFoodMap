package com.rba.modular.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rba.modular.mapd.domain.usecase.RestaurantUseCase
import com.rba.modular.model.model.ErrorModel
import com.rba.modular.model.model.RestaurantModel
import com.rba.modular.util.ScopedViewModel
import com.rba.modular.util.domain.ResultType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel(private val restaurantUseCase: RestaurantUseCase) : ScopedViewModel() {

    private val mutableModel = MutableLiveData<UiViewModel>()
    val model: LiveData<UiViewModel>
        get() {
            if (mutableModel.value == null) refresh()
            return mutableModel
        }

    init {
        initScope()
    }

    fun refresh() {
        mutableModel.value = UiViewModel.Refresh
    }

    fun getData(latitude: String, longitude: String) {
        GlobalScope.launch(Dispatchers.Main) {
            when (val result = restaurantUseCase.getRestaurantList(latitude, longitude)) {
                is ResultType.Success -> mutableModel.value = UiViewModel.ShowData(result.value)
                is ResultType.Error -> mutableModel.value = UiViewModel.ShowError(result.value)
            }
        }

    }

    fun onClickRestaurant(restaurantModel: RestaurantModel) {
        mutableModel.value = UiViewModel.Navigation(restaurantModel)
    }

    sealed class UiViewModel {
        class ShowData(val list: List<RestaurantModel>) : UiViewModel()
        class ShowError(val errorModel: ErrorModel) : UiViewModel()
        class Navigation(val restaurantModel: RestaurantModel) : UiViewModel()
        object Refresh : UiViewModel()
    }

    override fun onCleared() {
        destroyScope()
        super.onCleared()
    }

}