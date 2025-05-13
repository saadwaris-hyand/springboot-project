import { render, screen } from '@testing-library/react';
import TicketResult from '../components/TicketResult';

test('displays ticket status and errors', () => {
  const result = {
    status: 'FAILED',
    ticket: { id: 1, type: 'CHANGE_REQUEST' },
    errors: ['Missing description', 'Invalid priority']
  };

  render(<TicketResult result={result} index={0} />);

  expect(screen.getByText((content) =>
    content.includes('1.') && content.includes('FAILED')
  )).toBeInTheDocument();
  
  expect(screen.getByText(/Missing description/)).toBeInTheDocument();
});
