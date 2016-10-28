package com.canoo.dolphin.integration;

import com.canoo.dolphin.client.ClientConfiguration;
import com.canoo.dolphin.client.ClientContext;
import com.canoo.dolphin.client.ClientContextFactory;
import com.canoo.dolphin.util.DolphinRemotingException;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.testng.annotations.DataProvider;

import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

public class AbstractIntegrationTest {

    protected void waitUntilServerIsUp(String host, long time, TimeUnit timeUnit) throws TimeoutException {
        long startTime = System.currentTimeMillis();
        long waitMillis = timeUnit.toMillis(time);
        boolean connected = false;
        while (!connected) {
            if(System.currentTimeMillis() > startTime + waitMillis) {
                throw new TimeoutException("Server " + host + " is still down after " + waitMillis + " ms");
            }
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(host + "/health");
                if(client.execute(get).getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    connected = true;
                }
            } catch (Exception e) {
                //
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected ClientContext createClientContext(String endpoint) {
        try {
            waitUntilServerIsUp(endpoint, 5, TimeUnit.MINUTES);
            ClientConfiguration configuration = new ClientConfiguration(new URL(endpoint + "/dolphin"), r -> r.run());
            configuration.setDolphinLogLevel(Level.FINE);
            configuration.setConnectionTimeout(10_000L);
            return ClientContextFactory.connect(configuration).get(configuration.getConnectionTimeout(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new DolphinRemotingException("Can not create client context for endpoint " + endpoint, e);
        }
    }

    @DataProvider(name = "endpoints", parallel = true)
    public Object[][] getEndpoints() {
        return new String[][]{{"Payara", "http://localhost:8081/todo-app"},
                {"TomEE", "http://localhost:8082/todo-app"}};
    }
}
