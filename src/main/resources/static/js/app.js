// Time calculations for deadline-based status.
// D = days between today (local) and applicationDeadline
function daysUntil(dateStr){
  try{
    const today = new Date();                       // local date
    const d = new Date(dateStr + "T00:00:00");      // normalize to midnight
    const ms = d.setHours(0,0,0,0) - today.setHours(0,0,0,0);
    return Math.floor(ms / (1000*60*60*24));
  }catch(e){ return null; }
}

function applyExamStatuses(){
  document.querySelectorAll('.exam-card').forEach(card=>{
    const deadline = card.getAttribute('data-deadline');
    const D = daysUntil(deadline);
    if (D === null) return;

    card.classList.remove('is-last','is-on','is-up');
    if (D === 0) card.classList.add('is-last');      // Dernier jour
    else if (D >= 1 && D <= 7) card.classList.add('is-on'); // En cours
    else if (D > 7) card.classList.add('is-up');     // À venir
  });
}

// Guide (conditions) modal
function attachGuideHandlers(){
  const guideModal = document.getElementById('guideModal');
  const guideContent = document.getElementById('guideContent');
  if(!guideModal || !guideContent) return;

  document.querySelectorAll('.js-open-guide').forEach(btn=>{
    btn.addEventListener('click', ()=>{
      const card = btn.closest('.exam-card');
      const cond = card ? card.getAttribute('data-condition') : '';
      guideContent.textContent = cond || 'Aucune condition fournie.';
      guideModal.showModal();
    });
  });
}

// Centers dialog
function attachCentersHandlers(){
  const centersModal = document.getElementById('centersModal');
  const centersContent = document.getElementById('centersContent');
  if(!centersModal || !centersContent) return;

  document.querySelectorAll('.js-open-centers').forEach(btn=>{
    btn.addEventListener('click', async ()=>{
      const card = btn.closest('.exam-card');
      const examId = card ? card.getAttribute('data-exam-id') : null;
      if(!examId) return;

      centersContent.innerHTML = '<div class="muted">Chargement…</div>';
      centersModal.showModal();

      try{
        const res = await fetch(`/exams/${examId}/centers`, { headers: { 'Accept': 'application/json' }});
        if(!res.ok) throw new Error('HTTP ' + res.status);
        const data = await res.json();
        if(!Array.isArray(data) || data.length === 0){
          centersContent.innerHTML = '<div class="muted">Aucun centre trouvé.</div>';
          return;
        }
        centersContent.innerHTML = `
          <ul class="bullets">
            ${data.map(c => `<li><strong>${escapeHtml(c.name || '')}</strong> — ${escapeHtml(c.location || '')}</li>`).join('')}
          </ul>
        `;
      }catch(err){
        centersContent.innerHTML = '<div class="muted">Erreur lors du chargement des centres.</div>';
      }
    });
  });
}

function escapeHtml(s){
  return String(s).replace(/[&<>"']/g, m => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[m]));
}

document.addEventListener('DOMContentLoaded', ()=>{
  applyExamStatuses();
  attachGuideHandlers();
  attachCentersHandlers();
});






// load more exams

document.addEventListener('DOMContentLoaded', () => {
  const list = document.querySelector('.cards-stack');
  const btn  = document.getElementById('loadMoreBtn');
  if (!list || !btn) return;

  // Applique les pastilles aux cartes déjà présentes
  refreshStatusDots(document);

  btn.addEventListener('click', async () => {
    if (btn.classList.contains('is-done')) return;

    let nextPage = parseInt(btn.getAttribute('data-next-page') || '1', 10);
    const size   = 5;

    const original = btn.textContent;
    btn.disabled = true;
    btn.textContent = 'Chargement…';

    try {
      // on récupère la page HTML suivante (même route que la home)
      const res  = await fetch(`/?page=${nextPage}&size=${size}`, {
        headers: { 'X-Requested-With': 'XMLHttpRequest' }
      });
      const html = await res.text();

      // parse le HTML et récupère les nouvelles cartes
      const doc      = new DOMParser().parseFromString(html, 'text/html');
      const newCards = Array.from(doc.querySelectorAll('.exam-card'));

      if (newCards.length === 0) {
        // plus rien à charger → garder le bouton mais le griser
        btn.disabled = true;
        btn.classList.add('is-done');
        btn.textContent = 'Plus rien à charger';
        return;
      }

      // insère les nouvelles cartes
      const frag = document.createDocumentFragment();
      newCards.forEach(card => frag.appendChild(card));
      list.appendChild(frag);

      // calcule les pastilles pour les nouvelles cartes
      refreshStatusDots(list);

      // met à jour la prochaine page
      const newBtn = doc.querySelector('#loadMoreBtn');
      if (newBtn) {
        btn.setAttribute('data-next-page', newBtn.getAttribute('data-next-page'));
        btn.disabled = false;
        btn.textContent = original;
      } else {
        btn.disabled = true;
        btn.classList.add('is-done');
        btn.textContent = 'Plus rien à charger';
      }
    } catch (e) {
      console.error(e);
      btn.disabled = false;
      btn.textContent = 'Réessayer';
    }
  });
});

/* --------- Pastilles : calcul robuste par jours --------- */

// Parse 'YYYY-MM-DD' (ou ISO) -> Date locale (00:00)
function parseYMD(iso) {
  if (!iso) return null;
  const m = /^(\d{4})-(\d{2})-(\d{2})/.exec(iso);
  if (!m) return new Date(iso); // fallback
  return new Date(Number(m[1]), Number(m[2]) - 1, Number(m[3]));
}

function daysBetween(a, b) {
  const MS = 24 * 60 * 60 * 1000;
  const d1 = new Date(a.getFullYear(), a.getMonth(), a.getDate());
  const d2 = new Date(b.getFullYear(), b.getMonth(), b.getDate());
  return Math.round((d1 - d2) / MS);
}

/**
 * Statut:
 *  - 'last'  : date limite = aujourd'hui
 *  - 'on'    : date limite > aujourd'hui et <= 14 jours (en cours)
 *  - 'up'    : date limite au-delà de 14 jours (à venir)
 *  - 'closed': date limite < aujourd'hui (au cas où)
 */
function computeStatus(deadlineIso) {
  const d = parseYMD(deadlineIso);
  if (!d) return '';
  const today = new Date();
  const diff = daysBetween(d, today); // +n = deadline dans n jours

  if (diff === 0)  return 'last';
  if (diff > 14)   return 'up';
  if (diff > 0)    return 'on';
  return 'closed';
}

// Applique/rafraîchit les classes de pastille .status-dot
function refreshStatusDots(root = document) {
  root.querySelectorAll('.exam-card').forEach(card => {
    const deadline = card.getAttribute('data-deadline');
    const dot = card.querySelector('.status-dot');
    if (!dot || !deadline) return;
    const status = computeStatus(deadline);
    dot.classList.remove('last','on','up','closed');
    if (status) dot.classList.add(status);
  });
}



// document.addEventListener('DOMContentLoaded', () => {
//   const list = document.querySelector('.cards-stack');
//   const btn  = document.getElementById('loadMoreBtn');
//   if (!list || !btn) return;

//   btn.addEventListener('click', async () => {
//     const nextPage = parseInt(btn.dataset.nextPage || '1', 10);
//     const size     = parseInt(btn.dataset.size || '5', 5);

//     btn.disabled = true;
//     const original = btn.textContent;
//     btn.textContent = 'Chargement…';

//     try {
//       const res = await fetch(`/api/exams?page=${nextPage}&size=${size}`);
//       if (!res.ok) throw new Error('HTTP ' + res.status);
//       const page = await res.json();

//       // append new cards
//       page.content.forEach(exam => {
//         list.insertAdjacentHTML('beforeend', renderExamCard(exam));
//       });

//       // update button state
//       if (page.last) {
//         btn.remove(); // no more pages
//       } else {
//         btn.dataset.nextPage = page.number + 1; // next
//         btn.disabled = false;
//         btn.textContent = original;
//       }
//     } catch (e) {
//       console.error(e);
//       btn.disabled = false;
//       btn.textContent = 'Réessayer';
//     }
//   });
// });



// /* ------- helpers ------- */
// function renderExamCard(exam) {
//   const date     = fmt(exam.date);
//   const deadline = fmt(exam.applicationDeadline);
//   const status   = computeStatus(exam.applicationDeadline); // last/on/upcoming → class
//   return `
//   <div class="exam-card" data-exam-id="${exam.id}">
//     <header class="exam-head">
//       <span class="status-dot ${status}" aria-hidden="true"></span>
//       <h3>${escapeHtml(exam.title || '')}</h3>
//       <a class="btn-apply" href="/apply?examId=${encodeURIComponent(exam.id)}">Postuler</a>
//     </header>

//     <div class="exam-subtitle">
//       <span>📅 Date de l'examen : <strong>${date}</strong></span>
//     </div>

//     <ul class="meta">
//       <li>Date limite : <strong>${deadline}</strong></li>
//       ${num(exam.hours) ? `<li>Durée : <strong>${exam.hours}</strong> h</li>` : ''}
//       ${num(exam.quota) ? `<li>Places : <strong>${exam.quota}</strong></li>` : ''}
//       ${num(exam.testsCount) ? `<li>Épreuves : <strong>${exam.testsCount}</strong></li>` : ''}
//     </ul>

//     <footer class="links">
//       <button type="button" class="link-btn js-open-guide">Guide (conditions)</button>
//       <button type="button" class="link-btn js-open-centers">Centres d'examen</button>
//     </footer>
//   </div>`;
// }

// function fmt(iso){ if(!iso) return ''; const d=new Date(iso); return d.toLocaleDateString('fr-FR',{day:'2-digit',month:'2-digit',year:'numeric'}); }
// function num(v){ return v!==undefined && v!==null && v!==''; }
// function escapeHtml(s){ return String(s).replaceAll('&','&amp;').replaceAll('<','&lt;').replaceAll('>','&gt;').replaceAll('"','&quot;').replaceAll("'",'&#039;'); }
// function computeStatus(deadlineIso){
//   if(!deadlineIso) return '';
//   const today = new Date(); today.setHours(0,0,0,0);
//   const d = new Date(deadlineIso); d.setHours(0,0,0,0);
//   if (d.getTime() === today.getTime()) return 'last';     // Dernier jour
//   if (d > today) return 'on';                              // En cours
//   return 'up';                                             // À venir (fallback if you change logic)
// }
