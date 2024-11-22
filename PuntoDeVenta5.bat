@echo off
REM Configura el path al ejecutable de Java
SET JAVA_PATH=C:\Users\j2zromero\.jdks\corretto-17.0.10\bin\java.exe

REM Configura el path del SDK de JavaFX
SET JAVAFX_PATH=C:\Users\j2zromero\Desktop\javafx-sdk-17.0.13\lib

REM Configura el path al JAR de tu aplicación
SET APP_PATH=C:\Users\j2zromero\Desktop\Github\pventa-javafx\out\artifacts\PointOfSale_jar\PointOfSale.jar

REM Ejecuta la aplicación
"%JAVA_PATH%" --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml -jar "%APP_PATH%"

pause
