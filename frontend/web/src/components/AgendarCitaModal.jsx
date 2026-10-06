import React, { useState } from 'react';
import { SERVICIOS_DATA } from './ServiciosSection';
import { PSICOLOGOS_DATA } from './PsicologosSection';

export default function AgendarCitaModal({ initialServicio, initialPsicologo, onSuccess }) {
  const [formData, setFormData] = useState({
    tipoDocumento: 'DNI',
    numeroDocumento: '',
    nombres: '',
    apellidos: '',
    telefono: '',
    correo: '',
    servicioId: initialServicio?.id || '1',
    psicologoId: initialPsicologo?.id || '1',
    fecha: '',
    hora: '09:00',
    modalidad: 'PRESENCIAL',
    motivo: ''
  });

  const [error, setError] = useState('');
  const [successMsg, setSuccessMsg] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setError('');

    // Validacion DNI
    if (formData.tipoDocumento === 'DNI' && !/^\d{8}$/.test(formData.numeroDocumento)) {
      setError('El DNI debe contener exactamente 8 dígitos numéricos.');
      return;
    }

    if (!formData.nombres.trim() || !formData.apellidos.trim()) {
      setError('Por favor complete los nombres y apellidos del paciente.');
      return;
    }

    if (!formData.telefono.trim()) {
      setError('Por favor ingrese un número de teléfono de contacto.');
      return;
    }

    if (!formData.fecha) {
      setError('Por favor seleccione una fecha para la cita.');
      return;
    }

    // Simulacion exitosa
    setSuccessMsg(`¡Cita registrada con éxito para ${formData.nombres} ${formData.apellidos}! Te contactaremos al ${formData.telefono} para confirmar.`);
    if (onSuccess) {
      onSuccess(formData);
    }
  };

  return (
    <div className="card" style={{ maxWidth: '680px', margin: '2rem auto' }}>
      <div style={{ borderBottom: '1px solid var(--slate-200)', paddingBottom: '1rem', marginBottom: '1.5rem' }}>
        <h2 style={{ fontSize: '1.5rem', fontWeight: '800', color: 'var(--primary-800)' }}>
          🗓️ Reserva de Cita Psicológica
        </h2>
        <p style={{ fontSize: '0.875rem', color: 'var(--slate-600)' }}>
          Completa el formulario para programar tu atención presencial o virtual en SIKALMA.
        </p>
      </div>

      {error && (
        <div style={{ backgroundColor: 'var(--danger-50)', color: 'var(--danger-700)', padding: '0.75rem 1rem', borderRadius: 'var(--radius-md)', marginBottom: '1.5rem', fontSize: '0.875rem', fontWeight: '600' }}>
          ⚠️ {error}
        </div>
      )}

      {successMsg && (
        <div style={{ backgroundColor: 'var(--success-50)', color: 'var(--success-700)', padding: '1rem', borderRadius: 'var(--radius-md)', marginBottom: '1.5rem', fontSize: '0.875rem', fontWeight: '600' }}>
          ✅ {successMsg}
        </div>
      )}

      <form onSubmit={handleSubmit} style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
        <div>
          <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: '600', marginBottom: '0.25rem' }}>Tipo Documento</label>
          <select 
            name="tipoDocumento" 
            value={formData.tipoDocumento} 
            onChange={handleChange}
            style={{ width: '100%', padding: '0.625rem', borderRadius: 'var(--radius-sm)', border: '1px solid var(--slate-200)' }}
          >
            <option value="DNI">DNI (8 dígitos)</option>
            <option value="CARNET_EXTRANJERIA">Carné de Extranjería (9 dígitos)</option>
          </select>
        </div>

        <div>
          <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: '600', marginBottom: '0.25rem' }}>N° Documento</label>
          <input 
            type="text" 
            name="numeroDocumento" 
            placeholder="Ej. 74859612" 
            value={formData.numeroDocumento} 
            onChange={handleChange}
            maxLength={9}
            style={{ width: '100%', padding: '0.625rem', borderRadius: 'var(--radius-sm)', border: '1px solid var(--slate-200)' }}
            required
          />
        </div>

        <div>
          <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: '600', marginBottom: '0.25rem' }}>Nombres</label>
          <input 
            type="text" 
            name="nombres" 
            placeholder="Ej. Juan Carlos" 
            value={formData.nombres} 
            onChange={handleChange}
            style={{ width: '100%', padding: '0.625rem', borderRadius: 'var(--radius-sm)', border: '1px solid var(--slate-200)' }}
            required
          />
        </div>

        <div>
          <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: '600', marginBottom: '0.25rem' }}>Apellidos</label>
          <input 
            type="text" 
            name="apellidos" 
            placeholder="Ej. Pérez Gómez" 
            value={formData.apellidos} 
            onChange={handleChange}
            style={{ width: '100%', padding: '0.625rem', borderRadius: 'var(--radius-sm)', border: '1px solid var(--slate-200)' }}
            required
          />
        </div>

        <div>
          <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: '600', marginBottom: '0.25rem' }}>Teléfono / WhatsApp</label>
          <input 
            type="tel" 
            name="telefono" 
            placeholder="Ej. 951 234 567" 
            value={formData.telefono} 
            onChange={handleChange}
            style={{ width: '100%', padding: '0.625rem', borderRadius: 'var(--radius-sm)', border: '1px solid var(--slate-200)' }}
            required
          />
        </div>

        <div>
          <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: '600', marginBottom: '0.25rem' }}>Correo Electrónico</label>
          <input 
            type="email" 
            name="correo" 
            placeholder="Ej. paciente@gmail.com" 
            value={formData.correo} 
            onChange={handleChange}
            style={{ width: '100%', padding: '0.625rem', borderRadius: 'var(--radius-sm)', border: '1px solid var(--slate-200)' }}
          />
        </div>

        <div style={{ gridColumn: 'span 2' }}>
          <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: '600', marginBottom: '0.25rem' }}>Servicio Requerido</label>
          <select 
            name="servicioId" 
            value={formData.servicioId} 
            onChange={handleChange}
            style={{ width: '100%', padding: '0.625rem', borderRadius: 'var(--radius-sm)', border: '1px solid var(--slate-200)' }}
          >
            {SERVICIOS_DATA.map((s) => (
              <option key={s.id} value={s.id}>{s.nombre} — S/ {s.precio.toFixed(2)} ({s.duracion} min)</option>
            ))}
          </select>
        </div>

        <div>
          <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: '600', marginBottom: '0.25rem' }}>Psicólogo de Preferencia</label>
          <select 
            name="psicologoId" 
            value={formData.psicologoId} 
            onChange={handleChange}
            style={{ width: '100%', padding: '0.625rem', borderRadius: 'var(--radius-sm)', border: '1px solid var(--slate-200)' }}
          >
            {PSICOLOGOS_DATA.map((p) => (
              <option key={p.id} value={p.id}>Ps. {p.nombres} {p.apellidos} ({p.colegiatura})</option>
            ))}
          </select>
        </div>

        <div>
          <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: '600', marginBottom: '0.25rem' }}>Modalidad</label>
          <select 
            name="modalidad" 
            value={formData.modalidad} 
            onChange={handleChange}
            style={{ width: '100%', padding: '0.625rem', borderRadius: 'var(--radius-sm)', border: '1px solid var(--slate-200)' }}
          >
            <option value="PRESENCIAL">Presencial (Sede Huánuco)</option>
            <option value="VIRTUAL">Virtual (Google Meet / Zoom)</option>
          </select>
        </div>

        <div>
          <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: '600', marginBottom: '0.25rem' }}>Fecha de Cita</label>
          <input 
            type="date" 
            name="fecha" 
            value={formData.fecha} 
            onChange={handleChange}
            style={{ width: '100%', padding: '0.625rem', borderRadius: 'var(--radius-sm)', border: '1px solid var(--slate-200)' }}
            required
          />
        </div>

        <div>
          <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: '600', marginBottom: '0.25rem' }}>Hora de Cita</label>
          <select 
            name="hora" 
            value={formData.hora} 
            onChange={handleChange}
            style={{ width: '100%', padding: '0.625rem', borderRadius: 'var(--radius-sm)', border: '1px solid var(--slate-200)' }}
          >
            <option value="09:00">09:00 AM</option>
            <option value="10:00">10:00 AM</option>
            <option value="11:00">11:00 AM</option>
            <option value="15:00">03:00 PM</option>
            <option value="16:00">04:00 PM</option>
            <option value="17:00">05:00 PM</option>
            <option value="18:00">06:00 PM</option>
          </select>
        </div>

        <div style={{ gridColumn: 'span 2', marginTop: '1rem' }}>
          <button type="submit" className="btn btn-primary" style={{ width: '100%', padding: '0.75rem', fontSize: '1rem' }}>
            Confirmar Reserva de Cita
          </button>
        </div>
      </form>
    </div>
  );
}
