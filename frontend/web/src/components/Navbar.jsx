import React from 'react';

export default function Navbar({ activeTab, setActiveTab, user, onLogout }) {
  return (
    <header style={{ backgroundColor: '#ffffff', borderBottom: '1px solid var(--slate-200)', position: 'sticky', top: 0, zIndex: 50 }}>
      <div className="container" style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', height: '72px' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', cursor: 'pointer' }} onClick={() => setActiveTab('inicio')}>
          <div style={{ width: '40px', height: '40px', borderRadius: '8px', backgroundColor: 'var(--primary-800)', color: '#ffffff', display: 'flex', alignItems: 'center', justifyContent: 'center', fontWeight: '800', fontSize: '1.25rem' }}>
            S
          </div>
          <div>
            <h1 style={{ fontSize: '1.25rem', fontWeight: '800', color: 'var(--primary-800)', lineHeight: '1.1' }}>SIKALMA</h1>
            <span style={{ fontSize: '0.75rem', color: 'var(--slate-500)', fontWeight: '500' }}>Centro Psicológico • Huánuco</span>
          </div>
        </div>

        <nav style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          <button 
            className={`btn ${activeTab === 'inicio' ? 'btn-primary' : 'btn-outline'}`}
            onClick={() => setActiveTab('inicio')}
          >
            Inicio
          </button>
          <button 
            className={`btn ${activeTab === 'servicios' ? 'btn-primary' : 'btn-outline'}`}
            onClick={() => setActiveTab('servicios')}
          >
            Servicios
          </button>
          <button 
            className={`btn ${activeTab === 'psicologos' ? 'btn-primary' : 'btn-outline'}`}
            onClick={() => setActiveTab('psicologos')}
          >
            Psicólogos
          </button>
          <button 
            className={`btn ${activeTab === 'agendar' ? 'btn-secondary' : 'btn-outline'}`}
            onClick={() => setActiveTab('agendar')}
          >
            🗓️ Agendar Cita
          </button>
          <button 
            className={`btn ${activeTab === 'admin' ? 'btn-primary' : 'btn-outline'}`}
            onClick={() => setActiveTab('admin')}
            style={{ marginLeft: '0.5rem', borderColor: 'var(--primary-800)' }}
          >
            👥 Gestión Usuarios/Pacientes
          </button>
          
          {user ? (
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginLeft: '0.5rem' }}>
              <span className="badge badge-green" style={{ fontSize: '0.75rem' }}>
                👤 {user.username}
              </span>
              <button 
                className="btn btn-outline"
                style={{ padding: '0.35rem 0.65rem', fontSize: '0.75rem', borderColor: 'var(--danger-700)', color: 'var(--danger-700)' }}
                onClick={onLogout}
              >
                Salir
              </button>
            </div>
          ) : (
            <button 
              className={`btn ${activeTab === 'login' ? 'btn-primary' : 'btn-outline'}`}
              onClick={() => setActiveTab('login')}
              style={{ marginLeft: '0.5rem', fontWeight: '700' }}
            >
              🔐 Iniciar Sesión
            </button>
          )}
        </nav>
      </div>
    </header>
  );
}
