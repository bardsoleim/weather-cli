#!/usr/bin/env bash
# install.sh — downloads and installs the pre-built weather-cli binary
set -euo pipefail

REPO="bardsoleim/weather-cli"
INSTALL_DIR="$HOME/.local/bin"
BINARY_NAME="weather"

RED='\033[0;31m'; GREEN='\033[0;32m'; CYAN='\033[0;36m'; BOLD='\033[1m'; RESET='\033[0m'
info()    { echo -e "${CYAN}${BOLD}==> $*${RESET}"; }
success() { echo -e "${GREEN}${BOLD}✓  $*${RESET}"; }
die()     { echo -e "${RED}${BOLD}✗  $*${RESET}" >&2; exit 1; }

command -v curl >/dev/null 2>&1 || die "curl is required but not installed."

info "Fetching latest release..."
DOWNLOAD_URL=$(curl -fsSL "https://api.github.com/repos/${REPO}/releases/latest" \
  | grep "browser_download_url.*weather.kexe" \
  | cut -d '"' -f 4)

[ -n "$DOWNLOAD_URL" ] || die "Could not find a release binary. Check https://github.com/${REPO}/releases"

mkdir -p "$INSTALL_DIR"

info "Downloading binary..."
curl -fsSL "$DOWNLOAD_URL" -o "$INSTALL_DIR/$BINARY_NAME"
chmod +x "$INSTALL_DIR/$BINARY_NAME"

if ! echo "$PATH" | grep -q "$INSTALL_DIR"; then
    echo ""
    echo -e "${BOLD}Note:${RESET} $INSTALL_DIR is not in your PATH."
    echo "  bash/zsh → add to ~/.bashrc or ~/.zshrc:"
    echo "    export PATH=\"\$HOME/.local/bin:\$PATH\""
    echo "  fish      → run once:"
    echo "    fish_add_path ~/.local/bin"
    echo ""
fi

success "Installed! Run: weather --help"

