package com.telegram.bot.unit;

import com.MainApp;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;


@SpringBootTest(classes = MainApp.class)
public class UnitMockTest extends AbstractTestNGSpringContextTests {

    @Test
    public void checkBotApiSuccess(){

        Assert.assertTrue(true);
    }
}
