import { render, screen, fireEvent } from '@testing-library/react';
import App from '../App';

test('renders the main heading', () => {
  render(<App />);
  const heading = screen.getByText(/JSON Ticket Validator/i);
  expect(heading).toBeInTheDocument();
});

test('shows error on invalid JSON input', async () => {
  render(<App />);
  const textarea = screen.getByPlaceholderText(/Paste your JSON ticket/i);
  const validateBtn = screen.getByText(/Validate/i);

  fireEvent.change(textarea, { target: { value: '{invalid: json' } });
  fireEvent.click(validateBtn);

  expect(await screen.findByText(/Unexpected token/)).toBeInTheDocument();
});
