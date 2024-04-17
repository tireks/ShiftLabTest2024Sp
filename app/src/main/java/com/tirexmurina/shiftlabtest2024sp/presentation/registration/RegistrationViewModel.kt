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

    fun initializeScreen(){
        viewModelScope.launch {
            _state.value = RegistrationState.Loading
            //тут будет тест на эксепшн, если да - то стартуем форму, если нет, то стейт скип,
            // пока просто:
            lockControl()
        }
    }

    fun validateParam(
        param: UserParam,
        content: String?
    ) {
        viewModelScope.launch{
            if (content.isNullOrEmpty()){
                if (!emptyParamList.contains(param)){
                    emptyParamList.add(param)
                    wrongParamList.remove(param)
                }
            } else{
                emptyParamList.remove(param)

                val isParamValid = isParamValid(param, content)
                val paramInWrongs = wrongParamList.contains(param)

                if (isParamValid && paramInWrongs) {
                    wrongParamList.remove(param)
                } else if (!isParamValid && !paramInWrongs) {
                    wrongParamList.add(param)
                }
            }
            lockControl()
        }
    }

    private fun isParamValid(param: UserParam, content: String) : Boolean{
        return when(param){
            UserParam.Name -> isNameValid(content)

            UserParam.Surname -> isNameValid(content)

            UserParam.Birthdate -> isBirthdateValid(content)

            UserParam.Password -> {
                currentPassword = content
                isPasswordValid(content)
            }

            UserParam.PasswordConf -> isPasswordConfValid(content)
        }
    }

    private fun isNameValid(validationData : String) : Boolean{

        val containsOnlyLetters = Regex("[a-zA-Z]*").matches(validationData)
        return (validationData.length > 2 && containsOnlyLetters)
    }

    private fun isBirthdateValid(validationData : String): Boolean{
        return true // todo это пока заглушка
    }

    private fun isPasswordValid(validationData : String) : Boolean{

        val containsDigits = Regex("\\d").containsMatchIn(validationData)
        val containsUpperCaseLetters = Regex("[A-Z]").containsMatchIn(validationData)
        val containsLowerCaseLetters = Regex("[a-z]").containsMatchIn(validationData)

        return containsDigits && containsUpperCaseLetters && containsLowerCaseLetters
    }

    private fun isPasswordConfValid(validationData : String): Boolean{
        return (validationData == currentPassword)
    }

    private fun lockControl(){
        if (emptyParamList.isEmpty() && wrongParamList.isEmpty()){
            _state.value = RegistrationState.Content.Unlocked
            Log.d("MyTag", "FUCK YEAH")
        } else {
            _state.value = RegistrationState.Content.Locked(wrongParamList)
            Log.d("MyTag", "Содержимое emptyParamList:")
            emptyParamList.forEach { param ->
                Log.d("MyTag", param.name) // или другое представление параметра, если необходимо
            }

            Log.d("MyTag", "Содержимое wrongParamList:")
            wrongParamList.forEach { param ->
                Log.d("MyTag", param.name) // или другое представление параметра, если необходимо
            }
            Log.d("MyTag", "--------------------")
        }
    }

    fun navigateUser(name: String, surname : String){
        //его можно было бы создать User и прямо во фрагменте, но зачем лишние зависимости от домейна,
        // фрагменту все равно сущность User как таковая не нужна
        viewModelScope.launch {
            if (name.isEmpty() || surname.isEmpty()){
                _state.value = RegistrationState.Error.CorruptedDataFromForm
            }
            val user = User(name, surname)
            _state.value = RegistrationState.Loading
            try {
                register(user)
                deleteUserUseCase() //todo это пока чтобы базу не засорять
                _state.value = RegistrationState.Skip
            } catch (unknownException : Exception){
                _state.value = RegistrationState.Error.Unknown
            }
        }
    }

    private suspend fun register(user: User) {
        saveUserUseCase(user)
    }
}