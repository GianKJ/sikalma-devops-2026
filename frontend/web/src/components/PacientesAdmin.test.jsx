import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import PacientesAdmin from './PacientesAdmin';

describe('PacientesAdmin - Pruebas de Gestión de Pacientes', () => {
  it('muestra la lista inicial de pacientes y la barra de búsqueda', () => {
    render(<PacientesAdmin />);
    expect(screen.getByText(/Gestión de Pacientes y Admisión/i)).toBeInTheDocument();
    expect(screen.getByText(/Lucía Andrea Mendoza Carrillo/i)).toBeInTheDocument();
    expect(screen.getByText(/Jorge Luis Huamán Rivera/i)).toBeInTheDocument();
  });

  it('permite filtrar pacientes por DNI o nombre', () => {
    render(<PacientesAdmin />);
    const inputSearch = screen.getByPlaceholderText(/Buscar por DNI, Nombres o Apellidos/i);

    fireEvent.change(inputSearch, { target: { value: '74125896' } });
    expect(screen.getByText(/Lucía Andrea Mendoza Carrillo/i)).toBeInTheDocument();
    expect(screen.queryByText(/Jorge Luis Huamán Rivera/i)).not.toBeInTheDocument();
  });

  it('permite abrir el formulario de nuevo paciente y registrar uno nuevo', () => {
    render(<PacientesAdmin />);
    const btnNuevo = screen.getByRole('button', { name: /\+ Nuevo Paciente/i });
    fireEvent.click(btnNuevo);

    expect(screen.getByText(/Ficha de Admisión de Paciente/i)).toBeInTheDocument();

    const inputDni = screen.getByPlaceholderText('78945612');
    const inputNombres = screen.getByPlaceholderText('Nombres');
    const inputApellidos = screen.getByPlaceholderText('Apellidos');
    const inputTel = screen.getByPlaceholderText('951 234 567');
    const inputFecha = screen.getByLabelText(/Fecha Nacimiento/i, { selector: 'input' });
    const btnGuardar = screen.getByRole('button', { name: /Guardar Paciente/i });

    fireEvent.change(inputDni, { target: { value: '88776655' } });
    fireEvent.change(inputNombres, { target: { value: 'Elena' } });
    fireEvent.change(inputApellidos, { target: { value: 'Sánchez' } });
    fireEvent.change(inputTel, { target: { value: '988112233' } });
    fireEvent.change(inputFecha, { target: { value: '1995-03-12' } });

    fireEvent.click(btnGuardar);

    expect(screen.getByText(/Elena Sánchez/i)).toBeInTheDocument();
    expect(screen.getByText(/88776655/i)).toBeInTheDocument();
  });
});
