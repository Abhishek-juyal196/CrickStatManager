# Frontend Quick Start Guide

## ✅ Prerequisites Check

Before starting, make sure:
- ✅ Node.js is installed (v14 or higher) - You have v22.15.1 ✓
- ✅ npm is installed - You have 10.9.2 ✓
- ✅ Backend is running on port 8081 (start it first!)

## 🚀 Starting the Frontend

### Option 1: Use the Startup Script (Easiest)

```powershell
cd frontend
.\start-frontend.ps1
```

The script will:
- Check and install dependencies if needed
- Start the React development server
- Open http://localhost:3000 in your browser

### Option 2: Manual Start

1. **Navigate to frontend directory:**
   ```powershell
   cd frontend
   ```

2. **Install dependencies** (first time only):
   ```powershell
   npm install
   ```

3. **Start the development server:**
   ```powershell
   npm start
   ```

---

## 📋 What Happens When You Start

1. **Dependencies Installation** (first time only):
   - Installs React, Tailwind CSS, and other required packages
   - Takes 1-2 minutes
   - Creates `node_modules` directory

2. **Development Server Starts**:
   - Compiles React application
   - Opens browser at http://localhost:3000
   - Watches for file changes (auto-reloads)

3. **Connects to Backend**:
   - Frontend automatically connects to backend at http://localhost:8081
   - API calls are proxied through the React dev server

---

## ✅ Expected Output

When started successfully, you'll see:

```
Compiled successfully!

You can now view cricket-league-management in the browser.

  Local:            http://localhost:3000
  On Your Network:  http://192.168.x.x:3000

Note that the development build is not optimized.
To create a production build, use npm run build.
```

Your browser should automatically open to http://localhost:3000

---

## 🌐 Access Points

- **Frontend (React App)**: http://localhost:3000
- **Backend API**: http://localhost:8081
- **Backend Swagger UI**: http://localhost:8081/swagger-ui.html

---

## ⚠️ Important Notes

1. **Start Backend First**: 
   - The frontend needs the backend to be running
   - Make sure backend is started with: `cd backend && .\start-backend.ps1`

2. **Port 3000**: 
   - If port 3000 is busy, React will ask to use another port
   - Press `Y` to accept, or kill the process using port 3000

3. **Hot Reload**:
   - Changes to React files will automatically reload in the browser
   - No need to restart the server

---

## 🐛 Troubleshooting

### "Cannot GET /" Error
- **Solution**: Make sure you're accessing http://localhost:3000 (not 8081)

### "Failed to fetch" Errors
- **Solution**: Check that backend is running on port 8081
- Test backend: http://localhost:8081/actuator/health

### Port 3000 Already in Use
- **Solution**: 
  ```powershell
  # Find process using port 3000
  Get-NetTCPConnection -LocalPort 3000 | Select-Object OwningProcess
  
  # Stop it (replace PID with actual process ID)
  Stop-Process -Id <PID> -Force
  ```

### npm install Errors
- **Solution**:
  ```powershell
  # Clear npm cache
  npm cache clean --force
  
  # Delete node_modules and reinstall
  Remove-Item -Recurse -Force node_modules
  npm install
  ```

### Module Not Found Errors
- **Solution**: Run `npm install` again

---

## 🛑 Stopping the Frontend

To stop the development server:
- Press `Ctrl+C` in the terminal
- Confirm by pressing `Y` if prompted

---

## 📚 Next Steps

Once frontend is running:

1. **Verify Backend Connection**:
   - Open browser developer tools (F12)
   - Check Console tab for any API errors

2. **Test the Application**:
   - Navigate through different tabs (Home, Team, Admin, Live Scoring)
   - Try creating a league, team, or match from Admin panel

3. **Insert Sample Data**:
   - Use Swagger UI: http://localhost:8081/swagger-ui.html
   - Or use the Admin panel in the frontend

---

For more detailed setup, see `SETUP_GUIDE.md`

