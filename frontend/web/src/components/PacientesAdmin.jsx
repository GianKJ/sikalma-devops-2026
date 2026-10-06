import React, { useState } from 'react';

const INITIAL_PACIENTES = [
  {
    id: 1,
    tipoDocumento: 'DNI',
    numeroDocumento: '74125896',
    nombres: 'Lucía Andrea',
    apellidos: 'Mendoza Carrillo',
    telefono: '951 478 230',
    correo: 'lucia.mendoza@gmail.com',
    fechaNacimiento: '1998-05-14',
    activo: true
  },
  {
    id: 2,
    tipoDocumento: 'DNI',
    numeroDocumento: '45896321',
    nombres: 'Jorge Luis',
    apellidos: 'Huamán Rivera',
    telefono: '963 852 741',
    correo: 'jorge.huaman@outlook.com',
    fechaNacimiento: '1990-11-20',
    activo: true
  }
];

export default function PacientesAdmin() {
  const [pacientes, setPacientes] = useState(INITIAL_PACIENTES);
  const [searchTerm, setSearchTerm] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [nuevoPaciente, setNuevoPaciente] = useState({
    tipoDocumento: 'DNI',
    numeroDocumento: '',
    nombres: '',
    apellidos: '',
    telefono: '',
    correo: '',
    fechaNacimiento: '',
    direccion: ''
  });
  const [error, setError] = useState('');

  const filteredPacientes = pacientes.filter(
    (p) =>
      p.numeroDocumento.includes(searchTerm) ||
      p.nombres.toLowerCase().includes(searchTerm.toLowerCase()) ||
      p.apellidos.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleCreate = (e) => {
    e.preventDefault();
    setError('');

    if (nuevoPaciente.tipoDocumento === 'DNI' && !/^\d{8}$/.test(nuevoPaciente.numeroDocumento)) {
      setError('El DNI debe tener exactamente 8 dígitos.');
      return;
    }

    if (pacientes.some((p) => p.numeroDocumento === nuevoPaciente.numeroDocumento)) {
      setError('Ya existe un paciente registrado con este número de documento.');
      return;
    }

    const nuevo = {
      ...nuevoPaciente,
      id: Date.now(),
      activo: true
    };

    setPacientes([nuevo, ...pacientes]);
    setShowForm(false);
    setNuevoPaciente({
      tipoDocumento: 'DNI',
      numeroDocumento: '',
      nombres: '',
      apellidos: '',
      telefono: '',
      correo: '',
      fechaNacimiento: '',
      direccion: ''
    });
  };

  return (
    <div className="container" style={{ padding: '2rem 0' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
        <div>
          <h2 style={{ fontSize: '1.75rem', fontWeight: '800', color: 'var(--primary-800)' }}>
            📋 Gestión de Pacientes y Admisión
          </h2>
          <p style={{ color: 'var(--slate-600)', fontSize: '0.875rem' }}>
            Registro único de pacientes, verificación de identidad y expedientes clínicos.
          </p>
        </div>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancelar' : '+ Nuevo Paciente'}
        </button>
      </div>

      {showForm && (
        <div className="card" style={{ marginBottom: '2rem' }}>
          <h3 style={{ fontSize: '1.125rem', fontWeight: '700', marginBottom: '1rem', color: 'var(--slate-900)' }}>
            Ficha de Admisión de Paciente
          </h3>
          {error && (
            <div style={{ backgroundColor: 'var(--danger-50)', color: 'var(--danger-700)', padding: '0.5rem 1rem', borderRadius: 'var(--radius-sm)', marginBottom: '1rem', fontSize: '0.875rem' }}>
              ⚠️ {error}
            </div>
          )}
          <form onSubmit={handleCreate} style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '1rem' }}>
            <div>
              <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: '600' }}>Tipo Doc.</label>
              <select 
                value={nuevoPaciente.tipoDocumento} 
                onChange={(e) => setNuevoPaciente({ ...nuevoPaciente, tipoDocumento: e.target.value })}
                style={{ width: '100%', padding: '0.5rem', borderRadius: 'var(--radius-sm)', border: '1px solid var(--slate-200)' }}
              >
                <option value="DNI">DNI (8 dígitos)</option>
                <option value="CARNET_EXTRANJERIA">Carné de Extranjería</option>
              </select>
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: '600' }}>N° Documento</label>
              <input 
                type="text" 
                placeholder="78945612" 
                value={nuevoPaciente.numeroDocumento} 
                onChange={(e) => setNuevoPaciente({ ...nuevoPaciente, numeroDocumento: e.target.value })}
                style={{ width: '100%', padding: '0.5rem', borderRadius: 'var(--radius-sm)', border: '1px solid var(--slate-200)' }}
                required
              />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: '600' }}>Nombres</label>
              <input 
                type="text" 
                placeholder="Nombres" 
                value={nuevoPaciente.nombres} 
                onChange={(e) => setNuevoPaciente({ ...nuevoPaciente, nombres: e.target.value })}
                style={{ width: '100%', padding: '0.5rem', borderRadius: 'var(--radius-sm)', border: '1px solid var(--slate-200)' }}
                required
              />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: '600' }}>Apellidos</label>
              <input 
                type="text" 
                placeholder="Apellidos" 
                value={nuevoPaciente.apellidos} 
                onChange={(e) => setNuevoPaciente({ ...nuevoPaciente, apellidos: e.target.value })}
                style={{ width: '100%', padding: '0.5rem', borderRadius: 'var(--radius-sm)', border: '1px solid var(--slate-200)' }}
                required
              />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: '600' }}>Teléfono</label>
              <input 
                type="tel" 
                placeholder="951 234 567" 
                value={nuevoPaciente.telefono} 
                onChange={(e) => setNuevoPaciente({ ...nuevoPaciente, telefono: e.target.value })}
                style={{ width: '100%', padding: '0.5rem', borderRadius: 'var(--radius-sm)', border: '1px solid var(--slate-200)' }}
                required
              />
            </div>
            <div>
              <label htmlFor="fechaNacimiento" style={{ display: 'block', fontSize: '0.75rem', fontWeight: '600' }}>Fecha Nacimiento</label>
              <input 
                id="fechaNacimiento"
                type="date" 
                value={nuevoPaciente.fechaNacimiento} 
                onChange={(e) => setNuevoPaciente({ ...nuevoPaciente, fechaNacimiento: e.target.value })}
                style={{ width: '100%', padding: '0.5rem', borderRadius: 'var(--radius-sm)', border: '1px solid var(--slate-200)' }}
                required
              />
            </div>
            <div style={{ gridColumn: 'span 2' }}>
              <button type="submit" className="btn btn-primary" style={{ width: '100%' }}>
                Guardar Paciente
              </button>
            </div>
          </form>
        </div>
      )}

      <div className="card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
          <input 
            type="text" 
            placeholder="🔍 Buscar por DNI, Nombres o Apellidos..." 
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            style={{ width: '320px', padding: '0.625rem', borderRadius: 'var(--radius-sm)', border: '1px solid var(--slate-200)' }}
          />
          <span style={{ fontSize: '0.875rem', color: 'var(--slate-500)' }}>
            Mostrando {filteredPacientes.length} de {pacientes.length} pacientes
          </span>
        </div>

        <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: '0.875rem' }}>
          <thead>
            <tr style={{ backgroundColor: 'var(--slate-100)', color: 'var(--slate-700)', borderBottom: '2px solid var(--slate-200)' }}>
              <th style={{ padding: '0.75rem 1rem' }}>Documento</th>
              <th style={{ padding: '0.75rem 1rem' }}>Paciente</th>
              <th style={{ padding: '0.75rem 1rem' }}>Teléfono</th>
              <th style={{ padding: '0.75rem 1rem' }}>Correo</th>
              <th style={{ padding: '0.75rem 1rem' }}>Estado</th>
            </tr>
          </thead>
          <tbody>
            {filteredPacientes.map((p) => (
              <tr key={p.id} style={{ borderBottom: '1px solid var(--slate-200)' }}>
                <td style={{ padding: '0.75rem 1rem', fontWeight: '600' }}>
                  <span className="badge badge-blue">{p.tipoDocumento}</span> {p.numeroDocumento}
                </td>
                <td style={{ padding: '0.75rem 1rem', fontWeight: '700', color: 'var(--slate-900)' }}>
                  {p.nombres} {p.apellidos}
                </td>
                <td style={{ padding: '0.75rem 1rem', color: 'var(--slate-600)' }}>{p.telefono}</td>
                <td style={{ padding: '0.75rem 1rem', color: 'var(--slate-600)' }}>{p.correo || '—'}</td>
                <td style={{ padding: '0.75rem 1rem' }}>
                  <span className="badge badge-green">Activo</span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
