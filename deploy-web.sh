#!/usr/bin/env bash
# deploy-web.sh — Deploy arcade-hub-web static files to VPS.
# Usage: bash deploy-web.sh
#
# Copies HTML, CSS, and JS files to /var/www/portfolio/arcade/ on the VPS.
# nginx already serves static files from /var/www/portfolio/.

set -euo pipefail

VPS="vps"
REMOTE_DIR="/var/www/portfolio/arcade"
LOCAL_DIR="$(dirname "$0")/arcade-hub-web"

echo "Deploying arcade-hub-web to ${VPS}:${REMOTE_DIR}..."

# Ensure remote directory exists
ssh "$VPS" "sudo mkdir -p ${REMOTE_DIR}/css ${REMOTE_DIR}/js"

# Copy files
scp "${LOCAL_DIR}/index.html" "${LOCAL_DIR}/dashboard.html" "${LOCAL_DIR}/machines.html" "${VPS}:${REMOTE_DIR}/"
scp "${LOCAL_DIR}/css/app.css" "${VPS}:${REMOTE_DIR}/css/"
scp "${LOCAL_DIR}"/js/*.js "${VPS}:${REMOTE_DIR}/js/"

# Fix ownership
ssh "$VPS" "sudo chown -R www-data:www-data ${REMOTE_DIR}"

echo "Done. Visit http://213.199.32.18/arcade/"
