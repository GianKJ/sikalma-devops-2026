import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import AgendarCitaModal from './AgendarCitaModal';

describe('AgendarCitaModal - Pruebas de Validación y Reserva', () => {
  it('renderiza el formulario de reserva con sus campos principales', () => {
    render(<AgendarCitaModal />);
    expect(screen.getByText(/Reserva de Cita Psicológica/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/Ej. 74859612/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/Ej. Juan Carlos/i)).toBeInTheDocument();
  });

  it('muestra error de validación cuando el DNI no tiene 8 dígitos', () => {
    render(<AgendarCitaModal />);
    
    const inputDni = screen.getByPlaceholderText(/Ej. 74859612/i);
    const inputNombres = screen.getByPlaceholderText(/Ej. Juan Carlos/i);
    const inputApellidos = screen.getByPlaceholderText(/Ej. Pérez Gómez/i);
    const inputTel = screen.getByPlaceholderText(/Ej. 951 234 567/i);
    const inputFecha = screen.getByLabelText(/Fecha de Cita/i, { selector: 'input' });
    const btnSubmit = screen.getByRole('button', { name: /Confirmar Reserva de Cita/i });

    fireEvent.change(inputDni, { target: { value: '123' } }); // DNI inválido
    fireEvent.change(inputNombres, { target: { value: 'Ana' } });
    fireEvent.change(inputApellidos, { target: { value: 'Ríos' } });
    fireEvent.change(inputTel, { target: { value: '987654321' } });
    fireEvent.change(inputFecha, { target: { value: '2026-10-15' } });
    
    fireEvent.click(btnSubmit);

    expect(screen.getByText(/El DNI debe contener exactamente 8 dígitos numéricos/i)).toBeInTheDocument();
  });

  it('permite registrar la cita cuando todos los campos son válidos', () => {
    const handleSuccess = vi.fn();
    render(<AgendarCitaModal onSuccess={handleSuccess} />);
    
    const inputDni = screen.getByPlaceholderText(/Ej. 74859612/i);
    const inputNombres = screen.getByPlaceholderText(/Ej. Juan Carlos/i);
    const inputApellidos = screen.getByPlaceholderText(/Ej. Pérez Gómez/i);
    const inputTel = screen.getByPlaceholderText(/Ej. 951 234 567/i);
    const inputFecha = screen.getByLabelText(/Fecha de Cita/i, { selector: 'input' });
    const btnSubmit = screen.getByRole('button', { name: /Confirmar Reserva de Cita/i });

    fireEvent.change(inputDni, { target: { value: '78965412' } });
    fireEvent.change(inputNombres, { target: { value: 'Carlos' } });
    fireEvent.change(inputApellidos, { target: { value: 'Gómez' } });
    fireEvent.change(inputTel, { target: { value: '987654321' } });
    fireEvent.change(inputFecha, { target: { value: '2026-10-15' } });
    
    fireEvent.click(btnSubmit);

    expect(screen.getByText(/¡Cita registrada con éxito para Carlos Gómez!/i)).toBeInTheDocument();
    expect(handleSuccess).toHaveBeenCalled();
  });
});
