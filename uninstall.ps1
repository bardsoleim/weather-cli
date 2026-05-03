# uninstall.ps1 — removes weather-cli on Windows
$ErrorActionPreference = "Stop"

$InstallDir = "$HOME\.local\bin"
$Binary     = "$InstallDir\weather.exe"

function Success($msg) { Write-Host "✓  $msg" -ForegroundColor Green }
function Die($msg)     { Write-Host "✗  $msg" -ForegroundColor Red; exit 1 }

if (-not (Test-Path $Binary)) {
    Die "weather-cli not found at $Binary — nothing to uninstall."
}

Remove-Item $Binary -Force
Success "Removed $Binary"
Success "weather-cli uninstalled."

