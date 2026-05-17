package org.example.apimonitor.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogTestController {
    private static final Logger log = LoggerFactory.getLogger(LogTestController.class);

    @GetMapping("/test-logs")
    public String testLogs() {
        log.info("Test INFO message sent to ELK");
        log.warn("Test WARNING message");
        log.error("Test ERROR message");

        // Generate multiple logs
        for (int i = 1; i <= 5; i++) {
            log.debug("Iteration {} of test logs", i);
        }

        return "Logs sent! Check Kibana at http://localhost:5601";
    }
}
