const dom = {
    input: document.getElementById('query'),
    suggestions: document.getElementById('suggestions'),
    card: document.getElementById('result'),
    loader: document.getElementById('loader'),
    error: document.getElementById('error')
};

// Debounce helper for search suggestions
let debounceTimer;
dom.input.addEventListener('input', (e) => {
    clearTimeout(debounceTimer);
    debounceTimer = setTimeout(() => fetchSuggestions(e.target.value), 300);
});

// Hide suggestions on outside click
document.addEventListener('click', (e) => {
    if (e.target !== dom.input) dom.suggestions.classList.add('hide');
});

async function fetchSuggestions(query) {
    if (query.length < 2) {
        dom.suggestions.classList.add('hide');
        return;
    }
    try {
        const res = await fetch(`/api/pokemon/suggestions?query=${query}`);
        const list = await res.json();
        renderSuggestions(list);
    } catch (e) {
        console.error('Autocomplete failed', e);
    }
}

function renderSuggestions(list) {
    if (list.length === 0) {
        dom.suggestions.classList.add('hide');
        return;
    }
    dom.suggestions.innerHTML = list.map(name => 
        `<li onclick="selectSuggestion('${name}')">${name}</li>`
    ).join('');
    dom.suggestions.classList.remove('hide');
}

function selectSuggestion(name) {
    dom.input.value = name;
    dom.suggestions.classList.add('hide');
    executeSearch();
}

async function executeSearch() {
    const query = dom.input.value.trim();
    if (!query) return;

    // Reset UI state
    dom.card.classList.add('hide');
    dom.error.classList.add('hide');
    dom.loader.classList.remove('hide');
    dom.suggestions.classList.add('hide');

    try {
        const res = await fetch(`/api/pokemon/${query}`);
        if (!res.ok) throw new Error();
        const data = await res.json();
        renderCard(data);
    } catch (e) {
        dom.error.textContent = "Pokemon not found in database.";
        dom.error.classList.remove('hide');
    } finally {
        dom.loader.classList.add('hide');
    }
}

function renderCard(data) {
    document.getElementById('p-name').textContent = data.name;
    document.getElementById('p-id').textContent = '#' + String(data.id).padStart(3, '0');
    document.getElementById('p-img').src = data.sprites.other['official-artwork'].front_default || data.sprites.front_default;
    document.getElementById('p-height').textContent = (data.height / 10) + ' m';
    document.getElementById('p-weight').textContent = (data.weight / 10) + ' kg';

    const typesContainer = document.getElementById('p-types');
    typesContainer.innerHTML = data.types.map(t => {
        const color = getTypeColor(t.type.name);
        return `<span class="tag" style="background-color: ${color}">${t.type.name}</span>`;
    }).join('');

    const statsContainer = document.getElementById('p-stats');
    statsContainer.innerHTML = data.stats.map(s => {
        const pct = Math.min((s.base_stat / 150) * 100, 100);
        return `
            <div class="stat-row">
                <span class="stat-label">${getStatLabel(s.stat.name)}</span>
                <div class="stat-track">
                    <div class="stat-fill" style="width: ${pct}%"></div>
                </div>
            </div>`;
    }).join('');

    dom.card.classList.remove('hide');
}

function getStatLabel(name) {
    const map = { 'hp': 'HP', 'attack': 'ATK', 'defense': 'DEF', 'special-attack': 'SPA', 'special-defense': 'SPD', 'speed': 'SPE' };
    return map[name] || name.substring(0, 3).toUpperCase();
}

function getTypeColor(type) {
    const colors = {
        fire: '#F56565', water: '#4299E1', grass: '#48BB78', electric: '#ECC94B',
        ice: '#63B3ED', fighting: '#F56565', poison: '#9F7AEA', ground: '#ED8936',
        flying: '#667EEA', psychic: '#ED64A6', bug: '#48BB78', rock: '#A0AEC0',
        ghost: '#667EEA', dragon: '#667EEA', steel: '#A0AEC0', fairy: '#ED64A6',
        normal: '#A0AEC0'
    };
    return colors[type] || '#718096';
}
