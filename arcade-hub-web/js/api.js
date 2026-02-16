/**
 * api.js — Fetch wrapper and typed endpoint functions for the Arcade Hub API.
 * All calls go through apiFetch() which handles auth headers and error handling.
 */

const API_BASE = '/arcade/api';

/**
 * Core fetch wrapper.
 * Injects Authorization header, prepends API base, throws on non-2xx.
 * @param {string} path - path starting with /
 * @param {RequestInit} [options]
 * @returns {Promise<any>} parsed JSON body (or null for 204)
 */
async function apiFetch(path, options = {}) {
    const headers = {
        ...getAuthHeader(),
        'Content-Type': 'application/json',
        ...(options.headers ?? {}),
    };

    const response = await fetch(`${API_BASE}${path}`, { ...options, headers });

    if (response.status === 401) {
        logout();
        return;
    }

    if (!response.ok) {
        let msg = `HTTP ${response.status}`;
        try {
            const body = await response.json();
            msg = body.message ?? body.error ?? msg;
        } catch (_) { /* ignore parse errors */ }
        throw new Error(msg);
    }

    if (response.status === 204) return null;
    return response.json();
}

// ---------------------------------------------------------------------------
// Machine endpoints
// ---------------------------------------------------------------------------

function getMachines(params = {}) {
    const qs = new URLSearchParams();
    if (params.page != null) qs.set('page', params.page);
    if (params.size != null) qs.set('size', params.size);
    if (params.status) qs.set('status', params.status);
    return apiFetch(`/machines?${qs}`);
}

function getMachine(id) {
    return apiFetch(`/machines/${id}`);
}

// ---------------------------------------------------------------------------
// Location endpoints
// ---------------------------------------------------------------------------

function getLocations(params = {}) {
    const qs = new URLSearchParams();
    if (params.page != null) qs.set('page', params.page);
    if (params.size != null) qs.set('size', params.size);
    return apiFetch(`/locations?${qs}`);
}

// ---------------------------------------------------------------------------
// Event endpoints
// ---------------------------------------------------------------------------

function getMachineEvents(params = {}) {
    const qs = new URLSearchParams();
    if (params.machineId) qs.set('machineId', params.machineId);
    if (params.page != null) qs.set('page', params.page);
    if (params.size != null) qs.set('size', params.size);
    return apiFetch(`/machine-events?${qs}`);
}

// ---------------------------------------------------------------------------
// Report endpoint
// ---------------------------------------------------------------------------

function getDailyReport(locationId, date) {
    return apiFetch(`/reports/daily?locationId=${locationId}&date=${date}`);
}
