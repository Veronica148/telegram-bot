package com.telegram.bot.unit;

import com.MainApp;
import com.app.helpers.BotApiHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@SpringBootTest(classes = MainApp.class)
public class BotApiHelperTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private BotApiHelper botApiHelper;


    @Test
    public void checkBotApiSuccess(){
        String ourMessage = "someMess";
        String respMessage = botApiHelper.getResposeFromBot(ourMessage);
        Assert.assertEquals(respMessage, ourMessage.toUpperCase());
    }

    @Test
    public void checkBotApiFailure(){
        String ourMessage = "someMess";
        String respMessage = botApiHelper.getResposeFromBot(ourMessage);
        Assert.assertNotEquals(respMessage, ourMessage);
    }
    //mb add for update? xxx

}
