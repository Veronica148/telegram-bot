package com.telegram.bot.integration;

import com.MainApp;
import com.app.controllers.JsonBotMessageController;
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
public class UpdateTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserService userService;

    @Autowired
    private BotMessageService botMessageService;

    @Autowired
    private JsonBotMessageController jsonBotMessageController;

    private MockMvc mockMvc;

    @BeforeClass
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test(description = "post(http://localhost:8080/updateHistoryBetweenDates?id=531091708&startDate=date1&endDate=date2)")
    public void testUpdateHistoryByGoodId() throws Exception {
        long currentTime = System.currentTimeMillis();

        User user = userService.saveUser(new User( new Random().nextLong(), "firstNameVer", "LastNameLap"));
        BotMessage botMessage = botMessageService
                .saveBotMessage(new BotMessage(UUID.randomUUID().toString(), user,"lower","sent",
                        new Timestamp(currentTime)));
        BotMessage botMessage2 = botMessageService
                .saveBotMessage(new BotMessage(UUID.randomUUID().toString(), user,"lower2","sent2",
                        new Timestamp(currentTime)));

        mockMvc.perform(get("/updatehistorybetweendates")
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
                .andExpect(jsonPath("$[*].receivedMessage", hasItem(jsonBotMessageController.UPDATE_MESSAGE)))
                .andExpect(jsonPath("$[*].sentMessage", hasItems(botMessage.getSentMessage(), botMessage2.getSentMessage())));
    }

    @Test(description = "post(http://localhost:8080/updateHistoryBetweenDates?id=0000008&startDate=date1&endDate=date2)")
    public void testUpdateHistoryByBadId() throws Exception {
        String notExistentId = "1111";
        long currentTime = System.currentTimeMillis();

        User user = userService.saveUser(new User(new Random().nextLong(), "firstNameVer", "LastNameLap"));
        BotMessage botMessage = botMessageService
                .saveBotMessage(new BotMessage(UUID.randomUUID().toString(), user,"lower","sent",
                        new Timestamp(currentTime)));

        mockMvc.perform(get("/updatehistorybetweendates")
                .param("id", notExistentId)
                .param("startDate", String.valueOf(new Timestamp(currentTime - 1)))
                .param("endDate", String.valueOf(new Timestamp(currentTime + 1000)))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test(description = "post(http://localhost:8080/updateHistoryBetweenDates?id=goodId&startDate=baddate1&endDate=date2)")
    public void testUpdateHistoryByGoodIdAndBadDate() throws Exception {
        String badDate = "2021-34-35 00:00:00";
        long currentTime = System.currentTimeMillis();

        User user = userService.saveUser(new User( new Random().nextLong(), "firstNameVer", "LastNameLap"));
        BotMessage botMessage = botMessageService
                .saveBotMessage(new BotMessage(UUID.randomUUID().toString(), user,"lower","sent",
                        new Timestamp(currentTime)));

        mockMvc.perform(get("/updatehistorybetweendates")
                .param("id", user.getUserId().toString())
                .param("startDate", badDate)
                .param("endDate", String.valueOf(new Timestamp(currentTime + 1000)))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }
}
