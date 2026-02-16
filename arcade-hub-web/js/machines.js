/**
 * machines.js — Machine list table with live WebSocket updates.
 * Depends on: auth.js, utils.js, api.js, websocket.js
 */

let allMachines = [];
let currentFilter = '';

document.addEventListener('DOMContentLoaded', () => {
    requireAuth();

    const userEl = document.getElementById('sidebar-user');
    if (userEl) userEl.textContent = getCurrentUser() ?? '\u2014';

    document.getElementById('hamburger')?.addEventListener('click', toggleSidebar);
    document.getElementById('sidebar-overlay')?.addEventListener('click', closeSidebar);
    document.getElementById('refresh-btn')?.addEventListener('click', loadMachines);
    document.getElementById('status-filter')?.addEventListener('change', (e) => {
        currentFilter = e.target.value;
        renderTable();
    });

    loadMachines();

    // Connect WebSocket and register event handler
    connectWebSocket();
    onEvent(handleMachineEvent);
});

// ---------------------------------------------------------------------------
// Load machines from REST API
// ---------------------------------------------------------------------------

async function loadMachines() {
    try {
        const response = await getMachines({ size: 200 });
        allMachines = response.content ?? [];
        renderTable();
        document.getElementById('last-refreshed').textContent =
            `Last refreshed: ${formatDate(new Date().toISOString())}`;
    } catch (err) {
        showToast(`Failed to load machines: ${err.message}`, 'danger');
    }
}

// ---------------------------------------------------------------------------
// Render table
// ---------------------------------------------------------------------------

function renderTable() {
    const container = document.getElementById('machine-table-body');
    if (!container) return;

    const filtered = currentFilter
        ? allMachines.filter(m => m.status === currentFilter)
        : allMachines;

    if (filtered.length === 0) {
        container.innerHTML = `
            <tr>
                <td colspan="6" class="text-center text-muted py-4">
                    <i class="bi bi-inbox" style="font-size:2rem; display:block; margin-bottom:0.5rem;"></i>
                    No machines found.
                </td>
            </tr>`;
        return;
    }

    container.innerHTML = filtered.map(m => `
        <tr id="machine-${m.id}">
            <td class="fw-semibold">${m.name}</td>
            <td>${typeLabel(m.type)}</td>
            <td>${statusBadge(m.status)}</td>
            <td>${m.locationName ?? '\u2014'}</td>
            <td>
                <span class="heartbeat-indicator ${heartbeatFreshness(m.lastHeartbeat)}"
                      title="${m.lastHeartbeat ? formatDate(m.lastHeartbeat) : 'No heartbeat'}">
                    ${m.lastHeartbeat ? timeAgo(m.lastHeartbeat) : '\u2014'}
                </span>
            </td>
        </tr>
    `).join('');

    // Update machine count badge
    const countEl = document.getElementById('machine-count');
    if (countEl) countEl.textContent = `${filtered.length} machine${filtered.length !== 1 ? 's' : ''}`;
}

// ---------------------------------------------------------------------------
// Live WebSocket updates
// ---------------------------------------------------------------------------

function handleMachineEvent(event) {
    if (!event.machineId) return;

    // Find the machine in our local array and update it
    const machine = allMachines.find(m => m.id === event.machineId);
    if (machine) {
        // Update heartbeat timestamp for any event
        machine.lastHeartbeat = event.timestamp ?? new Date().toISOString();

        // Update status for status change events
        if (event.eventType === 'STATUS_CHANGE' && event.value) {
            // value might encode new status
        }
        if (event.eventType === 'ERROR') {
            machine.status = 'ERROR';
        }
        if (event.eventType === 'MAINTENANCE') {
            machine.status = 'MAINTENANCE';
        }

        renderTable();
    }
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
