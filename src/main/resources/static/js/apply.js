(function(){
  const sidebar  = document.getElementById('sidebar');
  const toggle   = document.getElementById('menuToggle');
  const closeBtn = document.getElementById('menuClose');
  const backdrop = document.getElementById('backdrop');

  const open = () => {
    if (!sidebar) return;
    sidebar.classList.add('open');
    document.body.classList.add('menu-open');
    backdrop && backdrop.classList.add('show');
    toggle && toggle.setAttribute('aria-expanded','true');
  };
  const close = () => {
    if (!sidebar) return;
    sidebar.classList.remove('open');
    document.body.classList.remove('menu-open');
    backdrop && backdrop.classList.remove('show');
    toggle && toggle.setAttribute('aria-expanded','false');
  };

  toggle && toggle.addEventListener('click', open);
  closeBtn && closeBtn.addEventListener('click', close);
  backdrop && backdrop.addEventListener('click', close);

  // Close when selecting a menu item (on mobile)
  document.querySelectorAll('.menu a').forEach(a => a.addEventListener('click', close));

  // Ensure closed if resized back to desktop
  window.addEventListener('resize', () => {
    if (window.innerWidth > 980) close();
  });
})();
