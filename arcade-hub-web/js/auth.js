/**
 * auth.js — Login state management for Arcade Hub Web.
 * Stores Basic Auth token in sessionStorage (clears on tab close).
 * Forked from device-manager-web/js/auth.js with arcade-hub endpoints.
 */

const AUTH_KEY = 'ahw_auth';
const USER_KEY = 'ahw_user';

/**
 * Attempt login: encode credentials, validate against arcade API, store in sessionStorage.
 * @param {string} username
 * @param {string} password
 * @returns {Promise<void>} resolves on success, rejects with Error on 401/network failure
 */
async function login(username, password) {
    const token = btoa(`${username}:${password}`);

    const response = await fetch('/arcade/api/machines?page=0&size=1', {
        headers: { 'Authorization': `Basic ${token}` }
    });

    if (!response.ok) {
        throw new Error(response.status === 401
            ? 'Invalid username or password.'
            : `Server error: ${response.status}`);
    }

    sessionStorage.setItem(AUTH_KEY, token);
    sessionStorage.setItem(USER_KEY, username);
    window.location.href = 'dashboard.html';
}

/**
 * Clear session and return to login page.
 */
function logout() {
    sessionStorage.removeItem(AUTH_KEY);
    sessionStorage.removeItem(USER_KEY);
    window.location.href = 'index.html';
}

/**
 * Guard for protected pages — call at DOMContentLoaded.
 * Redirects to index.html if no token is stored.
 */
function requireAuth() {
    if (!sessionStorage.getItem(AUTH_KEY)) {
        window.location.href = 'index.html';
    }
}

/**
 * Returns the Authorization header object for use in fetch() calls.
 * @returns {{ Authorization: string }}
 */
function getAuthHeader() {
    const token = sessionStorage.getItem(AUTH_KEY);
    return { 'Authorization': `Basic ${token}` };
}

/**
 * Returns the stored username, or null if not logged in.
 * @returns {string|null}
 */
function getCurrentUser() {
    return sessionStorage.getItem(USER_KEY);
}
