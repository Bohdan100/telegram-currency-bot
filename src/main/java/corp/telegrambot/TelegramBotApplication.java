	package corp.telegrambot;

	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.boot.SpringApplication;
	import org.springframework.context.ApplicationContext;

	import org.telegram.telegrambots.meta.TelegramBotsApi;
	import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
	import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
	import corp.telegrambot.bot.MyTelegramBot;

	@SpringBootApplication
	public class TelegramBotApplication {
		public static void main(String[] args) {
			ApplicationContext context = SpringApplication.run(TelegramBotApplication.class, args);

			MyTelegramBot telegramBot = context.getBean(MyTelegramBot.class);

			try {
				TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
				botsApi.registerBot(telegramBot);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
	}

