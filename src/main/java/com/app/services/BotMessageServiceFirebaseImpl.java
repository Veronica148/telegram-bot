package com.app.services;

import com.app.model.BotMessage;
import com.app.model.User;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import javassist.NotFoundException;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Primary
public class BotMessageServiceFirebaseImpl implements BotMessageService {

    public static final String TELEGRAM_MESSAGE = "telegram_message";

    @Override
    public List<BotMessage> getAllBotMessages() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        List<BotMessage> messages = new ArrayList<>();
        List<QueryDocumentSnapshot> documents = dbFirestore.collection(TELEGRAM_MESSAGE).get().get().getDocuments();

        messages = documents.stream()
            .map(document -> getBotMessage(document))
            .collect(Collectors.toList());
        messages.stream().forEach(System.out::println);
        return messages;
    }

//    @Override
//    public Optional<List<BotMessage>> getAllBotMessagesOpt() throws ExecutionException, InterruptedException {
//        Firestore dbFirestore = FirestoreClient.getFirestore();
//        List<QueryDocumentSnapshot> documents = dbFirestore.collection(TELEGRAM_MESSAGE).get().get().getDocuments();
//        List<BotMessage> messages = documents.stream()
//                .map(document -> getBotMessage(document))
//                .collect(Collectors.toList());
//        messages.stream().forEach(System.out::println);
//        return Optional.ofNullable(messages);
//    }

    private BotMessage getBotMessage(QueryDocumentSnapshot document) {
        BotMessage result = BotMessage.builder()
                .id(document.getId())
                .receivedMessage(document.getString("receivedMessage"))
                .sentMessage(document.getString("sentMessage"))
                .date(document.getTimestamp("date").toSqlTimestamp())
                .user(User.builder()
                        .firstName(((HashMap<String, String>) document.getData().get("user")).get("firstName"))
                        .lastName(((HashMap<String, String>) document.getData().get("user")).get("lastName"))
                        .userId(((HashMap<String, Long>) document.getData().get("user")).get("userId"))
                        .build())
                .build();
        return result;
    }

    @Override
    @SneakyThrows
    public BotMessage saveBotMessage(BotMessage entity) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }
        if (entity.getId() == null) {
            dbFirestore.collection(TELEGRAM_MESSAGE).document().set(entity).get();
        } else {
            dbFirestore.collection(TELEGRAM_MESSAGE).document(entity.getId()).set(entity).get();
        }
        return entity;
    }

    @Override
    public List<BotMessage> saveBotMessages(List<BotMessage> messages) {
        messages.stream()
                .forEach(message -> saveBotMessage(message));
        return messages;
    }

    @Override
    public void removeBotMessages(List<BotMessage> messages) {
        List<String> ids = messages.stream().map(message -> message.getId()).collect(Collectors.toList());
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference collection = dbFirestore.collection(TELEGRAM_MESSAGE);
        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = dbFirestore.collection(TELEGRAM_MESSAGE).get().get().getDocuments();
            documents.stream()
                    .filter(document -> ids.contains(document.getId()))
                    .forEach(document -> document.getReference().delete());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
