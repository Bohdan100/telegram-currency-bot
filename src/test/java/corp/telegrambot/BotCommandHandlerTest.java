package corp.telegrambot;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import corp.telegrambot.handler.BotCommandHandler;
import corp.telegrambot.service.CurrencyApiService;

class BotCommandHandlerTest {
    @Mock
    private CurrencyApiService currencyApiService;

    private BotCommandHandler botCommandHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        botCommandHandler = new BotCommandHandler(currencyApiService);
    }

    @Test
    void testStartCommandReturnCorrectMessage() {
        String userName = "TestUser";
        String expected = """
                Welcome to the bot, TestUser!

                You can find out the current exchange rate.
                Available commands:
                /usd - dollar rate
                /eur - euro rate
                /try - Turkish lira rate
                /ron - Romanian leu rate
                
                /convert - convert currency
                
                /help - help
                /begin - start over
                """;
        assertEquals(expected, botCommandHandler.startCommand(userName));
    }

    @Test
    void testHelpCommandReturnCorrectMessage() {
        String expected = """
                Help on commands:
                /usd - current dollar rate
                /eur - current euro rate
                /try - current Turkish lira rate
                /ron - current Romanian leu rate
                
                /convert - convert currency
                
                /help - help
                /begin - start over
                """;
        assertEquals(expected, botCommandHandler.helpCommand());
    }

    @Test
    void testUsdCommandCallsCurrencyApiServiceWithUSD() {
        String currencyRate = "USD: 38.50 UAH";
        when(currencyApiService.getExchangeRate("USD")).thenReturn(currencyRate);

        String expected = "USD rate:\n" + currencyRate;
        assertEquals(expected, botCommandHandler.usdCommand());
    }

    @Test
    void testEurCommandCallsCurrencyApiServiceWithEUR() {
        String currencyRate = "EUR: 41.30 UAH";
        when(currencyApiService.getExchangeRate("EUR")).thenReturn(currencyRate);

        String expected = "EUR rate:\n" + currencyRate;
        assertEquals(expected, botCommandHandler.eurCommand());
    }

    @Test
    void testTryCommandCallsCurrencyApiServiceWithTRY() {
        String currencyRate = "TRY: 1.50 UAH";
        when(currencyApiService.getExchangeRate("TRY")).thenReturn(currencyRate);

        String expected = "TRY rate:\n" + currencyRate;
        assertEquals(expected, botCommandHandler.tryCommand());
    }

    @Test
    void testRonCommandCallsCurrencyApiServiceWithRON() {
        String currencyRate = "RON: 8.50 UAH";
        when(currencyApiService.getExchangeRate("RON")).thenReturn(currencyRate);

        String expected = "RON rate:\n" + currencyRate;
        assertEquals(expected, botCommandHandler.ronCommand());
    }

    @Test
    void testUnknownCommandReturnCorrectMessage() {
        String expected = """
                Unknown or invalid command for bot!
                
                /help - help
                """;
        assertEquals(expected, botCommandHandler.unknownCommand());
    }
}
