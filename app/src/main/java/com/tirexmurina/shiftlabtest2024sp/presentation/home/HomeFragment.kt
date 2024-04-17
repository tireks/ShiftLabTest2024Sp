package com.tirexmurina.shiftlabtest2024sp.presentation.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.tirexmurina.shiftlabtest2024sp.R
import com.tirexmurina.shiftlabtest2024sp.databinding.FragmentHomeBinding
import com.tirexmurina.shiftlabtest2024sp.domain.entity.User
import com.tirexmurina.shiftlabtest2024sp.presentation.BaseFragment
import com.tirexmurina.shiftlabtest2024sp.utils.mainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel : HomeViewModel by viewModels()

    // наследование от BaseFragment в этом фрагменте -
    // исключительно чтобы избавиться от бойлерплэйта,
    // связанного с viewbinding в фрагментах

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::handleState)
        initializeScreen()
    }

    private fun handleState(homeState: HomeState) {
        when(homeState){
            is HomeState.Content -> showContent(homeState.user)
            is HomeState.Initial -> Unit
            is HomeState.Loading -> showLoading()
            is HomeState.Return -> navigateBack()
            is HomeState.Error -> handleError(homeState)
        }
    }
    private fun handleGreetingsButton() {
        viewModel.getUser()
    }

    private fun handleError(homeState: HomeState.Error) {
        when(homeState){
            is HomeState.Error.CorruptedDataFromDataSource ->
                showErrorDialog(getString(R.string.registration_error_from_data))
            is HomeState.Error.EmptyTable ->
                showErrorDialog(getString(R.string.registration_error_empty))
            is HomeState.Error.Unknown ->
                showErrorDialog(getString(R.string.registration_error_unknown))
        }
    }

    private fun handleAccountDelete() {
        viewModel.deleteUser()
    }
}