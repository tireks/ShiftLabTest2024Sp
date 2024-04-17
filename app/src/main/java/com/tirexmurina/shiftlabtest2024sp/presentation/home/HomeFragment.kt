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

    private fun initializeScreen() {
        //тут помимо прочего включаем  и настраиваем менюшку
        mainActivity.setSupportActionBar(binding.toolbar)
        setupMenu()
        binding.greetingsButton.setOnClickListener { handleGreetingsButton() }
        viewModel.initializeScreen()
    }

    private fun setupMenu() {
        //непосредственно - настройка менюшки
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.home_menu, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                //опция меня для удаления акка
                return when (menuItem.itemId) {
                    R.id.delete_menu_button -> {
                        handleAccountDelete()
                        true
                    }
                    else -> return false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun handleGreetingsButton() {
        viewModel.getUser()
    }

    private fun handleError(homeState: HomeState.Error) {
        //лбработчик ошибок
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

    private fun showContent(user: User?) {
        //контроллер отображения контента, показывает экран с кнопкой приветствия если пришел null вместо юзера,
        // и иначе - стартует диалог с данными юзера. контент с данными юзера приходит, естесственно после нажатия кнопки
        with(binding){
            mainContentContainer.isVisible = true
            progressBar.isVisible = false
        }
        if (user != null){
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.home_dialog_title))
                .setMessage(getString(R.string.home_greeting_message, user.firstName, user.lastName))
                .show()
        }
    }

    private fun showLoading() {
        with(binding){
            mainContentContainer.isVisible = false
            progressBar.isVisible = true
        }
    }

    private fun showErrorDialog(errorMsg : String){
        //тоже просто диалог для минимальной инфы по ошибке
        AlertDialog.Builder(context)
            .setTitle(errorMsg)
            .setMessage(getString(R.string.registration_error_dialog_base))
            .setPositiveButton(android.R.string.ok) {_, _, ->
                //из действий - по факту, только закрыть приложение.
                // Можно было бы еще сделать например полное удажение базы данных,
                // чтобы при следующем запуске она снова создавалась, но я не успел
                requireActivity().finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun navigateBack() {
        //сразу поясню зачем именно навигация
        // на предыдущий экран с помощью action - потому что это простой и
        // быстрый способ навигироваться на обновленный экран. Отменами транзакций это делается не так просто
        mainActivity.closeAccount()
    }
}