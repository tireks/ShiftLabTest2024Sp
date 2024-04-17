package com.tirexmurina.shiftlabtest2024sp.presentation.registration

import com.tirexmurina.shiftlabtest2024sp.utils.UserParam

sealed interface RegistrationState {

    data object Initial : RegistrationState

    data object Loading : RegistrationState

    data object Skip : RegistrationState

    sealed interface Error : RegistrationState {

        data object EmptyTable : Error

        data object CorruptedDataFromDataSource : Error

        data object Unknown : Error

        data object CorruptedDataFromForm : Error
    }

    sealed interface Content : RegistrationState {

        data class Locked(val troubleList: List<UserParam>) : Content

        data object Unlocked : Content
    }

}