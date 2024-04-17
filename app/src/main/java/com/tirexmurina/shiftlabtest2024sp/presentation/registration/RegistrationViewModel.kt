package com.tirexmurina.shiftlabtest2024sp.presentation.registration

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tirexmurina.shiftlabtest2024sp.domain.entity.User
import com.tirexmurina.shiftlabtest2024sp.domain.usecase.DeleteUserUseCase
import com.tirexmurina.shiftlabtest2024sp.domain.usecase.GetUserUseCase
import com.tirexmurina.shiftlabtest2024sp.domain.usecase.SaveUserUseCase
import com.tirexmurina.shiftlabtest2024sp.presentation.home.RegistrationState
import com.tirexmurina.shiftlabtest2024sp.utils.UserParam
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) : ViewModel() {
    private val _state = MutableLiveData<RegistrationState>(RegistrationState.Initial)
    val state: LiveData<RegistrationState> = _state

    private val emptyParamList = mutableListOf(
        UserParam.Name,
        UserParam.Surname,
        /*UserParam.Birthdate,*/
        UserParam.Password,
        UserParam.PasswordConf
    )

    private var currentPassword : String = ""

    private val wrongParamList = mutableListOf<UserParam>()
}