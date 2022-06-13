package kz.tastamat.bootstrap;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import kz.tastamat.payment.handler.PaymentHandler;
import kz.zx.api.app.AppContext;
import kz.zx.api.enums.ConfigKey;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

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
        initUnbookScheduler(vertx);
    }

    @Override
    public void beforeStoppingVertx(Vertx vertx) {
        log.debug("beforeStoppingVertx---");
        timerTasks.forEach(TimerTask::cancel);
        timers.forEach(Timer::cancel);
        log.debug("stopped timers---");
    }

    private void initUnbookScheduler(Vertx vertx) {
        JsonObject schedulerConfigs = this.config.getJsonObject(ConfigKey.EPAY_SCHEDULER.key());
        if (!schedulerConfigs.getBoolean(ConfigKey.ENABLED.key())) {
            log.warn("message resend scheduler is disabled, see config 'message_resender#enabled'");
            return;
        }

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                vertx.runOnContext(event -> new PaymentHandler(vertx, config).confirm(config.getJsonObject("epay")));
            }
        };
        Timer timer = new Timer();
        Calendar today = Calendar.getInstance();

        TimeUnit timeUnit = TimeUnit.DAYS;
        Integer interval = 1;


        if (schedulerConfigs.containsKey(ConfigKey.BEGIN.key())) {
            String begin[] = schedulerConfigs.getString("begin").split(":");
            today.set(Calendar.HOUR_OF_DAY, Integer.parseInt(begin[0]));
            today.set(Calendar.MINUTE, Integer.parseInt(begin[1]));
            today.set(Calendar.SECOND, Integer.parseInt(begin[2]));
        } else {
            today.add(Calendar.SECOND, 2);
        }

        if (schedulerConfigs.containsKey(ConfigKey.INTERVAL.key())) {
            String intervalStr = schedulerConfigs.getString(ConfigKey.INTERVAL.key());
            if (intervalStr.endsWith("s")) {
                timeUnit = TimeUnit.SECONDS;
                interval = Integer.parseInt(intervalStr.replace("s", ""));
            } else if (intervalStr.endsWith("m")) {
                timeUnit = TimeUnit.MINUTES;
                interval = Integer.parseInt(intervalStr.replace("m", ""));
            } else if (intervalStr.endsWith("h")) {
                timeUnit = TimeUnit.HOURS;
                interval = Integer.parseInt(intervalStr.replace("h", ""));
            } else if (intervalStr.endsWith("d")) {
                timeUnit = TimeUnit.DAYS;
                interval = Integer.parseInt(intervalStr.replace("d", ""));
            }
        }
        log.info("using {} with {} and starting at: {}", timeUnit, interval, today.getTime());

        timer.schedule(timerTask, today.getTime(), TimeUnit.MILLISECONDS.convert(interval, timeUnit));

        timers.add(timer);
        timerTasks.add(timerTask);
    }
}
