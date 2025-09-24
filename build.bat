@echo off
:: CCRM Build Script for Windows

echo "Building Campus Course & Records Manager...""

:: Create build directory if it doesn't already exist
if not exist "build" (
    echo Creating build directory...
    mkdir "build"
)

:: Compile Java sources
echo Compiling Java sources...
javac -d build -sourcepath src\main\java src\main\java\edu\ccrm\cli\CommandLineInterface.java

:: Check if compilation was successful by checking the errorlevel
:: A value of 0 means success
if %errorlevel% equ 0 (
    echo.
    echo  / \  Compilation successful!
    echo To run the application:
    echo    java -ea -cp build edu.ccrm.cli.CommandLineInterface
) else (
    echo.
    echo  \ /  Compilation failed!
    exit /b 1
)