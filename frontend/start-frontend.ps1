# Frontend Startup Script
# This script installs dependencies (if needed) and starts the React development server

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Cricket League Management Frontend" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if node_modules exists
if (-not (Test-Path "node_modules")) {
    Write-Host "Installing dependencies..." -ForegroundColor Yellow
    Write-Host "This may take a few minutes on first run.`n" -ForegroundColor Yellow
    
    npm install
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "`nERROR: Failed to install dependencies!" -ForegroundColor Red
        Write-Host "Please check your Node.js installation and try again." -ForegroundColor Yellow
        exit 1
    }
    
    Write-Host "`nDependencies installed successfully!`n" -ForegroundColor Green
} else {
    Write-Host "Dependencies found. Skipping installation.`n" -ForegroundColor Green
}

# Check Node.js version
Write-Host "Node.js Version:" -ForegroundColor Yellow
node --version
Write-Host ""

# Check npm version
Write-Host "npm Version:" -ForegroundColor Yellow
npm --version
Write-Host ""

# Start the development server
Write-Host "Starting React development server..." -ForegroundColor Cyan
Write-Host "Frontend will be available at: http://localhost:3000" -ForegroundColor Green
Write-Host "Backend API: http://localhost:8081" -ForegroundColor Green
Write-Host "Make sure the backend is running before using the frontend!`n" -ForegroundColor Yellow
Write-Host ""

npm start

