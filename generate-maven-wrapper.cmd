@echo off
echo Generating Maven Wrapper (requires Maven installed)
mvn -N io.takari:maven:wrapper
if %ERRORLEVEL% NEQ 0 (
  echo Failed to generate wrapper. Ensure Maven is installed and on PATH.
  exit /b %ERRORLEVEL%
)
echo Maven wrapper generated.
