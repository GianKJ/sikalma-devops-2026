import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import App from './App';

describe('SIKALMA Frontend - Suite de Pruebas Unitarias', () => {
  it('renderiza el título principal de SIKALMA y el lema', () => {
    render(<App />);
    expect(screen.getByText(/Tu Refugio de Calma y Equilibrio Emocional/i)).toBeInTheDocument();
    expect(screen.getAllByText(/Centro Psicológico • Huánuco/i)[0]).toBeInTheDocument();
  });

  it('permite navegar entre las pestañas de Servicios y Psicólogos', () => {
    render(<App />);
    
    const btnServicios = screen.getByRole('button', { name: /^Servicios$/i });
    fireEvent.click(btnServicios);
    expect(screen.getByText(/Nuestros Servicios Psicológicos/i)).toBeInTheDocument();

    const btnPsicologos = screen.getByRole('button', { name: /^Psicólogos$/i });
    fireEvent.click(btnPsicologos);
    expect(screen.getByText(/Nuestros Psicólogos Colegiados/i)).toBeInTheDocument();
  });

  it('permite navegar al formulario de Agendar Cita', () => {
    render(<App />);
    const btnAgendar = screen.getByRole('button', { name: /^🗓️ Agendar Cita$/i });
    fireEvent.click(btnAgendar);
    expect(screen.getByText(/Reserva de Cita Psicológica/i)).toBeInTheDocument();
  });

  it('permite acceder al Panel de Admisión de Pacientes', () => {
    render(<App />);
    const btnAdmin = screen.getByRole('button', { name: /Panel de Admisión/i });
    fireEvent.click(btnAdmin);
    expect(screen.getByText(/Gestión de Pacientes y Admisión/i)).toBeInTheDocument();
  });
});
