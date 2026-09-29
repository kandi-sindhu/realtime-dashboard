package com.sindhu.telemetry;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Simulates a fleet of connected trucks with a bounded random walk.
 * In a real system this Flux would come from a Kafka topic (for example reactor-kafka's KafkaReceiver).
 */
@Component
public class TelemetrySimulator {

    private int activeVehicles = 120;
    private double avgSpeed = 55.0;
    private double avgFuel = 70.0;
    private int openAlerts = 3;

    /** One shared (hot) stream, so every dashboard sees the same numbers. */
    private final Flux<TelemetrySnapshot> stream = Flux.interval(Duration.ofSeconds(1))
            .map(tick -> next())
            .share();

    public Flux<TelemetrySnapshot> stream() {
        return stream;
    }

    synchronized TelemetrySnapshot next() {
        var random = ThreadLocalRandom.current();
        activeVehicles = clamp(activeVehicles + random.nextInt(-3, 4), 80, 160);
        avgSpeed = clamp(avgSpeed + random.nextDouble(-2.5, 2.5), 35, 70);
        avgFuel = avgFuel < 25 ? 90 : clamp(avgFuel - random.nextDouble(0, 0.6), 20, 100); // refuel below 25%
        openAlerts = clamp(openAlerts + random.nextInt(-1, 2), 0, 12);
        return new TelemetrySnapshot(Instant.now(), activeVehicles, round(avgSpeed), round(avgFuel), openAlerts);
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static double round(double value) {
        return Math.round(value * 10) / 10.0;
    }
}
