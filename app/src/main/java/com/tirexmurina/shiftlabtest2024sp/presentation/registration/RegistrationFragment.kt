package com.tirexmurina.shiftlabtest2024sp.presentation.registration

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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.tirexmurina.shiftlabtest2024sp.R
import com.tirexmurina.shiftlabtest2024sp.databinding.FragmentRegistrationBinding
import com.tirexmurina.shiftlabtest2024sp.presentation.BaseFragment
import com.tirexmurina.shiftlabtest2024sp.presentation.home.RegistrationState
import com.tirexmurina.shiftlabtest2024sp.utils.AppTextWatcher
import com.tirexmurina.shiftlabtest2024sp.utils.UserParam
import com.tirexmurina.shiftlabtest2024sp.utils.mainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


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

    }

