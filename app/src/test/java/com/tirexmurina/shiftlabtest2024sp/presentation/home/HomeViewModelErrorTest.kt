package com.tirexmurina.shiftlabtest2024sp.presentation.home

import androidx.lifecycle.Observer
import com.tirexmurina.shiftlabtest2024sp.data.exceptions.EmptyTableException
import com.tirexmurina.shiftlabtest2024sp.data.exceptions.SizeException
import com.tirexmurina.shiftlabtest2024sp.domain.usecase.DeleteUserUseCase
import com.tirexmurina.shiftlabtest2024sp.domain.usecase.GetUserUseCase
import com.tirexmurina.shiftlabtest2024sp.presentation.utils.TestCoroutineExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import ru.ftc.junit.sample.utils.InstantTaskExecutorExtension
import java.util.stream.Stream

@ExtendWith(MockitoExtension::class, InstantTaskExecutorExtension::class, TestCoroutineExtension::class)
class HomeViewModelErrorTest {

    //набор тестов чисто демонстрационный, конечно, тут еще тонну можно придумать

    private companion object {

        @JvmStatic
        fun databaseErrors(): Stream<Exception> = Stream.of(
            EmptyTableException("EmptyTableException"),
            SizeException("SizeException"),
            Exception()
        )
    }

    private var getUsersUseCase: GetUserUseCase = mock()
    private var deleteUserUseCase: DeleteUserUseCase = mock()
    private var viewModel: HomeViewModel = HomeViewModel(getUsersUseCase, deleteUserUseCase)

    private val observer: Observer<HomeState> = mock()

    @ParameterizedTest
    @MethodSource("databaseErrors")
    fun `get user returns database error EXPECT error state`(exception: Exception) = runTest {
        whenever(getUsersUseCase()) doAnswer {throw exception}

        viewModel.getUser()
        viewModel.state.observeForever(observer)

        verify(observer).onChanged(any())

        // знаю что нужно избегать логики в тестах,
        // просто для моей иерархии классов ошибок смог придумать только такой параметризированный тест
        when (exception) {
            is EmptyTableException -> verify(observer).onChanged(HomeState.Error.EmptyTable)
            is SizeException -> verify(observer).onChanged(HomeState.Error.CorruptedDataFromDataSource)
            else -> verify(observer).onChanged(HomeState.Error.Unknown)
        }
    }

}