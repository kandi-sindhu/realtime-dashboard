# realtime-dashboard

A live **fleet telemetry dashboard**. A reactive **Spring Boot (WebFlux)** backend streams metrics over **Server-Sent Events**, and a **React** frontend draws them as they arrive, with no polling.

```
TelemetrySimulator ──Flux (hot, shared)──▶ GET /api/telemetry/stream (text/event-stream) ──▶ EventSource ──▶ React cards + sparklines
   (stand-in for a Kafka consumer)             Spring WebFlux                                    browser
```

## What it shows

- **Reactive streaming** with Project Reactor: one hot `Flux` shared by every client (`share()`), so all dashboards see the same data
- **Server-Sent Events** (`ServerSentEvent<T>`) as a lightweight alternative to WebSockets for one-way server-to-client updates
- A React **custom hook** (`useTelemetryStream`) around `EventSource`, with automatic reconnects and a connection status badge
- A dependency-free **SVG sparkline** component showing the last 60 seconds
- A Vite dev proxy, so the frontend and backend run separately with no CORS configuration
- Light and dark theme, and a responsive grid

## Tech stack

**Backend:** Java 21 · Spring Boot 3 · Spring WebFlux · Project Reactor · Actuator

**Frontend:** React 18 · Vite 5 · CSS

## Run it

```bash
# Backend: http://localhost:8080
cd backend && mvn spring-boot:run

# Frontend: http://localhost:5173 (new terminal)
cd frontend && npm install && npm run dev
```

To look at the raw stream: `curl -N localhost:8080/api/telemetry/stream`

## Project layout

```
backend/    Spring WebFlux service (simulator + SSE endpoint)
frontend/   React + Vite dashboard
```

## Next steps

- Replace the simulator with a `reactor-kafka` consumer reading from a telemetry topic
- Add per-vehicle drill-down and threshold-based alert notifications
- Package both apps with Docker Compose
