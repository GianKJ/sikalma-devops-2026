document.addEventListener('DOMContentLoaded', () => {
  initCurrentDate();
  initWelcomeOverlay();
  initKpiCounters();
  initBarCharts();
  initDonutChart();
  initSlotSuggestions();
  initPrintButtons();
});

function initCurrentDate() {
  const dateEl = document.getElementById('current-date-time');
  if (!dateEl) {
    return;
  }
  const prefix = dateEl.dataset.prefix || 'Panel';
  const now = new Date();
  const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
  dateEl.textContent = `${prefix} • ${now.toLocaleDateString('es-ES', options)}`;
}

function initWelcomeOverlay() {
  const overlay = document.getElementById('welcome-overlay');
  if (!overlay) {
    return;
  }
  const fill = overlay.querySelector('.welcome-progress-fill');
  const skipBtn = document.getElementById('welcome-skip-btn');

  requestAnimationFrame(() => {
    if (fill) {
      fill.style.width = '100%';
    }
  });

  const dismiss = () => {
    overlay.classList.add('is-hidden');
  };

  const timer = setTimeout(dismiss, 1850);
  if (skipBtn) {
    skipBtn.addEventListener('click', () => {
      clearTimeout(timer);
      dismiss();
    });
  }
}

function initKpiCounters() {
  document.querySelectorAll('[data-kpi-target]').forEach((el) => {
    const target = Number.parseInt(el.dataset.kpiTarget || '0', 10);
    const suffix = el.dataset.kpiSuffix || '';
    if (Number.isNaN(target) || target <= 0) {
      el.textContent = `0${suffix}`;
      return;
    }
    const duration = 800;
    const start = performance.now();

    const step = (now) => {
      const progress = Math.min(1, (now - start) / duration);
      const eased = 1 - Math.pow(1 - progress, 3);
      el.textContent = `${Math.round(target * eased)}${suffix}`;
      if (progress < 1) {
        requestAnimationFrame(step);
      }
    };

    requestAnimationFrame(step);
  });
}

function initBarCharts() {
  const fills = document.querySelectorAll('.bar-fill[data-bar-value]');
  if (fills.length === 0) {
    return;
  }

  let maxVal = 1;
  fills.forEach((fill) => {
    const val = Number.parseInt(fill.dataset.barValue || '0', 10);
    if (val > maxVal) {
      maxVal = val;
    }
  });

  requestAnimationFrame(() => {
    fills.forEach((fill) => {
      const val = Number.parseInt(fill.dataset.barValue || '0', 10);
      const pct = maxVal > 0 ? Math.round((val / maxVal) * 100) : 0;
      fill.style.width = `${pct}%`;
    });
  });
}

function initDonutChart() {
  const donutSvg = document.getElementById('donut-modalidad');
  if (!donutSvg) {
    return;
  }
  const presencial = Number.parseInt(donutSvg.dataset.presencial || '0', 10);
  const virtual = Number.parseInt(donutSvg.dataset.virtual || '0', 10);
  const total = presencial + virtual;
  const circlePresencial = document.getElementById('donut-segment-presencial');
  const circleVirtual = document.getElementById('donut-segment-virtual');
  const radius = 56;
  const circumference = 2 * Math.PI * radius;

  if (!circlePresencial || !circleVirtual) {
    return;
  }

  if (total <= 0) {
    circlePresencial.setAttribute('stroke-dasharray', `0 ${circumference}`);
    circleVirtual.setAttribute('stroke-dasharray', `0 ${circumference}`);
    return;
  }

  const lenPresencial = (presencial / total) * circumference;
  const lenVirtual = (virtual / total) * circumference;

  requestAnimationFrame(() => {
    circlePresencial.setAttribute('stroke-dasharray', `${lenPresencial} ${circumference}`);
    circleVirtual.setAttribute('stroke-dasharray', `${lenVirtual} ${circumference}`);
    circleVirtual.setAttribute('stroke-dashoffset', `${-lenPresencial}`);
  });
}

function initSlotSuggestions() {
  const horaInput = document.getElementById('horaInicio');
  if (!horaInput) {
    return;
  }
  document.querySelectorAll('[data-suggested-time]').forEach((btn) => {
    btn.addEventListener('click', () => {
      horaInput.value = btn.dataset.suggestedTime || '';
      horaInput.focus();
    });
  });
}

function initPrintButtons() {
  document.querySelectorAll('[data-print-trigger]').forEach((btn) => {
    btn.addEventListener('click', () => {
      window.print();
    });
  });
}
