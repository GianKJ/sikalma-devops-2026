import React, { useState } from 'react';

export default function Login({ onLoginSuccess, onCancel }) {
  const [username, setUsername] = useState('admin');
  const [password, setPassword] = useState('Admin123!');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    // Validación y simulación de autenticación con credenciales del sistema
    setTimeout(() => {
      if ((username === 'admin' || username === 'admin@sikalma.pe') && password === 'Admin123!') {
        setLoading(false);
        if (onLoginSuccess) {
          onLoginSuccess({ username, rol: 'ADMINISTRADOR', nombre: 'Administrador SIKALMA' });
        }
      } else {
        setLoading(false);
        setError('El usuario o la contraseña no son correctos.');
      }
    }, 400);
  };

  return (
    <div style={{ maxWidth: '440px', margin: '3rem auto', padding: '0 1rem' }}>
      <div className="card" style={{ boxShadow: '0 10px 25px -5px rgba(0, 0, 0, 0.1), 0 8px 10px -6px rgba(0, 0, 0, 0.1)', borderRadius: '12px', padding: '2.5rem 2rem' }}>
        <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
          <div style={{ 
            width: '48px', 
            height: '48px', 
            borderRadius: '12px', 
            backgroundColor: 'var(--primary-800)', 
            color: '#ffffff', 
            display: 'flex', 
            alignItems: 'center', 
            justifyContent: 'center', 
            fontWeight: '800', 
            fontSize: '1.5rem',
            margin: '0 auto 1rem'
          }}>
            S
          </div>
          <h2 style={{ fontSize: '1.5rem', fontWeight: '800', color: 'var(--primary-800)', margin: '0 0 0.5rem 0' }}>
            Iniciar Sesión
          </h2>
          <p style={{ color: 'var(--slate-600)', fontSize: '0.875rem', margin: 0 }}>
            Acceso al Sistema Privado de SIKALMA
          </p>
        </div>

        {error && (
          <div style={{ 
            backgroundColor: 'var(--danger-50)', 
            color: 'var(--danger-700)', 
            border: '1px solid #fecaca', 
            padding: '0.75rem 1rem', 
            borderRadius: 'var(--radius-sm)', 
            marginBottom: '1.5rem', 
            fontSize: '0.875rem' 
          }}>
            ⚠️ {error}
          </div>
        )}

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1.25rem' }}>
          <div>
            <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: '600', color: 'var(--slate-700)', marginBottom: '0.375rem' }}>
              Correo o usuario
            </label>
            <input 
              type="text" 
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="admin o usuario@sikalma.pe"
              required
              autoFocus
              style={{ 
                width: '100%', 
                padding: '0.625rem 0.875rem', 
                borderRadius: 'var(--radius-sm)', 
                border: '1px solid var(--slate-300)',
                fontSize: '0.9375rem',
                outline: 'none',
                boxSizing: 'border-box'
              }}
            />
          </div>

          <div>
            <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: '600', color: 'var(--slate-700)', marginBottom: '0.375rem' }}>
              Contraseña
            </label>
            <input 
              type="password" 
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
              required
              style={{ 
                width: '100%', 
                padding: '0.625rem 0.875rem', 
                borderRadius: 'var(--radius-sm)', 
                border: '1px solid var(--slate-300)',
                fontSize: '0.9375rem',
                outline: 'none',
                boxSizing: 'border-box'
              }}
            />
          </div>

          <div style={{ 
            backgroundColor: 'var(--slate-50)', 
            padding: '0.75rem', 
            borderRadius: 'var(--radius-sm)', 
            border: '1px dashed var(--slate-300)',
            fontSize: '0.75rem', 
            color: 'var(--slate-600)' 
          }}>
            🔑 <strong>Credenciales demo:</strong><br />
            Usuario: <code>admin</code> | Contraseña: <code>Admin123!</code>
          </div>

          <button 
            type="submit" 
            className="btn btn-primary" 
            disabled={loading}
            style={{ 
              width: '100%', 
              padding: '0.75rem', 
              fontSize: '1rem', 
              fontWeight: '700',
              marginTop: '0.5rem'
            }}
          >
            {loading ? 'Validando credenciales...' : 'Ingresar al Sistema'}
          </button>

          {onCancel && (
            <button 
              type="button" 
              onClick={onCancel}
              style={{ 
                background: 'none', 
                border: 'none', 
                color: 'var(--slate-500)', 
                fontSize: '0.8125rem', 
                cursor: 'pointer',
                textAlign: 'center',
                textDecoration: 'underline'
              }}
            >
              Volver al sitio público
            </button>
          )}
        </form>
      </div>
    </div>
  );
}
