package kz.tastamat.bootstrap;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import kz.zx.api.app.AppContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AppLauncher extends io.vertx.core.Launcher {

    private Logger log = LoggerFactory.getLogger(AppLauncher.class);

    private JsonObject config;

    private List<TimerTask> timerTasks;
    private List<Timer> timers;

    private static final String LOGGER_CONFIG_PROPERTY = "log4j.configurationFile";

    static {
        System.setProperty(LOGGER_CONFIG_PROPERTY, "conf/log4j2.xml");
        System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.Log4j2LogDelegateFactory");
    }

    public static void main(String[] args) {
        new AppLauncher().dispatch(args);
    }

    public AppLauncher() {
        this.timers = new ArrayList<>();
        this.timerTasks = new ArrayList<>();
    }

    @Override
    public void afterConfigParsed(JsonObject config) {
        log.debug("afterConfigParsed---");
        this.config = config;

        long start = System.currentTimeMillis();
        AppContext.init(config);
        long end = System.currentTimeMillis();
        log.debug("inited datasources in " + (end - start) + "ms");
    }

    @Override
    public void beforeDeployingVerticle(DeploymentOptions deploymentOptions) {
        log.debug("beforeDeployingVerticle---");
    }

    @Override
    public void afterStartingVertx(Vertx vertx) {
        log.debug("afterStartingVertx---");
    }

    @Override
    public void beforeStoppingVertx(Vertx vertx) {
        log.debug("beforeStoppingVertx---");
        timerTasks.forEach(TimerTask::cancel);
        timers.forEach(Timer::cancel);
        log.debug("stopped timers---");
    }
}
