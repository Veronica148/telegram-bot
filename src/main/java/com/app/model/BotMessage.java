package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BotMessage {

    String id;
    User user;
    String receivedMessage;
    String sentMessage;
    Timestamp date;
}
