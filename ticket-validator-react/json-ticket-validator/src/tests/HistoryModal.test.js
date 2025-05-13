import { render, screen, fireEvent } from '@testing-library/react';
import HistoryModal from '../components/HistoryModal';

test('renders and closes modal', () => {
  const mockClose = jest.fn();
  const mockHistory = [
    {
      status: 'PASSED',
      ticket: { id: 123, type: 'CHANGE_REQUEST' },
      errors: []
    }
  ];

  render(<HistoryModal open={true} onClose={mockClose} history={mockHistory} />);
  expect(screen.getByText(/Validation History/)).toBeInTheDocument();

  fireEvent.click(screen.getByText(/Close/i));
  expect(mockClose).toHaveBeenCalled();
});
