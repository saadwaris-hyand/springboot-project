import React, { useState } from 'react';
import {
  Button,
  Typography,
  Checkbox,
  FormControlLabel,
  Box,
} from '@mui/material';
import TicketResult from './components/TicketResult';
import HistoryModal from './components/HistoryModal';

const App = () => {
  const [jsonInput, setJsonInput] = useState('');
  const [bulkMode, setBulkMode] = useState(false);
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');
  const [historyOpen, setHistoryOpen] = useState(false);

  const handleValidate = async () => {
    try {
      const parsed = JSON.parse(jsonInput);
      const endpoint = bulkMode
        ? 'http://localhost:8080/validate/bulk'
        : 'http://localhost:8080/validate';

      const response = await fetch(endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(parsed),
      });

      const data = await response.json();
      setResult(data);
      setError('');
    } catch (err) {
      setError(`Error: ${err.message}`);
      setResult(null);
    }
  };

  return (
    <div className="container">
      <Typography variant="h4" gutterBottom>
        JSON Ticket Validator
      </Typography>

      <textarea
        value={jsonInput}
        onChange={(e) => setJsonInput(e.target.value)}
        placeholder="Paste your JSON ticket here..."
      />

      <Box className="controls">
        <FormControlLabel
          control={
            <Checkbox
              checked={bulkMode}
              onChange={() => setBulkMode(!bulkMode)}
            />
          }
          label="Bulk Mode"
        />

        <Button variant="contained" color="success" onClick={handleValidate}>
          Validate
        </Button>

        <Button variant="contained" color="primary" onClick={() => setHistoryOpen(true)}>
          History
        </Button>
      </Box>

      {error && <Typography color="error">{error}</Typography>}

      {result && (
        <TicketResult result={result} bulkMode={bulkMode} />
      )}

      <HistoryModal open={historyOpen} onClose={() => setHistoryOpen(false)} />
    </div>
  );
};

export default App;
