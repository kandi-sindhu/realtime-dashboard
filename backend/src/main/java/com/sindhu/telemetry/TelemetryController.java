package com.sindhu.telemetry;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/telemetry")
public class TelemetryController {

    private final TelemetrySimulator simulator;

    public TelemetryController(TelemetrySimulator simulator) {
        this.simulator = simulator;
    }

    /** Server-Sent Events stream: one "telemetry" event per second. */
    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<TelemetrySnapshot>> stream() {
        return simulator.stream()
                .map(snapshot -> ServerSentEvent.<TelemetrySnapshot>builder()
                        .event("telemetry")
                        .data(snapshot)
                        .build());
    }
}
