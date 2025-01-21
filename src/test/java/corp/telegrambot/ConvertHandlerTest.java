package corp.telegrambot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import corp.telegrambot.handler.ConvertHandler;
import corp.telegrambot.utils.ConversionState;

class ConvertHandlerTest {
    @Test
    void testStartConversion() {
        String expected = """
                What would you like to do?
                1. Convert hryvnia to foreign currency (/fromUAN)
                2. Convert foreign currency to hryvnia (/toUAN)
                Please choose an option (1 or 2):
                """;
        assertEquals(expected, ConvertHandler.startConversion());
    }

    @Test
    void testGetConvertInfoFromUAN() {
        String expected = """
                Enter the following with one space in between:
                1. Amount in hryvnias to convert to foreign currency
                2. Code of the foreign currency for conversion
                Examples:
                100 usd
                200 EUR
                
                List of foreign currencies you can convert to:
                1. USD - Dollars
                2. EUR - Euros
                3. TRY - Turkish lira
                4. RON - Romanian leu
                """;
        assertEquals(expected, ConvertHandler.getConvertInfoFromUAN());
    }

    @Test
    void testGetConvertInfoToUAN() {
        String expected = """
                Enter the following with one space in between:
                1. Amount in foreign currency to convert to hryvnia
                2. Code of the foreign currency for conversion
                Examples:
                100 usd
                200 EUR
                
                List of foreign currencies you can convert from:
                1. USD - Dollars
                2. EUR - Euros
                3. TRY - Turkish lira
                4. RON - Romanian leu
                """;
        assertEquals(expected, ConvertHandler.getConvertInfoToUAN());
    }

    @Test
    void testConvertWithInvalidValues() {
        ConversionState state = new ConversionState();
        state.setConvertToUAN(false);
        state.setConvertFromUAN(false);
        state.setAmount(100.0);
        state.setCurrency("USD");

        String expected = """
                    You have not selected any conversion options!
                    Please choose one of the two methods:
                    1. Convert hryvnia to foreign currency (/fromUAN)
                    2. Convert foreign currency to hryvnia (/toUAN)
                    Please choose an option (1 or 2):
                    """;

        assertEquals(expected, ConvertHandler.convert(state));
    }

    @Test
    void testConvertWithBothModesSelected() {
        ConversionState state = new ConversionState();
        state.setConvertToUAN(true);
        state.setConvertFromUAN(true);
        state.setAmount(100.0);
        state.setCurrency("USD");

        String expected = """
                You selected both conversion options at once!
                Please choose one of the two methods:
                1. Convert hryvnia to foreign currency (/fromUAN)
                2. Convert foreign currency to hryvnia (/toUAN)
                Please choose an option (1 or 2):
                """;
        assertEquals(expected, ConvertHandler.convert(state));
    }

    @Test
    void testConvertWithAmountOnly() {
        ConversionState state = new ConversionState();
        state.setConvertToUAN(false);
        state.setConvertFromUAN(true);
        state.setAmount(50.0);
        state.setCurrency(null);

        String expected = """
                Invalid currency for conversion:
                Please enter the amount and currency as shown in the examples:
                100 usd
                200 EUR
                
                Examples of valid currencies for conversion:
                USD
                usd
                EUR
                eUr
                TRY
                tRY
                RON
                Ron
                """;

        assertEquals(expected, ConvertHandler.convert(state));
    }

    @Test
    void testConvertWithCurrencyOnly() {
        ConversionState state = new ConversionState();
        state.setConvertToUAN(true);
        state.setConvertFromUAN(false);
        state.setAmount(null);
        state.setCurrency("USD");

        String expected = """
                Invalid amount for conversion:
                Please enter the amount and currency as shown in the examples:
                100 usd
                200 EUR
                
                Examples of valid amounts for conversion:
                100
                1000
                20000
                3000000
                """;

        assertEquals(expected, ConvertHandler.convert(state));
    }

    @Test
    void testConvertValidToUAN() {
        ConversionState state = new ConversionState();
        state.setConvertToUAN(true);
        state.setConvertFromUAN(false);
        state.setAmount(50.0);
        state.setCurrency("USD");

        String result = ConvertHandler.convert(state);

        assertTrue(result.contains("Conversion result:"));
        assertTrue(result.contains("50,00 USD = "));
    }

    @Test
    void testConvertValidFromUAN() {
        ConversionState state = new ConversionState();
        state.setConvertToUAN(false);
        state.setConvertFromUAN(true);
        state.setAmount(100.0);
        state.setCurrency("EUR");

        String result = ConvertHandler.convert(state);

        assertTrue(result.contains("Conversion result:"));
        assertTrue(result.contains("100,00 UAH = "));
    }
}

