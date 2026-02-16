/**
 * utils.js — Shared formatting helpers and domain constants for Arcade Hub.
 */

/**
 * Format an ISO 8601 date string into a human-readable form.
 * @param {string} isoStr
 * @returns {string} e.g. "14 Feb 2026, 10:30"
 */
function formatDate(isoStr) {
    if (!isoStr) return '\u2014';
    const d = new Date(isoStr);
    return d.toLocaleString('en-GB', {
        day: '2-digit', month: 'short', year: 'numeric',
        hour: '2-digit', minute: '2-digit'
    });
}

/**
 * Format a date as relative time (e.g., "5s ago", "2m ago").
 * @param {string} isoStr
 * @returns {string}
 */
function timeAgo(isoStr) {
    if (!isoStr) return '\u2014';
    const diff = Math.floor((Date.now() - new Date(isoStr).getTime()) / 1000);
    if (diff < 60) return `${diff}s ago`;
    if (diff < 3600) return `${Math.floor(diff / 60)}m ago`;
    if (diff < 86400) return `${Math.floor(diff / 3600)}h ago`;
    return `${Math.floor(diff / 86400)}d ago`;
}

/**
 * Return a Bootstrap badge HTML string for a machine status.
 * @param {string} status
 * @returns {string} HTML string
 */
function statusBadge(status) {
    const map = {
        ONLINE:      '<span class="badge bg-success">Online</span>',
        OFFLINE:     '<span class="badge bg-secondary">Offline</span>',
        MAINTENANCE: '<span class="badge bg-warning text-dark">Maintenance</span>',
        ERROR:       '<span class="badge bg-danger">Error</span>',
    };
    return map[status] ?? `<span class="badge bg-light text-dark">${status}</span>`;
}

/**
 * Return a Bootstrap badge for an event type.
 * @param {string} eventType
 * @returns {string} HTML string
 */
function eventBadge(eventType) {
    const map = {
        COIN_IN:     '<span class="badge bg-success">Coin In</span>',
        COIN_OUT:    '<span class="badge bg-info text-dark">Coin Out</span>',
        ERROR:       '<span class="badge bg-danger">Error</span>',
        MAINTENANCE: '<span class="badge bg-warning text-dark">Maintenance</span>',
        HEARTBEAT:   '<span class="badge bg-secondary">Heartbeat</span>',
        STATUS_CHANGE: '<span class="badge bg-primary">Status</span>',
    };
    return map[eventType] ?? `<span class="badge bg-light text-dark">${eventType}</span>`;
}

/**
 * Return a readable label for a machine type.
 * @param {string} type
 * @returns {string}
 */
function typeLabel(type) {
    const map = {
        SLOT:     'Slot Machine',
        POKER:    'Video Poker',
        ROULETTE: 'Roulette',
        CLAW:     'Claw Machine',
        PINBALL:  'Pinball',
        RACING:   'Racing',
        SHOOTER:  'Shooter',
        OTHER:    'Other',
    };
    return map[type] ?? type;
}

/**
 * Return a heartbeat freshness class based on seconds since last heartbeat.
 * @param {string} lastHeartbeat ISO date string
 * @returns {'fresh'|'stale'|'dead'} green < 60s, amber < 300s, red > 300s
 */
function heartbeatFreshness(lastHeartbeat) {
    if (!lastHeartbeat) return 'dead';
    const seconds = Math.floor((Date.now() - new Date(lastHeartbeat).getTime()) / 1000);
    if (seconds < 60) return 'fresh';
    if (seconds < 300) return 'stale';
    return 'dead';
}

/** Machine statuses for <select> dropdowns. */
const statusOptions = [
    { value: 'ONLINE',      label: 'Online' },
    { value: 'OFFLINE',     label: 'Offline' },
    { value: 'MAINTENANCE', label: 'Maintenance' },
    { value: 'ERROR',       label: 'Error' },
];

/**
 * Show a Bootstrap toast-style alert banner.
 * @param {string} message
 * @param {'success'|'danger'|'warning'|'info'} type
 */
function showToast(message, type = 'info') {
    const container = document.getElementById('toast-container');
    if (!container) return;
    const id = `toast-${Date.now()}`;
    container.insertAdjacentHTML('beforeend', `
        <div id="${id}" class="toast align-items-center text-bg-${type} border-0 show" role="alert">
            <div class="d-flex">
                <div class="toast-body">${message}</div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto"
                        data-bs-dismiss="toast"></button>
            </div>
        </div>
    `);
    setTimeout(() => document.getElementById(id)?.remove(), 4000);
}
