package com.example.walmart.presentation.details

import com.example.walmart.domain.error.ErrorFormatter
import com.example.walmart.domain.provider.DispatcherProvider
import com.example.walmart.presentation.countries.CountriesViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.rules.TestWatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.Description
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import com.example.walmart.domain.model.Country
import com.example.walmart.domain.repo.CountryRepo
import com.example.walmart.domain.usecase.SearchCountryUseCase
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle

@ExperimentalCoroutinesApi
class MainCoroutineRule: TestWatcher() {
    val testDispatcher = StandardTestDispatcher()
    override fun starting(description: Description?) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        Dispatchers.resetMain()
    }
}

@ExperimentalCoroutinesApi
class CountriesViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var mockRepo: CountryRepo

    private lateinit var dispatcherProvider: DispatcherProvider

    @MockK
    private lateinit var errorFormatter: ErrorFormatter

    private lateinit var viewModel: CountriesViewModel

    @MockK
    private lateinit var  searchCountryUseCase: SearchCountryUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        dispatcherProvider = object : DispatcherProvider {
            override fun io(): CoroutineDispatcher {
                return mainCoroutineRule.testDispatcher
            }

            override fun main(): CoroutineDispatcher {
                return mainCoroutineRule.testDispatcher
            }
        }

        every { errorFormatter.getDisplayErrorMessage(any()) } returns "Test Error"

        viewModel = CountriesViewModel(mockRepo, dispatcherProvider, errorFormatter)
    }

    @Test
    fun `test subscribe`() = runTest {
        val timeout = 200.toDuration(DurationUnit.MILLISECONDS)

//        coEvery { mockRepo. }
    }

    @Test
    fun `test search`() = runTest {
        val allCountries = listOf(
            Country("USA", "NA", "USA", "Washington"),
            Country("Canada", "NA", "CA", "Ottawa"),
            Country("Mexico", "NA", "MX","Mexico City")
        )
        coEvery { mockRepo.getCountries() } answers {
            when (firstArg<String>()) {
                "NA" -> allCountries
                else -> emptyList()
            }
        }

        val states = mutableListOf<CountriesViewModel.State>()
        val job = launch {viewModel.state.collect { states.add(it) }}

        viewModel.search("NA")
        advanceUntilIdle()

        val result = states.last()
        println(states)
        assertEquals(3, result.items.size)
        //assertEquals("Canada", result.items[0].name)

        job.cancel()

//        //viewModel = CountriesViewModel(mockRepo, dispatcherProvider, errorFormatter)
//        val query = "Afghan"
//        val result = listOf(Country("Afghanistan", "AS", "AF", "Kabul"))
//        val items = mockRepo.getCountries()
//        coEvery { searchCountryUseCase.invoke(items, query) } returns (result)
//
//        val states = mutableListOf<CountriesViewModel.State>()
//        val job = launch {viewModel.state.collect { states.add(it) }}
//
//        viewModel.search(query)
//
//        advanceUntilIdle()
//
//        coVerify { searchCountryUseCase.invoke(items, query) }
//
//        val loadingState = states.first()
//        println("Loading state loading status: ${loadingState.loading}")
//        println("${states[0].loading}")
//        //assertTrue(states[0].loading == loadingState.loading)
//
//        val successState = states.last()
//        //assertFalse(successState.loading)
//        println("${successState.loading}")
//        println("$result")
//        println(successState.items)
//        println(successState.errorMessage)
//        assertEquals(result, successState.items)
//        assertEquals(successState.errorMessage, "")
//
//
//        job.cancel()
    }

    @Test
    fun `test onClick`() = runTest {

    }

    @Test
    fun `test loadItems`() = runTest {

    }

    @Test
    fun `test errorHandler`() = runTest {

    }
}