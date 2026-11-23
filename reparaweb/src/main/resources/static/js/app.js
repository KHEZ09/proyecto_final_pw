let me = null;
let staffUser = false;
const $ = (id) => document.getElementById(id);
let messages = {};

async function api(url, options = {}) {
  const response = await fetch(url, {
    headers: { 'Content-Type': 'application/json', ...(options.headers || {}) }, ...options
  });
  if (response.status === 401 || response.status === 403) {
    location.href = '/login.html';
    throw Error('No autorizado');
  }
  if (!response.ok) throw Error(await response.text());
  return response.status === 204 ? null : response.json();
}

function esc(value) {
  return String(value ?? '').replace(/[&<>"']/g, (char) => ({
    '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
  }[char]));
}

async function cargar() {
  const list = await api('/api/reportes');
  $('rows').innerHTML = list.length ? list.map((report) => `<tr><td><b>${esc(report.codigo)}</b><br><small class="text-muted">${new Date(report.creadoEn).toLocaleDateString()}</small></td><td><b>${esc(report.titulo)}</b><br><small>${esc(report.ubicacion)}</small></td><td><span class="badge ${report.urgencia === 'ALTA' ? 'text-bg-danger' : 'text-bg-light'}">${esc(report.urgencia)}</span></td><td><span class="badge status-${report.estado}">${esc(report.estado.replace('_', ' '))}</span></td><td><small>${new Date(report.actualizadoEn).toLocaleString()}</small></td><td><button class="btn btn-sm btn-outline-primary" onclick="ver('${report.codigo}')">Ver</button>${staffUser && report.estado !== 'RESUELTO' ? ` <button class="btn btn-sm btn-outline-success" onclick="marcarRealizado(${report.id})">Realizado</button>` : ''}</td></tr>`).join('') : `<tr><td colspan="6" class="text-center text-muted py-4">${messages['dashboard.empty']}</td></tr>`;
}

async function marcarRealizado(id) {
  await api('/api/reportes/' + id, { method: 'DELETE' });
  await cargar();
}

async function ver(code) {
  const report = await api('/api/reportes/seguimiento/' + code);
  alert(`Código: ${report.codigo}\nEstado: ${report.estado}\nDescripción: ${report.descripcion}\nResponsable: ${report.responsable || 'Pendiente'}\nSolución: ${report.solucion || 'Pendiente'}`);
}

function setupLanguage() {
  const current = localStorage.getItem('reparaweb-language') || 'es';
  const selector = document.createElement('select');
  selector.className = 'form-select form-select-sm mb-3';
  selector.innerHTML = '<option value="es">ES</option><option value="en">EN</option><option value="pt">PT</option>';
  selector.value = current;
  selector.onchange = () => { localStorage.setItem('reparaweb-language', selector.value); location.reload(); };
  document.querySelector('.side').prepend(selector);
  document.querySelectorAll('[data-i18n]').forEach((element) => {
    if (messages[element.dataset.i18n]) element.textContent = messages[element.dataset.i18n];
  });
  const nav = document.querySelectorAll('.side nav a');
  if (nav[0]) nav[0].textContent = '＋ ' + messages['dashboard.new'];
  $('greeting').textContent = messages['dashboard.reports'];
  $('nuevo').querySelector('h2').textContent = messages['dashboard.new'];
  $('nuevo').querySelector('p').textContent = messages['dashboard.new.subtitle'];
  $('reportes').querySelector('p').textContent = messages['dashboard.history'];
  $('reportes').querySelector('button').textContent = messages['dashboard.refresh'];
  $('report-form').querySelector('button').textContent = messages['dashboard.send'];
  document.querySelector('.side button').textContent = messages['dashboard.logout'];
}

async function iniciar() {
  try {
    const lang = localStorage.getItem('reparaweb-language') || 'es';
    messages = await fetch('/api/i18n/messages?lang=' + encodeURIComponent(lang)).then((response) => response.json());
    setupLanguage();
    me = await api('/api/me');
    $('role').textContent = me.rol;
    const canCreate = ['USUARIO', 'ADMINISTRADOR'].includes(me.rol);
    if (!canCreate) {
      $('nuevo').classList.add('d-none');
      const newLink = document.querySelector('.side nav a[href="#nuevo"]');
      if (newLink) newLink.classList.add('d-none');
    }
    staffUser = ['ADMINISTRADOR', 'TECNICO'].includes(me.rol);
    if (staffUser) {
      $('stats').classList.remove('d-none');
      const stats = await api('/api/reportes/indicadores');
      Object.keys(stats).forEach((key) => { if ($('s-' + key)) $('s-' + key).textContent = stats[key]; });
    }
    cargar();
  } catch (error) { console.error(error); }
}

document.querySelector('#report-form').addEventListener('submit', async (event) => {
  event.preventDefault();
  const button = event.target.querySelector('button'); button.disabled = true;
  try {
    const report = await api('/api/reportes', { method: 'POST', body: JSON.stringify({
      titulo: $('titulo').value, descripcion: $('descripcion').value, categoria: $('categoria').value,
      ubicacion: $('ubicacion').value, urgencia: $('urgencia').value, fotoUrl: $('fotoUrl').value
    }) });
    $('form-message').innerHTML = `<div class="alert alert-success">Reporte creado. Tu código es <b>${report.codigo}</b>.</div>`;
    event.target.reset(); cargar();
  } catch (error) { $('form-message').innerHTML = '<div class="alert alert-danger">No fue posible crear el reporte.</div>'; }
  finally { button.disabled = false; }
});

iniciar();
