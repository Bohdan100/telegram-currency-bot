package corp.telegrambot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MyTelegramBotConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
