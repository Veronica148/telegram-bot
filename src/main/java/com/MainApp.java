package com;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@SpringBootApplication
public class MainApp {

	public static void main(String[] args) throws TelegramApiException {
		SpringApplication.run(MainApp.class, args);
	}

}
