import React, { useEffect, useState } from 'react';
import {
  Modal,
  Box,
  Typography,
  IconButton
} from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';

const HistoryModal = ({ open, onClose }) => {
  const [tickets, setTickets] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!open) return;

    const fetchHistory = async () => {
      try {
        const response = await fetch('http://localhost:8080/tickets');
        const data = await response.json();
        setTickets(data);
        setError('');
      } catch (err) {
        setError(`Failed to fetch history: ${err.message}`);
        setTickets([]);
      }
    };

    fetchHistory();
  }, [open]);

  return (
    <Modal open={open} onClose={onClose}>
      <Box
        sx={{
          position: 'absolute',
          top: '50%',
          left: '50%',
          transform: 'translate(-50%, -50%)',
          width: '90%',
          maxWidth: 700,
          maxHeight: '80vh',
          overflowY: 'auto',
          bgcolor: 'background.paper',
          boxShadow: 24,
          p: 4,
          borderRadius: 2,
        }}
      >
        <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
          <Typography variant="h6">Validation History</Typography>
          <IconButton onClick={onClose} color="error">
            <CloseIcon />
          </IconButton>
        </Box>

        {error && <Typography color="error">{error}</Typography>}

        {tickets.map((ticket, index) => (
          <Box
            key={index}
            sx={{
              border: '1px solid #ccc',
              borderRadius: 2,
              padding: 2,
              marginBottom: 2,
              backgroundColor: '#f9f9f9',
            }}
          >
            <Typography variant="subtitle1">#{index + 1}. {ticket.status}</Typography>
            <Typography variant="body2">Ticket:</Typography>
            <pre>{JSON.stringify(ticket.ticket, null, 2)}</pre>
            {ticket.errors && ticket.errors.length > 0 && (
              <>
                <Typography variant="body2">Errors:</Typography>
                <pre>{JSON.stringify(ticket.errors, null, 2)}</pre>
              </>
            )}
          </Box>
        ))}
      </Box>
    </Modal>
  );
};

export default HistoryModal;
