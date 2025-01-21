package corp.telegrambot.utils;

import org.springframework.stereotype.Component;
import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
@Component
public class ConversionState {
    private boolean convertToUAN = false;
    private boolean convertFromUAN = false;
    private Double amount;
    private String currency;
}
