package ru.isavinov.smallbot2024.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.isavinov.smallbot2024.service.SimpleGame;

@Component
public class SimpleGameTelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final String token;

    private final SimpleGame simpleGame;

    public SimpleGameTelegramBot(@Value("${botToken}") String token, SimpleGame simpleGame) {
        this.token = token;
        this.simpleGame = simpleGame;
        this.telegramClient = new OkHttpTelegramClient(getBotToken());
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String result = "";

            if (messageText.equals("/start")) {
                simpleGame.startGame(chatId);
                result="Игра запущена!";
            } else {
                if (isNumeric(messageText)){
                    result = simpleGame.checkAnswer(chatId, Integer.parseInt(messageText));
                } else {
                    result = "Введено не число!";
                }
            }

            SendMessage message = SendMessage
                    .builder()
                    .chatId(chatId)
                    .text(result)
                    .build();
            try {
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
