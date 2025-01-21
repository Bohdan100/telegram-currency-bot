package corp.telegrambot.handler;

import org.springframework.stereotype.Component;
import corp.telegrambot.service.CurrencyApiService;

@Component
public class BotCommandHandler {
    private final CurrencyApiService currencyApiService;

    public BotCommandHandler(CurrencyApiService currencyApiService) {
        this.currencyApiService = currencyApiService;
    }

    public String startCommand(String userName) {
        return String.format("""
                Welcome to the bot, %s!
                
                You can find out the current exchange rate.
                Available commands:
                /usd - dollar rate
                /eur - euro rate
                /try - Turkish lira rate
                /ron - Romanian leu rate
                
                /convert - convert currency
                
                /help - help
                /begin - start over
                """, userName);
    }

    public String helpCommand() {
        return """
                Help on commands:
                /usd - current dollar rate
                /eur - current euro rate
                /try - current Turkish lira rate
                /ron - current Romanian leu rate
                
                /convert - convert currency
                
                /help - help
                /begin - start over
                """;
    }

    public String usdCommand() {
        return "USD rate:\n" + currencyApiService.getExchangeRate("USD");
    }

    public String eurCommand() {
        return "EUR rate:\n" + currencyApiService.getExchangeRate("EUR");
    }

    public String tryCommand() {
        return "TRY rate:\n" + currencyApiService.getExchangeRate("TRY");
    }

    public String ronCommand() {
        return "RON rate:\n" + currencyApiService.getExchangeRate("RON");
    }

    public String unknownCommand() {
        return """
                Unknown or invalid command for bot!
                
                /help - help
                """;
    }
}
