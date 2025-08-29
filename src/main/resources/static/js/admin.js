// /static/js/admin.js
(function () {
  // Run after DOM is ready (safer if script isn't "defer")
  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }

  function init() {
    const sidebar  = byId("sidebar");     // <aside id="sidebar">
    const toggle   = byId("menuToggle");  // <button id="menuToggle">
    const closeBtn = byId("menuClose");   // <button id="menuClose"> inside sidebar (optional)
    let   backdrop = byId("backdrop");    // <div id="backdrop">

    // Create a backdrop if missing (defensive)
    if (!backdrop) {
      backdrop = document.createElement("div");
      backdrop.id = "backdrop";
      backdrop.className = "backdrop";
      backdrop.setAttribute("aria-hidden", "true");
      document.body.appendChild(backdrop);
    }

    // If required elements are missing, bail with a clear log
    if (!sidebar || !toggle) {
      console.warn("[admin.js] Missing #sidebar or #menuToggle in HTML.");
      return;
    }

    // Accessibility defaults
    toggle.setAttribute("aria-controls", "sidebar");
    toggle.setAttribute("aria-expanded", "false");
    sidebar.setAttribute("role", "navigation");
    sidebar.setAttribute("aria-label", "Menu admin");

    // Open / Close logic
    const open = () => {
      if (sidebar.classList.contains("open")) return;
      sidebar.classList.add("open");
      backdrop.classList.add("show");
      document.body.classList.add("menu-open");
      toggle.setAttribute("aria-expanded", "true");

      // Optional: focus the first link for keyboard users
      const firstLink = sidebar.querySelector(".menu a, a, button");
      firstLink && firstLink.focus({ preventScroll: true });
    };

    const close = () => {
      if (!sidebar.classList.contains("open")) return;
      sidebar.classList.remove("open");
      backdrop.classList.remove("show");
      document.body.classList.remove("menu-open");
      toggle.setAttribute("aria-expanded", "false");
      // Return focus to toggle for accessibility
      toggle.focus({ preventScroll: true });
    };

    // Click handlers
    toggle.addEventListener("click", (e) => {
      e.preventDefault();
      sidebar.classList.contains("open") ? close() : open();
    });

    backdrop.addEventListener("click", close);
    closeBtn && closeBtn.addEventListener("click", close);

    // Close when selecting a menu item (mobile)
    sidebar.querySelectorAll(".menu a").forEach((a) => {
      a.addEventListener("click", close);
    });

    // ESC to close
    document.addEventListener("keydown", (e) => {
      if (e.key === "Escape") close();
    });

    // On resize to desktop, ensure closed overlay state
    window.addEventListener("resize", () => {
      if (window.innerWidth > 980) close();
    });

    // Helper
    function byId(id) { return document.getElementById(id); }
  }
})();
