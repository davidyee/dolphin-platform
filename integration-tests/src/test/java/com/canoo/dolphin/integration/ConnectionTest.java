package com.canoo.dolphin.integration;

import com.canoo.dolphin.client.ClientConfiguration;
import com.canoo.dolphin.client.ClientContext;
import com.canoo.dolphin.client.ClientContextFactory;
import com.canoo.dolphin.client.javafx.JavaFXConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ConnectionTest {

    @Test
    public void testConnection() {
        ClientConfiguration configuration = new JavaFXConfiguration("http://localhost:8282/todo-app/dolphin");
        configuration.setConnectionTimeout(10_000L);
        try {
            ClientContext context = ClientContextFactory.connect(configuration).get();
        } catch (Exception e) {
            Assert.fail("Can not create connection", e);
        }
    }

}
