# install.ps1 — downloads and installs the pre-built weather-cli binary on Windows
$ErrorActionPreference = "Stop"

$Repo        = "bardsoleim/weather-cli"
$BinaryName  = "weather.exe"
$Asset       = "weather-windows-x64.exe"
$InstallDir  = "$HOME\.local\bin"

function Info($msg)    { Write-Host "==> $msg" -ForegroundColor Cyan }
function Success($msg) { Write-Host "✓  $msg"  -ForegroundColor Green }
function Die($msg)     { Write-Host "✗  $msg"  -ForegroundColor Red; exit 1 }

# ── Arch check ────────────────────────────────────────────────────────────────
if ($env:PROCESSOR_ARCHITECTURE -ne "AMD64") {
    Die "Unsupported architecture: $env:PROCESSOR_ARCHITECTURE (only x64 is supported)"
}

# ── Fetch latest release URL ──────────────────────────────────────────────────
Info "Fetching latest release..."
$Release = Invoke-RestMethod -Uri "https://api.github.com/repos/$Repo/releases/latest"
$DownloadUrl = ($Release.assets | Where-Object { $_.name -eq $Asset }).browser_download_url

if (-not $DownloadUrl) {
    Die "Could not find release binary '$Asset'. Check https://github.com/$Repo/releases"
}

# ── Download & install ────────────────────────────────────────────────────────
if (-not (Test-Path $InstallDir)) { New-Item -ItemType Directory -Force -Path $InstallDir | Out-Null }

Info "Downloading binary..."
Invoke-WebRequest -Uri $DownloadUrl -OutFile "$InstallDir\$BinaryName"

# ── Add to PATH if needed ─────────────────────────────────────────────────────
$UserPath = [Environment]::GetEnvironmentVariable("PATH", "User")
if ($UserPath -notlike "*$InstallDir*") {
    Info "Adding $InstallDir to your PATH..."
    [Environment]::SetEnvironmentVariable("PATH", "$UserPath;$InstallDir", "User")
    $env:PATH = "$env:PATH;$InstallDir"
}

Success "Installed! Open a new terminal and run: weather --help"

