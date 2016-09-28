package com.canoo.dolphin.integration;

import com.canoo.dolphin.client.ClientConfiguration;
import com.canoo.dolphin.client.ClientContext;
import com.canoo.dolphin.client.ClientContextFactory;
import org.opendolphin.core.client.comm.UiThreadHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

public class ConnectionTest {

    @Test
    public void testConnection() {
        try {
            ClientConfiguration configuration = null;

            configuration = new ClientConfiguration(new URL("http://localhost:8080/todo-app/dolphin"), new UiThreadHandler() {
                @Override
                public void executeInsideUiThread(Runnable runnable) {
                    runnable.run();
                }
            });

            configuration.setDolphinLogLevel(Level.FINE);
            configuration.setConnectionTimeout(10_000L);
            try {
                ClientContext context = ClientContextFactory.connect(configuration).get();
            } catch (Exception e) {
                Assert.fail("Can not create connection", e);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
