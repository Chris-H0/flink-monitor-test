package demo.flink_dash;

import demo.flink_dash.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final FlinkClient client;
    public JobController(FlinkClient c){this.client=c;}

    @GetMapping
    public List<JobStatus> all(){ return client.listJobs(); }
}