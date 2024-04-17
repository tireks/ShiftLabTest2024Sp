package com.tirexmurina.shiftlabtest2024sp.presentation.home

import androidx.lifecycle.Observer
import com.tirexmurina.shiftlabtest2024sp.domain.entity.User
import com.tirexmurina.shiftlabtest2024sp.domain.usecase.DeleteUserUseCase
import com.tirexmurina.shiftlabtest2024sp.domain.usecase.GetUserUseCase
import com.tirexmurina.shiftlabtest2024sp.presentation.utils.TestCoroutineExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import ru.ftc.junit.sample.utils.InstantTaskExecutorExtension

@ExtendWith(MockitoExtension::class, InstantTaskExecutorExtension::class, TestCoroutineExtension::class)
class HomeViewModelTest {

    //набор тестов чисто демонстрационный, конечно, тут еще тонну можно придумать

    private var getUsersUseCase: GetUserUseCase = mock()
    private var deleteUserUseCase: DeleteUserUseCase = mock()
    private var viewModel: HomeViewModel = HomeViewModel(getUsersUseCase, deleteUserUseCase)

    private val observer: Observer<HomeState> = mock()

    @Test
    fun `view model created EXPECT initial state`(){
        viewModel.state.observeForever(observer)

        Mockito.verify(observer).onChanged(HomeState.Initial)
    }

    @Test
    fun `load user EXPECT loading state`(){
        viewModel.state.observeForever(observer)
        viewModel.getUser()

        inOrder(observer) {
            verify(observer).onChanged(HomeState.Initial)
            verify(observer).onChanged(HomeState.Loading)
        }
    }

    @Test
    fun `load user EXPECT get countries`() = runTest {
        viewModel.getUser()

        verify(getUsersUseCase).invoke()
    }

    @Test
    fun `countries loaded EXPECT content state`() = runTest {
        val user : User?
        user = User("Jason","Bourne")
        whenever(getUsersUseCase()) doReturn user

        viewModel.getUser()
        viewModel.state.observeForever(observer)

        verify(observer).onChanged(HomeState.Content(user))
    }



}