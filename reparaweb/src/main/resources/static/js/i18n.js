(function () {
  const selected = new URLSearchParams(location.search).get('lang');
  const lang = selected || localStorage.getItem('reparaweb-language') ||
    (navigator.language.toLowerCase().startsWith('en') ? 'en' : 'es');
  localStorage.setItem('reparaweb-language', lang);
  document.documentElement.lang = lang;

  async function apply() {
    const response = await fetch('/api/i18n/messages?lang=' + encodeURIComponent(lang));
    const t = await response.json();
    document.querySelectorAll('[data-i18n]').forEach((element) => {
      const key = element.dataset.i18n;
      if (t[key]) element.textContent = t[key];
    });
    document.querySelectorAll('[data-i18n-placeholder]').forEach((element) => {
      const key = element.dataset.i18nPlaceholder;
      if (t[key]) element.placeholder = t[key];
    });
    const selector = document.querySelector('#language-selector');
    if (selector) {
      if (!selector.querySelector('option[value="pt"]')) {
        selector.append(new Option('PT', 'pt'));
      }
      selector.value = lang;
    }
  }

  window.changeLanguage = function (value) {
    localStorage.setItem('reparaweb-language', value);
    const url = new URL(location.href);
    url.searchParams.set('lang', value);
    location.href = url.toString();
  };

  document.addEventListener('DOMContentLoaded', () => apply().catch(console.error));
})();
