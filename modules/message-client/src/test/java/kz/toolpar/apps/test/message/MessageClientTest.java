package kz.toolpar.apps.test.message;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import kz.toolpar.message.client.MessageClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class MessageClientTest {

    private static MessageClient messageClient;

    private Logger log = LogManager.getLogger(MessageClientTest.class);

    @BeforeAll
    public static void setup() {
        Vertx vertx = Vertx.vertx();
        JsonObject cfg = new JsonObject();
        cfg.put("url", "https://testplatform.tastamat.com/platform/v1/rest");
        cfg.put("key", "postkz");
        cfg.put("token", "363830b9-d797-4fd0-a620-01da3b56c928");
        messageClient = MessageClient.create(vertx);
    }

    @Test
    @DisplayName("test send get")
    public void testSendGet(Vertx vertx, VertxTestContext testContext) {
//        messageClient.sendGet().setHandler(res -> {
//            log.info("result {} ", res);
//            testContext.completeNow();
//        });
    }
}
