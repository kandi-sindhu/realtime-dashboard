import Sparkline from './Sparkline.jsx';
import { useTelemetryStream } from './useTelemetryStream.js';

const METRICS = [
  { key: 'activeVehicles', label: 'Active vehicles', unit: '' },
  { key: 'avgSpeedMph', label: 'Average speed', unit: 'mph' },
  { key: 'avgFuelLevelPct', label: 'Average fuel level', unit: '%' },
  { key: 'openAlerts', label: 'Open alerts', unit: '' },
];

export default function App() {
  const { history, latest, status } = useTelemetryStream('/api/telemetry/stream');

  return (
    <main className="page">
      <header className="header">
        <h1>Fleet Telemetry</h1>
        <span className={`status status-${status}`}>{status}</span>
      </header>

      <section className="grid">
        {METRICS.map(({ key, label, unit }) => (
          <article key={key} className={`card ${key === 'openAlerts' && latest?.openAlerts > 8 ? 'card-alert' : ''}`}>
            <h2>{label}</h2>
            <p className="value">
              {latest ? latest[key] : '–'}
              {latest && unit && <span className="unit"> {unit}</span>}
            </p>
            <Sparkline values={history.map((s) => s[key])} />
          </article>
        ))}
      </section>

      <footer className="footer">
        {latest ? `Last update ${new Date(latest.timestamp).toLocaleTimeString()}` : 'Waiting for data…'}
      </footer>
    </main>
  );
}
