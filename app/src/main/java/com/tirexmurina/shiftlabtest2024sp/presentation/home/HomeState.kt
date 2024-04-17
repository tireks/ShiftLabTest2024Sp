package com.tirexmurina.shiftlabtest2024sp.presentation.home

import com.tirexmurina.shiftlabtest2024sp.domain.entity.User
import com.tirexmurina.shiftlabtest2024sp.presentation.registration.RegistrationState

sealed interface HomeState {

    data object Initial : HomeState

    data object Loading : HomeState

    data class Content(val user: User?) : HomeState

    data object Return : HomeState

    sealed interface Error : HomeState {

        data object EmptyTable : Error

        data object CorruptedDataFromDataSource : Error

        data object Unknown : Error
    }
}