package com.tirexmurina.shiftlabtest2024sp.presentation.registration

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tirexmurina.shiftlabtest2024sp.data.exceptions.EmptyTableException
import com.tirexmurina.shiftlabtest2024sp.data.exceptions.SizeException
import com.tirexmurina.shiftlabtest2024sp.domain.entity.User
import com.tirexmurina.shiftlabtest2024sp.domain.usecase.DeleteUserUseCase
import com.tirexmurina.shiftlabtest2024sp.domain.usecase.GetUserUseCase
import com.tirexmurina.shiftlabtest2024sp.domain.usecase.SaveUserUseCase
import com.tirexmurina.shiftlabtest2024sp.utils.DateUtils
import com.tirexmurina.shiftlabtest2024sp.utils.UserParam
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val dateUtils: DateUtils
) : ViewModel() {
    private val _state = MutableLiveData<RegistrationState>(RegistrationState.Initial)
    val state: LiveData<RegistrationState> = _state

    private val emptyParamList = mutableListOf(
        UserParam.Name,
        UserParam.Surname,
        UserParam.Birthdate,
        UserParam.Password,
        UserParam.PasswordConf
        //лист для хранения текущих пустых полей - не самое элегантное решение, но вроде норм
    )

    private var currentPassword : String = ""
    //хранение текущего пароля для PasswordConfirmation

    private val wrongParamList = mutableListOf<UserParam>()
    //лист для хранения текущих полей с неправильными данными - не самое элегантное решение, но вроде норм

    fun initializeScreen(){
        viewModelScope.launch {
            _state.value = RegistrationState.Loading
            //тут будет тест на эксепшн, если да - то стартуем форму, если нет, то стейт скип,
            // пока просто:
            try {
                // здесь напишу пояснение по поводу User - я не передаю данные User между экранами(никаких id, ничего),
                // не вижу в этом большого смысла, у нас и так вся инфраструктура бд написана так
                // чтобы в таблице не могло быть больше одного User
                // А в этой вьюмодели по факту сам полученный с базы юзер - не нужен. Нужен сам факт того,
                // что его можно получить, что он там есть. Можно было сделать под это отдельный юзкейс,
                // и в repoImpl как-нибудь его обрабатывать. Но я подумал что это избыточно
                getUserUseCase()
                Log.d("MyTag", "user got")
                _state.value = RegistrationState.Skip
            } catch (emptyException : EmptyTableException){
                lockControl()
            } catch (garbageException : SizeException){
                _state.value = RegistrationState.Error.CorruptedDataFromDataSource
            } catch (unknownException : Exception){
                _state.value = RegistrationState.Error.Unknown
            }
        }
    }

    fun validateParam(
        param: UserParam,
        content: String?
    ) {
        viewModelScope.launch{
            if (content.isNullOrEmpty()){
                //пришло пустое поле, надо обновить текущие списки
                if (!emptyParamList.contains(param)){
                    emptyParamList.add(param)
                    wrongParamList.remove(param)
                }
            } else{
                emptyParamList.remove(param) //поле не пустое - вычеркиваем его из этого списка

                val isParamValid = isParamValid(param, content) //отдаем содержимое поля на проверку
                val paramInWrongs = wrongParamList.contains(param) //проверим, а вдруг оно уже и так в ложных

                if (isParamValid && paramInWrongs) {
                    wrongParamList.remove(param)
                } else if (!isParamValid && !paramInWrongs) {
                    wrongParamList.add(param)
                    //добавляем в список неправильных - только если его там не было.
                    //можно было использовать для этих целей HashMap, и не городить огород, но я слишком поздно об этом вспомнил
                }
            }
            lockControl()
        }
    }

    private fun isParamValid(param: UserParam, content: String) : Boolean{
        return when(param){
            //раскидываем по специализированным функциям проверки
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
        return dateUtils.isOver18YearsOld(validationData)
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
        viewModelScope.launch {
            if (emptyParamList.isEmpty() && wrongParamList.isEmpty()){
                val emptyList = mutableListOf<UserParam>()
                _state.value = RegistrationState.Content.Locked(emptyList) //это нужно для финальной очистки экрана от последних эрроров на полях
                _state.value = RegistrationState.Content.Unlocked //а теперь наконец стейт с корректными данными и разблокированной кнопкой
            } else {
                _state.value = RegistrationState.Content.Locked(wrongParamList)
            }
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
                _state.value = RegistrationState.Skip //даем сигнал фрагменту что все ок, можно запускать переход на другой экран
            } catch (unknownException : Exception){
                _state.value = RegistrationState.Error.Unknown
            }
        }
    }

    private suspend fun register(user: User) {
        saveUserUseCase(user)
    }
}