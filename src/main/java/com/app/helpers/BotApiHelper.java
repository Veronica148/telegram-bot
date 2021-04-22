package com.app.helpers;

import org.springframework.stereotype.Component;

@Component
public class BotApiHelper {

    public String getResposeFromBot(String message) {

        return message.toUpperCase();
    }
}
