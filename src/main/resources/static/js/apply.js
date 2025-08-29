// ===================== CONFIG =====================
const API_BASE = "/api/applications";
const DEFAULT_EXAM_ID = null; // set a number if you don't use ?examId=

// ===================== HELPERS ====================
function getQueryParam(name){
  const url = new URL(window.location.href);
  return url.searchParams.get(name);
}
function show(el, msg){
  el.textContent = msg;
  el.style.display = "block";
}
function hide(el){ el.style.display = "none"; }

// Optional: simple validators (can be extended)
function normalizeNina(n){ return n.replace(/\s+/g, ""); }
function isValidNina(n){ return /^[0-9]{10,15}$/.test(normalizeNina(n)); }
function isValidEmail(e){ return /^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(e.trim()); }

// ===================== INIT =======================
(function initExamId(){
  const fromURL = getQueryParam("examId");
  const examId = fromURL ?? DEFAULT_EXAM_ID;
  if(!examId){
    console.warn("examId manquant. Ajoutez ?examId=123 à l’URL ou définissez DEFAULT_EXAM_ID.");
  }
  document.getElementById("examId").value = examId || "";
})();

// ===================== SUBMIT FLOW =================
document.getElementById("applicationForm").addEventListener("submit", async (e) => {
  e.preventDefault();

  const msgOk  = document.getElementById("msgOk");
  const msgErr = document.getElementById("msgErr");
  hide(msgOk); hide(msgErr);

  const submitBtn = document.getElementById("submitBtn");
  submitBtn.disabled = true; submitBtn.textContent = "Envoi en cours…";

  const payload = {
    examId: document.getElementById("examId").value ? Number(document.getElementById("examId").value) : null,
    ninaNumber: document.getElementById("nina").value.trim(),
    name: document.getElementById("firstname").value.trim(),
    surname: document.getElementById("surname").value.trim(),
    phone: document.getElementById("phone").value.trim(),
    email: document.getElementById("email").value.trim(),
    birthDate: document.getElementById("birthDate").value || null
  };

  try {
    // Front validations (light)
    if(!isValidNina(payload.ninaNumber)) throw new Error("Numéro NINA invalide.");
    if(!isValidEmail(payload.email))     throw new Error("Adresse email invalide.");

    // 1) Create application
  const res = await fetch(API_BASE, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text || "Impossible de créer la candidature.");
  }
  const application = await res.json();
  const appId = application.id;

  // 2) Upload documents if present
  const picture   = document.getElementById("picture").files[0] || null;
  const documents = document.getElementById("documents").files[0] || null;

  if (picture || documents) {
    const fd = new FormData();
    if (picture)   fd.append("picture", picture);
    if (documents) fd.append("documents", documents);

    const up = await fetch(`${API_BASE}/${appId}/documents`, {
      method: "POST",
      body: fd
    });
    if (!up.ok) {
      const t = await up.text();
      throw new Error(t || "Échec de l'envoi des pièces justificatives.");
    }
  }




  // ✅ Redirect to Thymeleaf success page
  window.location.href = `/apply/success/${appId}`;

} catch (err) {
  console.error(err);
  show(msgErr, "❌ " + (err.message || "Erreur inconnue."));
} finally {
  // You can keep this if you want the button back when errors happen
  submitBtn.disabled = false;
  submitBtn.textContent = "Soumettre ma candidature";
}});