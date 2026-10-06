import React from 'react';

export const PSICOLOGOS_DATA = [
  {
    id: 1,
    nombres: "Mariana",
    apellidos: "Vásquez Benavides",
    colegiatura: "C.Ps.P. 28415",
    especialidad: "Psicología Clínica y Psicoterapia Cognitiva",
    modalidad: "AMBAS",
    experiencia: "8 años de experiencia en trastornos emocionales y ansiedad.",
    telefono: "962 841 520"
  },
  {
    id: 2,
    nombres: "Carlos",
    apellidos: "Rojas Morales",
    colegiatura: "C.Ps.P. 31920",
    especialidad: "Terapia Familiar y Sistémica",
    modalidad: "PRESENCIAL",
    experiencia: "Especialista en dinámicas de pareja, comunicación y manejo de crisis.",
    telefono: "987 452 103"
  }
];

export default function PsicologosSection({ onSelectPsicologo }) {
  return (
    <section style={{ padding: '3rem 0', backgroundColor: '#ffffff', borderTop: '1px solid var(--slate-200)' }}>
      <div className="container">
        <div style={{ textAlign: 'center', marginBottom: '2.5rem' }}>
          <span className="badge badge-green" style={{ marginBottom: '0.5rem' }}>Staff Profesional Habilitado</span>
          <h2 style={{ fontSize: '1.875rem', fontWeight: '800', color: 'var(--primary-800)' }}>Nuestros Psicólogos Colegiados</h2>
          <p style={{ color: 'var(--slate-600)', marginTop: '0.5rem', maxWidth: '600px', margin: '0.5rem auto 0' }}>
            Profesionales acreditados por el Colegio de Psicólogos del Perú (C.Ps.P.) con vocación y experiencia asistencial.
          </p>
        </div>

        <div className="grid-2">
          {PSICOLOGOS_DATA.map((psi) => (
            <div key={psi.id} className="card" style={{ display: 'flex', gap: '1.25rem', alignItems: 'flex-start' }}>
              <div style={{ width: '64px', height: '64px', borderRadius: '12px', backgroundColor: 'var(--primary-50)', color: 'var(--primary-800)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontWeight: '800', fontSize: '1.5rem', flexShrink: 0 }}>
                {psi.nombres.charAt(0)}{psi.apellidos.charAt(0)}
              </div>
              <div style={{ flex: 1 }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.25rem' }}>
                  <h3 style={{ fontSize: '1.125rem', fontWeight: '700', color: 'var(--slate-900)' }}>
                    Ps. {psi.nombres} {psi.apellidos}
                  </h3>
                  <span className="badge badge-blue">{psi.colegiatura}</span>
                </div>
                <p style={{ fontSize: '0.875rem', color: 'var(--secondary-700)', fontWeight: '600', marginBottom: '0.5rem' }}>
                  {psi.especialidad}
                </p>
                <p style={{ fontSize: '0.875rem', color: 'var(--slate-600)', marginBottom: '1rem' }}>
                  {psi.experiencia}
                </p>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <span style={{ fontSize: '0.75rem', color: 'var(--slate-500)' }}>Modalidad: <strong>{psi.modalidad}</strong></span>
                  <button className="btn btn-outline" onClick={() => onSelectPsicologo(psi)}>
                    Ver Horarios
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
