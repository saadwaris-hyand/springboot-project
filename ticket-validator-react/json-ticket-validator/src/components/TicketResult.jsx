import React from 'react';
import { Typography, Box } from '@mui/material';

const TicketResult = ({ result, bulkMode }) => {
  if (!result) return null;

  const renderSingle = () => (
    <Box mb={2}>
      <Typography variant="h6" gutterBottom>
        {result.status || 'Validation Result'}
      </Typography>
      {result.errors && result.errors.length > 0 && (
        <>
          <Typography variant="subtitle1">Errors:</Typography>
          <pre>{JSON.stringify(result.errors, null, 2)}</pre>
        </>
      )}
    </Box>
  );

  const renderBulk = () => (
    <Box>
      <Typography variant="h6" gutterBottom>
        Bulk Validation Results
      </Typography>
      {result.map((ticket, index) => (
        <Box
          key={index}
          sx={{
            border: '1px solid #ccc',
            borderRadius: 2,
            padding: 2,
            marginBottom: 2,
            backgroundColor: '#fafafa',
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
  );

  return <div>{bulkMode ? renderBulk() : renderSingle()}</div>;
};

export default TicketResult;
