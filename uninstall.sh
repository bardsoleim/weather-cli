#!/usr/bin/env bash
# uninstall.sh — removes weather-cli
set -euo pipefail

BINARY_NAME="weather"

RED='\033[0;31m'; GREEN='\033[0;32m'; CYAN='\033[0;36m'; BOLD='\033[1m'; RESET='\033[0m'
info()    { echo -e "${CYAN}${BOLD}==> $*${RESET}"; }
success() { echo -e "${GREEN}${BOLD}✓  $*${RESET}"; }
die()     { echo -e "${RED}${BOLD}✗  $*${RESET}" >&2; exit 1; }

OS="$(uname -s)"
case "$OS" in
  Darwin) INSTALL_DIR="/usr/local/bin" ;;
  *)      INSTALL_DIR="$HOME/.local/bin" ;;
esac

BINARY="$INSTALL_DIR/$BINARY_NAME"

info "Uninstalling weather-cli..."

if [ ! -f "$BINARY" ]; then
    die "weather-cli not found at $BINARY — nothing to uninstall."
fi

if [ "$OS" = "Darwin" ]; then
    sudo rm "$BINARY"
else
    rm "$BINARY"
fi

success "Removed $BINARY"
success "weather-cli uninstalled."

