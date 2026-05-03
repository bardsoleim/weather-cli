#!/usr/bin/env bash
# install.sh — builds weather-cli from source and installs it to ~/.local/bin/weather
set -euo pipefail

REPO="https://github.com/bardsoleim/weather-cli.git"
INSTALL_DIR="$HOME/.local/bin"
BINARY_NAME="weather"
TMP_DIR="$(mktemp -d)"

# ── Colours ───────────────────────────────────────────────────────────────────
RED='\033[0;31m'; GREEN='\033[0;32m'; CYAN='\033[0;36m'; BOLD='\033[1m'; RESET='\033[0m'
info()    { echo -e "${CYAN}${BOLD}==> $*${RESET}"; }
success() { echo -e "${GREEN}${BOLD}✓  $*${RESET}"; }
die()     { echo -e "${RED}${BOLD}✗  $*${RESET}" >&2; exit 1; }

# ── Dependency checks ─────────────────────────────────────────────────────────
info "Checking dependencies..."

command -v git  >/dev/null 2>&1 || die "git is required but not installed."
command -v java >/dev/null 2>&1 || die "JDK 21+ is required. Install with: sudo pacman -S jdk21-openjdk"

JAVA_VER=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
[ "${JAVA_VER:-0}" -ge 21 ] 2>/dev/null || die "JDK 21+ required (found Java ${JAVA_VER:-unknown})."

# libcurl is needed by ktor-client-curl at runtime
if ! ldconfig -p 2>/dev/null | grep -q libcurl || ! pkg-config --exists libcurl 2>/dev/null; then
    command -v curl >/dev/null 2>&1 || die "libcurl is required. Install with: sudo pacman -S curl"
fi

success "All dependencies satisfied."

# ── Clone ─────────────────────────────────────────────────────────────────────
info "Cloning weather-cli..."
git clone --depth 1 "$REPO" "$TMP_DIR/weather-cli" >/dev/null 2>&1
success "Cloned."

# ── Build ─────────────────────────────────────────────────────────────────────
info "Building (this may take a few minutes on first run)..."
cd "$TMP_DIR/weather-cli"
./gradlew linkWeatherReleaseExecutableLinux --no-daemon -q
success "Build complete."

# ── Install ───────────────────────────────────────────────────────────────────
info "Installing to $INSTALL_DIR/$BINARY_NAME ..."
mkdir -p "$INSTALL_DIR"
cp build/bin/linux/weatherReleaseExecutable/weather.kexe "$INSTALL_DIR/$BINARY_NAME"
chmod +x "$INSTALL_DIR/$BINARY_NAME"

# ── PATH check ────────────────────────────────────────────────────────────────
if ! echo "$PATH" | grep -q "$INSTALL_DIR"; then
    echo ""
    echo -e "${BOLD}Note:${RESET} $INSTALL_DIR is not in your PATH."
    echo "  bash/zsh → add to ~/.bashrc or ~/.zshrc:"
    echo "    export PATH=\"\$HOME/.local/bin:\$PATH\""
    echo "  fish      → run once:"
    echo "    fish_add_path ~/.local/bin"
    echo ""
fi

# ── Cleanup ───────────────────────────────────────────────────────────────────
rm -rf "$TMP_DIR"

success "Installed! Run: weather --help"

