package com.app.beanconfig;

import com.app.bot.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@ComponentScan(basePackages = "com.app")
public class ProjectConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramBot bot) {
        TelegramBotsApi botsApi = null;
        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return botsApi;
    }
}
