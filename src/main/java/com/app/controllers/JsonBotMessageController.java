package com.app.controllers;

import com.app.helpers.TimeStampHelper;
import com.app.model.BotMessage;
import com.app.services.BotMessageService;
import com.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@RestController
public class JsonBotMessageController {

    public static final String UPDATE_MESSAGE = "xxx";

    @Autowired
    private BotMessageService service;

    @Autowired
    private UserService userService;

    @GetMapping("/getmessages")
    @ResponseBody
    public ResponseEntity<List<BotMessage>> getMessages(@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) throws ExecutionException, InterruptedException {
        HttpStatus status = OK;
        List<BotMessage> messages = null;
        try {
            var tsHelper = TimeStampHelper.getTimeStampHelperFromIntervals(startDate, endDate);
            messages = service.getAllBotMessages().stream()
                    .filter(x -> x.getDate().after(tsHelper.getStartDate()) && x.getDate().before(tsHelper.getEndDate()))
                    .collect(toList());
        } catch (NumberFormatException ex) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(messages, status);
    }

    @GetMapping("/getmessagesforuser")
    @ResponseBody
    public ResponseEntity<List<BotMessage>> getMessagesForUser(@RequestParam String id, @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate){
        List<BotMessage> messages = null;
        TimeStampHelper tsHelper;
        try {
            tsHelper = TimeStampHelper.getTimeStampHelperFromIntervals(startDate, endDate);
        } catch (NumberFormatException ex) {
            return new ResponseEntity(BAD_REQUEST);
        }

        try {
            var user = userService.getUserById(Long.valueOf(id)); //check if such user exists
            if (user == null){
                throw new NoSuchElementException();
            }

            messages = service.getAllBotMessages().stream()
                    .filter(x -> Objects.equals(x.getUser().getUserId(), Long.valueOf(id)))
                    .filter(x -> x.getDate().after(tsHelper.getStartDate()) && x.getDate().before(tsHelper.getEndDate()))
                    .collect(toList());
        }catch (NoSuchElementException ex) {
            return new ResponseEntity(NOT_FOUND);
        }catch (InterruptedException | ExecutionException ex) {
            return new ResponseEntity(BAD_REQUEST);
        }
        return new ResponseEntity<>(messages, OK);
    }

    @GetMapping("/updateHistoryBetweenDates")
    @ResponseBody
    public ResponseEntity<List<BotMessage>> updateHistoryBetweenDates(@RequestParam String id, @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) {
        HttpStatus status = OK;
        List<BotMessage> messages = null;
        try {
            var tsHelper = TimeStampHelper.getTimeStampHelperFromIntervals(startDate, endDate);
            var user = userService.getUserById(Long.valueOf(id)); //check if such user exists
            if (user == null){
                throw new NoSuchElementException();
            }

            messages = service.getAllBotMessages().stream()
                    .filter(x -> Objects.equals(x.getUser().getUserId(), Long.valueOf(id)))
                    .filter(x -> x.getDate().after(tsHelper.getStartDate()) && x.getDate().before(tsHelper.getEndDate()))
                    .collect(toList());
            messages.stream().forEach(x -> x.setReceivedMessage(UPDATE_MESSAGE));
            service.saveBotMessages(messages);
        } catch (NumberFormatException | InterruptedException | ExecutionException ex) {
            status = HttpStatus.BAD_REQUEST;
        } catch (NoSuchElementException ex) {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(messages, status);
    }

    @GetMapping("/deleteUserHistoryBetweenDates")
    @ResponseBody
    public ResponseEntity<List<BotMessage>> deleteUserHistoryBetweenDates(@RequestParam String id, @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) {
        HttpStatus status = OK;
        List<BotMessage> messages = List.of();
        try {
            var tsHelper = TimeStampHelper.getTimeStampHelperFromIntervals(startDate, endDate);
            var user = userService.getUserById(Long.valueOf(id)); //check if such user exists

            if(user == null){
                throw new NoSuchElementException();
            }

            messages = service.getAllBotMessages().stream()
                    .filter(x -> Objects.equals(x.getUser().getUserId(), Long.valueOf(id)))
                    .filter(x -> x.getDate().after(tsHelper.getStartDate()) && x.getDate().before(tsHelper.getEndDate()))
                    .collect(toList());
            service.removeBotMessages(messages);
        } catch (NumberFormatException ex) {
            status = HttpStatus.BAD_REQUEST;
        } catch (NoSuchElementException ex) {
            status = HttpStatus.NOT_FOUND;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(messages, status);
    }
}
