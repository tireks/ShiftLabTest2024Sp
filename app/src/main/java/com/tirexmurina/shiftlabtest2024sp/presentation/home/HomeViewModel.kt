package com.tirexmurina.shiftlabtest2024sp.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tirexmurina.shiftlabtest2024sp.data.exceptions.EmptyTableException
import com.tirexmurina.shiftlabtest2024sp.data.exceptions.SizeException
import com.tirexmurina.shiftlabtest2024sp.domain.usecase.DeleteUserUseCase
import com.tirexmurina.shiftlabtest2024sp.domain.usecase.GetUserUseCase
import com.tirexmurina.shiftlabtest2024sp.domain.usecase.SaveUserUseCase
import com.tirexmurina.shiftlabtest2024sp.presentation.registration.RegistrationState
import com.tirexmurina.shiftlabtest2024sp.utils.UserParam
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) : ViewModel() {

    private val _state = MutableLiveData<HomeState>(HomeState.Initial)
    val state: LiveData<HomeState> = _state

    fun initializeScreen(){
        _state.value = HomeState.Content(null)
    }

    fun getUser(){
        viewModelScope.launch {
            _state.value = HomeState.Loading
            try {
                val user = getUserUseCase()
                _state.value = HomeState.Content(user)
            } catch (emptyException : EmptyTableException){
                _state.value = HomeState.Error.EmptyTable
            } catch (garbageException : SizeException){
                _state.value = HomeState.Error.CorruptedDataFromDataSource
            } catch (unknownException : Exception){
                _state.value = HomeState.Error.Unknown
            }
        }
    }

    fun deleteUser(){
        viewModelScope.launch {
            _state.value = HomeState.Loading
            try {
                deleteUserUseCase()
                _state.value = HomeState.Return
            } catch (unknownException : Exception){
                _state.value = HomeState.Error.Unknown
            }
        }

    }

}