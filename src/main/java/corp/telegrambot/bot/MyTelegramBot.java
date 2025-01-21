package corp.telegrambot.bot;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import corp.telegrambot.handler.StartHandler;
import corp.telegrambot.handler.BotCommandHandler;
import corp.telegrambot.handler.ConvertHandler;
import corp.telegrambot.utils.ConversionState;
import corp.telegrambot.constants.BotConstants;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {
    private final String botUsername;
    private final String botToken;
    private final BotCommandHandler commandHandler;
    private final StartHandler startHandler;
    private final ConvertHandler convertHandler;
    private final ConversionState conversionState;

    public MyTelegramBot(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername,
            BotCommandHandler commandHandler,
            StartHandler startHandler,
            ConvertHandler convertHandler,
            ConversionState conversionState) {
        super(botToken);
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.commandHandler = commandHandler;
        this.startHandler = startHandler;
        this.convertHandler = convertHandler;
        this.conversionState = conversionState;
    }

    private static final Logger LOG = LoggerFactory.getLogger(MyTelegramBot.class);

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update);
        }
        else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
    }

    private void handleMessage(Update update) {
        var chatId = update.getMessage().getChatId();
        var messageText = update.getMessage().getText();
        var userName = update.getMessage().getChat().getFirstName();

        if (messageText.equals(BotConstants.fromUAN)) {
            conversionState.setConvertFromUAN(true);
            conversionState.setConvertToUAN(false);
        }

        if (messageText.equals(BotConstants.toUAN)) {
            conversionState.setConvertToUAN(true);
            conversionState.setConvertFromUAN(false);
        }

        if (messageText.matches("\\d+\\s\\w+")) {
            String[] parts = messageText.split("\\s+");
            conversionState.setAmount(Double.parseDouble(parts[0]));
            conversionState.setCurrency(parts[1]);
            messageText = BotConstants.TRANSFORM;
        }

        if (messageText.equals(BotConstants.BEGIN) || messageText.equals(BotConstants.START)) {
            conversionState.setConvertToUAN(false);
            conversionState.setConvertFromUAN(false);
        }

        switch (messageText) {
            case BotConstants.START -> {
                startHandler.saveUserToDatabase(update);
                startHandler.sendStartMessage(this, chatId, userName);
            }
            case BotConstants.BEGIN -> sendMessage(chatId, commandHandler.startCommand(userName));
            case BotConstants.USD -> sendMessage(chatId, commandHandler.usdCommand());
            case BotConstants.EUR -> sendMessage(chatId, commandHandler.eurCommand());
            case BotConstants.TRY -> sendMessage(chatId, commandHandler.tryCommand());
            case BotConstants.RON -> sendMessage(chatId, commandHandler.ronCommand());
            case BotConstants.HELP -> sendMessage(chatId, commandHandler.helpCommand());

            case BotConstants.CONVERT -> sendMessage(chatId, convertHandler.startConversion());
            case BotConstants.fromUAN -> sendMessage(chatId, convertHandler.getConvertInfoFromUAN());
            case BotConstants.toUAN -> sendMessage(chatId, convertHandler.getConvertInfoToUAN());
            case BotConstants.TRANSFORM -> sendMessage(chatId, convertHandler.convert(conversionState));
            default -> sendMessage(chatId, commandHandler.unknownCommand());
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        var chatId = callbackQuery.getMessage().getChatId();
        var callbackData = callbackQuery.getData();
        var userName = callbackQuery.getFrom().getFirstName();

        if (BotConstants.BEGIN.equals(callbackData)) {
            sendMessage(chatId, commandHandler.startCommand(userName));
        } else {
            sendMessage(chatId, commandHandler.unknownCommand());
        }
    }

    private void sendMessage(Long chatId, String text) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
