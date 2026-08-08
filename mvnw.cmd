@echo off
SETLOCAL
SET MVNW_DIR=.mvn\wrapper
if defined MAVEN_HOME (
  "%MAVEN_HOME%\bin\mvn" %*
  goto :eof
)

if exist "%~dp0%MVNW_DIR%\maven-wrapper.jar" (
  "%JAVA_HOME%\bin\java" -jar "%~dp0%MVNW_DIR%\maven-wrapper.jar" %*
  goto :eof
)

echo Maven not found and maven-wrapper.jar is missing.
echo To generate the wrapper run:
echo   mvn -N io.takari:maven:wrapper
exit /b 1
