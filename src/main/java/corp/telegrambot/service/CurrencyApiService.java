package corp.telegrambot.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.Getter;

@Service
public class CurrencyApiService {
    private static final String PRIVATBANK_API_URL = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";
    private static final String LIQPAY_API_URL = "https://api.liqpay.ua/doc/api/public/exchange";
    private static final String NBU_API_URL = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";

    private final RestTemplate restTemplate;

    public CurrencyApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getExchangeRate(String currencyCode) {
        if ("TRY".equalsIgnoreCase(currencyCode) || "RON".equalsIgnoreCase(currencyCode)) {
            return getExchangeRateFromNBU(currencyCode);
        } else if ("USD".equalsIgnoreCase(currencyCode) || "EUR".equalsIgnoreCase(currencyCode)) {
            return getExchangeRateFromPrivatAndBackup(currencyCode);
        } else {
            return "No information available for the selected currency.";
        }
    }

    private String getExchangeRateFromNBU(String currencyCode) {
        try {
            var response = restTemplate.getForObject(NBU_API_URL, NbuCurrencyResponse[].class);
            if (response != null) {
                for (NbuCurrencyResponse rate : response) {
                    if (rate.getCc().equalsIgnoreCase(currencyCode)) {
                        return String.format("%s: %.2f UAH\n", currencyCode, rate.getRate());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Connection error to NBU API: " + e.getMessage());
        }
        return "No exchange rate information from the main bank.";
    }

    private String getExchangeRateFromPrivatAndBackup(String currencyCode) {
        try {
            var response = restTemplate.getForObject(PRIVATBANK_API_URL, PrivatBankCurrencyResponse[].class);
            return parseCurrencyResponse(response, currencyCode);
        } catch (Exception e) {
            System.err.println("Connection error to PrivatBank API: " + e.getMessage());
        }

        try {
            var response = restTemplate.getForObject(LIQPAY_API_URL, LiqpayCurrencyResponse[].class);
            return parseBackupCurrencyResponse(response, currencyCode);
        } catch (Exception e) {
            System.err.println("Connection error to backup API: " + e.getMessage());
        }

        return "No exchange rate information from the main bank.";
    }

    private String parseCurrencyResponse(PrivatBankCurrencyResponse[] response, String currencyCode) {
        if (response != null) {
            for (PrivatBankCurrencyResponse rate : response) {
                if (rate.getCcy().equalsIgnoreCase(currencyCode)) {
                    return String.format("%s: %.2f UAH\n", currencyCode, rate.getSale());
                }
            }
        }
        return "Currency rate not found!";
    }

    private String parseBackupCurrencyResponse(LiqpayCurrencyResponse[] response, String currencyCode) {
        if (response != null) {
            for (LiqpayCurrencyResponse rate : response) {
                if (rate.getCurrency().equalsIgnoreCase(currencyCode)) {
                    return String.format("%s: %.2f UAH\n", currencyCode, rate.getSaleRate());
                }
            }
        }
        return "Currency rate not found!";
    }

    @Getter
    public static class PrivatBankCurrencyResponse {
        private String ccy;
        private double sale;

    }

    @Getter
    public static class LiqpayCurrencyResponse {
        private String currency;
        private double saleRate;

    }

    @Getter
    public static class NbuCurrencyResponse {
        private String cc;
        private double rate;

        public NbuCurrencyResponse(String aTry, double v) {
        }
    }
}
