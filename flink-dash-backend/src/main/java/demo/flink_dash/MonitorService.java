package demo.flink_dash;

import demo.flink_dash.FlinkClient;
import demo.flink_dash.JobStatus;
import org.slf4j.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class MonitorService {
    private static final Logger log = LoggerFactory.getLogger(MonitorService.class);
    private final FlinkClient client;

    public MonitorService(FlinkClient client) { this.client = client; }

    @Scheduled(fixedDelayString = "${monitor.poll-ms}")
    public void poll() throws InterruptedException {
        for (JobStatus js : client.listJobs()) {
            if (!"RUNNING".equals(js.state())) {
                log.warn("{} FAILED, attempting one restart", js.name());
                client.restart(js.id());
                Thread.sleep(60000);
                JobStatus after = client.listJobs().stream()
                        .filter(j -> j.id().equals(js.id()))
                        .findFirst().orElse(js);
                if (!"RUNNING".equals(after.state()))
                    log.error("ALERT: {} still {}", after.name(), after.state());
            }
        }
    }
}