package com.sindhu.telemetry;

import java.time.Instant;

/** One reading of fleet-wide metrics. */
public record TelemetrySnapshot(
        Instant timestamp,
        int activeVehicles,
        double avgSpeedMph,
        double avgFuelLevelPct,
        int openAlerts) {
}
