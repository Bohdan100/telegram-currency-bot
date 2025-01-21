package corp.telegrambot.handler;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import corp.telegrambot.bot.MyTelegramBot;
import corp.telegrambot.constants.BotConstants;
import corp.telegrambot.users.UserService;
import corp.telegrambot.users.User;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class StartHandler {
    private final UserService userService;

    public void saveUserToDatabase(Update update) {
        var message = update.getMessage();
        var userTelegram = message.getFrom();

        User user = new User();
        user.setTelegramId(userTelegram.getId());
        user.setFirstName(userTelegram.getFirstName());
        user.setLastName(userTelegram.getLastName());
        user.setUsername(userTelegram.getUserName());
        user.setLanguageCode(userTelegram.getLanguageCode());
        user.setChatId(message.getChatId());
        user.setLatitude(message.getLocation() != null ? message.getLocation().getLatitude() : null);
        user.setLongitude(message.getLocation() != null ? message.getLocation().getLongitude() : null);

        userService.saveUser(user);
    }

    public static void sendStartMessage(MyTelegramBot bot, Long chatId, String userName) {
        String greeting = String.format("""
                Welcome, %s!
                
                This bot provides information about the exchange rate of the Ukrainian currency - hryvnia (UAH) in relation to foreign currencies (USD, EUR, TRY, RON) in real-time, along with a currency conversion feature.
                
                You can calculate the conversion of a specific amount of foreign currency to hryvnia (UAH) and vice versa.
                
                Click the "Start/begin" button to get started.
                
                """, userName);

        InlineKeyboardButton beginButton = InlineKeyboardButton.builder()
                .text("Start/begin")
                .callbackData(BotConstants.BEGIN)
                .build();

        InlineKeyboardMarkup keyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboardRow(Collections.singletonList(beginButton))
                .build();

        SendMessage message = SendMessage.builder()
                .chatId(chatId.toString())
                .text(greeting)
                .replyMarkup(keyboardMarkup)
                .build();
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
