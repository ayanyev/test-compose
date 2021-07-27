package com.eazzyapps.repositories

import com.eazzyapps.repositories.ui.viewmodels.parseToMonthYear
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.text.ParseException
import java.util.*

@RunWith(Parameterized::class)
class DateParsingTest(
    private val isoDate: String,
    private val monthYear: String,
    private val local: Locale
) {

    @Test
    fun addition_isCorrect() {
        try {
            assertEquals(monthYear, isoDate.parseToMonthYear(local))
        } catch (e: Exception) {
            assert(e.javaClass.isAssignableFrom(ParseException::class.java))
        }
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf("2008-12-24T00:00:00Z", "Dez.08", Locale.GERMANY),
                arrayOf("2008-12-24T00:00:00Z", "Dec08", Locale.ENGLISH),
                arrayOf("2008-12-24T00:00:00Z", "Dec08", Locale.US),
                arrayOf("2009-Dec-24T00:00:00Z", "Dec08", Locale.US),
            )
        }
    }

}