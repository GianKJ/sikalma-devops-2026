import React, { useState } from 'react';
import Navbar from './components/Navbar';
import ServiciosSection from './components/ServiciosSection';
import PsicologosSection from './components/PsicologosSection';
import AgendarCitaModal from './components/AgendarCitaModal';
import PacientesAdmin from './components/PacientesAdmin';

export default function App() {
  const [activeTab, setActiveTab] = useState('inicio');
  const [selectedServicio, setSelectedServicio] = useState(null);
  const [selectedPsicologo, setSelectedPsicologo] = useState(null);

  const handleSelectServicio = (srv) => {
    setSelectedServicio(srv);
    setActiveTab('agendar');
  };

  const handleSelectPsicologo = (psi) => {
    setSelectedPsicologo(psi);
    setActiveTab('agendar');
  };

  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
      <Navbar activeTab={activeTab} setActiveTab={setActiveTab} />

      <main style={{ flex: 1 }}>
        {activeTab === 'inicio' && (
          <div>
            <section style={{ background: 'linear-gradient(135deg, var(--primary-800) 0%, var(--secondary-700) 100%)', color: '#ffffff', padding: '4rem 0' }}>
              <div className="container" style={{ textAlign: 'center' }}>
                <span className="badge" style={{ backgroundColor: 'rgba(255,255,255,0.2)', color: '#ffffff', marginBottom: '1rem' }}>
                  Centro Psicológico • Sede Huánuco
                </span>
                <h1 style={{ fontSize: '2.5rem', fontWeight: '800', lineHeight: '1.2', marginBottom: '1rem' }}>
                  Tu Refugio de Calma y Equilibrio Emocional
                </h1>
                <p style={{ fontSize: '1.125rem', opacity: 0.9, maxWidth: '700px', margin: '0 auto 2rem' }}>
                  Atención psicológica profesional, cálida y confidencial para niños, adolescentes, jóvenes y adultos. Agenda tu consulta presencial o virtual en minutos.
                </p>
                <div style={{ display: 'flex', gap: '1rem', justifyContent: 'center' }}>
                  <button className="btn btn-secondary" onClick={() => setActiveTab('agendar')} style={{ fontSize: '1rem', padding: '0.75rem 1.5rem' }}>
                    🗓️ Agendar Cita en Línea
                  </button>
                  <button className="btn btn-outline" onClick={() => setActiveTab('servicios')} style={{ fontSize: '1rem', padding: '0.75rem 1.5rem', backgroundColor: 'transparent', color: '#ffffff', borderColor: 'rgba(255,255,255,0.4)' }}>
                    Ver Servicios y Tarifario
                  </button>
                </div>
              </div>
            </section>

            <ServiciosSection onSelectServicio={handleSelectServicio} />
            <PsicologosSection onSelectPsicologo={handleSelectPsicologo} />
          </div>
        )}

        {activeTab === 'servicios' && (
          <ServiciosSection onSelectServicio={handleSelectServicio} />
        )}

        {activeTab === 'psicologos' && (
          <PsicologosSection onSelectPsicologo={handleSelectPsicologo} />
        )}

        {activeTab === 'agendar' && (
          <div className="container" style={{ padding: '2rem 0' }}>
            <AgendarCitaModal 
              initialServicio={selectedServicio} 
              initialPsicologo={selectedPsicologo}
              onSuccess={() => {}}
            />
          </div>
        )}

        {activeTab === 'admin' && (
          <PacientesAdmin />
        )}
      </main>

      <footer style={{ backgroundColor: 'var(--slate-900)', color: 'var(--slate-400)', padding: '2.5rem 0', borderTop: '1px solid var(--slate-800)' }}>
        <div className="container" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '1rem' }}>
          <div>
            <h3 style={{ color: '#ffffff', fontSize: '1.125rem', fontWeight: '800' }}>SIKALMA</h3>
            <p style={{ fontSize: '0.875rem' }}>Centro Psicológico • Huánuco, Perú | Calle Los Tulipanes 112, Amarilis</p>
            <p style={{ fontSize: '0.75rem', marginTop: '0.25rem' }}>Contacto: 993 668 057 • centropsicologicosikalma@gmail.com</p>
          </div>
          <div style={{ fontSize: '0.75rem' }}>
            © 2026 Centro Psicológico Sikalma. Todos los derechos reservados.
          </div>
        </div>
      </footer>
    </div>
  );
}
