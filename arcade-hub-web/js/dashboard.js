/**
 * dashboard.js — Stat cards, charts (Chart.js 4), and live event feed.
 * Depends on: auth.js, utils.js, api.js, websocket.js
 */

let typeChart = null;
let revenueChart = null;
let refreshTimer = null;
const MAX_EVENTS = 50;
const eventFeed = [];

document.addEventListener('DOMContentLoaded', () => {
    requireAuth();

    const userEl = document.getElementById('sidebar-user');
    if (userEl) userEl.textContent = getCurrentUser() ?? '\u2014';

    document.getElementById('hamburger')?.addEventListener('click', toggleSidebar);
    document.getElementById('sidebar-overlay')?.addEventListener('click', closeSidebar);
    document.getElementById('refresh-btn')?.addEventListener('click', loadDashboard);

    loadDashboard();
    refreshTimer = setInterval(loadDashboard, 30_000);

    // Connect WebSocket and register event handler
    connectWebSocket();
    onEvent(handleLiveEvent);
});

// ---------------------------------------------------------------------------
// Initial data load
// ---------------------------------------------------------------------------

async function loadDashboard() {
    try {
        const machines = await getMachines({ size: 200 });
        const list = machines.content ?? [];
        renderStatCards(list);
        renderTypeChart(list);
        document.getElementById('last-refreshed').textContent =
            `Last refreshed: ${formatDate(new Date().toISOString())}`;
    } catch (err) {
        showToast(`Failed to load dashboard: ${err.message}`, 'danger');
    }
}

// ---------------------------------------------------------------------------
// Stat cards
// ---------------------------------------------------------------------------

function renderStatCards(machines) {
    const total = machines.length;
    const online = machines.filter(m => m.status === 'ONLINE').length;
    const maint = machines.filter(m => m.status === 'MAINTENANCE').length;
    const error = machines.filter(m => m.status === 'ERROR').length;

    setText('stat-total', total);
    setText('stat-online', online);
    setText('stat-maintenance', maint);
    setText('stat-error', error);
}

function setText(id, value) {
    const el = document.getElementById(id);
    if (el) el.textContent = value;
}

// ---------------------------------------------------------------------------
// Type doughnut chart
// ---------------------------------------------------------------------------

function renderTypeChart(machines) {
    const counts = {};
    machines.forEach(m => {
        const label = typeLabel(m.type);
        counts[label] = (counts[label] ?? 0) + 1;
    });

    const labels = Object.keys(counts);
    const data = Object.values(counts);
    const colors = [
        '#f59e0b', '#22c55e', '#ef4444', '#8b5cf6',
        '#06b6d4', '#f97316', '#ec4899',
    ];

    const ctx = document.getElementById('chart-type')?.getContext('2d');
    if (!ctx) return;

    if (typeChart) {
        typeChart.data.labels = labels;
        typeChart.data.datasets[0].data = data;
        typeChart.update();
        return;
    }

    typeChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels,
            datasets: [{
                data,
                backgroundColor: colors.slice(0, data.length),
                borderWidth: 2,
                borderColor: '#fff',
            }],
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: { legend: { position: 'right' } },
        },
    });
}

// ---------------------------------------------------------------------------
// Live event feed
// ---------------------------------------------------------------------------

function handleLiveEvent(event) {
    // Add to feed (newest first)
    eventFeed.unshift(event);
    if (eventFeed.length > MAX_EVENTS) eventFeed.pop();
    renderEventFeed();

    // Update stat cards if it's a status change
    if (event.type === 'MACHINE_EVENT' || event.type === 'STATUS_CHANGE') {
        loadDashboard();
    }
}

function renderEventFeed() {
    const container = document.getElementById('event-feed');
    if (!container) return;

    if (eventFeed.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <i class="bi bi-broadcast"></i>
                Waiting for live events...
            </div>`;
        return;
    }

    container.innerHTML = eventFeed.map(e => `
        <div class="event-item">
            <div class="event-item-header">
                ${eventBadge(e.eventType ?? e.type)}
                <span class="event-machine">${e.machineName ?? 'Unknown'}</span>
                <span class="event-time">${timeAgo(e.timestamp)}</span>
            </div>
            ${e.value ? `<span class="event-value">&euro;${Number(e.value).toFixed(2)}</span>` : ''}
        </div>
    `).join('');
}

// ---------------------------------------------------------------------------
// Sidebar helpers (mobile)
// ---------------------------------------------------------------------------

function toggleSidebar() {
    document.getElementById('sidebar').classList.toggle('show');
    document.getElementById('sidebar-overlay').classList.toggle('show');
}

function closeSidebar() {
    document.getElementById('sidebar').classList.remove('show');
    document.getElementById('sidebar-overlay').classList.remove('show');
}
