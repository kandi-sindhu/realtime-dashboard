import { useEffect, useState } from 'react';

const HISTORY_SIZE = 60; // keep the last 60 seconds

/**
 * Subscribes to the backend's Server-Sent Events stream.
 * EventSource reconnects automatically if the connection drops.
 */
export function useTelemetryStream(url) {
  const [history, setHistory] = useState([]);
  const [status, setStatus] = useState('connecting');

  useEffect(() => {
    const source = new EventSource(url);

    source.onopen = () => setStatus('live');
    source.onerror = () => setStatus('reconnecting');
    source.addEventListener('telemetry', (event) => {
      const snapshot = JSON.parse(event.data);
      setHistory((prev) => [...prev.slice(-(HISTORY_SIZE - 1)), snapshot]);
    });

    return () => source.close();
  }, [url]);

  return { history, latest: history[history.length - 1], status };
}
