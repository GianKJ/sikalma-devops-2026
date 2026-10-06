import React from 'react';

export const SERVICIOS_DATA = [
  {
    id: 1,
    nombre: "Consulta Psicológica Individual",
    descripcion: "Evaluación y abordaje terapéutico personalizado para el bienestar y equilibrio emocional de jóvenes y adultos.",
    modalidad: "AMBAS",
    duracion: 50,
    precio: 80.00,
    publico: "Jóvenes y Adultos"
  },
  {
    id: 2,
    nombre: "Terapia de Pareja y Familiar",
    descripcion: "Acompañamiento especializado en comunicación asertiva, resolución de conflictos y reconstrucción del vínculo.",
    modalidad: "PRESENCIAL",
    duracion: 60,
    precio: 120.00,
    publico: "Parejas y Familias"
  },
  {
    id: 3,
    nombre: "Evaluación Psicométrica Integral",
    descripcion: "Aplicación de baterías de test psicológicos estandarizados con entrega de informe formal y devolución diagnóstica.",
    modalidad: "PRESENCIAL",
    duracion: 90,
    precio: 150.00,
    publico: "Niños, Jóvenes y Adultos"
  }
];

export default function ServiciosSection({ onSelectServicio }) {
  return (
    <section style={{ padding: '3rem 0' }}>
      <div className="container">
        <div style={{ textAlign: 'center', marginBottom: '2.5rem' }}>
          <span className="badge badge-blue" style={{ marginBottom: '0.5rem' }}>Especialidades y Tarifario</span>
          <h2 style={{ fontSize: '1.875rem', fontWeight: '800', color: 'var(--primary-800)' }}>Nuestros Servicios Psicológicos</h2>
          <p style={{ color: 'var(--slate-600)', marginTop: '0.5rem', maxWidth: '600px', margin: '0.5rem auto 0' }}>
            Atención clínica profesional y confidencial diseñada para cada etapa de tu vida.
          </p>
        </div>

        <div className="grid-3">
          {SERVICIOS_DATA.map((srv) => (
            <div key={srv.id} className="card" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
              <div>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
                  <span className="badge badge-blue">{srv.modalidad}</span>
                  <span style={{ fontSize: '0.875rem', color: 'var(--slate-500)', fontWeight: '600' }}>⏱️ {srv.duracion} min</span>
                </div>
                <h3 style={{ fontSize: '1.25rem', fontWeight: '700', color: 'var(--slate-900)', marginBottom: '0.5rem' }}>
                  {srv.nombre}
                </h3>
                <p style={{ fontSize: '0.875rem', color: 'var(--slate-600)', marginBottom: '1rem' }}>
                  {srv.descripcion}
                </p>
              </div>
              <div style={{ borderTop: '1px solid var(--slate-200)', paddingTop: '1rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <div>
                  <span style={{ fontSize: '0.75rem', color: 'var(--slate-500)', display: 'block' }}>Inversión</span>
                  <span style={{ fontSize: '1.25rem', fontWeight: '800', color: 'var(--primary-800)' }}>S/ {srv.precio.toFixed(2)}</span>
                </div>
                <button className="btn btn-secondary" onClick={() => onSelectServicio(srv)}>
                  Reservar
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
