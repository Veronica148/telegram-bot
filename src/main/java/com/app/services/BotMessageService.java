package com.app.services;

import com.app.model.BotMessage;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface BotMessageService {
    List<BotMessage> getAllBotMessages() throws ExecutionException, InterruptedException;
    BotMessage saveBotMessage(BotMessage entity) throws ExecutionException, InterruptedException;
    List<BotMessage> saveBotMessages(List<BotMessage> messages) throws ExecutionException, InterruptedException;
    void removeBotMessages(List<BotMessage> messages) throws ExecutionException, InterruptedException;
    Optional<List<BotMessage>> getAllBotMessagesOpt() throws ExecutionException, InterruptedException ;
}
