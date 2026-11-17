document.addEventListener('DOMContentLoaded', () => {
  initMobileMenu();
  initScrollHeaderAndProgress();
  initScrollRevealAndCounters();
  initHeroVideo();
  initKidsVerticalVideo();
  initServiceFilters();
  initFaqAccordion();
});

function initMobileMenu() {
  const menuButton = document.querySelector('[data-menu-button]');
  const menu = document.querySelector('[data-menu]');
  if (!menuButton || !menu) {
    return;
  }

  menuButton.addEventListener('click', () => {
    const expanded = menuButton.getAttribute('aria-expanded') === 'true';
    menuButton.setAttribute('aria-expanded', String(!expanded));
    menu.classList.toggle('is-open', !expanded);
  });

  menu.querySelectorAll('a').forEach((link) => {
    link.addEventListener('click', () => {
      menuButton.setAttribute('aria-expanded', 'false');
      menu.classList.remove('is-open');
    });
  });
}

function initScrollHeaderAndProgress() {
  const header = document.querySelector('.site-header');
  const progressBar = document.getElementById('scroll-progress');
  const stepper = document.querySelector('.stepper');
  const stepperLine = document.getElementById('stepper-progress-line');

  const updateOnScroll = () => {
    const scrollY = window.scrollY;
    if (header) {
      header.classList.toggle('is-scrolled', scrollY > 30);
    }
    if (progressBar) {
      const docHeight = document.documentElement.scrollHeight - window.innerHeight;
      const pct = docHeight > 0 ? Math.min(100, Math.round((scrollY / docHeight) * 100)) : 0;
      progressBar.style.width = `${pct}%`;
    }
    if (stepper && stepperLine) {
      const rect = stepper.getBoundingClientRect();
      const viewportMid = window.innerHeight * 0.65;
      const filled = Math.max(0, Math.min(rect.height, viewportMid - rect.top));
      const pctStep = rect.height > 0 ? Math.round((filled / rect.height) * 100) : 0;
      stepperLine.style.height = `${pctStep}%`;
    }
  };

  window.addEventListener('scroll', updateOnScroll, { passive: true });
  updateOnScroll();
}

function initScrollRevealAndCounters() {
  const revealElements = document.querySelectorAll('.reveal');
  const counters = document.querySelectorAll('[data-count-to]');

  if (!('IntersectionObserver' in window)) {
    revealElements.forEach((el) => el.classList.add('active'));
    counters.forEach((el) => {
      el.textContent = `${el.dataset.countTo || ''}${el.dataset.suffix || ''}`;
    });
    return;
  }

  const observer = new IntersectionObserver((entries, obs) => {
    entries.forEach((entry) => {
      if (!entry.isIntersecting) {
        return;
      }
      entry.target.classList.add('active');
      if (entry.target.hasAttribute('data-count-to')) {
        animateCounter(entry.target);
      }
      obs.unobserve(entry.target);
    });
  }, { threshold: 0.14, rootMargin: '0px 0px -36px 0px' });

  revealElements.forEach((el) => observer.observe(el));
  counters.forEach((el) => observer.observe(el));
}

function animateCounter(el) {
  const target = Number.parseInt(el.dataset.countTo || '0', 10);
  const suffix = el.dataset.suffix || '';
  if (Number.isNaN(target) || target <= 0) {
    el.textContent = `0${suffix}`;
    return;
  }
  const duration = 1100;
  const start = performance.now();

  const step = (now) => {
    const progress = Math.min(1, (now - start) / duration);
    const eased = 1 - Math.pow(1 - progress, 3);
    const current = Math.round(target * eased);
    el.textContent = `${current}${suffix}`;
    if (progress < 1) {
      requestAnimationFrame(step);
    }
  };

  requestAnimationFrame(step);
}

function initHeroVideo() {
  const video = document.getElementById('hero-bg-video');
  const toggleBtn = document.getElementById('hero-video-toggle');
  if (!video) {
    return;
  }
  video.playbackRate = 0.85;

  if (toggleBtn) {
    toggleBtn.addEventListener('click', () => {
      if (video.paused) {
        video.play().catch(() => {});
        toggleBtn.textContent = '⏸ Pausar video';
      } else {
        video.pause();
        toggleBtn.textContent = '▶ Reproducir video';
      }
    });
  }
}

function initKidsVerticalVideo() {
  const video = document.getElementById('kids-vertical-video');
  const playBtn = document.getElementById('kids-play-toggle');
  const soundBtn = document.getElementById('kids-sound-toggle');
  if (!video) {
    return;
  }

  if (playBtn) {
    playBtn.addEventListener('click', () => {
      if (video.paused) {
        video.play().catch(() => {});
        playBtn.textContent = '⏸ Pausar';
      } else {
        video.pause();
        playBtn.textContent = '▶ Reproducir';
      }
    });
  }

  if (soundBtn) {
    soundBtn.addEventListener('click', () => {
      video.muted = !video.muted;
      soundBtn.textContent = video.muted ? '🔇 Activar audio' : '🔊 Silenciar';
    });
  }
}

function initServiceFilters() {
  const buttons = document.querySelectorAll('[data-service-filter]');
  const cards = document.querySelectorAll('[data-service-modality]');
  if (buttons.length === 0 || cards.length === 0) {
    return;
  }

  buttons.forEach((btn) => {
    btn.addEventListener('click', () => {
      const selected = btn.dataset.serviceFilter || 'TODOS';
      buttons.forEach((b) => b.classList.toggle('is-active', b === btn));
      cards.forEach((card) => {
        const modality = card.dataset.serviceModality || 'AMBAS';
        const show = selected === 'TODOS' || modality === selected || modality === 'AMBAS';
        card.classList.toggle('is-hidden', !show);
      });
    });
  });
}

function initFaqAccordion() {
  const items = document.querySelectorAll('[data-faq-item]');
  items.forEach((item) => {
    const trigger = item.querySelector('[data-faq-trigger]');
    if (!trigger) {
      return;
    }
    trigger.addEventListener('click', () => {
      const wasOpen = item.classList.contains('is-open');
      items.forEach((other) => {
        other.classList.remove('is-open');
        const otherBtn = other.querySelector('[data-faq-trigger]');
        if (otherBtn) {
          otherBtn.setAttribute('aria-expanded', 'false');
        }
      });
      if (!wasOpen) {
        item.classList.add('is-open');
        trigger.setAttribute('aria-expanded', 'true');
      }
    });
  });
}
