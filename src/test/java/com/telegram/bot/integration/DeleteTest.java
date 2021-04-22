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
public class DeleteTest extends AbstractTestNGSpringContextTests {

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

    @Test(description = "delete(http://localhost:8080/deleteUserHistoryBetweenDates?id=860065132&startDate=2021-04-12 00:00:00 &endDate=2021-04-14 00:55:00)")
    public void testUserHistoryDeleteByIdBetweenDates() throws Exception {
        long currentTime = System.currentTimeMillis();
        User user = userService.saveUser(new User( new Random().nextLong(), "firstNameVer", "LastNameLap"));
        BotMessage botMessage = botMessageService
                .saveBotMessage(new BotMessage(UUID.randomUUID().toString(), user,"lower","sent",
                        new Timestamp(currentTime)));
        BotMessage botMessage2 = botMessageService
                .saveBotMessage(new BotMessage(UUID.randomUUID().toString(), user,"lower2","sent2",
                        new Timestamp(currentTime)));

        mockMvc.perform(get("/deleteUserHistoryBetweenDates")
                .param("id",user.getUserId().toString())
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

    @Test(description = "delete(http://localhost:8080/deleteUserHistoryBetweenDates?id=notExistentId&startDate=Timestamp1&endDate=Timestamp2)")
    public void testUserHistoryDeleteBetweenDatesByBadId() throws Exception {
        long currentTime = System.currentTimeMillis();

        User user = userService.saveUser(new User(new Random().nextLong(),"firstVer", "LastLap"));
        BotMessage botMessage = botMessageService
                .saveBotMessage(new BotMessage(UUID.randomUUID().toString(), user ,"lower","sent",
                        new Timestamp(currentTime)));

        mockMvc.perform(get("/deleteUserHistoryBetweenDates")
                .param("id","111")
                .param("startDate", String.valueOf(new Timestamp(currentTime - 1)))
                .param("endDate", String.valueOf(new Timestamp(currentTime + 1000)))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
