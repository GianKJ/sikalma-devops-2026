document.addEventListener('DOMContentLoaded', () => {
  const usernameInput = document.getElementById('username');
  const passwordInput = document.getElementById('password');
  const toggleBtn = document.getElementById('toggle-password-btn');

  if (toggleBtn && passwordInput) {
    toggleBtn.addEventListener('click', () => {
      const isPassword = passwordInput.type === 'password';
      passwordInput.type = isPassword ? 'text' : 'password';
      toggleBtn.textContent = isPassword ? 'Ocultar' : 'Mostrar';
    });
  }

  document.querySelectorAll('[data-demo-user]').forEach((btn) => {
    btn.addEventListener('click', () => {
      if (usernameInput && passwordInput) {
        usernameInput.value = btn.dataset.demoUser || '';
        passwordInput.value = btn.dataset.demoPass || '';
        usernameInput.focus();
      }
    });
  });
});
