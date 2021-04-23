package com.app.services;

import com.app.model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Primary
public class UserServiceFirebaseImpl implements UserService {

    public static final String TELEGRAM_USER = "telegram_user";

    @Override
    public User getUserById(long id) throws ExecutionException, InterruptedException {
        var dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<DocumentSnapshot> future = dbFirestore.collection(TELEGRAM_USER).document(String.valueOf(id)).get();

        return future.get().toObject(User.class);
    }

    @Override
    public Optional<User> getUserByIdOpt(long id) throws ExecutionException, InterruptedException {
        var dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<DocumentSnapshot> future = dbFirestore.collection(TELEGRAM_USER).document(String.valueOf(id)).get();
        return Optional.ofNullable(future.get().toObject(User.class));
    }


    @Override
    public List<User> getAllUsers() throws ExecutionException, InterruptedException {
        var dbFirestore = FirestoreClient.getFirestore();
        List<QueryDocumentSnapshot> documents = dbFirestore.collection(TELEGRAM_USER).get().get().getDocuments();
        List<User> users = documents.stream()
                .map(document -> new User(Long.valueOf(document.getId()), document.getString("firstName"), document.getString("lastName")))
                .collect(Collectors.toList());
        return users;
    }

    @Override
    public User saveUser(User entity) throws ExecutionException, InterruptedException {
        var dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(TELEGRAM_USER)
                .document(String.valueOf(entity.getUserId())).set(entity).get();
        return entity;
    }

    @Override
    public boolean removeUser(long id) {
        var dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(TELEGRAM_USER).document(String.valueOf(id)).delete();
        return true;
    }
}
