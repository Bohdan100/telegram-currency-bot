package corp.telegrambot.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import corp.telegrambot.service.CurrencyApiService;
import corp.telegrambot.utils.ConversionState;

@Component
public class ConvertHandler {
    private static final CurrencyApiService currencyService = new CurrencyApiService(new RestTemplate());

    public static String startConversion() {
        return """
                What would you like to do?
                1. Convert hryvnia to foreign currency (/fromUAN)
                2. Convert foreign currency to hryvnia (/toUAN)
                Please choose an option (1 or 2):
                """;
    }

    public static String getConvertInfoFromUAN() {
        return """
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
    }

    public static String getConvertInfoToUAN() {
        return """
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
    }

    public static String convert(ConversionState conversionState) {
        if (conversionState.getCurrency() == null) {
            return """
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
        }

        if (conversionState.getAmount() == null) {
            return """
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
        }

        if (conversionState.isConvertToUAN() && conversionState.isConvertFromUAN()) {
            return """
                    You selected both conversion options at once!
                    Please choose one of the two methods:
                    1. Convert hryvnia to foreign currency (/fromUAN)
                    2. Convert foreign currency to hryvnia (/toUAN)
                    Please choose an option (1 or 2):
                    """;
        }

        if (Boolean.logicalAnd(!conversionState.isConvertToUAN(), !conversionState.isConvertFromUAN())) {
            return """
                    You have not selected any conversion options!
                    Please choose one of the two methods:
                    1. Convert hryvnia to foreign currency (/fromUAN)
                    2. Convert foreign currency to hryvnia (/toUAN)
                    Please choose an option (1 or 2):
                    """;
        }

        String currencyCode = conversionState.getCurrency().toUpperCase();
        double amount = conversionState.getAmount();

        try {
            String rawRateResponse = currencyService.getExchangeRate(currencyCode);
            String[] rateInfo = rawRateResponse.split(":");
            if (rateInfo.length < 2) {
                return "Error: exchange rate for the specified currency not found!";
            }

            double exchangeRate = Double.parseDouble(rateInfo[1].trim().replace(" UAH", "").replace(",", "."));

            String conversionResult;
            if (conversionState.isConvertToUAN()) {
                double convertedAmount = amount * exchangeRate;
                conversionResult = String.format("Conversion result: %.2f %s = %.2f UAH", amount, currencyCode, convertedAmount);
            } else if (conversionState.isConvertFromUAN()) {
                double convertedAmount = amount / exchangeRate;
                conversionResult = String.format("Conversion result: %.2f UAH = %.2f %s", amount, convertedAmount, currencyCode);
            } else {
                return "Error: conversion mode not selected.";
            }

            return conversionResult + """
                    \n\nContinue conversion - enter other amounts or currency codes:
                    Example: 200 EUR
                    
                    /begin - return to the start menu
                    /convert - convert currency
                    """;
        } catch (Exception e) {
            return "Error during conversion. Please check your input and try again.";
        }
    }
}
