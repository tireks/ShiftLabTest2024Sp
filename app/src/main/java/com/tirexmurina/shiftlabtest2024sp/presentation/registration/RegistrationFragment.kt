package com.tirexmurina.shiftlabtest2024sp.presentation.registration

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.tirexmurina.shiftlabtest2024sp.R
import com.tirexmurina.shiftlabtest2024sp.databinding.FragmentRegistrationBinding
import com.tirexmurina.shiftlabtest2024sp.presentation.BaseFragment
import com.tirexmurina.shiftlabtest2024sp.utils.AppTextWatcher
import com.tirexmurina.shiftlabtest2024sp.utils.UserParam
import com.tirexmurina.shiftlabtest2024sp.utils.mainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone


@AndroidEntryPoint
class RegistrationFragment : BaseFragment<FragmentRegistrationBinding>() {

    private val viewModel : RegistrationViewModel by viewModels()

    // наследование от BaseFragment в этом фрагменте -
    // исключительно чтобы избавиться от бойлерплэйта,
    // связанного с viewbinding в фрагментах

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegistrationBinding {
        return FragmentRegistrationBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::handleState)
        initializeScreen()
    }

    private fun handleState(registrationState: RegistrationState) {
        when(registrationState){
            is RegistrationState.Initial -> Unit
            is RegistrationState.Content -> handleContent(registrationState)
            is RegistrationState.Error -> handleError(registrationState)
            is RegistrationState.Loading -> showLoading()
            is RegistrationState.Skip -> mainActivity.openAccount()
        }
    }

    private fun initializeScreen() {
        viewModel.initializeScreen()
        initializeFields()
    }

    private fun initializeFields() {
        val paramFields = listOf(
            binding.nameEditText to UserParam.Name,
            binding.surnameEditText to UserParam.Surname,
            binding.birthdateEditText to UserParam.Birthdate,
            binding.passwordEditText to UserParam.Password,
            binding.passwordConfirmEditText to UserParam.PasswordConf
        )
        paramFields.forEach {(editText, paramEnum) ->
            editText.addTextChangedListener(AppTextWatcher{
                if(editText.text.toString().isEmpty()){
                    viewModel.validateParam(paramEnum, null)
                } else viewModel.validateParam(paramEnum, editText.text.toString())
            })
        }
        binding.registrationButton.setOnClickListener { handleRegistrationClick() }
        binding.birthdateEditText.setOnClickListener { handleDatePick(binding.birthdateEditText) }
    }

    private fun handleContent(registrationState: RegistrationState.Content) {
        //todo здесь мы непосредственно контент показываем
        with(binding){
            ///toolbar.isVisible = true
            mainContentContainer.isVisible = true
            bottomButtonCard.isVisible = true
            progressBar.isVisible = false
        }
        when(registrationState){
            is RegistrationState.Content.Locked -> showLockedScreen(registrationState)
            is RegistrationState.Content.Unlocked -> showUnlockedScreen()
        }
    }

    private fun handleError(registrationState: RegistrationState.Error) {
        //todo здесь меняем контент на отображение ошибки, какой именно - решится далее
        when(registrationState){
            is RegistrationState.Error.CorruptedDataFromDataSource ->
                showErrorDialog(getString(R.string.registration_error_from_data))
            is RegistrationState.Error.EmptyTable ->
                showErrorDialog(getString(R.string.registration_error_empty))
            is RegistrationState.Error.Unknown ->
                showErrorDialog(getString(R.string.registration_error_unknown))
            is RegistrationState.Error.CorruptedDataFromForm ->
                showErrorDialog(getString(R.string.registration_error_from_form))
        }
    }

    private fun handleRegistrationClick() {
        val name = binding.nameEditText.text.toString()
        val surname = binding.surnameEditText.text.toString()
        Log.d("MyTag", "ButtonBoom")
        viewModel.navigateUser(name, surname)
    }

    private fun handleDatePick(birthdateEditText: TextInputEditText) {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()
        datePicker.show(parentFragmentManager, "tag")
        datePicker.addOnPositiveButtonClickListener {
            val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            utc.timeInMillis = it
            val format = SimpleDateFormat("dd.MM.yyyy")
            birthdateEditText.setText (format.format(utc.time))
        }
    }


    private fun showLockedScreen(registrationState: RegistrationState.Content.Locked) {
        binding.registrationButton.isClickable = false
        val color = ContextCompat.getColor(requireContext(),R.color.light_divider_color)
        binding.registrationButton.backgroundTintList = ColorStateList.valueOf(color)
        binding.registrationButtonTooltip.isVisible = true
        viewLifecycleOwner.lifecycleScope.launch {
            with(binding) {
                val correctParamsList = mutableListOf(
                    nameEditText,
                    surnameEditText,
                    birthdateEditText,
                    passwordEditText,
                    passwordConfirmEditText
                )

                registrationState.troubleList.forEach {
                    when (it) {
                        UserParam.Name -> {
                            correctParamsList.remove(nameEditText)
                            showWrongField(nameEditText, getString(R.string.registration_name_alert))
                        }
                        UserParam.Surname -> {
                            correctParamsList.remove(surnameEditText)
                            showWrongField(surnameEditText, getString(R.string.registration_name_alert))
                        }
                        UserParam.Birthdate -> {
                            correctParamsList.remove(birthdateEditText)
                            showWrongField(birthdateEditText, getString(R.string.registration_birthdate_alert))
                        }
                        UserParam.Password -> {
                            correctParamsList.remove(passwordEditText)
                            showWrongField(passwordEditText, getString(R.string.registration_password_alert))
                        }
                        UserParam.PasswordConf -> {
                            correctParamsList.remove(passwordConfirmEditText)
                            showWrongField(passwordConfirmEditText, getString(R.string.registration_password_conf_alert))
                        }
                    }
                }
                tideUpForm(correctParamsList)
            }
        }
    }

    private fun tideUpForm(correctParamsList: MutableList<TextInputEditText>) {
        correctParamsList.forEach {
            showCorrectField(it)
        }
    }

    private fun showWrongField(editText: TextInputEditText, alertText : String) {
        (editText.parent.parent as TextInputLayout).error = alertText
    }

    private fun showCorrectField(editText: TextInputEditText) {
        (editText.parent.parent as TextInputLayout).isErrorEnabled = false
    }

    private fun showUnlockedScreen() {
        viewLifecycleOwner.lifecycleScope.launch {
            with(binding) {
                val correctParamsList = mutableListOf(
                    nameEditText,
                    surnameEditText,
                    birthdateEditText,
                    passwordEditText,
                    passwordConfirmEditText
                )
                tideUpForm(correctParamsList)
            }
        }
        val color = ContextCompat.getColor(requireContext(),R.color.light_primary_color)
        binding.registrationButton.isClickable = true
        binding.registrationButton.backgroundTintList = ColorStateList.valueOf(color)
        binding.registrationButtonTooltip.isVisible = false
    }

    private fun showLoading() {
        with(binding){
            mainContentContainer.isVisible = false
            bottomButtonCard.isVisible = false
            progressBar.isVisible = true
        }
    }

    private fun showErrorDialog(errorMsg : String){
        AlertDialog.Builder(context)
            .setTitle(errorMsg)
            .setMessage(getString(R.string.registration_error_dialog_base))
            .setPositiveButton(android.R.string.ok) {_, _, ->
                requireActivity().finish()
            }
            .show()
    }



}
