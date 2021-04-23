package com.app.bot;

import com.app.helpers.BotApiHelper;
import com.app.model.BotMessage;
import com.app.model.User;
import com.app.services.BotMessageService;
import com.app.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private UserService userService;
    private BotMessageService botMessageService;
    private BotApiHelper botApiHelper;

    @Value("${bot.name}") private String botUsername;
    @Value("${bot.token}") private String botToken;

    @Autowired
    public TelegramBot(UserService userService, BotMessageService botMessageService, BotApiHelper botApiHelper) {
        this.userService = userService;
        this.botMessageService = botMessageService;
        this.botApiHelper = botApiHelper;
    }

    @Override
    public void onUpdateReceived(Update update) {
        var user = new User();
        user.setUserId(update.getMessage().getFrom().getId());
        user.setFirstName(update.getMessage().getFrom().getFirstName());
        user.setLastName(update.getMessage().getFrom().getLastName());
        try {
            userService.saveUser(user);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        var message = new BotMessage();
        String mesText = update.getMessage().getText();

        message.setUser(user);
        message.setReceivedMessage(mesText);
        message.setDate(Timestamp.from(Instant.ofEpochSecond(update.getMessage().getDate())));
        String respMessage = botApiHelper.getResposeFromBot(mesText);
        message.setSentMessage(respMessage);
        try {
            botMessageService.saveBotMessage(message);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        sendMsg(update.getMessage().getChatId().toString(), respMessage);
    }

    public synchronized void sendMsg(String chatId, String s) {
        var sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

}
