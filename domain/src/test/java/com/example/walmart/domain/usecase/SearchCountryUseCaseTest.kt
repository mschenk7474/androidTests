package com.example.walmart.domain.usecase

import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.Test
import com.example.walmart.domain.model.Country
import org.junit.Assert.assertEquals
import com.example.walmart.domain.repo.CountryRepo

// So the class looks like this
// have one invoke method that does the filtering
// if the query is blank, just show all the results
// in the case of a query, it filters down to just show those results
// it takes the name of the country and compares it to the user's input, ignoring case
// it does the same thing for region, ignoring case as well


@RunWith(Parameterized::class)
class SearchCountryUseCaseTest (private val countries: List<Country>, private val query: String, private val filteredCountries : List<Country>) {

    @Test
    fun testIfCountriesFilteredCorrectly() {
        val actualResults = SearchCountryUseCase().invoke(countries, query)
        assertEquals(filteredCountries, actualResults);
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index} isCountriesFilteredCorrectly({0}, {1}, {2})")
        fun data(): Iterable<Array<Any>> {
            val emptyCase = listOf<Country>()
            val restOfCases = listOf(
                Country("United States", "NA", "USA", "Washington DC"),
                Country("Bolivia", "SA", "BO", "La Pas"),
                Country("Canada", "NA", "CA", "Ottawa"),
                Country("Mexico", "NA", "MX", "Mexico City"),
                Country("England", "EU", "EG", "London"),
                Country("Luxembourg", "EU", "LX", "Luxembourg"),
                Country("Samoa", "OC", "WS", "Apia"))

            val case4Filtered = listOf(Country("Canada", "NA", "CA", "Ottawa"))

            val case6Filtered = listOf(Country("England", "EU", "EG", "London"),
                Country("Luxembourg", "EU", "LX", "Luxembourg"))

            val case8Filtered = listOf(Country("Bolivia", "SA", "BO", "La Pas"),
                Country("Samoa", "OC", "WS", "Apia"))

            return arrayListOf(
                arrayOf(emptyCase, "", emptyCase), // list is empty
                arrayOf(restOfCases, "", restOfCases), // no query, list is the same
                arrayOf(restOfCases, "afghan", emptyCase), // name not region, nothing found
                arrayOf(restOfCases, "Can", case4Filtered), // name not region, something found
                arrayOf(restOfCases, "AF" , emptyCase), // region not name, nothing found
                arrayOf(restOfCases, "EU" , case6Filtered), // region not name, something found
                arrayOf(restOfCases, "XA", emptyCase), // nothing for both region and name
                arrayOf(restOfCases, "SA", case8Filtered) // something for both region and name
            ).toList()
        }
    }
}
