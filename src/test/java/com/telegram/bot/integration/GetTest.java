package com.telegram.bot.integration;

import com.MainApp;
import com.app.model.BotMessage;
import com.app.model.User;
import com.app.services.BotMessageService;
import com.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.util.Random;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = MainApp.class)
public class GetTest extends AbstractTestNGSpringContextTests {

    public static final String TELEGRAM_USER = "telegram_user";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserService userService;

    @Autowired
    private BotMessageService botMessageService;

    private MockMvc mockMvc;

    @BeforeClass
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test(description = "get(http://localhost:8080/getusers/531091708)")
    public void testGetUserById() throws Exception {
        User user = userService.saveUser(new User(new Random().nextLong(), "Nika", "Petrova"));

        mockMvc.perform(get("/users/{id}", user.getUserId().toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[*].userId", hasItem(user.getUserId().longValue())))
                .andExpect(jsonPath("$[*].firstName", hasItem(user.getFirstName())))
                .andExpect(jsonPath("$[*].lastName", hasItem(user.getLastName())));
    }

    @Test(description = "get(http://localhost:8080/users/{badId})")
    public void testGetUserByBadId() throws Exception {
        mockMvc.perform(get("/users/{id}", "314"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test(description = "get(http://localhost:8080/users)")
    public void testGetAllUsers() throws Exception {
        User user = userService.saveUser(new User(new Random().nextLong(), "Nastja", "Ivanova"));
        User user2 = userService.saveUser(new User(new Random().nextLong(), "Olga", "Petrova"));

        mockMvc.perform(get("/users"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[*].userId", hasItems(user.getUserId(), user2.getUserId())))
                .andExpect(jsonPath("$[*].firstName", hasItems(user.getFirstName(), user2.getFirstName())))
                .andExpect(jsonPath("$[*].lastName", hasItems(user.getLastName(), user2.getLastName())));
    }

    @Test(description = "get(http://localhost:8080/getmessages)")
    public void testGetAllMessages() throws Exception {
        User user = userService.saveUser(new User(new Random().nextLong(), "Nasja", "Ivanova"));
        BotMessage botMessage = botMessageService
                .saveBotMessage(new BotMessage(UUID.randomUUID().toString(),user,"lowerInAllMess","sent",
                        new Timestamp(System.currentTimeMillis())));

        mockMvc.perform(get("/getmessages"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[*].id", hasItem(botMessage.getId())))
                .andExpect(jsonPath("$[*].user.userId", hasItem(user.getUserId())))
                .andExpect(jsonPath("$[*].user.firstName", hasItem(user.getFirstName())))
                .andExpect(jsonPath("$[*].user.lastName", hasItem(user.getLastName())))
                .andExpect(jsonPath("$[*].receivedMessage", hasItem(botMessage.getReceivedMessage())))
                .andExpect(jsonPath("$[*].sentMessage", hasItem(botMessage.getSentMessage())));
    }

    @Test(description = "get(http://localhost:8080/getmessages?startDate=2021-04-03 00:00:00&endDate=2021-04-12 10:00:00)")
    public void testGetMessagesFromDateTillDate() throws Exception {
        long currentTime = System.currentTimeMillis();

        User user = userService.saveUser(new User( new Random().nextLong(), "Nikita", "Petrov"));
        BotMessage botMessage = botMessageService
                .saveBotMessage(new BotMessage(UUID.randomUUID().toString(),user,"lower","sent",
                        new Timestamp(currentTime)));
        BotMessage botMessage2 = botMessageService
                .saveBotMessage(new BotMessage(UUID.randomUUID().toString(),user,"lower2","sent2",
                        new Timestamp(currentTime)));

        mockMvc.perform(get("/getmessages")
                .param("startDate", String.valueOf(new Timestamp(currentTime - 1)))
                .param("endDate", String.valueOf(new Timestamp(currentTime + 1000)))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", hasItems(botMessage.getId(), botMessage2.getId())))
                .andExpect(jsonPath("$[*].user.userId", hasItem(user.getUserId())))
                .andExpect(jsonPath("$[*].user.firstName", hasItem(user.getFirstName())))
                .andExpect(jsonPath("$[*].user.lastName", hasItem(user.getLastName())))
                .andExpect(jsonPath("$[*].receivedMessage", hasItems(botMessage.getReceivedMessage(), botMessage2.getReceivedMessage())))
                .andExpect(jsonPath("$[*].sentMessage", hasItems(botMessage.getSentMessage(), botMessage2.getSentMessage())));
    }
}