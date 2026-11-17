import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import App from './App';

describe('SIKALMA Frontend - Suite de Pruebas Unitarias', () => {
  it('renderiza el título principal de SIKALMA y el lema', () => {
    render(<App />);
    expect(screen.getByText(/Tu Refugio de Calma y Equilibrio Emocional/i)).toBeInTheDocument();
    expect(screen.getAllByText(/Centro Psicológico • Huánuco/i).length).toBeGreaterThanOrEqual(1);
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
    const btnAgendar = screen.getAllByRole('button', { name: /Agendar Cita/i })[0];
    fireEvent.click(btnAgendar);
    expect(screen.getByText(/Reserva de Cita Psicológica/i)).toBeInTheDocument();
  });

  it('permite acceder al Panel de Admisión de Pacientes y al acceso Login', () => {
    render(<App />);
    const btnAdmin = screen.getByRole('button', { name: /Gestión Usuarios\/Pacientes/i });
    fireEvent.click(btnAdmin);
    expect(screen.getByText(/Gestión de Pacientes y Admisión/i)).toBeInTheDocument();

    const btnLogin = screen.getByRole('button', { name: /Iniciar Sesión/i });
    fireEvent.click(btnLogin);
    expect(screen.getByRole('heading', { name: /Acceso SIKALMA|Iniciar Sesión/i })).toBeInTheDocument();
  });
});
