@echo off
echo Testing database connection...
echo.

REM Test if PostgreSQL is running
netstat -an | findstr :5432
if %errorlevel% neq 0 (
    echo ERROR: PostgreSQL is not running on port 5432
    echo Please start PostgreSQL service first
    pause
    exit /b 1
)

echo PostgreSQL is running on port 5432
echo.

REM Test database connection using psql
echo Testing database connection...
echo You will need to enter the postgres password when prompted
echo.

REM Try to connect to the cricket_league database
"C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -d cricket_league -c "SELECT 1;" 2>nul
if %errorlevel% equ 0 (
    echo SUCCESS: Database connection test passed!
    echo The cricket_league database exists and is accessible
) else (
    echo WARNING: Database connection test failed
    echo This might mean:
    echo 1. The cricket_league database doesn't exist yet
    echo 2. The cricket_user doesn't have proper permissions
    echo 3. Wrong password was entered
    echo.
    echo Please run the database_setup.sql script manually in pgAdmin or psql
)

echo.
echo Database setup instructions:
echo 1. Open pgAdmin or any PostgreSQL client
echo 2. Connect as postgres user
echo 3. Run the database_setup.sql script
echo 4. Or run: psql -U postgres -f database_setup.sql
echo.
pause
