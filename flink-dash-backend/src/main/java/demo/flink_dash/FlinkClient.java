package demo.flink_dash;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class FlinkClient {
    private final RestTemplate rt = new RestTemplate();
    @Value("${flink.base-url}") private String base;

    public List<JobStatus> listJobs() {
        Map<?,?> m = rt.getForObject(base + "/jobs/overview", Map.class);
        return ((List<Map<?,?>>) m.get("jobs")).stream()
                .map(j -> new JobStatus(
                        (String) j.get("jid"),
                        (String) j.get("name"),
                        (String) j.get("state")
                )).toList();
    }

    public void restart(String id) {
        rt.postForLocation(base + "/jobs/" + id + "/restart", null);
    }
}