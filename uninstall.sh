#!/usr/bin/env bash
# uninstall.sh — removes weather-cli from ~/.local/bin
set -euo pipefail

INSTALL_DIR="$HOME/.local/bin"
BINARY_NAME="weather"
BINARY="$INSTALL_DIR/$BINARY_NAME"

RED='\033[0;31m'; GREEN='\033[0;32m'; CYAN='\033[0;36m'; BOLD='\033[1m'; RESET='\033[0m'
info()    { echo -e "${CYAN}${BOLD}==> $*${RESET}"; }
success() { echo -e "${GREEN}${BOLD}✓  $*${RESET}"; }
die()     { echo -e "${RED}${BOLD}✗  $*${RESET}" >&2; exit 1; }

info "Uninstalling weather-cli..."

if [ ! -f "$BINARY" ]; then
    die "weather-cli not found at $BINARY — nothing to uninstall."
fi

rm "$BINARY"
success "Removed $BINARY"
success "weather-cli uninstalled."

