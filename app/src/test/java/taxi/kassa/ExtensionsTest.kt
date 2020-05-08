package taxi.kassa

import junit.framework.Assert.assertEquals
import org.junit.Test
import taxi.kassa.util.Constants.MASTERCARD
import taxi.kassa.util.Constants.PUSH_PATTERN
import taxi.kassa.util.Constants.VISA
import taxi.kassa.util.convertToTime
import taxi.kassa.util.getCardType

class ExtensionsTest {

    @Test
    fun check_card_type() {
        assertEquals(MASTERCARD, "5445666644755465".getCardType())
        assertEquals(VISA, "4276582588545556".getCardType())
        assertEquals("Unknown", "0000000000000000".getCardType())
    }

    @Test
    fun check_long_to_date_converting() {
        assertEquals("19:56, 07 мая", 1588870606317L.convertToTime(PUSH_PATTERN))
    }
}